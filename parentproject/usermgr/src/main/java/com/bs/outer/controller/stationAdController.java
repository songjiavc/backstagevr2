package com.bs.outer.controller;

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
import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.entity.AppAdAndArea;
import com.sdf.manager.ad.entity.FoundStationAdStatus;
import com.sdf.manager.ad.entity.StationAdNextStatus;
import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.service.AdvertisementService;
import com.sdf.manager.ad.service.AppAdAndAreaService;
import com.sdf.manager.ad.service.FoundStationAdStatusService;
import com.sdf.manager.ad.service.StationAdStatusService;
import com.sdf.manager.ad.service.UploadfileService;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.order.controller.OrderController;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.service.RoleService;
import com.sdf.manager.user.service.UserService;


/**
 * 
 * @ClassName: stationAdController
 * @Description: TODO:通行证通过微信平台发布应用广告的后台管理系统处理类
 * @author: banna
 * @date: 2016年3月23日 下午1:44:58
 */
@Controller
@RequestMapping("/stationAd")
public class stationAdController extends GlobalExceptionHandler
{
	 private static final Logger logger = LoggerFactory.getLogger(stationAdController.class);
	
	 
	 @Autowired
	 private ProvinceService provinceService;
	 
	 @Autowired
	 private CityService cityService;
	 
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private RoleService roleService;
	 
	 @Autowired
	 private FoundStationAdStatusService foundStationAdStatusService;//状态跟踪表的service层
	 
	 @Autowired
	 private StationAdStatusService stationAdStatusService;//站点应用广告审批状态业务层
	 
	 
	 @Autowired
	 private AdvertisementService advertisementService;
	 
	 @Autowired
	 private UploadfileService uploadfileService;
	 
	 @Autowired
	 private AppService appService;
	 
	 @Autowired
	 private StationService stationService;
	 
	 @Autowired
	private AppAdAndAreaService adAndAreaService;
	 
	 
	 
	 
	 public static final int SERIAL_NUM_LEN = 6;//订单流水号中自动生成的数字位数
	 
	 public static final String OPERORTYPE_SAVE = "0";//站点编辑页面，保存
	 public static final String OPERORTYPE_SAVEANDCOMMIT = "1";//站点编辑页面，保存并提交
	
	 /***站点应用广审批状态静态变量 start***/
	 public static final String STATION_SAVE_AD = "01";//站点通过微信平台保存应用广告
	 public static final String STATION_AD_FINISH = "21";//审批完成且已发布的审批状态
	 public static final String STATION_AD_STOP = "31";//审批不通过，终止站点应用广告审批状态
	 /***订单状态静态变量 end***/
	
	 public static final String DIRECTION_GO = "1";//前进方向标志位
	 public static final String DIRECTION_BACK = "1";//后退方向标志位
	 
