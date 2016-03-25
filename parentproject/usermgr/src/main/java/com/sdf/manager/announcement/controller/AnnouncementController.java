package com.sdf.manager.announcement.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.sdf.manager.announcement.dto.AnnouncementDTO;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.announcement.entity.AnnouncementAndArea;
import com.sdf.manager.announcement.service.AnnouncementAndAreaService;
import com.sdf.manager.announcement.service.AnnouncementService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.user.service.UserService;
import com.sdf.manager.userGroup.dto.UserGroupDTO;
import com.sdf.manager.userGroup.entity.UserGroup;
import com.sdf.manager.userGroup.service.UserGroupService;

/**
 * 
 * @ClassName: AnnouncementController
 * @Description: TODO:通告模块控制层
 * @author: banna
 * @date: 2016年2月15日 下午12:31:15
 */
@Controller
@RequestMapping("announcement")
public class AnnouncementController {

	private Logger logger = LoggerFactory.getLogger(AnnouncementController.class);
	
	@Autowired
	private AnnouncementService announcementService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private AnnouncementAndAreaService announcementAndAreaService;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private UserService userService;
	
	public static final String ANNOUNCEMENT_FB="1";
	public static final String ANNOUNCEMENT_BC="0";
	
	/**
	 * 
	 * @Title: getDetailAnnouncement
	 * @Description: 根据id获取通告数据详情
	 * @author:banna
	 * @return: AnnouncementDTO
	 */
	@RequestMapping(value = "/getDetailAnnouncement", method = RequestMethod.GET)
	public @ResponseBody AnnouncementDTO getDetailAnnouncement(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		Announcement announcement = announcementService.getAnnouncementById(id);
		
		AnnouncementDTO announcementDTO = announcementService.toDTO(announcement);
		
		
		return announcementDTO;
	}
	
	/**
	 * 
	 * @Title: getAnnouncementList
	 * @Description: 根据筛选条件获取通告数据
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	@RequestMapping(value = "/getAnnouncementList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAnnouncementList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="province",required=false) String province,//省份
			@RequestParam(value="city",required=false) String city,//市
			@RequestParam(value="announcementName",required=false) String announcementName,//通告名称
			@RequestParam(value="startTime",required=false) String startTime,//应用公告有效结束时间
			@RequestParam(value="endTime",required=false) String endTime,//应用公告有效结束时间
			@RequestParam(value="lotteryType",required=false) String lotteryType,//应用公告彩种
			@RequestParam(value="announceStatus",required=false) String announceStatus,//通告状态，发布or保存
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
		
		
		/*if(null != province && !"".equals(province.trim())&&!Constants.PROVINCE_ALL.equals(province))
		{
			params.add(province);
			buffer.append(" and appAdAndAreas.province = ?").append(params.size());
		}
		
		if(null != city && !"".equals(city.trim()) && !Constants.CITY_ALL.equals(city))
		{
			params.add(city);
			buffer.append(" and appAdAndAreas.city = ?").append(params.size());
		}*/
		
		if(null != announcementName && !"".equals(announcementName.trim()))
		{
			params.add("%"+announcementName+"%");
			buffer.append(" and announcementName like ?").append(params.size());
		}
		
		if(null != startTime&& !"".equals(startTime.trim()) )//
		{
			Date st =  DateUtil.formatStringToDate(startTime.toString(), DateUtil.SIMPLE_DATE_FORMAT);//将前台传过来的字符串日期转换为date时，使用的格式应该与接收的字符串一样，即：若2014-02-01则应该用DateUtil.SIMPLE_DATE_FORMAT，在date和时间戳之间可以跨越格式转换
			params.add(DateUtil.formatDateToTimestamp(st,  DateUtil.FULL_DATE_FORMAT));
			buffer.append(" and startTime >= ?").append(params.size());
		}
		
		if(null != endTime && !"".equals(endTime.trim()))//
		{
			Date et =  DateUtil.formatStringToDate(endTime.toString(), DateUtil.SIMPLE_DATE_FORMAT);
			params.add(DateUtil.formatDateToTimestamp(et,  DateUtil.FULL_DATE_FORMAT));
			buffer.append(" and endTime <= ?").append(params.size());
		}
		
