package com.sdf.manager.companyNotice.controller;

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
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.dto.ComnoticeDTO;
import com.sdf.manager.companyNotice.entity.ComnoticeAndArea;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.companyNotice.service.ComnoticeAndAreaService;
import com.sdf.manager.companyNotice.service.CompanynoticeService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.user.service.UserService;
import com.sdf.manager.userGroup.dto.UserGroupDTO;
import com.sdf.manager.userGroup.entity.UserGroup;
import com.sdf.manager.userGroup.service.UserGroupService;

/**
 * 
 * @ClassName: CompanynoticeController
 * @Description: TODO:公司公告模块控制层
 * @author: banna
 * @date: 2016年2月15日 下午12:32:08
 */
@Controller
@RequestMapping("companynotice")
public class CompanynoticeController extends GlobalExceptionHandler {

private Logger logger = LoggerFactory.getLogger(CompanynoticeController.class);
	
	
	@Autowired
	private CompanynoticeService companynoticeService;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ComnoticeAndAreaService comnoticeAndAreaService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	
	public static final String COMPANYNOTIE_FB="1";//公司公告的发布状态
	public static final String COMPANYNOTIE_BC="0";//公司公告的保存状态
	
	/**
	 * 
	 * @Title: getDetailCompanynotice
	 * @Description: 根据id获取公司公告详情数据
	 * @author:banna
	 * @return: ComnoticeDTO
	 */
	@RequestMapping(value = "/getDetailCompanynotice", method = RequestMethod.GET)
	public @ResponseBody ComnoticeDTO getDetailCompanynotice(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		CompanyNotice companyNotice = companynoticeService.getCompanyNoticeById(id);
		

		ComnoticeDTO comnoticeDTO = companynoticeService.toDTO(companyNotice);
		
		return comnoticeDTO;
	}
	
