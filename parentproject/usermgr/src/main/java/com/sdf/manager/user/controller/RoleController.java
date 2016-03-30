package com.sdf.manager.user.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.bean.TreeBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.user.bean.RoleAuthBean;
import com.sdf.manager.user.dto.AuthorityDTO;
import com.sdf.manager.user.dto.RoleDTO;
import com.sdf.manager.user.entity.Authority;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.service.AuthService;
import com.sdf.manager.user.service.RoleService;


/**
 * 
  * @ClassName: RoleController 
  * @Description: TODO(角色管理控制层) 
  * @author bann@sdfcp.com
  * @date 2015年10月19日 上午9:12:37 
  *
 */
@Controller
@RequestMapping("/role")
public class RoleController extends GlobalExceptionHandler
{
	private static final Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	 @Autowired
	 private RoleService roleService;
	 
	 @Autowired
	 private AuthService authService;
	 
	 
	 /**
	  * 
	 * @Description: TODO(根据id获取角色详情) 
	 * @author bann@sdfcp.com
	 * @date 2015年10月19日 下午2:11:51
	  */
	 @RequestMapping(value = "/getDetailRole", method = RequestMethod.GET)
	 public @ResponseBody RoleDTO getDetailRole(
			@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	 {
	 	Role role = new Role();
		
	 	role = roleService.getRoleById(id);
	 	
	 	RoleDTO RoleDTO = roleService.toDTO(role);
	 	
		return RoleDTO;
	 }
	 
	 /**
	  * 
	 * @Description:获取指定角色对应的权限列表数据
	 * @author bann@sdfcp.com
	 * @date 2015年11月24日 上午9:56:29
	  */
	 @RequestMapping(value = "/getAuthListOfRole", method = RequestMethod.GET)
	 public @ResponseBody List<AuthorityDTO> getAuthListOfRole(
			@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		 Role role = new Role();
			
		 role = roleService.getRoleById(id);
		 
		 List<Authority> authorities = role.getAuthorities();
		 
		 List<AuthorityDTO> authorityDTOs = authService.toDTOS(authorities);
		 
		 return authorityDTOs;
	}
	 
	 /**
	  * 
	 * @Description: TODO(保存/更新角色数据) 
	 * @author bann@sdfcp.com
	 * @date 2015年10月19日 下午2:20:54
	  */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="code",required=false) String code,
				@RequestParam(value="name",required=false) String name,
				@RequestParam(value="parentRole",required=false) String parentRole,
//				@RequestParam(value="status",required=false) String status,
				@RequestParam(value="parentRolename",required=false) String parentRolename,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   Role role ;
		   role = roleService.getRoleById(id);
		   