	 public static final String PAGE_OPERORTYPE_SAVE = "1";//站点应用广告列表，提交
	 public static final String PAGE_OPERORTYPE_PASS = "2";//市中心站点应用广告，审批通过
	 public static final String PAGE_OPERORTYPE_REJECT = "3";//市中心站点应用广告表，审批驳回
	 public static final String PAGE_OPERORTYPE_STOP = "4";//市中心站点应用广告，不通过
	 
	 
	 
	 
	/**
	 * 
	* @Description: 根据站点应用广告id查询应用广告数据
	* @author bann@sdfcp.com
	* @date 2016年03月23日 上午9:59:00
	 */
	 @RequestMapping(value = "/getDetailAStaionAd", method = RequestMethod.GET)
	 public @ResponseBody AdvertisementDTO getDetailAStaionAd(
			@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	 {
		 Advertisement advertisement = advertisementService.getAdvertisementById(id);
		 
		 if(AdvertisementController.AD_IMG.equals(advertisement.getImgOrWord()))
		 {
			 StringBuffer imgUrl = new StringBuffer();
				
			Uploadfile uploadfile = uploadfileService.getUploadfileByNewsUuid(advertisement.getAppImgUrl());
			
			imgUrl.append(uploadfile.getUploadfilepath()+uploadfile.getUploadRealName());
			
			advertisement.setAppImgUrl(imgUrl.toString());
		 }
		 
		 AdvertisementDTO advertisementDTO = advertisementService.toDTO(advertisement);
		 
		 return advertisementDTO;
		 
	 }
	 
	/**
	 * 
	* @Description:获取站点应用广告列表数据
	* @author bann@sdfcp.com
	* @date 2015年11月16日 下午3:50:33
	 */
	 @RequestMapping(value = "/getStationAdList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getStationAdList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="adName",required=false) String adName,//模糊查询填写的站点应用广告名称
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
			
			if(null != adName && !"".equals(adName))
			{
				params.add("%"+adName+"%");//根据订单名称模糊查询
				buffer.append(" and appAdName like ?").append(params.size());
			}
			
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			/*TODO:获取当前市中心管辖下的站点的应用广告列表，并列出应用广告的审批状态，站点广告，addType=0，
			不能获取市中心可以管理的其他应用广告数据.且获取到的应用广告数据应该是creatorStationd的值不是空值的*/
			
			
			/*QueryResult<Orders> orderlist = orderService.getOrdersList(Orders.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			
			List<Orders> orders = orderlist.getResultList();
			Long totalrow = orderlist.getTotalRecord();
			
			List<OrdersDTO> orderDtos = orderService.toDTOS(orders);//将实体转换为dto
			
			returnData.put("rows", orderDtos);
			returnData.put("total", totalrow);*/
			
			return returnData;
		}
	 
	 
	
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="stationId",required=false) String stationId,//TODO:后期对接
				@RequestParam(value="appAdName",required=false) String appAdName,
				@RequestParam(value="addWord",required=false) String addWord,
				@RequestParam(value="appImgUrl",required=false) String appImgUrl,//TODO:广告图片上传//TODO:后期对接
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//应用广告的有效结束时间
				@RequestParam(value="adTime",required=false) String adTime,//应用广告的轮播时长
				@RequestParam(value="imgOrWord",required=false) String imgOrWord,//应用广告的展示形式，站点只能发布图片类广告
				@RequestParam(value="addType",required=false) String addType,//应用广告类型，//站点广告，addType=0
				@RequestParam(value="adStatus",required=false) String adStatus,//应用广告的状态，0：保存1：发布
				@RequestParam(value="appsdata",required=false) String appsdata,//TODO:后期对接，当前站点要给哪些应用发布这个应用广告
				@RequestParam(value="operatype",required=false) String operatype,//0:保存 1：保存并提交//TODO:后期对接
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   Advertisement advertisement = advertisementService.getAdvertisementById(id);
		   
		 //提取当前站点要给哪些应用发布这个应用广告
		   JSONObject appdatas = JSONObject.parseObject(appsdata);
		   List<String> apps =  (List<String>) appdatas.get("keys");
		   
		   //获取当前操作应用广告的通行证
		   Station station = stationService.getSationById(stationId);
		   String stationProvince = station.getProvinceCode();
		   String stationCity = station.getCityCode();
		   
		   
		   
		   
		   