	/**
	 * 
	 * @Title: getComnoticeList
	 * @Description: 根据筛选获取公司公告数据
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	@RequestMapping(value = "/getComnoticeList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getComnoticeList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="province",required=false) String province,//省份
			@RequestParam(value="city",required=false) String city,//市
			@RequestParam(value="comnoticeName",required=false) String comnoticeName,//通告名称
			@RequestParam(value="startTime",required=false) String startTime,//应用公告有效结束时间
			@RequestParam(value="endTime",required=false) String endTime,//应用公告有效结束时间
			@RequestParam(value="lotteryType",required=false) String lotteryType,//应用公告彩种
			@RequestParam(value="comnoticeStatus",required=false) String comnoticeStatus,//通告状态，发布or保存
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
		
		//只查询当前登录用户创建的公司公告数据
		if(!"admin".equals(LoginUtils.getAuthenticatedUserCode(httpSession)))
		{
			params.add(LoginUtils.getAuthenticatedUserCode(httpSession));
			buffer.append(" and creater = ?").append(params.size());
		}
		
		
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
		
		if(null != comnoticeName && !"".equals(comnoticeName.trim()))
		{
			params.add("%"+comnoticeName+"%");
			buffer.append(" and comnoticeName like ?").append(params.size());
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
		
		if(null != comnoticeStatus && !"".equals(comnoticeStatus.trim()))
		{
			params.add(comnoticeStatus);
			buffer.append(" and comnoticeStatus = ?").append(params.size());
		}
	 	
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("createrTime", "desc");
		
		QueryResult<CompanyNotice> comResult = companynoticeService.getCompanynoticeList(CompanyNotice.class,
				buffer.toString(), params.toArray(),orderBy, pageable);
		List<CompanyNotice> companyNotices = comResult.getResultList();
		Long totalrow = comResult.getTotalRecord();
		
		//将实体转换为dto
		List<ComnoticeDTO> comnoticeDTOs = companynoticeService.toRDTOS(companyNotices);
		
		returnData.put("rows", comnoticeDTOs);
		returnData.put("total", totalrow);
	 	
	 	return returnData;
	}
	
	
	/**
	 * 
	 * @Title: saveOrUpdate
	 * @Description: 保存或修改公司公告数据
	 * @author:banna
	 * @return: ResultBean
	 */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="comnoticeName",required=false) String comnoticeName,
				@RequestParam(value="comnoticeContent",required=false) String comnoticeContent,
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//公司公告的有效结束时间
				@RequestParam(value="comnoticeStatus",required=false) String comnoticeStatus,//公司公告的状态，0：保存1：发布
				@RequestParam(value="stationGdata",required=false) String stationGdata,//绑定的通行证组数据list
				@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   CompanyNotice companyNotice = companynoticeService.getCompanyNoticeById(id);
		   
		   //提取绑定的通行证组数据
		   JSONObject userGroups = JSONObject.parseObject(stationGdata);
		   List<String> uGroups =  (List<String>) userGroups.get("keys");
		   
		   //提取绑定的区域数据
		   JSONObject areas = JSONObject.parseObject(areadata);
		   List<String> cityIds =  (List<String>) areas.get("keys");
		   
		   
		   if(null != companyNotice)
		   {//通告数据不为空，则进行修改操作
			   
			   companyNotice.setComnoticeName(comnoticeName);
			   companyNotice.setComnoticeContent(comnoticeContent);
			   companyNotice.setLotteryType(lotteryType);
			   companyNotice.setComnoticeStatus(comnoticeStatus);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   companyNotice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   companyNotice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   companyNotice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   companyNotice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   //1.整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   companyNotice.setUserGroups(uList);
			   
			   //2.整理并放置区域数据
			   
			   //清空上次绑定的区域数据
			   List<ComnoticeAndArea> comnoticeAndAreasBefore = companyNotice.getComnoticeAndAreas();
			   for (ComnoticeAndArea comnoticeAndArea : comnoticeAndAreasBefore) {
				   comnoticeAndAreaService.delete(comnoticeAndArea);
				   logger.info("删除公司公告与区域关联数据--关联id="+comnoticeAndArea.getId()+"==通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   }
			   
			   //重新绑定区域数据
			   List<ComnoticeAndArea> comnoticeAndAreas = new ArrayList<ComnoticeAndArea>();
			   for (String cityId : cityIds) {
				
				   ComnoticeAndArea comnoticeAndArea = new ComnoticeAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   comnoticeAndArea.setCompanyNotice(companyNotice);
				   comnoticeAndArea.setCity(city.getCcode());
				   comnoticeAndArea.setProvince(city.getProvinceCode());
				   
				   comnoticeAndAreaService.save(comnoticeAndArea);
				   
				   comnoticeAndAreas.add(comnoticeAndArea);
			   }
			   
			   companyNotice.setComnoticeAndAreas(comnoticeAndAreas);
			   
			 
			   companynoticeService.update(companyNotice);
			  
			   resultBean.setMessage("修改公司公告成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改通告--公司公告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   companyNotice = new CompanyNotice();
			   companyNotice.setId(UUID.randomUUID().toString());
			   companyNotice.setComnoticeName(comnoticeName);
			   companyNotice.setComnoticeContent(comnoticeContent);
			   companyNotice.setLotteryType(lotteryType);
			   companyNotice.setComnoticeStatus(comnoticeStatus);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   companyNotice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   companyNotice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   companyNotice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   companyNotice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   companyNotice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   companyNotice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   companyNotice.setIsDeleted("1");
			   
			   
			   //整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   companyNotice.setUserGroups(uList);
			   
			   
			   
			   companynoticeService.save(companyNotice);//保存公司公告数据
			   
			   
			   //整理并放置区域数据
			   List<ComnoticeAndArea> comnoticeAndAreas = new ArrayList<ComnoticeAndArea>();
			   for (String cityId : cityIds) {
				
				   ComnoticeAndArea comnoticeAndArea = new ComnoticeAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   comnoticeAndArea.setCompanyNotice(companyNotice);
				   comnoticeAndArea.setCity(city.getCcode());
				   comnoticeAndArea.setProvince(city.getProvinceCode());
				   
				   comnoticeAndAreaService.save(comnoticeAndArea);
				   
				   comnoticeAndAreas.add(comnoticeAndArea);
			   }
			   
			   companyNotice.setComnoticeAndAreas(comnoticeAndAreas);
			   
			 
			   companynoticeService.update(companyNotice);
			  
			   
			 
			   
			   resultBean.setMessage("添加公司公告数据成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加公司公告--公司公告id="+companyNotice.getId()+"操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 
	  * @Title: deleteComnotice
	  * @Description: 删除公司公告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/deleteComnotice", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteComnotice(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 CompanyNotice companyNotice;
		 List<UserGroup> userGroups = new ArrayList<UserGroup>();
		 List<ComnoticeAndArea> comnoticeAndAreas = new ArrayList<ComnoticeAndArea>();
		 for (String id : ids) 
			{
			 companyNotice = companynoticeService.getCompanyNoticeById(id);
			 if(null != companyNotice)
			 	{
				 companyNotice.setIsDeleted("0");
				 companyNotice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				 companyNotice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		//清空删除的通告与其他数据的关联，放置产生冗余关联数据
				 companyNotice.setUserGroups(userGroups);
				 
				 
				 //删除关联的区域数据，也可以通过公司公告的数据获取其关联的区域数据进行删除
				 List<ComnoticeAndArea> comnoticeAndAreasBefore = companyNotice.getComnoticeAndAreas();
				   for (ComnoticeAndArea comnoticeAndArea : comnoticeAndAreasBefore) {
					   comnoticeAndAreaService.delete(comnoticeAndArea);
					   logger.info("删除公司公告与区域关联数据--关联id="+comnoticeAndArea.getId()+"==通告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   }
				 companyNotice.setComnoticeAndAreas(comnoticeAndAreas);	
				 companynoticeService.update(companyNotice);
			 		 //日志输出
					 logger.info("删除公司公告--公司公告-id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: publishComnotice
	  * @Description: 批量发布公司公告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/publishComnotice", method = RequestMethod.POST)
		public @ResponseBody ResultBean  publishComnotice(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 CompanyNotice companyNotice;
		 for (String id : ids) 
			{
			 companyNotice = companynoticeService.getCompanyNoticeById(id);
			 if(null != companyNotice&&CompanynoticeController.COMPANYNOTIE_BC.equals(companyNotice.getComnoticeStatus()))
			 	{
				 companyNotice.setComnoticeStatus(CompanynoticeController.COMPANYNOTIE_FB);
				 companyNotice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				 companyNotice.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 companynoticeService.update(companyNotice);
			 		 //日志输出
					 logger.info("发布公司公告--公司公告-id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
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
	  * @Description: 获取公告公告对应的通行证组数据
	  * @author:banna
	  * @return: List<UserGroupDTO>
	  */
	 @RequestMapping(value = "/getStationOfUsergroup", method = RequestMethod.GET)
		public @ResponseBody List<UserGroupDTO>  getStationOfUsergroup(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 CompanyNotice companyNotice = companynoticeService.getCompanyNoticeById(id);
		 
		 List<UserGroup> userGroups = companyNotice.getUserGroups();
		 
		 List<UserGroupDTO> userGroupDTOs = userGroupService.toRDTOS(userGroups);
		 
		 return userGroupDTOs;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getAreasOfComnotice
	  * @Description: 获取公司公告数据对应的区域数据
	  * @author:banna
	  * @return: List<String>
	  */
	 @RequestMapping(value = "/getAreasOfComnotice", method = RequestMethod.GET)
		public @ResponseBody List<String>  getAreasOfComnotice(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 CompanyNotice companyNotice = companynoticeService.getCompanyNoticeById(id);
		 
		 List<ComnoticeAndArea> comnoticeAndAreas = companyNotice.getComnoticeAndAreas();
		 
		 List<String> cityIds = new ArrayList<String>();
		 
		 for (ComnoticeAndArea comnoticeAndArea : comnoticeAndAreas) {
			
			 cityIds.add(comnoticeAndArea.getCity());
		}
		 
		 return cityIds;
		 
	 }
}

