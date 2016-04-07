package com.sdf.manager.app.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.Column;
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

import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.service.AppUPriceService;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.appversion.service.AppversionService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.bean.TreeBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.goods.controller.GoodsController;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.user.entity.Authority;


@Controller
@RequestMapping("/app")
public class AppController  extends GlobalExceptionHandler{
	
	private static final Logger logger = LoggerFactory.getLogger(AppController.class);

	@Autowired
	private AppService appService;
	
	@Autowired
	private AppversionService appversionService;
	
	@Autowired
	private AppUPriceService appUPriceService;
	
	@Autowired
	private CityService cityService;
	
	/**应用管理模块的默认值**/
	public static final String APP_STATUS_DSJ = "0";//应用状态(0:待上架)
	public static final String APP_STATUS_SJ = "1";//应用状态(1:上架)
	public static final String APP_STATUS_XJ = "2";//应用状态(02:下架)
	public static final String APP_STATUS_GX = "3";//应用状态(3:更新)
	
	
	/**
	 * 
	* @Title: getDetailApp
	* @Description: 根据id获取app内容
	* @Author : banna
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return AppDTO    返回类型
	* @throws
	 */
	@RequestMapping(value = "/getDetailApp", method = RequestMethod.GET)
	public @ResponseBody AppDTO getDetailApp(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		App app = appService.getAppById(id);
		
		AppDTO appDto = appService.toDTO(app);
		
		
		return appDto;
	}
	
	/**
	 * 
	* @Title: getAppList
	* @Description: 根据筛选条件获取应用列表数据
	* @Author : banna
	* @param @param page
	* @param @param rows
	* @param @param appCode
	* @param @param appType
	* @param @param appName
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	 @RequestMapping(value = "/getAppList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getAppList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="appCode",required=false) String appCode,//应用编码
				@RequestParam(value="appType",required=false) String appType,//应用状态
				@RequestParam(value="appName",required=false) String appName,//应用名称
				@RequestParam(value="province",required=false) String province,//省份
				@RequestParam(value="city",required=false) String city,//市
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种
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
			
			//连接查询条件
			if(null != appCode&&!"".equals(appCode.trim()))
			{
				params.add("%"+appCode+"%");
				buffer.append(" and appCode like ?").append(params.size());
			}
			
			if(null != appName&&!"".equals(appName.trim()))
			{
				params.add("%"+appName+"%");
				buffer.append(" and appName like ?").append(params.size());
			}
			
			if(null != appType&&!"".equals(appType.trim()))
			{//根据应用状态查询应用数据列表
				params.add(appType);
				buffer.append(" and appStatus = ?").append(params.size());
			}
			
			if(null != lotteryType && !"".equals(lotteryType.trim()))
			{
				params.add(lotteryType);
				buffer.append(" and lotteryType = ?").append(params.size());
			}
			
			/**
			 * 根据省市筛选应用数据，当前是应用广告模块在使用这个功能，start
			 */
			if(null != province&&!"".equals(province.trim()))
			{
				params.add(province);
				buffer.append(" and province = ?").append(params.size());
			}
			