		   if(null != advertisement)
		   {//应用广告数据不为空，则进行修改操作
			   
			   advertisement.setAppAdName(appAdName);
			   
			   if(AdvertisementController.AD_IMG.equals(imgOrWord))//imgOrWord=0
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
			   
			   String currentStatus = advertisement.getStationAdStatus();
			   if(stationAdController.OPERORTYPE_SAVE.equals(operatype))
			   {//保存
				   currentStatus = advertisement.getStationAdStatus();
			   }
			   else if(OrderController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
			   {
				   //根据当前状态获取下一状态
				   StationAdNextStatus stationAdNextStatus = stationAdStatusService.
						   getStationAdNextStatusBycurrentStatusId(advertisement.getStationAdStatus(), "1");
				   currentStatus = stationAdNextStatus.getNextStatusId();//通行证保存并提交应用广告
				   
			   }
			   
			   advertisement.setStationAdStatus(currentStatus);
			   advertisement.setStationAdStatusTime(new Timestamp(System.currentTimeMillis()));
			   advertisementService.update(advertisement);
			  
			   resultBean.setMessage("通行证修改应用广告成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("通行证修改应用广告--应用广告id="+id+"--操作通行证id="+stationId);
			   
		   }
		   else
		   {
			   advertisement = new Advertisement();
			   advertisement.setId(UUID.randomUUID().toString());
			   advertisement.setAppAdName(appAdName);
			   advertisement.setAdTime("30");//单位：秒
			   if(AdvertisementController.AD_IMG.equals(imgOrWord))//imgOrWord=0
			   {//图片类型的广告只放置图片
				   advertisement.setAppImgUrl(appImgUrl);//放置的是uploadfile表的newsUUid，用来关联图片附件数据
			   }
			   else if(AdvertisementController.AD_WORD.equals(imgOrWord))
			   {//文字类型的广告只放置文字
				   advertisement.setAddWord(addWord);
			   }
			   advertisement.setImgOrWord(imgOrWord);
			   advertisement.setAddType("0");//应用广告类别，广告类别,0：站点广告 1.市中心应用广告2.省中心应用广告3.公司应用广告
			   advertisement.setAdFontColor("#000000");
			   advertisement.setAdStatus(adStatus);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   advertisement.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   advertisement.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   
			   //因为是通行证发布的站点类别的应用广告，所以要放入站点创建人字段值
			   advertisement.setCreatorStation(stationId);
			   
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
			   
			   
			   advertisement.setIsDeleted("1");//有效数据标记位
			   
			   String currentStatus = "01";
			   if(stationAdController.OPERORTYPE_SAVE.equals(operatype))
			   {//保存
				   currentStatus = advertisement.getStationAdStatus();
			   }
			   else if(OrderController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
			   {
				   //根据当前状态获取下一状态
				   StationAdNextStatus stationAdNextStatus = stationAdStatusService.
						   getStationAdNextStatusBycurrentStatusId(advertisement.getStationAdStatus(), "1");
				   currentStatus = stationAdNextStatus.getNextStatusId();//通行证保存并提交应用广告
				   
			   }
			   
			   advertisement.setStationAdStatus(currentStatus);
			   advertisement.setStationAdStatusTime(new Timestamp(System.currentTimeMillis()));
			   
			   advertisementService.save(advertisement);
			   
			   Advertisement advertisementBeforeSave = advertisementService.getAdvertisementById(advertisement.getId());
			   
			   //整理并放置区域数据
			   List<AppAdAndArea> adAndAreas = new ArrayList<AppAdAndArea>();
				
			   AppAdAndArea aaa = new AppAdAndArea();//站点发布应用广告，则发布的区域就是通行证所在的区域
			   City city = cityService.getCityByCcode(stationCity);
			   aaa.setAdvertisement(advertisementBeforeSave);
			   aaa.setCity(city.getCcode());
			   aaa.setProvince(stationProvince);
			   
			   adAndAreaService.save(aaa);//保存应用广告与区域关联表的数据
			   
			   adAndAreas.add(aaa);
			   advertisementBeforeSave.setAppAdAndAreas(adAndAreas);
			   
			   advertisementService.update(advertisementBeforeSave);
			  
			   
			 
			   
			   resultBean.setMessage("通行证添加应用广告成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("通行证添加应用广告--操作通行证id="+stationId);
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 
	 /**
	  * 
	 * @Description: 保存站点应用广告状态跟踪表数据
	 * @author bann@sdfcp.com
	 * @date 2015年11月19日 下午1:45:17
	  */
	 private void saveFoundStationAdStatus(String creator,String currentStatus,Advertisement advertisement)
	 {
		 FoundStationAdStatus foundStationAdStatus = new FoundStationAdStatus();
		 foundStationAdStatus.setCreator(creator);
		 foundStationAdStatus.setStatus(currentStatus);
		 foundStationAdStatus.setStatusSj(new Timestamp(System.currentTimeMillis()));
		 foundStationAdStatus.setAdvertisement(advertisement);
	     foundStationAdStatusService.save(foundStationAdStatus);
	 }
	 
	 /**
	  * 
	 * @Description: 校验应用广告是否已审批完成并发布 
	 * @author bann@sdfcp.com
	 * @date 2015年11月19日 下午2:36:41
	  */
	 @RequestMapping(value = "/checkAppAdStatus", method = RequestMethod.POST)
		public @ResponseBody ResultBean checkAppAdStatus(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	ResultBean resultBean = new ResultBean();
		 	boolean stationAdFinish = false;
		 	
		 	Advertisement advertisement = advertisementService.getAdvertisementById(id);
		 	
		 	if(stationAdController.STATION_AD_FINISH.equals(advertisement.getStationAdStatus()))
		 	{
		 		stationAdFinish = true;
		 	}
		 	resultBean.setExist(stationAdFinish);
		 	return resultBean;
		}
	 
	
	 
	
	 /**
	  * 
	 * @Title: deleteStationAd
	 * @Description: 删除通行证发布的应用广告信息
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/deleteStationAd", method = RequestMethod.POST)
		public @ResponseBody ResultBean deleteStationAd(
				@RequestParam(value="ids",required=false) String[] ids,
				@RequestParam(value="stationId",required=false) String stationId,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 ResultBean resultBean = new ResultBean();
		 
		 Advertisement advertisement;
		 List<App> apps = new ArrayList<App>();
		 List<AppAdAndArea> appAdAndAreas = new ArrayList<AppAdAndArea>();
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
			 		
			 		
			 		//删除关联的区域数据
			 		List<AppAdAndArea> adAndAreasBefore = advertisement.getAppAdAndAreas();//获取之前绑定的应用广告的区域信息
					   for (AppAdAndArea appAdAndArea : adAndAreasBefore) {
						   adAndAreaService.delete(appAdAndArea);
						   logger.info("通行证删除应用广告与区域关联数据--关联id="+appAdAndArea.getId()+"==应用广告告id="+id+"--操作通行证id="+stationId);
					   }
					
					advertisement.setAppAdAndAreas(appAdAndAreas);
			 		advertisementService.update(advertisement);
			 		
			 		 //日志输出
					 logger.info("通行证删除应用广告数据--应用广告id="+id+"--操作通行证id="+stationId);
				   
			 	}
			}
		 String returnMsg = "通行证删除应用广告成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
		}
	 
	 
	 /**
	  * 
	 * @Description: 更新应用广告状态
	 * @author bann@sdfcp.com
	 * @date 2015年11月23日 下午2:48:16
	  */
	 @RequestMapping(value = "/approveStationAds", method = RequestMethod.POST)
		public @ResponseBody ResultBean approveStationAds(
				@RequestParam(value="adId",required=false) String adId,
				@RequestParam(value="operortype",required=false) String operortype,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			ResultBean resultBean = new ResultBean();
			
			Advertisement advertisement = advertisementService.getAdvertisementById(adId);
		    String currentStatus = advertisement.getStationAdStatus();
		    String directFlag = "1";
		    if(stationAdController.PAGE_OPERORTYPE_SAVE.equals(operortype))
		    {//通行证再次操作应用广告提交
		    	StationAdNextStatus stationAdNextStatus = stationAdStatusService.getStationAdNextStatusBycurrentStatusId(currentStatus, directFlag);
		    	currentStatus = stationAdNextStatus.getNextStatusId();
		    }
		    else if(stationAdController.PAGE_OPERORTYPE_PASS.equals(operortype))
		    {//市中心审批通过
		    	StationAdNextStatus stationAdNextStatus = stationAdStatusService.getStationAdNextStatusBycurrentStatusId(currentStatus, directFlag);
		    	currentStatus = stationAdNextStatus.getNextStatusId();
			  
				   
		    	advertisement.setAdStatus("1");//若市中心审批通过则置通行证发布的这个应用广告为发布状态
		    	
		    	//TODO:同时要设置之前通行证申请发布的应用广告为无效，且不可以展示，因为通行证在一个应用上只能发布一个应用广告！！！
			   
			  
		    }
		    else if(stationAdController.PAGE_OPERORTYPE_REJECT.equals(operortype))
		    {//市中心审批驳回
		       directFlag = "0";
		       StationAdNextStatus stationAdNextStatus = stationAdStatusService.getStationAdNextStatusBycurrentStatusId(currentStatus, directFlag);
		    	currentStatus = stationAdNextStatus.getNextStatusId();
		    }
		    else if(stationAdController.PAGE_OPERORTYPE_STOP.equals(operortype))
		    {//财管订单列表不通过
			   currentStatus = stationAdController.STATION_AD_STOP;//置订单状态为“不通过”的状态码
		    }
		    
		   advertisement.setStationAdStatus(currentStatus);
		   advertisement.setStationAdStatusTime(new Timestamp(System.currentTimeMillis()));
		   advertisementService.update(advertisement);
		   
		   //由于状态变化，将变化状态存入到状态流程跟踪表中
			this.saveFoundStationAdStatus(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,advertisement);
			
			resultBean.setStatus("success");
			resultBean.setMessage("审批通行证应用广告成功!");
			
			return resultBean;
		}
	 
	 
	 
	 /**
	  * 
	 * @Description: 获取当前登录用户的角色
	 * @author bann@sdfcp.com
	 * @date 2015年11月24日 下午2:44:42
	  */
	 @RequestMapping(value = "/getLoginuserRole", method = RequestMethod.POST)
		public @ResponseBody ResultBean getLoginuserRole(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	ResultBean resultBean = new ResultBean();
		 	
		 	//获取session中的登录数据
			String code = LoginUtils.getAuthenticatedUserCode(httpSession);
			User user = userService.getUserByCode(code);
			//获取当前登录人员的角色list
			List<Role> roles = user.getRoles();
			
			//根据“代理”和“财政管理员”的角色id获取对应的角色数据，用来判断当前用户的角色中是否有权限
			Role roleProxy = roleService.getRoleById(Constants.ROLE_PROXY_ID);
			Role roleCityManger = roleService.getRoleById(Constants.ROLE_CITY_CENTER_ID);
			
			if(roles.contains(roleProxy))
			{
				resultBean.setProxy(true);
				//若为代理，返回当前登陆人的id
				resultBean.setMessage(code);
			}
			else
			{
				resultBean.setProxy(false);
			}
			
			if(roles.contains(roleCityManger))
			{
				resultBean.setCityCenterManager(true);
				resultBean.setMessage(code);
			}
			else
			{
				resultBean.setCityCenterManager(false);
			}
			
		 	
		 	return resultBean;
		}
	
	
	
	 
	 
	 
	 
}
