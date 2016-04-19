package com.sdf.manager.user.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sdf.manager.common.bean.DictBean;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.BizException;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.user.bean.AccountBean;
import com.sdf.manager.user.bean.RoleBean;
import com.sdf.manager.user.bean.UserRelaRoleBean;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.entity.UserRelaRole;
import com.sdf.manager.user.service.UserService;


/** 
  * @ClassName: AccountController 
  * @Description: 帐号controller
  * @author songj@sdfcp.com
  * @date 2015年10月12日 上午11:19:38 
  *  
  */
@Controller
@RequestMapping("/account")
public class AccountController {
	
	@Autowired
	private UserService userService;
	
    /**
	 * demo登录提交后跳转方法
	 * @param userName
	 * @param password
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody ResultBean saveOrUpdate(
			AccountBean accountBean,
			ModelMap model,HttpSession httpSession)   {
			ResultBean resultBean = new ResultBean();
			try{
				String userId = LoginUtils.getAuthenticatedUserCode(httpSession);
				userService.saveOrUpdate(accountBean,userId);
				resultBean.setMessage("操作成功!");
				resultBean.setStatus("success");
			}catch(BizException bizEx){
				resultBean.setMessage(bizEx.getMessage());
				resultBean.setStatus("failure");
			}
			catch (Exception e) {
				resultBean.setMessage("操作异常!");
				resultBean.setStatus("failure");
				e.printStackTrace();
			}finally{
				return resultBean;
			}
	}
	
	@RequestMapping(value = "/getUserList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getUserList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		Map<String,Object> rtMap = new HashMap<String, Object>(); 
		Pageable pageable = new PageRequest(page-1, rows);
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add(Constants.IS_NOT_DELETED);//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		rtMap = userService.getScrollDataByJpql(User.class,buffer.toString(), params.toArray(),null , pageable);
		return rtMap;
	}
	@RequestMapping(value = "/getDetailAccount", method = RequestMethod.GET)
	public @ResponseBody AccountBean getDetailAccount(
			@RequestParam(value="id",required=true) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		AccountBean accountBean = new AccountBean();
		User user = userService.getUserById(id);
		accountBean.setId(user.getId());
		accountBean.setCode(user.getCode());
		accountBean.setName(user.getName());
		accountBean.setPassword(user.getPassword());
		accountBean.setTelephone(user.getTelephone());
		accountBean.setStatus(user.getStatus());
		accountBean.setCity(user.getCityCode());
		accountBean.setProvince(user.getProvinceCode());
		accountBean.setLotteryType(user.getLotteryType());//date：2016-4-18ADD
		return accountBean;
	}
	
	
	/**
	 * 
	* @Description: TODO(权限输入值校验，用来校验code唯一性和authname唯一性) 
	* @author bann@sdfcp.com
	* @date 2015年10月15日 上午10:55:07
	 */
	@RequestMapping(value = "/checkValue", method = RequestMethod.GET)
	public @ResponseBody boolean  checkValue(
			@RequestParam(value="code",required=false) String code,
			ModelMap model,HttpSession httpSession) throws Exception {
		User user = userService.getUserByCode(code);
		if(null == user){
			return false;
		}else{
			return true;
		}
	}
	
	

