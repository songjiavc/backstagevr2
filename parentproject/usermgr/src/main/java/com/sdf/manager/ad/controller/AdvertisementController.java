package com.sdf.manager.ad.controller;

import java.io.File;
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
import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.entity.AppAdAndArea;
import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.service.AdvertisementService;
import com.sdf.manager.ad.service.AppAdAndAreaService;
import com.sdf.manager.ad.service.UploadfileService;
import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.bean.TreeBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
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
 * @ClassName: AdvertisementController
 * @Description: TODO:应用广告模块控制层
 * @author: banna
 * @date: 2016年2月15日 下午12:30:13
 */
@Controller
@RequestMapping("advertisement")
public class AdvertisementController extends GlobalExceptionHandler
{

	private Logger logger = LoggerFactory.getLogger(AdvertisementController.class);
	
	
	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private AppAdAndAreaService adAndAreaService;
	
	@Autowired
	private UploadfileService uploadfileService;
	
	
	public static final String SJX_ROLE_CODE="5";//数据库初始化内容，市中心角色,应用公告也用
	public static final String PROVINCE_ROLE_CODE="4";//数据库初始化内容，省中心角色,应用公告也用
	public static final String AD_IMG="0";//图片类型广告
	public static final String AD_WORD="1";//文字类型广告
	public static final String AD_STATUS_FB="1";//应用广告发布状态位
	public static final String AD_STATUS_BC="0";//应用广告保存状态位
	protected static final String AD_TYPE_SJX="2";//省中心广告
	protected static final String AD_TYPE_CITY="1";//市中心广告
	protected static final String AD_TYPE_COMPANY="3";//公司广告
	protected static final String AD_TYPE_STATION="0";//站点广告
	
	/**
	 * 
	 * @Title: getDetailAdvertisement
	 * @Description: 根据id获取广告详情
	 * @author:banna
	 * @return: AdvertisementDTO
	 */
	@RequestMapping(value = "/getDetailAdvertisement", method = RequestMethod.GET)
	public @ResponseBody AdvertisementDTO getDetailAdvertisement(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		Advertisement advertisement = advertisementService.getAdvertisementById(id);
		
		AdvertisementDTO advertisementDTO = advertisementService.toDTO(advertisement);
		
		
		return advertisementDTO;
	}
	