		if(null != lotteryType && !"".equals(lotteryType.trim()))
		{
			params.add(lotteryType);
			buffer.append(" and lotteryType = ?").append(params.size());
		}
		
		if(null != announceStatus && !"".equals(announceStatus.trim()))
		{
			params.add(announceStatus);
			buffer.append(" and announceStatus = ?").append(params.size());
		}
	 	
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("id", "desc");
		
		QueryResult<Announcement> annResult = announcementService.getAnnouncementList(Announcement.class,
				buffer.toString(), params.toArray(),orderBy, pageable);
		List<Announcement> announceList = annResult.getResultList();
		Long totalrow = annResult.getTotalRecord();
		
		//将实体转换为dto
		List<AnnouncementDTO> announcementDTOs = announcementService.toRDTOS(announceList);
		
		returnData.put("rows", announcementDTOs);
		returnData.put("total", totalrow);
	 	
	 	return returnData;
	}
	
	
	/**
	 * 
	 * @Title: saveOrUpdate
	 * @Description: 保存或修改通告数据
	 * @author:banna
	 * @return: ResultBean
	 */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="announcementName",required=false) String announcementName,
				@RequestParam(value="announcementContent",required=false) String announcementContent,
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//应用广告的有效结束时间
				@RequestParam(value="announceStatus",required=false) String announceStatus,//应用广告的状态，0：保存1：发布
				@RequestParam(value="stationGdata",required=false) String stationGdata,//绑定的通行证组数据list
				@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   Announcement announcement = announcementService.getAnnouncementById(id);
		   
		   //提取绑定的通行证组数据
		   JSONObject userGroups = JSONObject.parseObject(stationGdata);
		   List<String> uGroups =  (List<String>) userGroups.get("keys");
		   
		   //提取绑定的区域数据
		   JSONObject areas = JSONObject.parseObject(areadata);
		   List<String> cityIds =  (List<String>) areas.get("keys");
		   
		   
		   if(null != announcement)
		   {//通告数据不为空，则进行修改操作
			   
			   announcement.setAnnouncementName(announcementName);
			   announcement.setAnnouncementContent(announcementContent);
			   announcement.setLotteryType(lotteryType);
			   announcement.setAnnounceStatus(announceStatus);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   announcement.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   announcement.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   announcement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   announcement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   //1.整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   announcement.setUserGroups(uList);
			   
			   //2.整理并放置区域数据
			   
			   //清空上次绑定的区域数据
			   List<AnnouncementAndArea> announcementAreasBefore = announcement.getAnnouncementAndAreas();
			   for (AnnouncementAndArea announcementAndArea : announcementAreasBefore) {
				   announcementAndAreaService.delete(announcementAndArea);
				   logger.info("删除通告与区域关联数据--关联id="+announcementAndArea.getId()+"==通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   }
			   
			   //重新绑定区域数据
			   List<AnnouncementAndArea> announcementAndAreas = new ArrayList<AnnouncementAndArea>();
			   for (String cityId : cityIds) {
				
				   AnnouncementAndArea announcementArea = new AnnouncementAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   announcementArea.setAnnouncement(announcement);
				   announcementArea.setCity(city.getCcode());
				   announcementArea.setProvince(city.getProvinceCode());
				   
				   announcementAndAreaService.save(announcementArea);
				   
				   announcementAndAreas.add(announcementArea);
			   }
			   
			   announcement.setAnnouncementAndAreas(announcementAndAreas);
			   
			 
			   announcementService.update(announcement);
			  
			   resultBean.setMessage("修改通告成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改通告--通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   announcement = new Announcement();
			   announcement.setId(UUID.randomUUID().toString());
			   announcement.setAnnouncementName(announcementName);
			   announcement.setAnnouncementContent(announcementContent);
			   announcement.setLotteryType(lotteryType);
			   announcement.setAnnounceStatus(announceStatus);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   announcement.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   announcement.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   announcement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   announcement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   announcement.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   announcement.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   announcement.setIsDeleted("1");
			   
			   
			   //整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   announcement.setUserGroups(uList);
			   
			   
			   
			   announcementService.save(announcement);//保存通告数据
			   
			   
			   //整理并放置区域数据
			   List<AnnouncementAndArea> announcementAndAreas = new ArrayList<AnnouncementAndArea>();
			   for (String cityId : cityIds) {
				
				   AnnouncementAndArea announcementArea = new AnnouncementAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   announcementArea.setAnnouncement(announcement);
				   announcementArea.setCity(city.getCcode());
				   announcementArea.setProvince(city.getProvinceCode());
				   
				   announcementAndAreaService.save(announcementArea);
				   
				   announcementAndAreas.add(announcementArea);
			   }
			   
			   announcement.setAnnouncementAndAreas(announcementAndAreas);
			   
			 
			   announcementService.update(announcement);
			  
			   
			 
			   
			   resultBean.setMessage("添加通告数据成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加通告--通告id="+announcement.getId()+"操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 
	  * @Title: deleteAnnouncement
	  * @Description: 删除通告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/deleteAnnouncement", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteAnnouncement(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Announcement announcement;
		 List<UserGroup> userGroups = new ArrayList<UserGroup>();
		 List<AnnouncementAndArea> announcementAndAreas = new ArrayList<AnnouncementAndArea>();
		 for (String id : ids) 
			{
			 announcement = announcementService.getAnnouncementById(id);
			 if(null != announcement)
			 	{
					 announcement.setIsDeleted("0");
					 announcement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					 announcement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		//清空删除的通告与其他数据的关联，放置产生冗余关联数据
					 announcement.setUserGroups(userGroups);
					
					 
					 //删除关联的区域数据
					 List<AnnouncementAndArea> announcementAreasBefore = announcement.getAnnouncementAndAreas();
					   for (AnnouncementAndArea announcementAndArea : announcementAreasBefore) {
						   announcementAndAreaService.delete(announcementAndArea);
						   logger.info("删除通告与区域关联数据--关联id="+announcementAndArea.getId()+"==通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   }
					 announcement.setAnnouncementAndAreas(announcementAndAreas);
					 announcementService.update(announcement);
			 		 //日志输出
					 logger.info("删除通告--通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: publishAnnouncement
	  * @Description: 批量发布通告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/publishAnnouncement", method = RequestMethod.POST)
		public @ResponseBody ResultBean  publishAnnouncement(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Announcement announcement;
		 for (String id : ids) 
			{
			 announcement = announcementService.getAnnouncementById(id);
			 //只发布数据不为空且发布状态是“保存”的通告数据
			 if(null != announcement&&AnnouncementController.ANNOUNCEMENT_BC.equals(announcement.getAnnounceStatus()))
			 	{
					 announcement.setAnnounceStatus(AnnouncementController.ANNOUNCEMENT_FB);
					 announcement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					 announcement.setModifyTime(new Timestamp(System.currentTimeMillis()));
					 announcementService.update(announcement);
			 		 //日志输出
					 logger.info("发布通告--通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "发布成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: getStationOfUsergroup
	  * @Description: 获取通告对应的通行证组数据
	  * @author:banna
	  * @return: List<UserGroupDTO>
	  */
	 @RequestMapping(value = "/getStationOfUsergroup", method = RequestMethod.GET)
		public @ResponseBody List<UserGroupDTO>  getStationOfUsergroup(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Announcement announcement = announcementService.getAnnouncementById(id);
		 
		 List<UserGroup> userGroups = announcement.getUserGroups();
		 
		 List<UserGroupDTO> userGroupDTOs = userGroupService.toRDTOS(userGroups);
		 
		 return userGroupDTOs;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getAreasOfAnnouncement
	  * @Description: 获取通告数据对应的区域数据
	  * @author:banna
	  * @return: List<String>
	  */
	 @RequestMapping(value = "/getAreasOfAnnouncement", method = RequestMethod.GET)
		public @ResponseBody List<String>  getAreasOfAnnouncement(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Announcement announcement = announcementService.getAnnouncementById(id);
		 
		 List<AnnouncementAndArea> announcementAreas = announcement.getAnnouncementAndAreas();
		 
		 List<String> cityIds = new ArrayList<String>();
		 
		 for (AnnouncementAndArea announcementArea : announcementAreas) {
			
			 cityIds.add(announcementArea.getCity());
		}
		 
		 return cityIds;
		 
	 }
}