			if(null != city&&!"".equals(city.trim()))
			{
				List<String> paraArr = new ArrayList<String> ();
				paraArr.add(city);
				paraArr.add(Constants.CITY_ALL);
				params.add(paraArr);
				buffer.append(" and city in ?").append(params.size());
			}
			/**
			 * 根据省市筛选应用数据，当前是应用广告模块在使用这个功能，end
			 */
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<App> applist = appService.getAppList(App.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<App> apps = applist.getResultList();
			Long totalrow = applist.getTotalRecord();
			
			//将实体转换为dto
			List<AppDTO> appDTOs = appService.toRDTOS(apps);
			
			returnData.put("rows", appDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	
	 /**
	  * 
	 * @Title: saveOrUpdate
	 * @Description: 保存或修改应用数据
	 * @Author : banna
	 * @param @param id
	 * @param @param appCode
	 * @param @param appName
	 * @param @param appStatus
	 * @param @param appDeveloper
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
				@RequestParam(value="appCode",required=false) String appCode,
				@RequestParam(value="appName",required=false) String appName,
				@RequestParam(value="appStatus",required=false) String appStatus,
				@RequestParam(value="appDeveloper",required=false) String appDeveloper,
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="city",required=false) String city,
				@RequestParam(value="appMoney",required=false) String appMoney,//应用的默认单价
				@RequestParam(value="lotteryType",required=false) String lotteryType,//应用的彩种
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   App app = appService.getAppById(id);
		   
		   if(null != app)
		   {//应用数据不为空，则进行修改操作
			   
			   String oldProvince = app.getProvince();//修改之前的省区域信息
			   String oldCity = app.getCity();//修改之前的市区域信息
			   String oldPrice = app.getAppMoney();//修改之前的app的默认单价
			   
			   app.setAppName(appName);
			   app.setAppStatus(appStatus);
			   app.setAppDeveloper(appDeveloper);
			   app.setProvince(province);
			   app.setLotteryType(lotteryType);
			   app.setCity(city);
			   app.setAppMoney(appMoney);
			   app.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   app.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   appService.update(app);
			   
			   if(!oldProvince.equals(province)||!oldCity.equals(city))
			   {
				 //本次修改若修改了应用的区域，则应用之前的区域单价数据应为无效
				   List<AppUnitPrice> appUnitPrices = app.getAppUnitPrices();
				   
				   for (AppUnitPrice appUnitPrice : appUnitPrices) {
					
					   appUnitPrice.setApp(null);
					   appUnitPrice.setIsDeleted("0");
					   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   appUPriceService.update(appUnitPrice);
					   
					   logger.info("因应用id为:"+id+"的应用数据更新了区域信息，所以清空其区域单价数据，区域单价数据id为："+appUnitPrice.getId());
				   }
				   
				   //处理默认应用单价数据
				   if(Constants.CITY_ALL.equals(city))
				   {
					   //获取当前省份下的区域数据
					   List<City> cities = cityService.findCitiesOfProvice(province);
					   for (City cityEntity : cities) {
						   
						   AppUnitPrice appUnitPrice = new AppUnitPrice();
						   appUnitPrice.setUnitPrice(appMoney);
						   appUnitPrice.setApp(app);
						   appUnitPrice.setProvince(province);
						   appUnitPrice.setCity(cityEntity.getCcode());
						   appUnitPrice.setPriceType("0");//单价类别，0：app默认单价，在创建app时写入的初始单价，之后也会跟随app的单价变化而变化 1：自定义单价，通过应用区域单价模块进行设置的单价，不随应用单价的变化而变化 
						   appUnitPrice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
						   appUnitPrice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
						   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
						   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
						   appUnitPrice.setIsDeleted("1");//有效数据标记位
						   
						   appUPriceService.save(appUnitPrice);
						   
						   logger.info("应用id为:"+app.getId()+"添加应用区域单价数据--应用区域单价省份="+province+"，市="+cityEntity.getCcode()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
						   
					   }
				   }
				   else
				   {
					   AppUnitPrice appUnitPrice = new AppUnitPrice();
					   appUnitPrice.setUnitPrice(appMoney);
					   appUnitPrice.setApp(app);
					   appUnitPrice.setProvince(province);
					   appUnitPrice.setCity(city);
					   appUnitPrice.setPriceType("0");//单价类别，0：app默认单价，在创建app时写入的初始单价，之后也会跟随app的单价变化而变化 1：自定义单价，通过应用区域单价模块进行设置的单价，不随应用单价的变化而变化 
					   appUnitPrice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
					   appUnitPrice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
					   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   appUnitPrice.setIsDeleted("1");//有效数据标记位
					   
					   appUPriceService.save(appUnitPrice);
					   logger.info("应用id为:"+app.getId()+"添加应用区域单价数据--应用区域单价省份="+province+"，市="+city+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   
				   }
				   
				   
			   }
			   else
			   {
				   //判断应用的单价是否改变
				   if(!oldPrice.equals(appMoney))
				   {
					   List<AppUnitPrice> appUnitPrices = app.getAppUnitPrices();
					   
					   for (AppUnitPrice appUnitPrice : appUnitPrices) {
						
						   if("0".equals(appUnitPrice.getPriceType()))//修改区域的默认单价的值，这些区域的单价跟随默认单价来更新
						   {
							   appUnitPrice.setUnitPrice(appMoney);
							   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
							   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
							   
							   appUPriceService.update(appUnitPrice);
							   logger.info("因应用id为:"+id+"的应用数据更新了默认单价信息，所以更新当前应用对应的区域单价类型为“0”的区域默认单价值，区域单价数据id为："+appUnitPrice.getId());
						   }
						   
						   
					   }
					   
				   }
				   
			   }
			  
			   
			   
			   
			  
			   resultBean.setMessage("修改应用信息成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改应用--应用id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   app = new App();
			   app.setId(UUID.randomUUID().toString());
			   app.setAppCode(appCode);
			   app.setAppName(appName);
			   app.setAppStatus(appStatus);
			   app.setAppDeveloper(appDeveloper);
			   app.setAppMoney(appMoney);
			   app.setLotteryType(lotteryType);
			   app.setProvince(province);
			   app.setCity(city);
			   app.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   app.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   app.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   app.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   app.setIsDeleted("1");//有效数据标记位
			   
			   appService.save(app);
			   
			   if(Constants.CITY_ALL.equals(city))
			   {
				   //获取当前省份下的区域数据
				   List<City> cities = cityService.findCitiesOfProvice(province);
				   for (City cityEntity : cities) {
					   
					   AppUnitPrice appUnitPrice = new AppUnitPrice();
					   appUnitPrice.setUnitPrice(appMoney);
					   appUnitPrice.setApp(app);
					   appUnitPrice.setProvince(province);
					   appUnitPrice.setCity(cityEntity.getCcode());
					   appUnitPrice.setPriceType("0");//单价类别，0：app默认单价，在创建app时写入的初始单价，之后也会跟随app的单价变化而变化 1：自定义单价，通过应用区域单价模块进行设置的单价，不随应用单价的变化而变化 
					   appUnitPrice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
					   appUnitPrice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
					   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   appUnitPrice.setIsDeleted("1");//有效数据标记位
					   
					   appUPriceService.save(appUnitPrice);
					   
					   logger.info("应用id为:"+app.getId()+"添加应用区域单价数据--应用区域单价省份="+province+"，市="+cityEntity.getCcode()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   
				   }
			   }
			   else
			   {
				   AppUnitPrice appUnitPrice = new AppUnitPrice();
				   appUnitPrice.setUnitPrice(appMoney);
				   appUnitPrice.setApp(app);
				   appUnitPrice.setProvince(province);
				   appUnitPrice.setCity(city);
				   appUnitPrice.setPriceType("0");//单价类别，0：app默认单价，在创建app时写入的初始单价，之后也会跟随app的单价变化而变化 1：自定义单价，通过应用区域单价模块进行设置的单价，不随应用单价的变化而变化 
				   appUnitPrice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
				   appUnitPrice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   appUnitPrice.setIsDeleted("1");//有效数据标记位
				   
				   appUPriceService.save(appUnitPrice);
				   logger.info("应用id为:"+app.getId()+"添加应用区域单价数据--应用区域单价省份="+province+"，市="+city+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			   }
			   
			   
			   
			   
			   resultBean.setMessage("添加应用信息成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加应用--应用code="+appCode+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 
	 * @Title: generateAppcode
	 * @Description: 删除应用数据
	 * @Author : banna
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/deleteApps", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteApps(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 App app;
		 for (String id : ids) 
			{
			 	app = appService.getAppById(id);
			 	if(null != app)
			 	{
			 		app.setIsDeleted("0");
			 		app.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		app.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		appService.update(app);
			 		
			 		 //日志输出
					 logger.info("删除应用--应用id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 
	 /**
	  * 
	 * @Title: checkAppIsableDel
	 * @Description: 校验应用是否可以删除（应用下有版本数据时不可以删除）
	 * @Author : banna
	 * @param @param id
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/checkAppIsableDel", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkAppIsableDel(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 App app = appService.getAppById(id);
		 
		 List<Appversion> appversions = app.getAppVersions();
		 
		 boolean delFlag = true;
		 
		 if(appversions.size()>0)
		 {
			 delFlag = false;
		 }
		 
		 resultBean.setExist(delFlag);
		 
		 
		 return resultBean;
		 
	 }
	 
	 /**
	  * 
	 * @Title: getTreedata
	 * @Description: 获取app版本树节点数据
	 * @Author : banna
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return List<TreeBean>    返回类型
	 * @throws
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
			
//			params.add(AppController.APP_STATUS_SJ);
//			buffer.append(" and  appStatus = ?").append(params.size());
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("appCode", "desc");
			
			QueryResult<App> applist = appService.getAppList(App.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			List<App> apps = applist.getResultList();
			
			List<TreeBean> treeBeanList = new ArrayList<TreeBean> ();
			
			//初始化应用版本树根
			TreeBean treeBeanGen = new TreeBean();
			treeBeanGen.setId("0");
			treeBeanGen.setName("应用根");
			treeBeanGen.setOpen(true);
			treeBeanGen.setpId("");
			treeBeanList.add(treeBeanGen);
			
			for (App app : apps) {
				
				TreeBean treeBeanIn = new TreeBean();
				treeBeanIn.setId(app.getId());
//				treeBeanIn.setParent(true);
				treeBeanIn.setName(app.getAppName());
				treeBeanIn.setOpen(true);
				treeBeanIn.setpId(treeBeanGen.getId());//应用的父级是应用根
				treeBeanList.add(treeBeanIn);
				
				List<Appversion> appversions = app.getAppVersions();
				for (Appversion appversion : appversions) {
					TreeBean treeBeanIntwo = new TreeBean();
					treeBeanIntwo.setId(appversion.getId());
					treeBeanIntwo.setName(appversion.getAppVersionName());
					treeBeanIntwo.setOpen(true);
					treeBeanIntwo.setpId(appversion.getApp().getId());//应用的父级是上级应用
					treeBeanList.add(treeBeanIn);
				}
			}
			
			return treeBeanList;
		}
	 
	 /**
	  * 
	 * @Title: updateAppStatus
	 * @Description: 更新应用状态
	 * @Author : banna
	 * @param @param ids
	 * @param @param appStatus
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/updateAppStatus", method = RequestMethod.POST)
		public @ResponseBody ResultBean  updateAppStatus(
				@RequestParam(value="ids",required=false) String[] ids,
				@RequestParam(value="appStatus",required=false) String appStatus,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 App app;
		 for (String id : ids) 
			{
			 	app = appService.getAppById(id);
			 	if(null != app)
			 	{
			 		app.setAppStatus(appStatus);
			 		app.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		app.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		appService.update(app);
			 		
			 		 //日志输出
					 logger.info("更新应用状态--应用id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "更新应用状态!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 
	 /**
	  * 
	 * @Title: checkGoodsName
	 * @Description: 校验应用名称唯一性（全局唯一）
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
	 @RequestMapping(value = "/checkAppName", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkAppName(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="name",required=false) String name,
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
			
			if(null != name && !"".equals(name))
			{
				params.add(name);
				buffer.append(" and appName = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<App> alist = appService.getAppList(App.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(alist.getResultList().size()>0)
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
	* @Title: generateAppcode
	* @Description: 生成应用编码
	* @Author : banna
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	 @RequestMapping(value = "/generateAppcode", method = RequestMethod.POST)
		public @ResponseBody Map<String,Object>  generateAppcode(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 Map<String,Object> returndata = new HashMap<String, Object>();
		 String code = this.codeGenertor();
		 returndata.put("code", code);
		 returndata.put("operator", LoginUtils.getAuthenticatedUserName(httpSession));
		 
		 return returndata;
				 
	 }
	 
	/**
	 * 
	* @Description:生成应用编码 
	* //规则：年月日(yyyyMMdd)+6位流水号
	* @author bann@sdfcp.com
	* @date 2015年11月18日 上午10:31:02
	 */
	 private  synchronized String codeGenertor()
	 {
		 
		 StringBuffer appCode = new StringBuffer("App");
		//获取当前年月日
		 String date = "";
		 Date dd  = Calendar.getInstance().getTime();
		 date = DateUtil.formatDate(dd, DateUtil.FULL_DATE_FORMAT);
		 String year = date.substring(0, 4);//半包，不包括最大位数值
		 String month = date.substring(5, 7);
		 String day = date.substring(8, 10);
		 appCode.append(year).append(month).append(day);
		 
		 //验证当天是否已生成应用
		//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			params.add(appCode+"%");//根据应用编码模糊查询
			buffer.append(" appCode like ?").append(params.size());
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("appCode", "desc");//大号的code排在前面
			
			QueryResult<App> applist = appService.getAppList(App.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(applist.getResultList().size()>0)
			{
				String maxCode = applist.getResultList().get(0).getAppCode();
				String weihao = maxCode.substring(11, maxCode.length());
				int num = Integer.parseInt(weihao);
				String newNum = (++num)+"";
				int needLen = (GoodsController.SERIAL_NUM_LEN-newNum.length());
				for(int i=0;i<needLen;i++)
				{
					newNum = "0"+newNum;
				}
				appCode.append(newNum);
			}
			else
			{//当天还没有生成应用编码
				appCode.append("000001");
			}
			
		 
			return appCode.toString();
	 }
	 
	
	
}
