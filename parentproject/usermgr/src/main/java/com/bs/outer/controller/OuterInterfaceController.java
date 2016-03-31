package com.bs.outer.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bs.outer.entity.AnnouncementReceipt;
import com.bs.outer.entity.Fast3;
import com.bs.outer.entity.Fast3Analysis;
import com.bs.outer.service.AnnouncementReceiptService;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.service.AdvertisementService;
import com.sdf.manager.ad.service.UploadfileService;
import com.sdf.manager.announcement.dto.AnnouncementDTO;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.announcement.service.AnnouncementService;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.service.AppUPriceService;
import com.sdf.manager.appversion.controller.AppversionController;
import com.sdf.manager.appversion.dto.AppversionDTO;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.appversion.service.AppversionService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.bean.ResultBeanData;
import com.sdf.manager.common.bean.ResultBeanDataList;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.dto.ComnoticeDTO;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.companyNotice.service.CompanynoticeService;
import com.sdf.manager.notice.controller.NoticeController;
import com.sdf.manager.notice.dto.NoticeDTO;
import com.sdf.manager.notice.entity.ForecastMessage;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.service.NoticeService;
import com.sdf.manager.order.dto.RenewAppDTO;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.order.service.RelaBsStaAppHisService;
import com.sdf.manager.order.service.RelaBsStaAppService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.station.application.dto.StationDto;
import com.sdf.manager.station.controller.StationController;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.user.bean.AccountBean;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.service.UserService;
import com.sdf.manager.userGroup.entity.UserGroup;


/**
 * 
 * @ClassName: OuterInterfaceController
 * @Description: TODO二代后台外部接口控制层
 * @author: banna
 * @date: 2016年3月2日 上午10:46:50
 */
@Controller
@RequestMapping("outerInterface")
public class OuterInterfaceController //extends GlobalExceptionHandler
{

	private Logger logger = LoggerFactory.getLogger(OuterInterfaceController.class);
	
	@Autowired
	private StationService stationService;//通行证业务层
	
	@Autowired
	private RelaBsStaAppService relaBsStaAppService;//通行证与应用关联表的管理层
	
	@Autowired
	private AppService appService;//应用业务层
	
	@Autowired
	private OuterInterfaceService outerInterfaceService;//接口业务层
	
	@Autowired
	private AppversionService appversionService;//应用版本业务层
	
	@Autowired
	private StationController stationController;//通行证控制层
	
	@Autowired
	private UserService userService;//帐号业务层
	
	 @Autowired
	 private AppUPriceService appUPriceService;//应用区域单价业务层
	 
	 @Autowired
	 private RelaBsStaAppHisService relaBsStaAppHisService;
	 
	 @Autowired
	 private AnnouncementService announcementService;//通告业务层
	 
	 @Autowired
	 private CompanynoticeService companynoticeService;//公司公告业务层
	 
	 @Autowired
	 private AdvertisementService advertisementService;//应用广告业务层
	 
	 @Autowired
	 private NoticeService noticeService;//应用公告业务层
	 
	 @Autowired
	 private UploadfileService uploadfileService;//应用广告附件业务层
	 
	 @Autowired
	 private ProvinceService provinceService;//省份业务层
	 
	 @Autowired
	 private CityService cityService;//市业务层
	 
	 @Autowired
	 private AnnouncementReceiptService announcementReceiptService;//通告回执表业务层
	 
	 //静态变量
	 public static final String DEFAULT_FREE_USE_DAY_OF_YEARS = "365";//免费使用时间天数的默认值
	 
	 public static final String ANNOUNCEMENT_ALREADY_READ = "1";//通告回执表的通告已读状态位
	 
	 public static final String ANNOUNCEMENT_NOT_READ = "0";//通告回执表的通告未读状态位
	
	/**
	 * 
	 * @Title: Login
	 * @Description: TODO一体机登录接口
	 * @author:banna
	 * @return: ResultBean
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> Login(@RequestParam(value="stationCode",required=true) String stationCode,//登录名
										 @RequestParam(value="password",required=false) String password)//密码
	{
		Map<String,Object> returnResult = new HashMap<String, Object>();
		
		try
		{
			String message = "登录成功!";//登录返回信息
			
			boolean loginFlag =true;//登录状态，true：登录成功，false：登录失败
			
			Station station = stationService.getStationByCode(stationCode);
			
			
			if(null == station)//根据登录的登录编码（也就是登录名）没有获取到信息
			{
				loginFlag = false;
				message = "登录失败,当前登录名不存在!";
			}
			else
			{
				if(!password.equals(station.getPassword()))
				{
					loginFlag = false;
					message = "登录失败,登录密码错误!";
				}
			}
			
			
			returnResult.put("message", message);
			returnResult.put("loginFlag", loginFlag);
			//登录成功还要返回的内容
			if(loginFlag)
			{
				//TODO:1.返回登录的通行证信息
				StationDto stationDto = stationController.toDto(station);
				returnResult.put("stationDto", stationDto);
				//TODO:2.返回通行证对应的通告,调用获取通行证的通告数据的接口,返回的是dto数据
				List<AnnouncementDTO> announcement = new ArrayList<AnnouncementDTO>();
				announcement = this.getAnnouncementOfStation(station.getId());
				returnResult.put("announcement", announcement);
				//TODO:3.返回通行证对应的公司公告,调用获取通行证的公司公告数据的接口,返回的是dto数据
				List<ComnoticeDTO> companyNotice = new ArrayList<ComnoticeDTO>();
				companyNotice = this.getComnoticeOfStations(station.getId());
				returnResult.put("companyNotice", companyNotice);
				//4.获取当前通行证正在使用的应用及有效期
				List<RenewAppDTO> useAppDTOs = new ArrayList<RenewAppDTO>();
				useAppDTOs = this.getAppListOfStation(station.getId());
				returnResult.put("useAppDTOs", useAppDTOs);
				
			}
			logger.info("访问登录接口成功 stationCode="+stationCode+"&&password="+password);
			
			/*returnResult.put("status", "1");
			returnResult.put("message", "数据获取成功");*/
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.info("访问登录接口失败 stationCode="+stationCode+"&&password="+password);
			