	/**
	 * 
	 * @Title: getAdvertisementList
	 * @Description: 根据筛选条件查询应用广告数据，获取时不会获取到“通行证发布的应用广告数据”，因为在获取应用广告时带“and creater”
	 * 				的查询条件，只能获取自己创建的应用广告，而“通行证发布的应用广告数据”是通行证创建的
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	 @RequestMapping(value = "/getAdvertisementList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getAdvertisementList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="province",required=false) String province,//省份
				@RequestParam(value="city",required=false) String city,//市
				@RequestParam(value="adName",required=false) String adName,//应用广告名称
				@RequestParam(value="startTime",required=false) String startTime,//应用广告有效结束时间
				@RequestParam(value="endTime",required=false) String endTime,//应用广告有效结束时间
				@RequestParam(value="imgOrword",required=false) String imgOrword,//应用广告展示类型是文字还是图片
				@RequestParam(value="adStatus",required=false) String adStatus,//应用广告状态，发布or保存
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
			
			//只查询当前登录用户创建的应用广告数据且约束若登录人为“市中心”用户则不会获取其下属通行证创建的应用广告数据
			params.add(LoginUtils.getAuthenticatedUserCode(httpSession));
			buffer.append(" and creater = ?").append(params.size());
			
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
			
			if(null != adName && !"".equals(adName.trim()))
			{
				params.add("%"+adName+"%");
				buffer.append(" and appAdName like ?").append(params.size());
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
			
			if(null != imgOrword && !"".equals(imgOrword.trim()))
			{
				params.add(imgOrword);
				buffer.append(" and imgOrWord = ?").append(params.size());
			}
			
			if(null != adStatus && !"".equals(adStatus.trim()))
			{
				params.add(adStatus);
				buffer.append(" and adStatus = ?").append(params.size());
			}
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<Advertisement> adverlist = advertisementService.getAdvertisementList(Advertisement.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<Advertisement> ads = adverlist.getResultList();
			Long totalrow = adverlist.getTotalRecord();
			
			//将实体转换为dto
			List<AdvertisementDTO> advertisementDTOs = advertisementService.toRDTOS(ads);
			
			returnData.put("rows", advertisementDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	 
	
	 
	 /**
	  * 
	  * @Title: saveOrUpdate
	  * @Description: TODO:保存或修改应用广告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="appAdName",required=false) String appAdName,
				@RequestParam(value="addWord",required=false) String addWord,
				@RequestParam(value="appImgUrl",required=false) String appImgUrl,//TODO:广告图片上传
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//应用广告的有效结束时间
				@RequestParam(value="adTime",required=false) String adTime,//应用广告的轮播时长
				@RequestParam(value="imgOrWord",required=false) String imgOrWord,//应用广告的展示形式
				@RequestParam(value="addType",required=false) String addType,//应用广告类型
				@RequestParam(value="adStatus",required=false) String adStatus,//应用广告的状态，0：保存1：发布
				@RequestParam(value="appsdata",required=false) String appsdata,//绑定的应用数据list
				@RequestParam(value="stationGdata",required=false) String stationGdata,//绑定的通行证组数据list
				@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   Advertisement advertisement = advertisementService.getAdvertisementById(id);
		   
		 //提取绑定的应用数据
		   JSONObject appdatas = JSONObject.parseObject(appsdata);
		   List<String> apps =  (List<String>) appdatas.get("keys");
		   
		   //提取绑定的通行证组数据
		   JSONObject userGroups = JSONObject.parseObject(stationGdata);
		   List<String> uGroups =  (List<String>) userGroups.get("keys");
		   
		   //TODO:提取绑定的区域数据
		   JSONObject areas = JSONObject.parseObject(areadata);
		   List<String> cityIds =  (List<String>) areas.get("keys");
		   
		   
		   if(null != advertisement)
		   {//应用广告数据不为空，则进行修改操作
			   
			   advertisement.setAppAdName(appAdName);
			   
			   if(AdvertisementController.AD_IMG.equals(imgOrWord))
			   {
				   advertisement.setAddWord("");//清空另一种类型的数据
				   advertisement.setAppImgUrl(appImgUrl);//放置的是uploadfile表的newsUUid，用来关联图片附件数据
			   }
			   else if(AdvertisementController.AD_WORD.equals(imgOrWord))
			   {
				   advertisement.setAppImgUrl("");//清空另一种类型的数据
				   advertisement.setAddWord(addWord);
			   }
			   
			   advertisement.setImgOrWord(imgOrWord);
//			   advertisement.setAddType(addType);//修改应用广告不用放置应用广告类别
			   advertisement.setAdStatus(adStatus);
//			   advertisement.setAppImgUrl(appImgUrl);//放置的是uploadfile表的newsUUid，用来关联图片附件数据
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   advertisement.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   advertisement.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   advertisement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   advertisement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   //1.整理并放置应用数据
			   List<App> aList = new ArrayList<App>();
			   for (String appId : apps) {
				   
				   App app = appService.getAppById(appId);
				   aList.add(app);
			   }
			   advertisement.setApps(aList);
			   
			   //2.整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   advertisement.setUserGroups(uList);
			   
			   //3.整理并放置区域数据
			   //清空上次绑定的区域数据
			   List<AppAdAndArea> adAndAreasBefore = advertisement.getAppAdAndAreas();//获取之前绑定的应用广告的区域信息
			   for (AppAdAndArea appAdAndArea : adAndAreasBefore) {
				   adAndAreaService.delete(appAdAndArea);
				   logger.info("删除应用广告与区域关联数据--关联id="+appAdAndArea.getId()+"==应用广告告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   }
			   //重新绑定区域数据
			   List<AppAdAndArea> adAndAreas = new ArrayList<AppAdAndArea>();
			   
			   for (String cityId : cityIds) {
				
				   AppAdAndArea aaa = new AppAdAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   aaa.setAdvertisement(advertisement);
				   aaa.setCity(city.getCcode());
				   aaa.setProvince(city.getProvinceCode());
				   
				   adAndAreaService.save(aaa);//保存应用广告与区域关联表的数据
				   
				   adAndAreas.add(aaa);
			   }
			   
			   advertisement.setAppAdAndAreas(adAndAreas);
			   
			   
			   advertisementService.update(advertisement);
			  
			   resultBean.setMessage("修改应用广告成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改应用广告--应用广告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   advertisement = new Advertisement();
			   advertisement.setId(UUID.randomUUID().toString());
			   advertisement.setAppAdName(appAdName);
			   advertisement.setAdTime("30");//单位：秒
			   if(AdvertisementController.AD_IMG.equals(imgOrWord))
			   {//图片类型的广告只放置图片
				   advertisement.setAppImgUrl(appImgUrl);//放置的是uploadfile表的newsUUid，用来关联图片附件数据
			   }
			   else if(AdvertisementController.AD_WORD.equals(imgOrWord))
			   {//文字类型的广告只放置文字
				   advertisement.setAddWord(addWord);
			   }
			   advertisement.setImgOrWord(imgOrWord);
			   advertisement.setAddType(addType);//应用广告类别，广告类别,0：站点广告 1.市中心应用广告2.省中心应用广告3.公司应用广告
			   if(AdvertisementController.AD_TYPE_COMPANY.equals(addType))
			   {
				   advertisement.setAdFontColor("#FF4500");
			   }
			   else
			   {
				   advertisement.setAdFontColor("#000000");
			   }
			   advertisement.setAdStatus(adStatus);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   advertisement.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   advertisement.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   advertisement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   advertisement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   advertisement.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   advertisement.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   
			   //整理并放置应用数据
			   List<App> aList = new ArrayList<App>();
			   for (String appId : apps) {
				   
				   App app = appService.getAppById(appId);
				   aList.add(app);
			   }
			   advertisement.setApps(aList);
			   
			   //整理并放置通行证组数据
			   List<UserGroup> uList = new ArrayList<UserGroup>();
			   for (String ugroupId : uGroups) {
				   UserGroup uGroup = userGroupService.getUserGroupById(ugroupId);
				   uList.add(uGroup);
			   }
			   advertisement.setUserGroups(uList);
			   
			   advertisement.setIsDeleted("1");//有效数据标记位
			   
			   advertisementService.save(advertisement);
			   
			   Advertisement advertisementBeforeSave = advertisementService.getAdvertisementById(advertisement.getId());
			   
			   //整理并放置区域数据
			   List<AppAdAndArea> adAndAreas = new ArrayList<AppAdAndArea>();
			   for (String cityId : cityIds) {
				
				   AppAdAndArea aaa = new AppAdAndArea();
				   City city = cityService.getCityByCcode(cityId);
				   aaa.setAdvertisement(advertisementBeforeSave);
				   aaa.setCity(city.getCcode());
				   aaa.setProvince(city.getProvinceCode());
				   
				   adAndAreaService.save(aaa);//保存应用广告与区域关联表的数据
				   
				   adAndAreas.add(aaa);
			   }
			   advertisementBeforeSave.setAppAdAndAreas(adAndAreas);
			   
			   advertisementService.update(advertisementBeforeSave);
			  
			   
			 
			   
			   resultBean.setMessage("添加应用广告成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加应用广告--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 
	  * @Title: getTreedataOfAdvertisement
	  * @Description: 获取应用对应的区域树数据
	  * @author:banna
	  * @return: List<TreeBean>
	  */
	 @RequestMapping(value = "/getTreedataOfAdvertisement", method = RequestMethod.GET)
		public @ResponseBody List<TreeBean> getTreedataOfAdvertisement(
				@RequestParam(value="appsdata",required=false) String appsdata,//绑定的通行证组数据list
				@RequestParam(value="isProvince",required=false) boolean isProvince,//是否为“省中心”角色
				@RequestParam(value="provinceCode",required=false) String provinceCode,//当前登录人员所在省份
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	List<TreeBean> treeList = new ArrayList<TreeBean>();
		 	
		   
		 //整理并放置应用数据
		 	List<Province> provinces = new ArrayList<Province>();
		 	if(isProvince)//若为“省中心”角色，则只获取当前角色下的城市信息
		 	{
		 		Province province = provinceService.getProvinceByPcode(provinceCode);
		 		provinces.add(province);
		 	}
		 	else
		 	{
		 		provinces = provinceService.findAll();
		 	}
		 	
		 	
		 	for (Province province : provinces) {
				
		 		TreeBean treeBeanIn = new TreeBean();
				treeBeanIn.setId(province.getPcode());//pcode：区域编码
				treeBeanIn.setParent(true);//设置当前节点是否为父级节点，在放置选中数据时不进行放置
				treeBeanIn.setName(province.getPname());
				treeBeanIn.setOpen(false);
				treeBeanIn.setpId("");
				treeList.add(treeBeanIn);
				
				List<City> cities = cityService.findCitiesOfProvice(province.getPcode());
				for (City city : cities) {
					TreeBean treeBeancity = new TreeBean();
					treeBeancity.setId(city.getCcode());//ccode：区域编码
					treeBeancity.setParent(false);//设置当前节点是否为父级节点，在放置选中数据时进行放置
					treeBeancity.setName(city.getCname());
//					treeBeancity.setOpen(true);
					treeBeancity.setpId(province.getPcode());
					treeList.add(treeBeancity);
				}
			}
		 	
		 	return treeList;
		}
	 
