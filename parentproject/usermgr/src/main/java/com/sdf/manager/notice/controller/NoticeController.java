package com.sdf.manager.notice.controller;

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
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.dto.ForecastDTO;
import com.sdf.manager.notice.dto.NoticeDTO;
import com.sdf.manager.notice.entity.AppNoticeAndArea;
import com.sdf.manager.notice.entity.ForecastMessage;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.service.AppNoticeAndAreaService;
import com.sdf.manager.notice.service.ForecastService;
import com.sdf.manager.notice.service.NoticeService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.service.UserService;
import com.sdf.manager.userGroup.dto.UserGroupDTO;
import com.sdf.manager.userGroup.entity.UserGroup;
import com.sdf.manager.userGroup.service.UserGroupService;

/**
 * 
 * @ClassName: NoticeController
 * @Description: TODO:应用公告模块控制层
 * @author: banna
 * @date: 2016年2月15日 下午12:32:53
 */
@Controller
@RequestMapping("notice")
public class NoticeController extends GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(NoticeController.class);
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private AppNoticeAndAreaService appNoticeAndAreaService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private ForecastService forecastService;
	
	@Autowired
	private OuterInterfaceService outerInterfaceService;
	
	public static final String NOTICE_STATUS_FB="1";//应用公告发布状态位
	public static final String NOTICE_STATUS_BC="0";//应用公告保存状态位
	public static final String APP_CATEGORY_SJX="0";//省中心公告
	public static final String APP_CATEGORY_CITY="1";//市中心公告
	public static final String APP_CATEGORY_COMPANY="2";//公司普通公告
	public static final String APP_CATEGORY_COMPANY_KAIJIANG="3";//公司开奖公告
	public static final String APP_CATEGORY_FORCAST="4";//公告预测类公告
	
	
	/**
	 * 
	 * @Title: getDetailNotice
	 * @Description: 根据id获取应用公告详情
	 * @author:banna
	 * @return: NoticeDTO
	 */
	@RequestMapping(value = "/getDetailNotice", method = RequestMethod.GET)
	public @ResponseBody NoticeDTO getDetailNotice(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		Notice notice = noticeService.getNoticeById(id);
		
		NoticeDTO noticeDTO = noticeService.toDTO(notice);
		
		
		return noticeDTO;
	}
	
	
	/**
	 * 
	 * @Title: getNoticeList
	 * @Description: 根据筛选条件查询应用公告数据
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	 @RequestMapping(value = "/getNoticeList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getNoticeList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="province",required=false) String province,//省份
				@RequestParam(value="city",required=false) String city,//市
				@RequestParam(value="appNoticeName",required=false) String appNoticeName,//应用公告名称
				@RequestParam(value="startTime",required=false) String startTime,//应用公告有效结束时间
				@RequestParam(value="endTime",required=false) String endTime,//应用公告有效结束时间
				@RequestParam(value="lotteryType",required=false) String lotteryType,//应用公告彩种
				@RequestParam(value="noticeStatus",required=false) String noticeStatus,//应用公告状态，发布or保存
				@RequestParam(value="appcategory",required=false) String appcategory,//应用公告的类型
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
			
			//只查询当前登录用户创建的应用公告数据
			if(null == appcategory ||!appcategory.equals(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG))
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
			
			if(null != appNoticeName && !"".equals(appNoticeName.trim()))
			{
				params.add("%"+appNoticeName+"%");
				buffer.append(" and appNoticeName like ?").append(params.size());
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
			
			if(null != noticeStatus && !"".equals(noticeStatus.trim()))
			{
				params.add(noticeStatus);
				buffer.append(" and noticeStatus = ?").append(params.size());
			}
			
			if(null != appcategory && !"".equals(appcategory.trim()))//根据应用公告的类型查询,目前只有公司内部人员在操作“开奖公告补录”模块时appcategory会有传值，且值为“3”
			{
				params.add(appcategory);
				buffer.append(" and appCategory = ?").append(params.size());
			}
			else
			{//※※※※除“开奖公告补录”模块外获取的都是非开奖公告数据※※※※
				params.add(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG);
				buffer.append(" and appCategory != ?").append(params.size());
			}
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<Notice> noticelist = noticeService.getForecastList(Notice.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
			List<Notice> notices = noticelist.getResultList();
			Long totalrow = noticelist.getTotalRecord();
			
			//将实体转换为dto
			List<NoticeDTO> noticeDTOs = noticeService.toRDTOS(notices);
			
			returnData.put("rows", noticeDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	 
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="appNoticeName",required=false) String appNoticeName,
				@RequestParam(value="appNoticeWord",required=false) String appNoticeWord,
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//应用广告的有效结束时间
				@RequestParam(value="appCategory",required=false) String appCategory,//应用公告类型
				@RequestParam(value="noticeStatus",required=false) String noticeStatus,//应用广告的状态，0：保存1：发布
				@RequestParam(value="appsdata",required=false) String appsdata,//绑定的应用数据list
				@RequestParam(value="stationGdata",required=false) String stationGdata,//绑定的通行证组数据list
				@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,
				@RequestParam(value="forecastdata",required=false) String forecastdata,//绑定的预测信息数据list,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   Notice notice = noticeService.getNoticeById(id);
		   
		 //提取绑定的应用数据
		   JSONObject appdatas = JSONObject.parseObject(appsdata);
		   List<String> apps =  (List<String>) appdatas.get("keys");
		   
		   //提取绑定的通行证组数据
		   JSONObject userGroups = JSONObject.parseObject(stationGdata);
		   List<String> uGroups =  (List<String>) userGroups.get("keys");
		   
		   //TODO:提取绑定的区域数据
		   JSONObject areas = JSONObject.parseObject(areadata);
		   List<String> cityIds =  (List<String>) areas.get("keys");
		   
		   //提取绑定的预测信息数据
		   JSONObject forecasts = JSONObject.parseObject(forecastdata);
		   List<String> forecastIds =  (List<String>) forecasts.get("keys");
		   
		   
		   if(null != notice)
		   {//应用公告数据不为空，则进行修改操作
			   
			   notice.setAppNoticeName(appNoticeName);
			   
			   notice.setAppNoticeWord(appNoticeWord);//若为预测类型的应用公告，则公告内容为空
			   
			   notice.setLotteryType(lotteryType);
			   notice.setNoticeStatus(noticeStatus);
			   notice.setAppCategory(appCategory);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   notice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   notice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   //0.整理并放置应用数据
			   List<App> aList = new ArrayList<App>();
			   for (String appId : apps) {
				   
				   App app = appService.getAppById(appId);
				   aList.add(app);
			   }
			   notice.setApps(aList);
			   
			   //1.整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   notice.setUserGroups(uList);
			   
			   //2.整理并放置区域数据
			   
			   //清空上次绑定的区域数据
			   List<AppNoticeAndArea> noticeAndAreasBefore = notice.getAppNoticeAndAreas();
			   for (AppNoticeAndArea appNoticeAndArea : noticeAndAreasBefore) {
				   appNoticeAndAreaService.delete(appNoticeAndArea);
				   logger.info("删除应用公告与区域关联数据--关联id="+appNoticeAndArea.getId()+"==应用公告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   }
			   
			   //重新绑定区域数据
			   List<AppNoticeAndArea> noticeAndAreas = new ArrayList<AppNoticeAndArea>();
			   for (String cityId : cityIds) {
				
				   AppNoticeAndArea aNoticeAndArea = new AppNoticeAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   aNoticeAndArea.setNotice(notice);
				   aNoticeAndArea.setCity(city.getCcode());
				   aNoticeAndArea.setProvince(city.getProvinceCode());
				   
				   appNoticeAndAreaService.save(aNoticeAndArea);
				   
				   noticeAndAreas.add(aNoticeAndArea);
			   }
			   
			   notice.setAppNoticeAndAreas(noticeAndAreas);
			   
			   //3.整理并放置预测信息数据,如果发布的是预测类型的应用公告才放置这个数据
			   List<ForecastMessage> forecastMessages = new ArrayList<ForecastMessage>();
			   if(NoticeController.APP_CATEGORY_FORCAST.equals(appCategory))
			   {
				   
				   for (String forecastId : forecastIds) {
					
					   ForecastMessage forecastMessage = forecastService.getForecastMessageById(forecastId);
					   
					   
					   forecastMessages.add(forecastMessage);
				   }
				   
				   notice.setForecastMessages(forecastMessages);
				   //设置预测类公告字体颜色
				   notice.setNoticeFontColor("#DC143C");
			   }
			   else{
				   notice.setForecastMessages(forecastMessages);
				   
				   if(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG.equals(appCategory))
				   {
					   //设置开奖类公告字体颜色
					   notice.setNoticeFontColor("#1E90FF");
				   }
				   else{
					   notice.setNoticeFontColor("#000000");//默认字体颜色为黑色
				   }
			   }
				   
			   
				
			   
			   
			   
			   noticeService.update(notice);
			  
			   resultBean.setMessage("修改应用公告成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改应用广告--应用广告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   notice = new Notice();
			   notice.setId(UUID.randomUUID().toString());
			   notice.setAppNoticeName(appNoticeName);
			   
			   notice.setAppNoticeWord(appNoticeWord);//若为预测类型的应用公告，则公告内容为空
			   
			   notice.setLotteryType(lotteryType);
			   notice.setNoticeStatus(noticeStatus);
			   notice.setAppCategory(appCategory);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   notice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   notice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   notice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   notice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   notice.setIsDeleted("1");
			   
			   //整理并放置应用数据
			   List<App> aList = new ArrayList<App>();
			   for (String appId : apps) {
				   
				   App app = appService.getAppById(appId);
				   aList.add(app);
			   }
			   notice.setApps(aList);
			   
			   //整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   notice.setUserGroups(uList);
			   
			   
			   //整理并放置预测信息数据,如果发布的是预测类型的应用公告才放置这个数据
			   if(NoticeController.APP_CATEGORY_FORCAST.equals(appCategory))
			   {
				   List<ForecastMessage> forecastMessages = new ArrayList<ForecastMessage>();
				   
				   for (String forecastId : forecastIds) {
					
					   ForecastMessage forecastMessage = forecastService.getForecastMessageById(forecastId);
					   
					   
					   forecastMessages.add(forecastMessage);
				   }
				   
				   notice.setForecastMessages(forecastMessages);
				   
				   //设置预测类公告字体颜色
				   notice.setNoticeFontColor("#DC143C");
			   }
			   else
				   if(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG.equals(appCategory))
				   {
					   //设置开奖类公告字体颜色
					   notice.setNoticeFontColor("#1E90FF");
				   }
				   else{
					   notice.setNoticeFontColor("#000000");//默认字体颜色为黑色
				   }
			   
			   noticeService.save(notice);//保存应用公告数据
			   
//			   Notice noticeBefore = noticeService.getNoticeById(notice.getId());
			   
			   //整理并放置区域数据
			   List<AppNoticeAndArea> noticeAndAreas = new ArrayList<AppNoticeAndArea>();
			   
			   for (String cityId : cityIds) {
				
				   AppNoticeAndArea aNoticeAndArea = new AppNoticeAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   aNoticeAndArea.setNotice(notice);
				   aNoticeAndArea.setCity(city.getCcode());
				   aNoticeAndArea.setProvince(city.getProvinceCode());
				   
				   appNoticeAndAreaService.save(aNoticeAndArea);
				   
				   noticeAndAreas.add(aNoticeAndArea);
			   }
			   
			   notice.setAppNoticeAndAreas(noticeAndAreas);
			   
			   noticeService.update(notice);
			  
			   
			 
			   
			   resultBean.setMessage("添加应用公告成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加应用公告--应用公告id="+notice.getId()+"操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 
	  * @Title: saveOrUpdateKj
	  * @Description: 保存或修改开奖公告
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/saveOrUpdateKj", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdateKj(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="appNoticeName",required=false) String appNoticeName,
				@RequestParam(value="appNoticeWord",required=false) String appNoticeWord,
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种
				@RequestParam(value="noticeStatus",required=false) String noticeStatus,//应用广告的状态，0：保存1：发布
				@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   List<AppNoticeAndArea> appnoticeAndAreas = new ArrayList<AppNoticeAndArea>();
		   Notice notice = noticeService.getNoticeById(id);
		   
		   //TODO:提取绑定的区域数据
		   JSONObject areas = JSONObject.parseObject(areadata);
		   List<String> cityIds =  (List<String>) areas.get("keys");
		   
		   
		   if(null != notice)
		   {//开奖公告数据不为空，则进行修改操作
			   
			   //若当前开奖公告为发布状态，则要清理之前的开奖公告
			   if(NoticeController.NOTICE_STATUS_FB.equals(noticeStatus))
			   {
				   List<Notice> lastlist = outerInterfaceService.getLastKjNoticeOfNoticename(appNoticeName).getResultList();
		        	for (Notice noticelast : lastlist) {
		        		noticelast.setIsDeleted("0");
		        		noticelast.setModify("sysauto");
		        		noticelast.setModifyTime(new Timestamp(System.currentTimeMillis()));
		    	 		
				 		
		        		noticelast.setAppNoticeAndAreas(appnoticeAndAreas);
		    	 		noticeService.update(noticelast);
		    	 		logger.info("删除开奖公告数据--id="+notice.getId()+"--操作人=sysauto");
		    		}
			   }
				
			   
			   
			   notice.setAppNoticeName(appNoticeName);
			   
			   notice.setAppNoticeWord(appNoticeWord);
			   
			   notice.setLotteryType(lotteryType);
			   notice.setNoticeStatus(noticeStatus);
			   notice.setAppCategory(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG);
			   //！！！当前系统默认有效期为一天
			   Date st = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);//注意：在将string转换为date时，转换的格式一定要与string的格式统一，否则无法转换，eg：“2016-03-21”转换为date类型，只能使用DateUtil.SIMPLE_DATE_FORMAT转换，否则抛出异常
			   notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.getNextDayOfCurrentTime(new Timestamp(System.currentTimeMillis()), 365);
			   
			   notice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   //1.整理并放置区域数据
			   
			   //清空上次绑定的区域数据
			   List<AppNoticeAndArea> noticeAndAreasBefore = notice.getAppNoticeAndAreas();
			   for (AppNoticeAndArea appNoticeAndArea : noticeAndAreasBefore) {
				   appNoticeAndAreaService.delete(appNoticeAndArea);
				   logger.info("删除开奖公告与区域关联数据--关联id="+appNoticeAndArea.getId()+"==开奖公告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   }
			   
			   //重新绑定区域数据
			   List<AppNoticeAndArea> noticeAndAreas = new ArrayList<AppNoticeAndArea>();
			   for (String cityId : cityIds) {
				
				   AppNoticeAndArea aNoticeAndArea = new AppNoticeAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   aNoticeAndArea.setNotice(notice);
				   aNoticeAndArea.setCity(city.getCcode());
				   aNoticeAndArea.setProvince(city.getProvinceCode());
				   
				   appNoticeAndAreaService.save(aNoticeAndArea);
				   
				   noticeAndAreas.add(aNoticeAndArea);
			   }
			   
			   notice.setAppNoticeAndAreas(noticeAndAreas);
			   
			   notice.setNoticeFontColor("#1E90FF");//开奖公告字体颜色
			   
				   
				   
			  
			   
			   
			   
			   noticeService.update(notice);
			  
			   resultBean.setMessage("修改开奖公告成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改开奖广告--开奖广告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   //若当前开奖公告为发布状态，则要清理之前的开奖公告
			   if(NoticeController.NOTICE_STATUS_FB.equals(noticeStatus))
			   {
				   List<Notice> lastlist = outerInterfaceService.getLastKjNoticeOfNoticename(appNoticeName).getResultList();
		        	for (Notice noticelast : lastlist) {
		        		noticelast.setIsDeleted("0");
		        		noticelast.setModify("sysauto");
		        		noticelast.setModifyTime(new Timestamp(System.currentTimeMillis()));
		    	 		
				 		
		        		noticelast.setAppNoticeAndAreas(appnoticeAndAreas);
		    	 		noticeService.update(noticelast);
		    	 		logger.info("删除开奖公告数据--id="+noticelast.getId()+"--操作人=sysauto");
		    		}
			   }
			   
			   notice = new Notice();
			   notice.setId(UUID.randomUUID().toString());
			   notice.setAppNoticeName(appNoticeName);
			   
			   notice.setAppNoticeWord(appNoticeWord);//开奖公告内容
			   
			   notice.setLotteryType(lotteryType);
			   notice.setNoticeStatus(noticeStatus);
			   
			   notice.setAppCategory(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG);
			   //！！！当前系统默认有效期为一天
			   Date st = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);
			   notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.getNextDayOfCurrentTime(new Timestamp(System.currentTimeMillis()), 365);
			   
			   notice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   notice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   notice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   notice.setIsDeleted("1");
			   
			   //设置开奖类公告字体颜色
			   notice.setNoticeFontColor("#1E90FF");
			   
			 
			   
			   
			   noticeService.save(notice);//保存应用公告数据
			   
//			   Notice noticeBefore = noticeService.getNoticeById(notice.getId());
			   
			   //整理并放置区域数据
			   List<AppNoticeAndArea> noticeAndAreas = new ArrayList<AppNoticeAndArea>();
			   
			   for (String cityId : cityIds) {
				
				   AppNoticeAndArea aNoticeAndArea = new AppNoticeAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   aNoticeAndArea.setNotice(notice);
				   aNoticeAndArea.setCity(city.getCcode());
				   aNoticeAndArea.setProvince(city.getProvinceCode());
				   
				   appNoticeAndAreaService.save(aNoticeAndArea);
				   
				   noticeAndAreas.add(aNoticeAndArea);
			   }
			   
			   notice.setAppNoticeAndAreas(noticeAndAreas);
			   
			   noticeService.update(notice);
			  
			   
			 
			   
			   resultBean.setMessage("添加开奖公告成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加开奖公告--开奖公告id="+notice.getId()+"操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 
	 /**
	  * 
	  * @Title: deleteNotices
	  * @Description: 删除应用公告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/deleteNotices", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteNotices(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Notice notice;
		 List<App> apps = new ArrayList<App>();
		 List<UserGroup> userGroups = new ArrayList<UserGroup>();
		 List<AppNoticeAndArea> appnoticeAndAreas = new ArrayList<AppNoticeAndArea>();
		 List<ForecastMessage> forecastMessages = new ArrayList<ForecastMessage>();
		 for (String id : ids) 
			{
			 	notice = noticeService.getNoticeById(id);
			 	if(null != notice)
			 	{
			 		notice.setIsDeleted("0");
			 		notice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		//清空删除的应用广告与其他数据的关联，放置产生冗余关联数据
			 		notice.setApps(apps);
			 		notice.setUserGroups(userGroups);
			 		notice.setForecastMessages(forecastMessages);
			 		
			 		//？暂时不删除区域数据，在后期做统计和查找可能要用
			 		//删除关联的区域数据
			 		 List<AppNoticeAndArea> noticeAndAreasBefore = notice.getAppNoticeAndAreas();
			 		 for (AppNoticeAndArea appNoticeAndArea : noticeAndAreasBefore) {
					   appNoticeAndAreaService.delete(appNoticeAndArea);
					   logger.info("删除应用公告与区域关联数据--关联id="+appNoticeAndArea.getId()+"==应用公告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			 		 }
			 		
			 		notice.setAppNoticeAndAreas(appnoticeAndAreas);
			 		noticeService.update(notice);
			 		
			 		 //日志输出
					 logger.info("删除应用公告数据--应用公告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: publishsNotices
	  * @Description: 批量发布应用公告
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/publishsNotices", method = RequestMethod.POST)
		public @ResponseBody ResultBean  publishsNotices(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Notice notice;
		 for (String id : ids) 
			{
			 	notice = noticeService.getNoticeById(id);
			 	//只对有效的数据，且状态为保存的应用公告进行发布的操作
			 	if(null != notice&&NoticeController.NOTICE_STATUS_BC.equals(notice.getNoticeStatus()))
			 	{
			 		notice.setNoticeStatus(NoticeController.NOTICE_STATUS_FB);
			 		notice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		noticeService.update(notice);
			 		
			 		 //日志输出
					 logger.info("发布应用公告数据--应用公告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "发布成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: getAppsOfNotice
	  * @Description: 获取当前应用广告之前选择的应用发布范围
	  * @author:banna
	  * @return: List<AppDTO>
	  */
	 @RequestMapping(value = "/getAppsOfNotice", method = RequestMethod.GET)
		public @ResponseBody List<AppDTO>  getAppsOfNotice(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Notice notice = noticeService.getNoticeById(id);
		 
		 List<App> apps = notice.getApps();
		 
		 List<AppDTO> appDTOs = appService.toRDTOS(apps);
		 
		 return appDTOs;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getForecastsOfNotice
	  * @Description:获取当前应用公告关联的预测信息数据
	  * @author:banna
	  * @return: List<ForecastDTO>
	  */
	 @RequestMapping(value = "/getForecastsOfNotice", method = RequestMethod.GET)
		public @ResponseBody List<ForecastDTO>  getForecastsOfNotice(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Notice notice = noticeService.getNoticeById(id);
		 
		 List<ForecastMessage> forecastMessages = notice.getForecastMessages();
		 
		 List<ForecastDTO> forecastDTOs = forecastService.toRDTOS(forecastMessages);
		 
		 return forecastDTOs;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getLoginArea
	  * @Description: 获取当前登录用户的区域信息和当前可以发布的应用公告的类型数据
	  * @author:banna
	  * @return: Map<String,Object>
	  */
	 @RequestMapping(value = "/getLoginArea", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object>  getLoginArea(
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Map<String,Object> returndata = new HashMap<String, Object>();
		 
		 String code = LoginUtils.getAuthenticatedUserCode(httpSession);
		 User user = userService.getUserByCode(code);
		 String lotteryType = user.getLotteryType();
		 
		 //获取当前登录人员的角色list
		 List<Role> roles = user.getRoles();
		 
		 //公告类别,0:省中心公告1：市中心公告2：公司普通公告3：公司开奖公告4：公司预测公告
		 String appcategory ="2";//默认广告类别为公司广告
		 for (Role role : roles) {
			if(AdvertisementController.SJX_ROLE_CODE.equals(role.getCode()))
			{
				appcategory = "1";
				break;
			}
			else
				if(AdvertisementController.PROVINCE_ROLE_CODE.equals(role.getCode()))
				{
					appcategory = "0";
					break;
				}
				
		 }
		 
		 
		 if(null != user)
		 {
			 returndata.put("province", user.getProvinceCode());
			 returndata.put("city", user.getCityCode());
			 returndata.put("appcategory", appcategory);
			 returndata.put("lotteryType", lotteryType);
		 }
		 
		 return returndata;
	 }
	 
	 /**
	  * 
	  * @Title: getStationOfUsergroup
	  * @Description:获取当前应用公告发布的通行证组的范围
	  * @author:banna
	  * @return: List<UserGroupDTO>
	  */
	 @RequestMapping(value = "/getStationOfUsergroup", method = RequestMethod.GET)
		public @ResponseBody List<UserGroupDTO>  getStationOfUsergroup(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Notice notice = noticeService.getNoticeById(id);
		 
		 List<UserGroup> userGroups = notice.getUserGroups();
		 
		 List<UserGroupDTO> userGroupDTOs = userGroupService.toRDTOS(userGroups);
		 
		 return userGroupDTOs;
		 
	 }
	 
	 
	 /**
	  * 
	  * @Title: getAreasOfNotice
	  * @Description: 获取当前应用公告发布的区域数据
	  * @author:banna
	  * @return: List<String>
	  */
	 @RequestMapping(value = "/getAreasOfNotice", method = RequestMethod.GET)
		public @ResponseBody List<String>  getAreasOfNotice(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Notice notice = noticeService.getNoticeById(id);
		 
		 List<AppNoticeAndArea> appnoticeAndAreas = notice.getAppNoticeAndAreas();
		 
		 List<String> cityIds = new ArrayList<String>();
		 
		 for (AppNoticeAndArea noticearea : appnoticeAndAreas) {
			
			 cityIds.add(noticearea.getCity());
		}
		 
		 return cityIds;
		 
	 }
	 
	 /**
	  * 
	  * @Title: checkUseUgroup
	  * @Description: 查询当前登录用户是否可以选择通行证组的发布范围，在应用公告的管理中
	  * 只有省中心不可以选择通行证组，公司内部和市中心都可以选择通行证组
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/checkUseUgroup", method = RequestMethod.GET)
		public @ResponseBody ResultBean  checkUseUgroup(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 String code = LoginUtils.getAuthenticatedUserCode(httpSession);
		 User user = userService.getUserByCode(code);
		 //获取当前登录人员的角色list
		 List<Role> roles = user.getRoles();
		 
		 //市中心用户的角色code是5
		 boolean isSZX = true;
		 for (Role role : roles) {
			if(AdvertisementController.PROVINCE_ROLE_CODE.equals(role.getCode()))
			{
				isSZX = false;
				break;
			}
		 }
		 
		 resultBean.setExist(isSZX);
		 
		 return resultBean;
	 }
	
	 
	 
	 

}