			/*returnResult.put("status", "0");
			returnResult.put("message", "数据获取失败");*/
		}
		
		
		
		return returnResult;
	}
	
	/**
	 * 
	 * @Title: updatePassword
	 * @Description: 更新通行证密码
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	@RequestMapping(value="/updatePassword",method=RequestMethod.GET)
	public @ResponseBody Map<String,Object> updatePassword(@RequestParam(value="stationCode",required=true) String stationCode,//登录名
										 @RequestParam(value="password",required=false) String password)//更新密码值
	{
		Map<String,Object> returnResult = new HashMap<String, Object>();
		Station station = null;
		try
		{
			boolean updateFlag =true;//更新密码状态，true：更新成功，false：更新失败
			
			station = stationService.getStationByCode(stationCode);
			
			station.setPassword(password);
			
			stationService.update(station);
			
			
			returnResult.put("updateFlag", updateFlag);
			logger.info("更新密码操作成功 更新的通行证id为："+station.getId());
			/*returnResult.put("status", "1");
			returnResult.put("message", "访问接口成功");*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("更新密码操作失败！更新的通行证id为："+station.getId());
			/*returnResult.put("status", "0");
			returnResult.put("message", "接口访问失败");*/
		}
		
		
		
		return returnResult;
	}
	
	/**
	 * 
	 * @Title: getAppListOfStation
	 * @Description: TODO获取当前等的通行证正在使用的应用及使用期限（暂时一体机接口没有需要使用这个接口的位置）
	 * @author:banna
	 * @return: List<AppDTO>
	 */
	@RequestMapping(value="/getAppListOfStation",method=RequestMethod.GET)
	public @ResponseBody List<RenewAppDTO> getAppListOfStation(@RequestParam(value="stationId",required=true) String stationId)
	{
		
		//放置分页参数
		Pageable pageable = new PageRequest(0,100000);
		List<RenewAppDTO> renewAppDTOs = new ArrayList<RenewAppDTO>();
		
		try
		{
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			params.add("1");//正在使用的状态
			buffer.append(" and status = ?").append(params.size());
			
			Date et = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);//注意：在将string转换为date时，转换的格式一定要与string的格式统一，否则无法转换，eg：“2016-03-21”转换为date类型，只能使用DateUtil.SIMPLE_DATE_FORMAT转换，否则抛出异常
			params.add(et);//有效时间大于等于今天的应用
			buffer.append(" and endTime >= ?").append(params.size());
			
			
			//连接查询条件
			if(null != stationId&&!"".equals(stationId.trim()))
			{
				params.add(stationId);
				buffer.append(" and station.id = ?").append(params.size());
			}
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<RelaBsStationAndApp> applist = relaBsStaAppService.getRelaBsStationAndAppList(RelaBsStationAndApp.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<RelaBsStationAndApp> sapps = applist.getResultList();
			
			
			
			//整理通行证可以续费的应用数据
			for (RelaBsStationAndApp sapp : sapps) {
				
				RenewAppDTO renewAppDTO = new RenewAppDTO();
				
				renewAppDTO.setAppId(sapp.getApp().getId());
				renewAppDTO.setAppName(sapp.getApp().getAppName());
				
				renewAppDTO.setProvince(sapp.getApp().getProvince());
				renewAppDTO.setCity(sapp.getApp().getCity());
				
				//放置应用省区域名称
				Province proentity = new Province();
				proentity = provinceService.getProvinceByPcode(sapp.getApp().getProvince());
				renewAppDTO.setProvinceName(null != proentity?proentity.getPname():"");
				
				//放置应用市区域名称
				if(Constants.CITY_ALL.equals(sapp.getApp().getCity()))
				{
					renewAppDTO.setCityName(Constants.CITY_ALL_NAME);
				}
				else
				{
					City cityentity = new City();
					cityentity = cityService.getCityByCcode(sapp.getApp().getCity());
					renewAppDTO.setCityName(null != cityentity?cityentity.getCname():"");
				}
				//放置上次购买时间的值
				renewAppDTO.setLastPurchaseTime(DateUtil.formatDate(sapp.getModifyTime(), DateUtil.FULL_DATE_FORMAT));
				
				//有效期结束时间
				renewAppDTO.setEndTime(DateUtil.formatDate(sapp.getEndTime(), DateUtil.FULL_DATE_FORMAT));
				
				//上次购买此应用的剩余使用时间(endtime-当前时间)
				long currentTime = System.currentTimeMillis();
				long endtime = sapp.getEndTime().getTime();
				int betweenDays=0;
				try {
					betweenDays = DateUtil.daysBetween(currentTime, endtime);
				} catch (ParseException e) {
					e.printStackTrace();
					logger.info("有效期转换错误，通行证与app关联数据id="+sapp.getId());
				}
				
				renewAppDTO.setSurplusDays(betweenDays+"");
				
				renewAppDTOs.add(renewAppDTO);
				
				logger.info("获取当前通行证可以使用的应用及使用期限接口getAppListOfStation成功！访问接口的传入stationId："+stationId);
				/*returnResult.put("status", "1");
				returnResult.put("message", "接口访问成功");*/
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("获取当前通行证可以使用的应用及使用期限接口错误！访问接口的传入stationId："+stationId);
			/*returnResult.put("status", "0");
			returnResult.put("message", "接口访问失败");*/
		}
		
		
		
		return renewAppDTOs;
	}
	
	/**
	 * 
	 * @Title: addReceiptOfAnnouncement
	 * @Description: 通告确定已读的状态更新接口
	 * @author:banna
	 * @return: ResultBean
	 */
	@RequestMapping(value="/addReceiptOfAnnouncement",method=RequestMethod.GET)
	public @ResponseBody ResultBean addReceiptOfAnnouncement(@RequestParam(value="stationId",required=true) String stationId)
	{
		ResultBean resultBean = new ResultBean();
		
		try
		{
			//更新有效期内的通告回执表数据的“已读”状态
			Date et = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);//注意：在将string转换为date时，转换的格式一定要与string的格式统一，否则无法转换，eg：“2016-03-21”转换为date类型，只能使用DateUtil.SIMPLE_DATE_FORMAT转换，否则抛出异常
			Timestamp endTime = DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT);
			 
			//获取今天可以展示的数据，将今天展示的数据的标记位置为“已读”
			 List<AnnouncementReceipt> announcementReceipts = announcementReceiptService.
					 getAnnouncementReceiptByStatusAndEndTimeAndStationId(OuterInterfaceController.ANNOUNCEMENT_NOT_READ, endTime, stationId);
			 
			 for (AnnouncementReceipt announcementReceipt : announcementReceipts) {
				
				 announcementReceipt.setStatus(OuterInterfaceController.ANNOUNCEMENT_ALREADY_READ);
				 announcementReceipt.setStatusTime(new Timestamp(System.currentTimeMillis()));
				 announcementReceiptService.update(announcementReceipt);
				 
				 logger.info("更新通行证回执表已读状态，通行证id="+stationId+"&&更新的通告回执表id="+announcementReceipt.getId());
			}
			 
			 resultBean.setStatus("1");
			 resultBean.setMessage("更新成功");
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
			logger.error("通告回执已读状态接口报错，通行证id="+stationId);
			
			 resultBean.setStatus("0");
			 resultBean.setMessage("更新失败");
		}
		
		return resultBean;
	}
	
	/**
	 * 
	 * @Title: getAuthOfStationAndApp
	 * @Description: TODO根据通行证和应用id判断当前通行证是否有权限使用
	 * 	TODO：要判断当前应用是否有权限使用，第一步要看是否有关联关系，
	 * 		第二步如果没有关联关系，则要判断应用是否免费，若免费则创建关联关系并返回可以使用的权限，若收费则返回不可以使用
	 * 	判断这个区域使用这个应用是否免费
	 * @author:banna
	 * @return: ResultBean
	 */
	@RequestMapping(value="/getAuthOfStationAndApp",method=RequestMethod.GET)
	public @ResponseBody ResultBean getAuthOfStationAndApp(@RequestParam(value="stationId",required=true) String stationId,//通行证id
															@RequestParam(value="appId",required=true) String appId)//应用id 
															throws Exception
	{
		ResultBean resultBean = new  ResultBean();
		String message = "";
		
		try
		{
			//放置分页参数
			Pageable pageable = new PageRequest(0,10000);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			params.add("1");//正在使用的应用
			buffer.append(" and status = ?").append(params.size());
			
			params.add(new Timestamp(System.currentTimeMillis()));//有效时间大于等于今天的应用
			buffer.append(" and endTime >= ?").append(params.size());
			
			
			//连接查询条件
			if(null != stationId&&!"".equals(stationId.trim()))
			{
				params.add(stationId);
				buffer.append(" and station.id = ?").append(params.size());
			}
			
			if(null != appId&&!"".equals(appId.trim()))//关联应用id数据
			{
				params.add(appId);
				buffer.append(" and app.id = ?").append(params.size());
			}
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<RelaBsStationAndApp> applist = relaBsStaAppService.getRelaBsStationAndAppList(RelaBsStationAndApp.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<RelaBsStationAndApp> sapps = applist.getResultList();
			
			boolean useFlag = true;
			
			if(null == applist || sapps.size()==0)
			{
				//第一步判断结果：没有关联关系
				//第二步判断是否为免费应用，若为免费则创建关联关系，若付费则没有权限使用
				String price = "";
			 	
			 	//获取当前通行证的区域
			 	Station station = stationService.getSationById(stationId);
			 	String sprovince = station.getProvinceCode();
			 	String scity = station.getCityCode();
			 	String sLotteryType = station.getStationType();//获取通行证的彩种
			 	
			 	//获取应用的单价
			 	App app = appService.getAppById(appId);
			 	String appProvince = app.getProvince();//获取应用的省份
			 	String appCity = app.getCity();//获取应用的市
			 	String appLotteryType = app.getLotteryType();//获取应用的彩种
			 	String morenPrice = app.getAppMoney();//当前通行证想要购买的应用的默认单价
			 	
			 	//1.判断当前获取权限的应用发售区域是否包含当前通行证的区域
			 	boolean createConcact = false;//是否可以创建当前通行证和应用的关联
			 	//判断当前应用的发售区域是否包括当前通行证
			 	if(Constants.CITY_ALL.equals(appCity)&&appProvince.equals(sprovince))//判断当前应用是否为发售到全省的应用,若应用发售区域是当前通行证所在省份且发布范围是全省，则若应用免费可以创建关联
			 	{
			 		createConcact = true;
			 	}
			 	else
			 		if(appProvince.equals(sprovince)&&appCity.equals(scity))//若当前应用是发布给当前通行证所在省所在市的，则若应用免费可以创建关联
				 	{
			 			createConcact = true;
				 	}
			 	
			 	
			 	
			 	//2.查询通行证所在区域是否有特殊定价
			 	AppUnitPrice cityDj = appUPriceService.
			 					getAppUnitPriceByAppIdAndProvinceAndCity(appId, sprovince, scity);
			 	if(null != cityDj)
			 	{
			 		price = cityDj.getUnitPrice();
			 	}
			 	else
			 	{
			 		AppUnitPrice provinceDj = appUPriceService.
		 					getAppUnitPriceByAppIdAndProvinceAndCity(appId, sprovince, Constants.CITY_ALL);
			 		
			 		if(null != provinceDj)
			 		{
			 			price = provinceDj.getUnitPrice();
			 		}
			 		else
			 		{
			 			price = morenPrice;
			 		}
			 	}
			 	
			 	//3.判断当前应用的彩种和通行证的彩种是否相同，要判断通行证可否看其他彩种的应用
			 	boolean lotteryTypeEqual = false;
			 	if(sLotteryType.equals(appLotteryType))
			 	{
			 		lotteryTypeEqual = true;
			 	}
			 	
			 	//判断price是否是“0”
			 	if("0".equals(price)&&createConcact&&lotteryTypeEqual)////免费应用且当前应用的发售区域包括当前通行证的区域,且通行证的彩种与应用的彩种一致
			 	{
			 		//若price是0，则为免费应用，后台自动创建关联
			 		 //1.向“通行证与应用”关联表中插入数据
			 		   App appEntity = appService.getAppById(appId);
			 		   RelaBsStationAndApp relaBsStationAndApp  = new RelaBsStationAndApp();
					   relaBsStationAndApp.setApp(appEntity);
					   relaBsStationAndApp.setStation(station);
					   relaBsStationAndApp.setStartTime(new Timestamp(System.currentTimeMillis()));//开始时间
					   Date endtime;
					   endtime = DateUtil.getNextDay(Integer.parseInt(OuterInterfaceController.DEFAULT_FREE_USE_DAY_OF_YEARS));//免费应用创建关联时添加默认使用时间天数
					   relaBsStationAndApp.setEndTime(DateUtil.formatDateToTimestamp(endtime, DateUtil.FULL_DATE_FORMAT));
					   relaBsStationAndApp.setStatus("1");//正在使用状态
					   relaBsStationAndApp.setIsDeleted(Constants.IS_NOT_DELETED);
					   relaBsStationAndApp.setCreater("interfaceAuto");
					   relaBsStationAndApp.setCreaterTime(new Timestamp(System.currentTimeMillis()));
					   relaBsStationAndApp.setModify("interfaceAuto");
					   relaBsStationAndApp.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   
					   relaBsStaAppService.save(relaBsStationAndApp);
					   
					   logger.info("外部接口获取免费应用权限，创建通行证和应用关联数据，通行证id="+station.getId()+";免费应用id="+appId);
					   
					   //2.向“通行证和应用历史记录关联表”插入数据
					   RelaBsStationAndAppHis relaBsStationAndAppHis = new RelaBsStationAndAppHis();
					   relaBsStationAndAppHis.setApp(appEntity);
					   relaBsStationAndAppHis.setStation(station);
					   relaBsStationAndAppHis.setStartTime(new Timestamp(System.currentTimeMillis()));//开始时间
					   relaBsStationAndAppHis.setEndTime(DateUtil.formatDateToTimestamp(endtime, DateUtil.FULL_DATE_FORMAT));
					   relaBsStationAndAppHis.setIsDeleted(Constants.IS_NOT_DELETED);
					   relaBsStationAndAppHis.setCreater("interfaceAuto");
					   relaBsStationAndAppHis.setCreaterTime(new Timestamp(System.currentTimeMillis()));
					   relaBsStationAndAppHis.setModify("interfaceAuto");
					   relaBsStationAndAppHis.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   
					   relaBsStaAppHisService.save(relaBsStationAndAppHis);
					   
					   logger.info("外部接口获取免费应用权限，创建通行证和应用的历史表关联数据，通行证id="+station.getId()+";免费应用id="+appId);
					   
			 	}
			 	else
			 	{
			 		useFlag = false; 
					message = "对不起,您没有权限使用当前应用";
			 	}
				
				
			}
			
			
			resultBean.setUseFlag(useFlag);
			resultBean.setMessage(message);
			
			
			/*resultBean.setStatus("1");*/
			logger.info("获取当前通行证的应用使用权限接口成功getAuthOfStationAndApp！访问接口的传入stationId："+stationId+"&&appid="+appId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("获取当前通行证的应用使用权限接口错误getAuthOfStationAndApp！访问接口的传入stationId："+stationId+"&&appid="+appId);
			/*resultBean.setStatus("1");
			resultBean.setMessage("接口访问失败");*/
		}
		
		
		
		return resultBean;
	}
	
	/**
	 * 
	 * @Title: getTopnewAppVersionOfApp
	 * @Description: TODO判断当前使用的应用版本对应的应用是否有最新的版本更新，若有则返回（暂时没有使用的位置）
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	@RequestMapping(value="/getTopnewAppVersionOfApp",method = RequestMethod.GET)
	public @ResponseBody Map<String ,Object> getTopnewAppVersionOfApp(@RequestParam(value="appversionId",required=true) String appversionId )
	{
		Map<String ,Object> returnResult = new HashMap<String, Object>();
		
		try
		{
			boolean haveNewVersion = false;
			AppversionDTO appversionDTO = new AppversionDTO();
			
			Appversion oldAppversion = appversionService.getAppversionById(appversionId);
			
			String appId = oldAppversion.getApp().getId();//获取应用id
			
			String oldversionFlowId = oldAppversion.getVersionFlowId();//获取当前使用版本的版本流水号
			
			/**获取最新版本数据**/

		 	//放置分页参数
			Pageable pageable = new PageRequest(0,10000);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			//判断当前应用是否有更新版本则要在已上架的应用版本数据中筛选
			params.add(AppversionController.APP_V_STATUS_SJ);//获取的版本是已上架的版本
			buffer.append(" and appVersionStatus = ?").append(params.size());
			
			//连接查询条件
		 	
			if(null != appId&&!"".equals(appId.trim()))//根据所属应用查询
			{
				params.add(appId);
				buffer.append(" and app.id = ?").append(params.size());
			}
			
			if(null != oldversionFlowId&&!"".equals(oldversionFlowId.trim()))//获取当前版本流水号后发布的版本的数据
			{
				params.add(oldversionFlowId);
				buffer.append(" and versionFlowId > ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("versionFlowId", "desc");//降序，获取的第一个数据是版本号最大的
			
			QueryResult<Appversion> appversionlist = appversionService.getAppversionList(Appversion.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
			
			List<Appversion> resultList = appversionlist.getResultList();
			
			if(null != resultList && resultList.size()>0)
			{
				haveNewVersion = true;
				
				Appversion topNewVersion = resultList.get(0);//获取版本号比当前大，且在获取数据里面最大的数据
				appversionDTO = appversionService.toDTO(topNewVersion);//整理最新的应用版本数据的dto
			}
			
			
			returnResult.put("haveNewVersion", haveNewVersion);//是否有最新版本，true：有；false：没有
			returnResult.put("appversion", appversionDTO);//最新版本数据，若没有时，则前台获取的数据是null
			
			
			/*returnResult.put("status", "1");
			returnResult.put("message", "接口访问成功");*/
			logger.info("获取当前使用的应用版本对应的应用是否有最新的版本更新接口成功getTopnewAppVersionOfApp！访问接口的传入appversionId："+appversionId);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("获取当前使用的应用版本对应的应用是否有最新的版本更新接口错误getTopnewAppVersionOfApp！访问接口的传入appversionId："+appversionId);
			/*returnResult.put("status", "0");
			returnResult.put("message", "接口访问失败");*/
		}
		
		
		return returnResult;
	}
	
	/**
	 * 
	 * @Title: getAnnouncementOfStation
	 * @Description: TODO:根据通行证id获取其通告数据
	 * @author:banna
	 * @return: List<AnnouncementDTO>
	 */
	@RequestMapping(value="/getAnnouncementOfStation",method = RequestMethod.GET)
	public @ResponseBody List<AnnouncementDTO> getAnnouncementOfStation(@RequestParam(value="stationId",required=true) String stationId)
	{
		List<AnnouncementDTO> announcementDTOs = new ArrayList<AnnouncementDTO>();
		
		//获取通告数据的sql：获取的数据应该是回执表中未到有效结束时间的且未读状态的通告
		//生成回执表的sql：有效期内的通告数据且不存在于通告回执表的数据
		
		
		
		try
		{
			/*String province;
			String city;
			String lotteryType;
			
			Station station = stationService.getSationById(stationId);
			
			province = station.getProvinceCode();
			city = station.getCityCode();
			lotteryType = station.getStationType();//1；体彩2：福彩
			
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,1000000);//查询所有的数据
			
			StringBuffer ugroupArr = new StringBuffer();
			if(null!=station.getUserGroups() && station.getUserGroups().size()>0)
			{
				for(int i=0;i<station.getUserGroups().size();i++)
				{
					if(i==0)
					{
						ugroupArr.append("'"+station.getUserGroups().get(i).getId()+"'");
					}
					else
					{
						ugroupArr.append(",");
						ugroupArr.append("'"+station.getUserGroups().get(i).getId()+"'");
					}
				}
			}
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			QueryResult<Announcement> announceResult = outerInterfaceService.
					getAnnouncementOfSta(Announcement.class, buffer.toString(), params.toArray(),
							orderBy, pageable, ugroupArr.toString(), province, city,lotteryType);
			
			List<Announcement> announcements = announceResult.getResultList();*/
			
			List<Announcement> announcements = new ArrayList<Announcement>();
			
			 Date et = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);//注意：在将string转换为date时，转换的格式一定要与string的格式统一，否则无法转换，eg：“2016-03-21”转换为date类型，只能使用DateUtil.SIMPLE_DATE_FORMAT转换，否则抛出异常
    		 Timestamp endTime = DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT);
    		 
    		 List<AnnouncementReceipt> announcementReceipts = announcementReceiptService.
    				 getAnnouncementReceiptByStatusAndEndTimeAndStationId(OuterInterfaceController.ANNOUNCEMENT_NOT_READ, endTime, stationId);
    		 
    		 for (AnnouncementReceipt announcementReceipt : announcementReceipts) {
				
    			 Announcement announcement = new Announcement();
    			 announcement = announcementService.getAnnouncementById(announcementReceipt.getAnnouncementId());
    			 
    			 announcements.add(announcement);
    			 
			}
			
			announcementDTOs = announcementService.toRDTOS(announcements);
			
			
			logger.info("获取根据通行证id获取其通告数据接口成功getAnnouncementOfStation！访问接口的传入stationId："+stationId);
			/*returnResult.put("status", "1");
			returnResult.put("message", "接口访问成功");*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("获取根据通行证id获取其通告数据接口错误getAnnouncementOfStation！访问接口的传入stationId："+stationId);
			/*returnResult.put("status", "0");
			returnResult.put("message", "接口访问失败");*/
		}
		
		
		
		return announcementDTOs;
	}
	
	/**
	 * 
	 * @Title: getComnoticeOfStations
	 * @Description: TODO:根据通行证id获取其公司公告数据
	 * @author:banna
	 * @return: List<ComnoticeDTO>
	 */
	@RequestMapping(value="/getComnoticeOfStation",method = RequestMethod.GET)
	public @ResponseBody List<ComnoticeDTO> getComnoticeOfStations(@RequestParam(value="stationId",required=true) String stationId)
	{
		List<ComnoticeDTO> comnoticeDTOs = new ArrayList<ComnoticeDTO>();
		
		List<UserGroup> ugroups = new ArrayList<UserGroup>();
		String province;
		String city;
		String lotteryType;
		
		try
		{
			Station station = stationService.getSationById(stationId);
			
			province = station.getProvinceCode();
			city = station.getCityCode();
			lotteryType = station.getStationType();//1；体彩2：福彩
			
			ugroups = station.getUserGroups();//获取通行证组数据
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,1000000);//查询所有的数据
			
			StringBuffer ugroupArr = new StringBuffer();
			if(ugroups.size()>0)
			{
				for(int i=0;i<ugroups.size();i++)
				{
					if(i==0)
					{
						ugroupArr.append("'"+ugroups.get(i).getId()+"'");
					}
					else
					{
						ugroupArr.append(",");
						ugroupArr.append("'"+ugroups.get(i).getId()+"'");
					}
				}
			}
			
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<CompanyNotice> comQueryResult = outerInterfaceService.
					getCompanyNoticeOfSta(CompanyNotice.class, buffer.toString(), params.toArray(),
							orderBy, pageable, ugroupArr.toString(), province, city,lotteryType);
			 
			List<CompanyNotice> companyNotices = comQueryResult.getResultList();
			
			comnoticeDTOs = companynoticeService.toRDTOS(companyNotices);
			
			
			
			logger.info("获取根据通行证id获取其公司公告数据接口成功getComnoticeOfStation！访问接口的传入stationId："+stationId);
			/*returnResult.put("status", "1");
			returnResult.put("message", "接口访问成功");*/
		}
		catch(Exception e)
		{
			e.printStackTrace();
			logger.error("获取根据通行证id获取其公司公告数据接口错误getComnoticeOfStation！访问接口的传入stationId："+stationId);
			/*returnResult.put("status", "0");
			returnResult.put("message", "接口访问失败");*/
		}
		
		
		
		
		return comnoticeDTOs; 
	}
	
	/**
	 * 
	 * @Title: getAdsOfStationAndApp
	 * @Description: TODO:根据通行证id和应用id获取这个通行证在这个应用中可以展示的应用广告数据
	 * @author:banna
	 * @return: List<AdvertisementDTO>
	 */
	@RequestMapping(value="/getAdsOfStationAndApp",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getAdsOfStationAndApp(@RequestParam(value="stationId",required=true) String stationId,
													@RequestParam(value="appId",required=true) String appId	)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		List<AdvertisementDTO> advertisementDTOs = new ArrayList<AdvertisementDTO>();
		List<UserGroup> ugroups = new ArrayList<UserGroup>();
		String province;
		String city;
		String lotteryType;
		try
		{
			Station station = stationService.getSationById(stationId);
			
			App app = appService.getAppById(appId);
			lotteryType = app.getLotteryType();
			
			province = station.getProvinceCode();
			city = station.getCityCode();
			
			ugroups = station.getUserGroups();//获取通行证组数据
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,1000000);//查询所有的数据
			
			StringBuffer ugroupArr = new StringBuffer();
			if(ugroups.size()>0)
			{
				for(int i=0;i<ugroups.size();i++)
				{
					if(i==0)
					{
						ugroupArr.append("'"+ugroups.get(i).getId()+"'");
					}
					else
					{
						ugroupArr.append(",");
						ugroupArr.append("'"+ugroups.get(i).getId()+"'");
					}
				}
			}
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<Advertisement> adQueryResult = outerInterfaceService.
					getAdvertisementOfStaAndApp(Advertisement.class, buffer.toString(), params.toArray(),
							orderBy, pageable, ugroupArr.toString(), province, city,appId,lotteryType);
			 
			List<Advertisement> advertisements = adQueryResult.getResultList();
			
			for (Advertisement advertisement : advertisements) 
			{
				if(AdvertisementController.AD_IMG.equals(advertisement.getImgOrWord()))//若为图片广告，则将图片的路径整理到
				{
					StringBuffer imgUrl = new StringBuffer();
					
					Uploadfile uploadfile = uploadfileService.getUploadfileByNewsUuid(advertisement.getAppImgUrl());
					
					imgUrl.append(uploadfile.getUploadfilepath()+uploadfile.getUploadRealName());
					
					advertisement.setAppImgUrl(imgUrl.toString());
				}
			}
			
			advertisementDTOs = advertisementService.toRDTOS(advertisements);
			
			result.put("advertisementDTOs", advertisementDTOs);
			result.put("status", "1");
			result.put("message", "数据获取成功！");
		}
		catch(Exception e)
		{
			logger.error("获取应用广告数据时报错！stationId="+stationId+"&& appId="+appId);
			result.put("status", "0");
			result.put("message", "数据获取失败！");
		}
		
		
		
		return result;
	}
	
	/**
	 * 
	 * @Title: getNoticesOfStationAndApp
	 * @Description: TODO:根据通行证id和应用id获取这个通行证在这个应用中可以展示的普通应用公告数据
	 * @author:banna
	 * @return: List<AdvertisementDTO>
	 */
	@RequestMapping(value="/getNoticesOfStationAndApp",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getNoticesOfStationAndApp(@RequestParam(value="stationId",required=true) String stationId,
													@RequestParam(value="appId",required=true) String appId	)
	{
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<NoticeDTO> noticeDTOs = new ArrayList<NoticeDTO>();
		
		List<UserGroup> ugroups = new ArrayList<UserGroup>();
		String province;
		String city;
		String lotteryType;
		
		try
		{
		
			Station station = stationService.getSationById(stationId);
			
			App app = appService.getAppById(appId);
			lotteryType = app.getLotteryType();
			
			province = station.getProvinceCode();
			city = station.getCityCode();
			
			ugroups = station.getUserGroups();//获取通行证组数据
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,1000000);//查询所有的数据
			
			StringBuffer ugroupArr = new StringBuffer();
			if(ugroups.size()>0)
			{
				for(int i=0;i<ugroups.size();i++)
				{
					if(i==0)
					{
						ugroupArr.append("'"+ugroups.get(i).getId()+"'");
					}
					else
					{
						ugroupArr.append(",");
						ugroupArr.append("'"+ugroups.get(i).getId()+"'");
					}
				}
			}
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<Notice> nQueryResult = outerInterfaceService.
					getNoticeOfStaAndApp(Notice.class, buffer.toString(), params.toArray(),
							orderBy, pageable, ugroupArr.toString(), province, city,appId,lotteryType);
			 
			List<Notice> notices = nQueryResult.getResultList();
			
			for (Notice notice : notices) 
			{
				
				if(NoticeController.APP_CATEGORY_FORCAST.equals(notice.getAppCategory()))//若为预测类应用公告，则返回的公告内容就是预测信息的连接
				{
					List<ForecastMessage> forecastMessages = notice.getForecastMessages();
					StringBuffer content = new StringBuffer();
					
					for (ForecastMessage forecastMessage : forecastMessages) 
					{
						
						String forcastName = forecastMessage.getForecastName();
						String forcastMes = forecastMessage.getForecastContent();
						
						content.append(forcastName+":"+forcastMes+";");
						
					}
					
					notice.setAppNoticeWord(content.toString());//重新放置预测信息
				}
			}
			
			noticeDTOs = noticeService.toRDTOS(notices);
			
			result.put("noticeDTOs", noticeDTOs);
			result.put("status", "1");
			result.put("message", "数据获取成功！");
		
		}
		catch(Exception e)
		{
			logger.error("获取应用公告数据时报错！stationId="+stationId+"&& appId="+appId);
			result.put("status", "0");
			result.put("message", "数据获取失败！");
		}
		return result;
	}
	
	
	/**
	 * 
	 * @Title: getKaijiangNoticesOfStationAndApp
	 * @Description: 获取开奖公告数据
	 * @author:banna
	 * @return: List<NoticeDTO>
	 */
	@RequestMapping(value="/getKaijiangNoticesOfStationAndApp",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getKaijiangNoticesOfStationAndApp(@RequestParam(value="stationId",required=true) String stationId,
													@RequestParam(value="appId",required=true) String appId	)
	{
		
		Map<String,Object> result = new HashMap<String, Object>();
		
		List<NoticeDTO> noticeDTOs = new ArrayList<NoticeDTO>();
		
		List<UserGroup> ugroups = new ArrayList<UserGroup>();
		String province;
		String city;
		String lotteryType;
		
		try
		{
			Station station = stationService.getSationById(stationId);
			
			App app = appService.getAppById(appId);
			lotteryType = app.getLotteryType();
			
			province = station.getProvinceCode();
			city = station.getCityCode();
			
			ugroups = station.getUserGroups();//获取通行证组数据
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,1000000);//查询所有的数据
			
			StringBuffer ugroupArr = new StringBuffer();//获取开奖公告不需要用户组参数
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<Notice> nQueryResult = outerInterfaceService.
					getKaijiangNoticeOfStaAndApp(Notice.class, buffer.toString(), params.toArray(),
							orderBy, pageable, ugroupArr.toString(), province, city,appId,lotteryType);
			 
			List<Notice> notices = nQueryResult.getResultList();
			
			
			
			noticeDTOs = noticeService.toRDTOS(notices);
			
			
			result.put("KaijiangnoticeDTOs", noticeDTOs);
			result.put("status", "1");
			result.put("message", "数据获取成功！");
		
		}
		catch(Exception e)
		{
			logger.error("获取开奖公告数据时报错！stationId="+stationId+"&& appId="+appId);
			result.put("status", "0");
			result.put("message", "数据获取失败！");
		}
		
		
		
		return result;
	}
	
	/**
	 * 
	 * @Title: getProxyOfStation
	 * @Description: TODO:根据通行证id获取其上级的代理信息
	 * 对应的返回json数据结构：proxyOfStation.json
	 * @author:banna
	 * @return: AccountBean
	 */
	@RequestMapping(value="/getProxyOfStation",method = RequestMethod.GET)
	public @ResponseBody AccountBean getProxyOfStation(@RequestParam(value="stationId",required=true) String stationId) throws Exception
	{
		AccountBean accountBean = new AccountBean();
		
		try
		{
			Station station = stationService.getSationById(stationId);
			
			String agentId = station.getAgentId();
			
			User user = userService.getUserById(agentId);//根据代理id获取代理详情
			accountBean.setId(user.getId());
			accountBean.setCode(user.getCode());
			accountBean.setName(user.getName());
//			accountBean.setPassword(user.getPassword());
			accountBean.setTelephone(user.getTelephone());
			accountBean.setStatus(user.getStatus());
			accountBean.setCity(user.getCityCode());
			accountBean.setProvince(user.getProvinceCode());
			
			/*result.put("accountBean", accountBean);
			result.put("status", "1");
			result.put("message", "数据获取成功！");*/
			logger.info("获取根据通行证id获取其上级的代理信息接口数据成功getProxyOfStation！stationId="+stationId);
			
			
		}
		catch(Exception e)
		{
			logger.error("获取根据通行证id获取其上级的代理信息接口数据报错getProxyOfStation！stationId="+stationId);
			/*result.put("status", "0");
			result.put("message", "数据获取失败！");*/
		}
		
		
		return accountBean;
	}
	
	/**
	 * 
	 * @Title: getAppversionsOfnew
	 * @Description: TODO:根据应用id获取一体机当前安装的所有应用的最新版本数据
	 * * 对应的返回json数据结构：appVersionOfnew.json
	 * @author:banna
	 * @return: List<AppversionDTO>
	 */
	@RequestMapping(value="/getAppversionsOfnew",method = RequestMethod.GET)
	public @ResponseBody List<AppversionDTO> getAppversionsOfnew(@RequestParam(value="appIds",required=true) String[] appIds)
	{
		List<AppversionDTO> appversionDTOs = new ArrayList<AppversionDTO>();
		List<Appversion> appversions = new ArrayList<Appversion>();
		
		try
		{
			for (String appId : appIds) {
				
				//获取当前appId下的且已上架的应用版本的最新版本数据
				String maxVersionFlowId =
						appversionService.
						findMaxVersionFlowId(appId, AppversionController.APP_V_STATUS_SJ);//获取当前应用下的应用版本数据是上架状态的最大版本流水号
				
				if(null!=maxVersionFlowId)
				{
					Appversion appversion = appversionService.
							getAppversionByAppIdAndVersionFlowId(appId, maxVersionFlowId);//根据appId和版本流水号获取应用版本数据
					
					Uploadfile uploadfile = uploadfileService.getUploadfileByNewsUuid(appversion.getAppVersionUrl());
					
					String apkUrl = "";
					if(null!=uploadfile)
					{
						apkUrl = uploadfile.getUploadfilepath()+uploadfile.getUploadRealName();//抓取附件的真实存储路径
						appversion.setAppVersionUrl(apkUrl);
					}
					
					
					
					appversions.add(appversion);
				}
				else
				{
					continue;
				}
				
				
			}
			appversionDTOs = appversionService.toRDTOS(appversions);
			
			
			/*result.put("appversionDTOs", appversionDTOs);
			result.put("status", "1");
			result.put("message", "数据获取成功！");*/
			logger.info("获取根据应用id获取一体机当前安装的所有应用的最新版本数据接口数据成功getAppversionsOfnew！appIds="+appIds);
		}
		catch(Exception e)
		{
			logger.error("获取根据应用id获取一体机当前安装的所有应用的最新版本数据接口数据报错getAppversionsOfnew！appIds="+appIds);
			/*result.put("status", "0");
			result.put("message", "数据获取失败！");*/
		}
		
		
		
		return appversionDTOs;
	}
	
	/**
	 * 
	 * @Title: getLotteryNum
	 * @Description:  获取开奖结果内容
	 * * 对应的返回json数据结构：
	 * @author:songjia
	 * @return: Fast3
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value="/getLotteryNum",method = RequestMethod.GET)
	public @ResponseBody ResultBeanData<Fast3> getLotteryNum(@RequestParam(value="issueNumber",required=true) String issueNumber,@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		ResultBeanData<Fast3> fast3Bean  = new ResultBeanData<Fast3>();
		try{
			Fast3 fast3 = outerInterfaceService.getKaiJiangNumberByIssueId(issueNumber, provinceNumber);
			fast3Bean.setStatus("1");
			fast3Bean.setMessage("success");
			fast3Bean.setEntity(fast3);
		}catch(Exception ex){
			fast3Bean.setStatus("0");
			fast3Bean.setMessage("failure");
		}finally{
			return fast3Bean;
		}
	}
	
	/**
	 * 
	 * @Title: getLotteryNum
	 * @Description:  获取开奖结果内容
	 * * 对应的返回json数据结构：
	 * @author:songjia
	 * @return: Fast3
	 */
	@SuppressWarnings("finally")
	@RequestMapping(value="/getLotteryNumList",method = RequestMethod.GET)
	public @ResponseBody ResultBeanDataList<Fast3> getLotteryNumList(@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		ResultBeanDataList<Fast3> resultList = new ResultBeanDataList<Fast3>();
		try{
			List<Fast3> fast3List = outerInterfaceService.getKaijiangNumberListByProvinceNumber(provinceNumber);
			resultList.setDataList(fast3List);
			resultList.setMessage("success");
			resultList.setStatus("1");
		}catch(Exception ex){
			logger.error("获取初始化开奖号码结果集错误！");
			resultList.setMessage("failure");
			resultList.setStatus("0");
		}finally{
			return resultList;
		}
	}
	
	/**
	 * 
	 * @Title: getLotteryNum
	 * @Description:  获取遗漏统计内容
	 * * 对应的返回json数据结构：
	 * @author:songjia
	 * @return: Fast3
	 */
	@RequestMapping(value="/getAnalysisInfo",method = RequestMethod.GET)
	public @ResponseBody ResultBeanDataList<Fast3Analysis> getAnalysisInfo(@RequestParam(value="issueNumber",required=true) String issueNumber,@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		ResultBeanDataList<Fast3Analysis> resultList = new ResultBeanDataList<Fast3Analysis>();
		try{
			List<Fast3Analysis> fast3List = outerInterfaceService.getAnalysisListByIssueNumber( issueNumber, provinceNumber);
			resultList.setDataList(fast3List);
			resultList.setMessage("success");
			resultList.setStatus("1");
		}catch(Exception ex){
			logger.error("获取遗漏统计结果集错误！");
			resultList.setMessage("failure");
			resultList.setStatus("0");
		}finally{
			return resultList;
		}
	}
}