	 /**
	  * 
	  * @Title: deleteAdvertisements
	  * @Description:删除应用广告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/deleteAdvertisements", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteAdvertisements(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Advertisement advertisement;
		 List<App> apps = new ArrayList<App>();
		 List<UserGroup> userGroups = new ArrayList<UserGroup>();
		 List<AppAdAndArea> appAdAndAreas = new ArrayList<AppAdAndArea>();
		 
		 
		//获取项目根路径
		 String savePath = httpSession.getServletContext().getRealPath("");
	     savePath = savePath +File.separator+ "upload"+File.separator;
	     //删除附件文件相关s
		 Uploadfile uploadfile = null;
		 File dirFile = null;
		 boolean deleteFlag = false;//删除附件flag
		//删除附件文件相关e
		 
		 for (String id : ids) 
			{
			 	advertisement = advertisementService.getAdvertisementById(id);
			 	if(null != advertisement)
			 	{
			 		advertisement.setIsDeleted("0");
			 		advertisement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		advertisement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		//清空删除的应用广告与其他数据的关联，放置产生冗余关联数据
			 		advertisement.setApps(apps);
			 		advertisement.setUserGroups(userGroups);
			 		
			 		
			 		//删除关联的区域数据
			 		List<AppAdAndArea> adAndAreasBefore = advertisement.getAppAdAndAreas();//获取之前绑定的应用广告的区域信息
					   for (AppAdAndArea appAdAndArea : adAndAreasBefore) {
						   adAndAreaService.delete(appAdAndArea);
						   logger.info("删除应用广告与区域关联数据--关联id="+appAdAndArea.getId()+"==应用广告告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   }
					
					advertisement.setAppAdAndAreas(appAdAndAreas);
			 		advertisementService.update(advertisement);
			 		
			 		//若为图片广告则删除图片附件
			 		if(AdvertisementController.AD_IMG.equals(advertisement.getImgOrWord()))
			 		{
			 			//删除附件s
				 		//1.获取附件
				 		uploadfile = uploadfileService.getUploadfileByNewsUuid(advertisement.getAppImgUrl());
				 		if(null != uploadfile)
				 		{
				 			//2.删除附件
					 		dirFile = new File(savePath+uploadfile.getUploadRealName());
					 		logger.info("待删除文件路径："+dirFile);
					        // 如果dir对应的文件不存在，或者不是一个目录，则退出
				        	deleteFlag = dirFile.delete();
				        	if(deleteFlag)
				        	{//删除附件(清空附件关联newsUuid)
				        		uploadfile.setNewsUuid("");
				        		uploadfileService.update(uploadfile);
				        		logger.info("删除附件数据--附件id="+uploadfile.getId()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				        	}
				        	else
				        	{
				        		 logger.error("应用广告数据id为："+advertisement.getId()+"的数据没有文件");
				        	}
					      //删除附件e
				 		}
				 		
			 		}
			 		
			 		
			 		 //日志输出
					 logger.info("删除应用广告数据--应用广告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: publishAdvertisements
	  * @Description: 批量发布应用广告数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/publishAdvertisements", method = RequestMethod.POST)
		public @ResponseBody ResultBean  publishAdvertisements(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Advertisement advertisement;
		 for (String id : ids) 
			{
			 	advertisement = advertisementService.getAdvertisementById(id);
			 	//只有在应用广告不是空时且应用广告是保存状态的才进行发布
			 	if(null != advertisement&&AdvertisementController.AD_STATUS_BC.equals(advertisement.getAdStatus()))
			 	{
			 		advertisement.setAdStatus(AdvertisementController.AD_STATUS_FB);
			 		advertisement.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		advertisement.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		advertisementService.update(advertisement);
			 		
			 		 //日志输出
					 logger.info("发布应用广告数据--应用广告id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "发布成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	
	 /**
	  * 
	  * @Title: getAppsOfAdvertisement
	  * @Description: 获取当前应用广告之前选择的应用发布范围
	  * @author:banna
	  * @return: List<AppDTO>
	  */
	 @RequestMapping(value = "/getAppsOfAdvertisement", method = RequestMethod.GET)
		public @ResponseBody List<AppDTO>  getAppsOfAdvertisement(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Advertisement advertisement = advertisementService.getAdvertisementById(id);
		 
		 List<App> apps = advertisement.getApps();
		 
		 List<AppDTO> appDTOs = appService.toRDTOS(apps);
		 
		 return appDTOs;
		 
	 }
	 