		   if(null != role)
		   {//角色数据不为空，则进行修改操作
			   role.setId(id);
			   role.setCode(code);
			   role.setName(name);
			   role.setParentRole(parentRole);//上级角色名称
//			   role.setStatus(status);
			   role.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   role.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   role.setParentRolename(parentRolename);
			   roleService.update(role);
			   
			   resultBean.setMessage("修改角色成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
			   logger.info("修改角色--角色id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
			   
		   }
		   else
		   {
			   role = new Role();
			   role.setCode(code);
			   role.setName(name);
			   role.setParentRole(parentRole);
			   role.setParentRolename(parentRolename);//上级角色名称
			   role.setIsSystem("0");//页面中操作添加的角色都是非系统数据
//			   role.setStatus(status);
			   role.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   role.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   role.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   role.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   role.setIsDeleted("1");
			   roleService.save(role);
			   
			   resultBean.setMessage("添加角色成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
			   logger.info("添加角色--角色code="+code+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	/**
	 * 
	* @Description: TODO(删除角色实体) 
	* @author bann@sdfcp.com
	* @date 2015年10月19日 下午2:30:04
	 */
	 @RequestMapping(value = "/deleteRole", method = RequestMethod.POST)
		public @ResponseBody ResultBean deleteRole(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			ResultBean resultBean = new ResultBean();
			
			Role role ;
			List<Authority> authList = new ArrayList<Authority> ();
			for (String id : ids) 
			{
				role = new Role();
				role =  roleService.getRoleById(id);
				role.setIsDeleted("0");;//设置当前数据为已删除状态
				role.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				role.setModifyTime(new Timestamp(System.currentTimeMillis()));
				role.setAuthorities(authList);//清空角色与权限的关联表数据，因为当前角色已删除，所以关联数据无意义
				roleService.update(role);//保存更改状态的角色实体
				
				//日志输出
				logger.info("删除角色--角色id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			}
			
			
			resultBean.setStatus("success");
			resultBean.setMessage("删除成功!");
			
			return resultBean;
		}
	 
	 /**
	  * 
	 * @Description: TODO(权限设置) 
	 * @author bann@sdfcp.com
	 * @date 2015年10月19日 下午4:29:17
	  */
	 @RequestMapping(value = "/manageRoleAndauth", method = RequestMethod.POST)
		public @ResponseBody ResultBean manageRoleAndauth(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="authes",required=false) String[] authes,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			ResultBean resultBean = new ResultBean();
			
			Role role = roleService.getRoleById(id);
			
			List<Authority> authList = new ArrayList<Authority> ();
			
			for (String authId : authes) 
			{
				Authority authority = authService.getAuthorityByCode(authId);
				authList.add(authority);
			}
			
			role.setAuthorities(authList);//设置权限
			roleService.update(role);
			
			
			resultBean.setStatus("success");
			resultBean.setMessage("权限设置成功!");
			
			return resultBean;
		}
	 
	 /**
	  * 
	 * @Description: TODO(获取权限树) 
	 * @author bann@sdfcp.com
	 * @date 2015年10月19日 下午4:16:30
	  */
	 @RequestMapping(value = "/getTreedata", method = RequestMethod.POST)
		public @ResponseBody List<TreeBean> getTreedata(ModelMap model,HttpSession httpSession) throws Exception 
		{
			//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			params.add("1");//树种只显示启用状态的权限数据
			buffer.append(" and  status = ?").append(params.size());
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("code", "desc");
			
			QueryResult<Authority> rolelist = authService.getAuthList(Authority.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			List<Authority> authorities = rolelist.getResultList();
			
			List<TreeBean> treeBeanList = new ArrayList<TreeBean> ();
			
			for (Authority authority : authorities) {
				
				TreeBean treeBeanIn = new TreeBean();
				treeBeanIn.setId(authority.getId());
//				treeBeanIn.setParent(true);
				treeBeanIn.setName(authority.getAuthName());
				treeBeanIn.setOpen(true);
				treeBeanIn.setpId(authority.getParentAuth());
				treeBeanList.add(treeBeanIn);
			}
			
			return treeBeanList;
		}
	 
	 /**
	  * 
	 * @Description: TODO(根据条件获取角色列表数据) 
	 * @author bann@sdfcp.com
	 * @date 2015年10月19日 下午2:33:23
	  */
		@RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getRoleList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
//				@RequestParam(value="status",required=false) String status,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			Map<String,Object> returnData = new HashMap<String,Object> ();
			
			//放置分页参数
			Pageable pageable = new PageRequest(page-1,rows);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
//			if(null != status && !"".equals(status))
//			{
//				params.add(status);//只查询有效的数据
//				buffer.append(" and status = ?").append(params.size());
//			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("code", "desc");
			
			QueryResult<Role> rolelist = roleService.getRolelist(Role.class, buffer.toString(),
					params.toArray(), orderBy, pageable);
			//处理返回数据
			List<Role> roles = rolelist.getResultList();
			Long totalrow = rolelist.getTotalRecord();
			
			List<RoleDTO> roleDTOs = roleService.toDTOS(roles);
			
			returnData.put("rows", roleDTOs);
			returnData.put("total", totalrow);
			
			return returnData;
		}
		
		
		/**
		 * 
		* @Description: TODO(获取上级角色列表数据) 
		* @author bann@sdfcp.com
		* @date 2015年10月19日 下午2:55:48
		 */
		@RequestMapping(value = "/getParentRole", method = RequestMethod.POST)
		public @ResponseBody List<RoleAuthBean> getParentRole(
//				@RequestParam(value="status",required=false) String status,
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			List<RoleAuthBean> roleBeans = new ArrayList<RoleAuthBean> ();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			if(null != id && !"".equals(id))
			{
				params.add(id);//只查询有效的数据
				buffer.append(" and  id != ?").append(params.size());
			}
			
//			if(null != status && !"".equals(status))
//			{
//				params.add(status);//只查询有效的数据
//				buffer.append(" and  status = ?").append(params.size());
//			}
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "asc");
			
			QueryResult<Role> roleList = roleService.getRolelist(Role.class, buffer.toString(), params.toArray(), 
					orderBy, pageable);
			
			List<Role> roles = roleList.getResultList();
			
			for (Role role : roles) 
			{
				RoleAuthBean roleBean = new RoleAuthBean();
				
				roleBean.setId(role.getId());
				roleBean.setName(role.getName());
				
				roleBeans.add(roleBean);
			}
			
			
			
			return roleBeans;
		}
	 
		/**
		 * 
		* @Description: TODO(校验角色数据) 
		* @author bann@sdfcp.com
		* @date 2015年10月20日 下午2:14:25
		 */
		@RequestMapping(value = "/checkRoleValue", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkRoleValue(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="code",required=false) String code,
				@RequestParam(value="name",required=false) String name,
				@RequestParam(value="parentRole",required=false) String parentRole,
				ModelMap model,HttpSession httpSession) throws Exception {
			
			ResultBean resultBean = new ResultBean ();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			if(null != code && !"".equals(code))
			{
				params.add(code);
				buffer.append(" and code = ?").append(params.size());
			}
			
			if(null != name && !"".equals(name))
			{
				params.add(name);
				buffer.append(" and name = ?").append(params.size());
			}
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//连接父级权限id查询条件，用来查询当前id下是否有子级角色
			if(null != parentRole && !"".equals(parentRole))
			{
				params.add(parentRole);
				buffer.append(" and parentRole = ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<Role> authlist = roleService.getRolelist(Role.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(authlist.getResultList().size()>0)
			{
				resultBean.setExist(true);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
			}
			else
			{
				resultBean.setExist(false);
			}
			
			return resultBean;
			
		}
	 
	 
	 
	 
	 
	 
	 
}