	/** 
	  * @Description: 帐号删除
	  * @author songj@sdfcp.com
	  * @date 2015年10月22日 下午1:35:59 
	  * @param ids
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception 
	  */
	@SuppressWarnings("finally")
	@RequestMapping(value = "/deleteAccountByIds", method = RequestMethod.POST)
	public @ResponseBody ResultBean deleteAccount(
			@RequestParam(value="ids",required=false) String[] ids,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		try {
			String userId = LoginUtils.getAuthenticatedUserCode(httpSession);
			userService.deleteAccountByIds(ids,userId);
			resultBean.setStatus("success");
			resultBean.setMessage("删除成功!");
		}catch(BizException bizEx){
			resultBean.setStatus("failure");
			resultBean.setMessage(bizEx.getMessage());
		}catch (Exception e) {
			resultBean.setStatus("failure");
			resultBean.setMessage(e.getMessage());
		}finally{
			return resultBean;
		}
	}
	@SuppressWarnings("finally")
	@RequestMapping(value = "/getUserRelaRoleList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getUserRelaRoleList(
			@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
	    Map<String,Object> returnMap = new HashMap<String,Object>();
		List<UserRelaRoleBean> roleBeans = new ArrayList<UserRelaRoleBean>();
	    List<Role> roles = new ArrayList<Role>();
		try {
			User user = userService.getUserById(id);
			roles = user.getRoles();
			if(roles != null && roles.size() > 0){
				for(Role role : roles){
					UserRelaRoleBean roleBean = new UserRelaRoleBean();
					roleBean.setRoleId(role.getId());
					roleBean.setRoleCode(role.getCode());
					roleBean.setRoleName(role.getName());
					roleBean.setParentRolename(role.getParentRolename());
					roleBean.setParentRole(role.getParentRole());
					roleBean.setParentUid(user.getParentUid());
					roleBeans.add(roleBean);
				}
			}
			returnMap.put("rows", roleBeans);
			returnMap.put("total", roleBeans.size());
		}catch(BizException bizEx){
			returnMap.put("success", false);
			returnMap.put("message", bizEx.getMessage());
		}catch (Exception e) {
			returnMap.put("success", false);
			returnMap.put("message",e.getMessage());
		}finally{	
			return returnMap;
		}
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value = "/saveUserRleaRole", method = RequestMethod.POST)
	public @ResponseBody ResultBean  saveUserRleaRole(
			@RequestParam(value="role",required=true) String role,
			@RequestParam(value="userId",required=true) String userId,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		try{
			//把json字符串转换成对象
			JSONObject jsonObj = JSONObject.parseObject(	role);
			userService.saveUserRelaRole(userId,(UserRelaRoleBean)JSONObject.toJavaObject(jsonObj,UserRelaRoleBean.class));
			resultBean.setStatus("success");
			resultBean.setMessage("保存成功!");
		}catch(BizException bizEx){
			resultBean.setStatus("failure");
			resultBean.setMessage(bizEx.getMessage());
		}catch(Exception e){
			resultBean.setStatus("failure");
			resultBean.setMessage(e.getMessage());
		}finally{
			return resultBean;
		}
	}
	
	@SuppressWarnings("finally")
	@RequestMapping(value = "/updatePassword", method = RequestMethod.POST)
	public @ResponseBody ResultBean  updatePassword(
			@RequestParam(value="password",required=true) String newPassword,
			ModelMap model,HttpSession httpSession) throws Exception{
		ResultBean resultBean = new ResultBean();
		try {
			String userCode = LoginUtils.getAuthenticatedUserCode(httpSession);
			userService.savePassword(newPassword, userCode);
			resultBean.setStatus("success");
			resultBean.setMessage("密码修改成功!");
		}catch (Exception e) {
			resultBean.setStatus("failure");
			resultBean.setMessage(e.getMessage());
		}finally{
			return resultBean;
		}
	}
	
	/** 
	  * @Description: 根据角色code获取角色下面对应所有人
	  * @author songj@sdfcp.com
	  * @date 2015年11月27日 上午11:00:04 
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception 
	  */
	@RequestMapping(value = "/getRoleRelaUserList", method = RequestMethod.POST)
	public @ResponseBody List<DictBean> getRoleRelaUserList(
			@RequestParam(value="parentRoleId",required=true) String parentRoleId,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		List<DictBean> dictList = new ArrayList<DictBean>();
		if(StringUtils.isEmpty(parentRoleId)){
			return dictList;
		}
		List<AccountBean> userList = userService.findAccountsByRoleId(parentRoleId);
		if(userList != null && userList.size() > 0){
			for(AccountBean accountBean : userList){
				DictBean dictBean = new DictBean();
				dictBean.setId(accountBean.getId());
				dictBean.setName(accountBean.getName());
				dictList.add(dictBean);
			}
		}
		return dictList;
	}
}