	 /**
	  * 
	  * @Title: checkUseUgroup
	  * @Description: 判断当前用户是否为市中心用户
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
		 boolean isSZX = false;
		 for (Role role : roles) {
			if(AdvertisementController.SJX_ROLE_CODE.equals(role.getCode()))
			{
				isSZX = true;
				break;
			}
		 }
		 
		 resultBean.setExist(isSZX);
		 
		 return resultBean;
	 }
	 
	 /**
	  * 
	  * @Title: getLoginArea
	  * @Description: 获取当前登录用户的区域信息
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
		 
		 //广告类别,0：站点广告 1.市中心应用广告2.省中心应用广告3.公司应用广告
		 String adType =AdvertisementController.AD_TYPE_COMPANY;//默认广告类别为公司广告
		 for (Role role : roles) {
			if(AdvertisementController.SJX_ROLE_CODE.equals(role.getCode()))
			{
				adType = AdvertisementController.AD_TYPE_CITY;
				break;
			}
			else
				if(AdvertisementController.PROVINCE_ROLE_CODE.equals(role.getCode()))
				{
					adType = AdvertisementController.AD_TYPE_SJX;
					break;
				}
				
		 }
		 
		 
		 if(null != user)
		 {
			 returndata.put("province", user.getProvinceCode());
			 returndata.put("city", user.getCityCode());
			 returndata.put("adType", adType);
			 returndata.put("lotteryType", lotteryType);
		 }
		 
		 return returndata;
	 }
	 
	 /**
	  * 
	  * @Title: getStationOfUsergroup
	  * @Description:获取当前应用广告发布的通行证组范围
	  * @author:banna
	  * @return: List<UserGroupDTO>
	  */
	 @RequestMapping(value = "/getStationOfUsergroup", method = RequestMethod.GET)
		public @ResponseBody List<UserGroupDTO>  getStationOfUsergroup(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Advertisement advertisement = advertisementService.getAdvertisementById(id);
		 
		 List<UserGroup> userGroups = advertisement.getUserGroups();
		 
		 List<UserGroupDTO> userGroupDTOs = userGroupService.toRDTOS(userGroups);
		 
		 return userGroupDTOs;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getAreasOfAdvertisement
	  * @Description: 获取当前应用广告发布的区域数据
	  * @author:banna
	  * @return: List<String>
	  */
	 @RequestMapping(value = "/getAreasOfAdvertisement", method = RequestMethod.GET)
		public @ResponseBody List<String>  getAreasOfAdvertisement(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Advertisement advertisement = advertisementService.getAdvertisementById(id);
		 
		 List<AppAdAndArea> appAdAndAreas = advertisement.getAppAdAndAreas();
		 
		 List<String> cityIds = new ArrayList<String>();
		 
		 for (AppAdAndArea adarea : appAdAndAreas) {
			
			 cityIds.add(adarea.getCity());
		}
		 
		 return cityIds;
		 
	 }
	 
	 /**
	  * 
	  * @Title: saveFujian
	  * @Description: 保存广告图片附件
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/saveFujian", method = RequestMethod.GET)
		public @ResponseBody ResultBean  saveFujian(
				@RequestParam(value="realname",required=false) String realname,
				@RequestParam(value="filename",required=false) String filename,
				@RequestParam(value="uplId",required=false) String uplId,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 String type=getExt(filename);
		 String uploadfilepath = "/upload/";
		 
		 Uploadfile uploadfile = uploadfileService.getUploadfileByNewsUuid(uplId);
		 
		 //因为一个应用只能有一个图片附件，所以当这个upId有数据的话就进行修改操作，如果没有数据就创建数据
		 if(null != uploadfile)
		 {
			 //①：因为广告图片只有一个附件，所以在上传其他附件替换上一个附件时，要先把上一个附件文件删除
			 String savePath = httpSession.getServletContext().getRealPath("");//获取项目根路径
		     savePath = savePath +File.separator+ "upload"+File.separator;
		     //删除附件文件相关s
			 File dirFile = null;
			 boolean deleteFlag = false;//删除附件flag
			//2.删除附件
	 		dirFile = new File(savePath+uploadfile.getUploadRealName());
	 		logger.info("待删除文件路径："+dirFile);
	        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        	deleteFlag = dirFile.delete();
        	if(deleteFlag)
        	{//删除附件(清空附件关联newsUuid)
        		logger.info("saveFujian==删除原附件文件数据--附件id="+uploadfile.getId()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
        	}
		    //删除附件e
			 
			 //②：保存新的附件文件
			 uploadfile.setUploadFileName(filename);
			 uploadfile.setUploadRealName(realname);
			 uploadfile.setUploadfilepath(uploadfilepath);
			 uploadfile.setUploadContentType(type);
			 
			 uploadfileService.update(uploadfile);
		 }
		 else
		 {
			 uploadfile = new Uploadfile();
			 uploadfile.setNewsUuid(uplId);
			 uploadfile.setUploadFileName(filename);
			 uploadfile.setUploadRealName(realname);
			 uploadfile.setUploadfilepath(uploadfilepath);
			 uploadfile.setUploadContentType(type);
			 
			 uploadfileService.save(uploadfile);
		 }
		 
		 resultBean.setStatus("success");
		 
		 return resultBean;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getFileOfAppad
	  * @Description: 根据附件主键获取附件信息
	  * @author:banna
	  * @return: Uploadfile
	  */
	 @RequestMapping(value = "/getFileOfAppad", method = RequestMethod.GET)
		public @ResponseBody Uploadfile  getFileOfAppad(
				@RequestParam(value="uplId",required=false) String uplId,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Uploadfile uploadfile = uploadfileService.getUploadfileByNewsUuid(uplId);
		 
		 if(null == uploadfile)
		 {
			 uploadfile = new Uploadfile();
			 uploadfile.setId(0);
		 }
		 
		 return uploadfile;
	 }
	 
	 /**
	  * 
	  * @Title: getAdtypeOfLoginRole
	  * @Description: 根据角色获取发布的应用广告的类别
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/getAdtypeOfLoginRole", method = RequestMethod.GET)
		public @ResponseBody ResultBean  getAdtypeOfLoginRole(
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 String code = LoginUtils.getAuthenticatedUserCode(httpSession);
		 User user = userService.getUserByCode(code);
		 //获取当前登录人员的角色list
		 List<Role> roles = user.getRoles();
		 
		 //广告类别,0：站点广告 1.市中心应用广告2.省中心应用广告3.公司应用广告
		 String adType =AdvertisementController.AD_TYPE_COMPANY;//默认广告类别为公司广告
		 for (Role role : roles) {
			if(AdvertisementController.SJX_ROLE_CODE.equals(role.getCode()))
			{
				adType = AdvertisementController.AD_TYPE_CITY;
				break;
			}
			else
				if(AdvertisementController.PROVINCE_ROLE_CODE.equals(role.getCode()))
				{
					adType = AdvertisementController.AD_TYPE_SJX;
					break;
				}
				
		 }
		 
		 
		 resultBean.setMessage(adType);
		 
		 return resultBean;
	 }
	 
	 /**
	  * 
	  * @Title: checkCouldAddAds
	  * @Description: TODO:判断此今天是否还可以添加应用广告
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/checkCouldAddAds", method = RequestMethod.GET)
		public @ResponseBody ResultBean  checkCouldAddAds(
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 
		 return resultBean;
	 }
	 
	 /**
	  * 
	  * @Title: getExt
	  * @Description: TODO
	  * @author:banna
	  * @return: String
	  */
	 private String getExt(String fileName) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
}
