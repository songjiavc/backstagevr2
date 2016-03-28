package com.sdf.manager.userGroup.controller;

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

import com.alibaba.fastjson.JSONObject;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.goods.dto.RelaSdfGoodProductDTO;
import com.sdf.manager.goods.entity.Goods;
import com.sdf.manager.station.application.dto.StationDto;
import com.sdf.manager.station.controller.StationController;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.userGroup.dto.UserGroupDTO;
import com.sdf.manager.userGroup.entity.UserGroup;
import com.sdf.manager.userGroup.service.UserGroupService;


@Controller
@RequestMapping("userGroup")
public class UserGroupController // extends GlobalExceptionHandler 
{
	
	Logger logger = LoggerFactory.getLogger(UserGroupController.class);

	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private StationController stationcontroller;
	
	@Autowired
	private StationService stationService;
	
	@RequestMapping(value = "/getDetailUserGroup", method = RequestMethod.GET)
	public @ResponseBody UserGroupDTO getDetailUserGroup(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		UserGroupDTO userGroupDTO = new UserGroupDTO();
		
		UserGroup userGroup = userGroupService.getUserGroupById(id);
		
		userGroupDTO = userGroupService.toDTO(userGroup);
		
		return userGroupDTO;
	}
	
	/**
	 * 
	* @Title: getUsergroupList
	* @Description: 根据筛选条件获取通行证组数据
	* @Author : banna
	* @param @param page
	* @param @param rows
	* @param @param userGroupName
	* @param @param userGroupCode
	* @param @param userGroupDescription
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	 @RequestMapping(value = "/getUsergroupList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getUsergroupList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="userGroupName",required=false) String userGroupName,
				@RequestParam(value="userGroupCode",required=false) String userGroupCode,
				@RequestParam(value="userGroupDescription",required=false) String userGroupDescription,
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
			
			//只查询当前登录用户创建的通行证组数据
			params.add(LoginUtils.getAuthenticatedUserCode(httpSession));
			buffer.append(" and creater = ?").append(params.size());
			
			//连接查询条件
			if(null != userGroupName&&!"".equals(userGroupName.trim()))
			{
				params.add("%"+userGroupName+"%");
				buffer.append(" and userGroupName like ?").append(params.size());
			}
			
			if(null != userGroupCode&&!"".equals(userGroupCode.trim()))
			{
				params.add("%"+userGroupCode+"%");
				buffer.append(" and userGroupCode like ?").append(params.size());
			}
		 	
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<UserGroup> userGrouplist = userGroupService.getAppversionList(UserGroup.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<UserGroup> userGroups = userGrouplist.getResultList();
			
			Long totalrow = userGrouplist.getTotalRecord();
			
			//将实体转换为dto
			List<UserGroupDTO> uGroupDTOs = userGroupService.toRDTOS(userGroups);
			
			returnData.put("rows", uGroupDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	 
	 /**
	  * 
	 * @Title: getStationOfUsergroup
	 * @Description: 获取当前用户组id获取其下属的通行证数据
	 * @Author : banna
	 * @param @param id
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return List<StationDto>    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/getStationOfUsergroup", method = RequestMethod.GET)
	 public @ResponseBody List<StationDto> getStationOfUsergroup(
			@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	 {
		UserGroup userGroup = new UserGroup();
		
		userGroup = userGroupService.getUserGroupById(id);
		
		List<Station> stations = null;
		List<StationDto> stationDtos = null;
		if(null != userGroup)
		{
			stations = userGroup.getStations();
			stationDtos = stationcontroller.toDtos(stations);
		}
	 	
		 return stationDtos;
	 }
	 
	 /**
	  * 
	 * @Title: saveOrUpdate
	 * @Description: 保存通行证组数据
	 * @Author : banna
	 * @param @param id
	 * @param @param userGroupCode
	 * @param @param userGroupName
	 * @param @param userGroupDescription
	 * @param @param stationList
	 * @param @param appVersionList
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="userGroupCode",required=false) String userGroupCode,
				@RequestParam(value="userGroupName",required=false) String userGroupName,
				@RequestParam(value="userGroupDescription",required=false) String userGroupDescription,
				@RequestParam(value="stationdata",required=false) String stationdata,//绑定的通行证数据list
				@RequestParam(value="appVersionList",required=false) String appVersionList,//绑定的应用版本数据list
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   UserGroup userGroup = userGroupService.getUserGroupById(id);
		   
		   JSONObject stations = JSONObject.parseObject(stationdata);
		   List<String> stationIds =  (List<String>) stations.get("keys");
		   
		   if(null != userGroup)
		   {//通行证组数据不为空，则进行修改操作
			   
//			   userGroup.setUserGroupCode(userGroupCode);
			   userGroup.setUserGroupName(userGroupName);
			   userGroup.setUserGroupDescription(userGroupDescription);
			   
			 //整理通行证组下的通行证数据
			   List<Station> sList = new ArrayList<Station>();
			   for (String stationId : stationIds) {
				   Station station = stationService.getSationById(stationId);
				   sList.add(station);
			   }
			   userGroup.setStations(sList);//放置通行证数据，创建通行证组合通行证的关联关系
			   
			   userGroup.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   userGroup.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   userGroupService.update(userGroup);
			   
			   resultBean.setMessage("修改通行证组信息成功!");
			   resultBean.setStatus("success");
			  
			   //日志输出
				 logger.info("修改通行证组--通行证组id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   userGroup = new UserGroup();
			   userGroup.setUserGroupCode(userGroupCode);//在添加的时候添加通行证组编码的数据
			   userGroup.setUserGroupName(userGroupName);
			   userGroup.setUserGroupDescription(userGroupDescription);
			   userGroup.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   userGroup.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   //整理通行证组下的通行证数据
			   List<Station> sList = new ArrayList<Station>();
			   for (String stationId : stationIds) {
				   Station station = stationService.getSationById(stationId);
				   sList.add(station);
			   }
			   userGroup.setStations(sList);//放置通行证数据，创建通行证组合通行证的关联关系
			   
			   userGroup.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   userGroup.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   userGroup.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   userGroup.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   userGroup.setIsDeleted("1");
			   
			   userGroupService.save(userGroup);
			   
			   resultBean.setMessage("添加通行证组信息成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加通行证组--通行证组code="+userGroupCode+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 
	 /**
	  * 
	 * @Title: deleteUsergroups
	 * @Description: 删除通行证组数据
	 * @Author : banna
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/deleteUsergroups", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteUsergroups(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 UserGroup userGroup;
		 for (String id : ids) 
			{
			 	userGroup = userGroupService.getUserGroupById(id);
			 	if(null != userGroup)
			 	{
			 		userGroup.setIsDeleted("0");
			 		userGroup.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		userGroup.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		userGroupService.update(userGroup);
			 		
			 		 //日志输出
					 logger.info("删除通行证组--通行证组id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	
	 /**
	  * 
	 * @Title: checkUsergroupName
	 * @Description: 校验通行证组名称的方法
	 * @Author : banna
	 * @param @param id
	 * @param @param name
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/checkUsergroupName", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkUsergroupName(
				@RequestParam(value="id",required=false) String id,//用来校验通行证组名称唯一的条件
				@RequestParam(value="name",required=false) String name,
				@RequestParam(value="code",required=false) String code,
				ModelMap model,HttpSession httpSession) throws Exception {
			
			ResultBean resultBean = new ResultBean ();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,10000);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			if(null != name && !"".equals(name))
			{
				params.add(name);
				buffer.append(" and userGroupName = ?").append(params.size());
			}
			
			if(null != code && !"".equals(code))
			{//校验通行证组编码的唯一性
				params.add(code);
				buffer.append(" and userGroupCode = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<UserGroup> uGrouplist = userGroupService.getAppversionList(UserGroup.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(uGrouplist.getResultList().size()>0)
			{
				resultBean.setExist(true);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
			}
			else
			{
				resultBean.setExist(false);
			}
			
			return resultBean;
			
		}
	 
	 /**
	  * 
	  * @Title: checkDeletedvisible
	  * @Description: 校验用户组是否可以被删除
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/checkDeletedvisible", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkDeletedvisible(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 boolean deletedFlag = true;
		 
		 UserGroup userGroup = userGroupService.getUserGroupById(id);
		 
		 //若通行证组与“应用广告”或“通告”或“公司公告”或“应用公告”有关联，则不可以被删除
		 if(userGroup.getAdvertisements().size()>0||userGroup.getAnnouncements().size()>0||userGroup.getCompanyNotices().size()>0
				 ||userGroup.getNotices().size()>0)
		 {
			 deletedFlag = false;
		 }
		 
		 
		 resultBean.setExist(deletedFlag);
		 return resultBean;
	 }
	
}
