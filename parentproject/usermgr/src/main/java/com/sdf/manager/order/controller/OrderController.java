package com.sdf.manager.order.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import com.sdf.manager.app.controller.AppController;
import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.entity.UserYearDiscount;
import com.sdf.manager.appUnitPrice.service.AppUPriceService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.goods.service.GoodsService;
import com.sdf.manager.order.dto.OrdersDTO;
import com.sdf.manager.order.dto.RenewAppDTO;
import com.sdf.manager.order.entity.FoundOrderStatus;
import com.sdf.manager.order.entity.OrderNextStatus;
import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.order.service.FoundOrderStatusService;
import com.sdf.manager.order.service.OrderService;
import com.sdf.manager.order.service.OrderStatusService;
import com.sdf.manager.order.service.RelaBsStaAppHisService;
import com.sdf.manager.order.service.RelaBsStaAppService;
import com.sdf.manager.order.service.RelaSdfStationProService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProductService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.service.RoleService;
import com.sdf.manager.user.service.UserService;


/**
 * 
  * @ClassName: ProductController 
  * @Description: 产品管理的控制层类
  * @author bann@sdfcp.com
  * @date 2015年11月3日 上午9:59:21 
  *
 */
@Controller
@RequestMapping("/order")
public class OrderController extends GlobalExceptionHandler
{
	 private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	 @Autowired
	 private OrderService orderService;
	 
	 @Autowired
	 private ProvinceService provinceService;
	 
	 @Autowired
	 private CityService cityService;
	 
	 @Autowired
	 private GoodsService goodsService;
	 
	 @Autowired
	 private UserService userService;
	 
	 @Autowired
	 private RoleService roleService;
	 
	 @Autowired
	 private FoundOrderStatusService foundOrderStatusService;//状态跟踪表的service层
	 
	 @Autowired
	 private OrderStatusService orderStatusService;//订单状态service层
	 
	 @Autowired
	 private RelaSdfStationProService relaSdfStationProService;
	 
	 @Autowired
	 private StationService stationService;
	 
	 @Autowired
	 private ProductService productService;
	 
	 @Autowired
	 private AppService appService;
	 
	 @Autowired
	 private AppUPriceService appUPriceService;
	 
	 @Autowired
	 private RelaBsStaAppService relaBsStaAppService;
	 
	 @Autowired
	 private RelaBsStaAppHisService relaBsStaAppHisService;
	 
	 
	 public static final int SERIAL_NUM_LEN = 6;//订单流水号中自动生成的数字位数
	 
	 public static final String OPERORTYPE_SAVE = "0";//代理编辑页面，保存
	 public static final String OPERORTYPE_SAVEANDCOMMIT = "1";//代理编辑页面，保存并提交
	
	 /***订单状态静态变量 start***/
	 public static final String PROXY_SAVE_ORDER = "01";//代理购买商品形成订单时，代理保存的状态码
	 public static final String ORDER_FINISH = "21";//审批完成且已归档的订单状态
	 public static final String ORDER_STOP = "31";//审批不通过，终止订单审批状态
	 /***订单状态静态变量 end***/
	
	 public static final String DIRECTION_GO = "1";//前进方向标志位
	 public static final String DIRECTION_BACK = "1";//后退方向标志位
	 
	 public static final String PAGE_OPERORTYPE_SAVE = "1";//代理订单列表，提交
	 public static final String PAGE_OPERORTYPE_PASS = "2";//财管订单列表，审批通过
	 public static final String PAGE_OPERORTYPE_REJECT = "3";//财管订单列表，审批驳回
	 public static final String PAGE_OPERORTYPE_STOP = "4";//财管订单列表，不通过
	 
	 public static final String STATION_PRODUCT_INVALID_STATUS = "0";//站点和产品关联数据无效
	 public static final String STATION_PRODUCT_VALID_STATUS = "1";//站点和产品关联数据有效
	 
	 
	 
	 
	/**
	 * 
	* @Description: 根据订单id查询订单数据
	* @author bann@sdfcp.com
	* @date 2015年11月16日 上午9:59:00
	 */
	 @RequestMapping(value = "/getDetailOrders", method = RequestMethod.GET)
	 public @ResponseBody OrdersDTO getDetailOrders(
			@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	 {
		 Orders order = new Orders();
		 
		 order = orderService.getOrdersById(id);
		 
		 OrdersDTO orderDto = orderService.toDTO(order);
	 	 
		 return orderDto;
	 }
	 
	/**
	 * 
	* @Description:获取订单列表数据（※根据当前登录人员的信息加载订单数据，代理是加载其下属站点对应的所有订单的数据）
	* @author bann@sdfcp.com
	* @date 2015年11月16日 下午3:50:33
	 */
	 @RequestMapping(value = "/getOrdersList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getOrdersList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="orderName",required=false) String orderName,//模糊查询填写的订单名称
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
			
			if(null != orderName && !"".equals(orderName))
			{
				params.add("%"+orderName+"%");//根据订单名称模糊查询
				buffer.append(" and name like ?").append(params.size());
			}
			
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<Orders> orderlist = orderService.getOrdersList(Orders.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			
			List<Orders> orders = orderlist.getResultList();
			Long totalrow = orderlist.getTotalRecord();
			
			List<OrdersDTO> orderDtos = orderService.toDTOS(orders);//将实体转换为dto
			
			returnData.put("rows", orderDtos);
			returnData.put("total", totalrow);
			
			return returnData;
		}
	 
	 
	/**
	 * 
	* @Description:保存/修改产品数据
	* @author bann@sdfcp.com
	* @date 2015年11月3日 上午10:04:28
	 */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="code",required=false) String code,
				@RequestParam(value="name",required=false) String name,
				@RequestParam(value="price",required=false) String price,
				@RequestParam(value="transCost",required=false) String transCost,//运费
				@RequestParam(value="payMode",required=false) String payMode,//支付方式，下拉框
				@RequestParam(value="receiveAddr",required=false) String receiveAddr,//收件人地址
				@RequestParam(value="receiveTele",required=false) String receiveTele,//收件人电话
				@RequestParam(value="status",required=false) String status,//状态
				@RequestParam(value="appId",required=false) String appId,//购买的应用的id
				@RequestParam(value="station",required=false) String station,//通行证id
				@RequestParam(value="userYearId",required=false) String userYearId,//使用年限id
				@RequestParam(value="operatype",required=false) String operatype,//0:保存 1：保存并提交
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   Orders order ;
		   order = orderService.getOrdersById(id);
		   
		   if(null != order)
		   {//商品数据不为空，则进行修改操作
			  
			   /*修改订单时为哪个通行证购买应用是不可以修改的，且购买的是哪个应用也是不可以修改的，
			    * 因为形成订单的最小单位是以应用为单位形成订单，也就是说购买一个应用形成一个订单
			    */
			   App app = appService.getAppById(appId);
			   Station stationentity = stationService.getSationById(station);
			   UserYearDiscount userYearDiscount = appUPriceService.getUserYearDiscountById(userYearId);
//			   order.setName(app.getAppName());//订单名称就是应用名称
			   order.setPrice(price);
//			   order.setApp(app);
//			   order.setStation(stationentity);
			   order.setUserYearDiscount(userYearDiscount);
			   
//			   order.setCode(code);
//			   order.setTransCost(transCost);
//			   order.setPayMode(payMode);
//			   order.setReceiveAddr(receiveAddr);
//			   order.setReceiveTele(receiveTele);
			   
			   
			   order.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   order.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   String currentStatus = order.getStatus();
			   if(OrderController.OPERORTYPE_SAVE.equals(operatype))
			   {//保存
				   currentStatus = order.getStatus();
			   }
			   else if(OrderController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
			   {
				   //根据当前状态获取下一状态
				   OrderNextStatus orderNextStatus = orderStatusService.getOrderNextStatusBycurrentStatusId(order.getStatus(), "1");
				   currentStatus = orderNextStatus.getNextStatusId();//代理保存并提交
				   
			   }
			   order.setStatus(currentStatus);
			   order.setStatusTime(new Timestamp(System.currentTimeMillis()));
			   orderService.update(order);
			   resultBean.setMessage("修改订单信息成功!");
			   resultBean.setStatus("success");
			   
			   if(OrderController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
			   {
				 //由于状态变化，将变化状态存入到状态流程跟踪表中
				   this.saveFoundOrderStatus(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,order);
			   }
			   
			  
			   //日志输出
			   logger.info("修改订单--订单code="+code+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   
			   
			   order = new Orders();
			   String ordercode = this.codeGenertor();
			   App app = appService.getAppById(appId);
			   Station stationentity = stationService.getSationById(station);
			   UserYearDiscount userYearDiscount = appUPriceService.getUserYearDiscountById(userYearId);
			   order.setName(app.getAppName());//订单名称就是应用名称
			   order.setPrice(price);
			   order.setApp(app);
			   order.setStation(stationentity);
			   order.setUserYearDiscount(userYearDiscount);
			   
			   order.setName(app.getAppName());
			   order.setCode(ordercode);
//			   order.setTransCost(transCost);
//			   order.setPayMode(payMode);
//			   order.setReceiveAddr(receiveAddr);
//			   order.setReceiveTele(receiveTele);
			   order.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   
			   
			   //setCreator中放置的是创建订单人的name
			   order.setCreator(LoginUtils.getAuthenticatedUserName(httpSession));
			   order.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   order.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   order.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   order.setIsDeleted("1");//有效数据
			   String currentStatus = "01";
			   if(OrderController.OPERORTYPE_SAVE.equals(operatype))
			   {
				   currentStatus = OrderController.PROXY_SAVE_ORDER;//购买商品形成订单时，代理保存的状态
			   }
			   else if(OrderController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
			   {
				   OrderNextStatus orderNextStatus = orderStatusService.
						   getOrderNextStatusBycurrentStatusId(OrderController.PROXY_SAVE_ORDER,OrderController.DIRECTION_GO );
				   currentStatus = orderNextStatus.getNextStatusId();//代理保存并提交
			   }
			   order.setStatus(currentStatus);//代理保存订单
			   order.setStatusTime(new Timestamp(System.currentTimeMillis()));
			   orderService.save(order);
			   
			   
			   //跟踪订单状态，添加订单状态到订单状态跟踪表中
			   
			   /*finishSaveOrder用处：用来做订单跟踪表与订单表的数据关联，因为若不获取，
			   	则当前操作的订单还不存在，无法存入其状态(订单编码也是全局唯一的！！！)*/
			   Orders finishSaveOrder = orderService.getOrdersByCode(ordercode);
			   
			   if(OrderController.OPERORTYPE_SAVE.equals(operatype))
			   {
				   currentStatus = OrderController.PROXY_SAVE_ORDER;//购买商品形成订单时，代理保存的状态
				   this.saveFoundOrderStatus(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,finishSaveOrder);
			   }
			   else if(OrderController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
			   {  /*若代理在购买商品时保存并提交到财务管理员审批时，当前订单是有两个状态变化的，
				   所以要向订单状态跟踪表中放入两条数据*/
				  //1.保存“代理保存订单”状态跟踪数据
				   this.saveFoundOrderStatus(LoginUtils.getAuthenticatedUserCode(httpSession),OrderController.PROXY_SAVE_ORDER,order);
				   
				   //2.保存“提交财务管理员”状态跟踪数据
				   this.saveFoundOrderStatus(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,finishSaveOrder);
			   }
			   
			   
			  
			   
			   resultBean.setMessage("添加订单信息成功!请在订单管理中跟踪订单的审批进度。");
			   resultBean.setStatus("success");
			   
			   //日志输出
			   logger.info("添加订单--订单code="+code+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	/* *//**
	  * 
	 * @Title: calculateDuration
	 * @Description: TODO(计算使用期和试用期的和)
	 * @Author : banna
	 * @param productId:放置的是产品id
	 * @param probation：站点对应该产品的试用期
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	  *//*
	 private String calculateDuration(String productId,String probation)
	 {
		 
		 Product product = productService.getProductById(productId);
		 
		 String durationOfUser = product.getDurationOfusers().getNumOfDays();
		 
		 int duration = Integer.parseInt(durationOfUser);
		 int probationDay = Integer.parseInt(probation);
		 
		 int count  = duration + probationDay;
		 
		 String countResult = count+"";
		 
		 return countResult;
	 }*/
	 
	 /**
	  * 
	 * @Description: 保存订单状态跟踪表数据
	 * @author bann@sdfcp.com
	 * @date 2015年11月19日 下午1:45:17
	  */
	 private void saveFoundOrderStatus(String creator,String currentStatus,Orders order)
	 {
		 FoundOrderStatus foundOrderStatus = new FoundOrderStatus();
	     foundOrderStatus.setCreator(creator);
	     foundOrderStatus.setStatus(currentStatus);
	     foundOrderStatus.setStatusSj(new Timestamp(System.currentTimeMillis()));
	     foundOrderStatus.setOrders(order);
	     foundOrderStatusService.save(foundOrderStatus);
	 }
	 
	 /**
	  * 
	 * @Description: 校验订单是否已审批完成并已归档 
	 * @author bann@sdfcp.com
	 * @date 2015年11月19日 下午2:36:41
	  */
	 @RequestMapping(value = "/checkOrderStatus", method = RequestMethod.POST)
		public @ResponseBody ResultBean checkOrderStatus(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	ResultBean resultBean = new ResultBean();
		 	boolean orderFinish = false;
		 	
		 	Orders order = orderService.getOrdersById(id);
		 	
		 	if(OrderController.ORDER_FINISH.equals(order.getStatus()))
		 	{
		 		orderFinish = true;
		 	}
		 	resultBean.setExist(orderFinish);
		 	return resultBean;
		}
	 
	 /**
	  * 
	 * @Description: 获取当前订单的商品数据 
	 * @author bann@sdfcp.com
	 * @date 2015年11月19日 下午2:55:36
	  *//*
	 @RequestMapping(value = "/getGoodsOfOrder", method = RequestMethod.GET)
		public @ResponseBody List<GoodsDTO> getGoodsOfOrder(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	List<Goods> goods = new ArrayList<Goods>();
		 	
		 	Orders order = orderService.getOrdersById(id);
		 	
		 	if(null != order.getGoods() && order.getGoods().size()>0)
		 	{
		 		goods = order.getGoods();
		 	}
		 	
		 	
		 	List<GoodsDTO> goodsDtos = goodsService.toDTOS(goods);
		 	
		 	return goodsDtos;
		}*/
	 
	/* *//**
	  * 
	 * @Description: 根据订单id获取订单下选中的商品下产品和站点关联的数据
	 * @author bann@sdfcp.com
	 * @date 2015年11月26日 下午5:37:11
	  *//*
	 @RequestMapping(value = "/getStationAndProducts", method = RequestMethod.POST)
		public @ResponseBody List<RelaSdfStationProduct> getStationAndProducts(
				@RequestParam(value="orderId",required=false) String orderId,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	List<RelaSdfStationProduct> relaSdfStationProducts = relaSdfStationProService.getRelaSdfStationProductByOrderId(orderId);
		
		 	return relaSdfStationProducts;
		}*/
	 
	 
	
	 /**
	  * 
	 * @Title: deleteOrders
	 * @Description: 删除订单信息
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/deleteOrders", method = RequestMethod.POST)
		public @ResponseBody ResultBean deleteOrders(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			ResultBean resultBean = new ResultBean();
			
			Orders orders;
			App app = new App();
			Station station = new Station();
			for (String id : ids) 
			{
				orders = new Orders();
				orders =  orderService.getOrdersById(id);
				orders.setIsDeleted("0");;//设置当前数据为已删除状态
				
				//删除和应用还有通行证的关联关系
				orders.setApp(app);
				orders.setStation(station);
				
				orders.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				orders.setModifyTime(new Timestamp(System.currentTimeMillis()));
				orderService.update(orders);
				
				 //日志输出
				 logger.info("删除订单--订单id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			}
			
			
			resultBean.setStatus("success");
			resultBean.setMessage("删除成功!");
			
			return resultBean;
		}
	 
	 
	 /**
	  * 
	 * @Description: 更新订单状态
	 * @author bann@sdfcp.com
	 * @date 2015年11月23日 下午2:48:16
	  */
	 @RequestMapping(value = "/approveOrders", method = RequestMethod.POST)
		public @ResponseBody ResultBean approveOrders(
				@RequestParam(value="orderId",required=false) String orderId,
				@RequestParam(value="operortype",required=false) String operortype,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			ResultBean resultBean = new ResultBean();
			
			Orders order = orderService.getOrdersById(orderId);
		    String currentStatus = order.getStatus();
		    String directFlag = "1";
		    if(OrderController.PAGE_OPERORTYPE_SAVE.equals(operortype))
		    {//代理审批通过
		    	OrderNextStatus orderNextStatus = orderStatusService.getOrderNextStatusBycurrentStatusId(currentStatus, directFlag);
		    	currentStatus = orderNextStatus.getNextStatusId();
		    }
		    else if(OrderController.PAGE_OPERORTYPE_PASS.equals(operortype))
		    {//财管订单列表审批通过
			   OrderNextStatus orderNextStatus = orderStatusService.getOrderNextStatusBycurrentStatusId(currentStatus,directFlag);
			   currentStatus = orderNextStatus.getNextStatusId();
			  
			   //TODO:财务管理员审批通过后，要向“通行证和应用关联表”中插入数据，且同时也要向“通行证和应用关联从表”中插入数据，这个表用来购买的所有历史记录
			   String stationId = order.getStation().getId();
			   String appId = order.getApp().getId();
			   
			   //根据通行证id和应用id获取当前通行证正在使用该应用的关联数据sql：where  u.isDeleted ='1' and u.status = '1' and  u.station.id =?1 and  u.app.id =?2
			   RelaBsStationAndApp relaBsStationAndApp = relaBsStaAppService.
					   		getRelaBsStationAndAppByStationIdAndAppId(stationId, appId);
			   if(null != relaBsStationAndApp)
			   {//已经购买过应用，做的续费操作
				   //1.更新“通行证与应用”关联表中结束时间
				   Timestamp lastEndtime = relaBsStationAndApp.getEndTime();//上次一购买应用的到期时间
				   Date newStartTime = DateUtil.
						   				getNextDayOfCurrentTime(lastEndtime, 1);//新的开始时间是从上次购买的到期时间的第二天开始计算的
				   Timestamp newStTimestamp = DateUtil.formatDateToTimestamp(newStartTime, DateUtil.FULL_DATE_FORMAT);//新数据的开始时间
				   
				   Date endtime;//新数据的结束时间
				   endtime = DateUtil.getNextDayOfCurrentTime(newStTimestamp, Integer.parseInt(order.getUserYearDiscount().getDayOfyear()));
				   
				   relaBsStationAndApp.setEndTime(DateUtil.formatDateToTimestamp(endtime, DateUtil.FULL_DATE_FORMAT));
				   relaBsStationAndApp.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndApp.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   relaBsStaAppService.update(relaBsStationAndApp);//通行证对当前应用续费后，更新通行证使用当前应用的到期时间
				   
				   //2.向“通行证和应用历史记录关联表”插入数据
				   RelaBsStationAndAppHis relaBsStationAndAppHis = new RelaBsStationAndAppHis();
				   relaBsStationAndAppHis.setApp(order.getApp());
				   relaBsStationAndAppHis.setStation(order.getStation());
				   relaBsStationAndAppHis.setStartTime(newStTimestamp);//开始时间
				   relaBsStationAndAppHis.setEndTime(DateUtil.formatDateToTimestamp(endtime, DateUtil.FULL_DATE_FORMAT));
				   relaBsStationAndAppHis.setIsDeleted(Constants.IS_NOT_DELETED);
				   relaBsStationAndAppHis.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndAppHis.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				   relaBsStationAndAppHis.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndAppHis.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   
				   relaBsStaAppHisService.save(relaBsStationAndAppHis);
				   
			   }
			   else
			   {//第一次购买应用
				   //1.向“通行证与应用”关联表中插入数据
				   relaBsStationAndApp = new RelaBsStationAndApp();
				   relaBsStationAndApp.setApp(order.getApp());
				   relaBsStationAndApp.setStation(order.getStation());
				   relaBsStationAndApp.setStartTime(new Timestamp(System.currentTimeMillis()));//开始时间
				   Date endtime;
				   endtime = DateUtil.getNextDay(Integer.parseInt(order.getUserYearDiscount().getDayOfyear()));
				   relaBsStationAndApp.setEndTime(DateUtil.formatDateToTimestamp(endtime, DateUtil.FULL_DATE_FORMAT));
				   relaBsStationAndApp.setStatus("1");//正在使用状态
				   relaBsStationAndApp.setIsDeleted(Constants.IS_NOT_DELETED);
				   relaBsStationAndApp.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndApp.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				   relaBsStationAndApp.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndApp.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   
				   relaBsStaAppService.save(relaBsStationAndApp);
				   
				   //2.向“通行证和应用历史记录关联表”插入数据
				   RelaBsStationAndAppHis relaBsStationAndAppHis = new RelaBsStationAndAppHis();
				   relaBsStationAndAppHis.setApp(order.getApp());
				   relaBsStationAndAppHis.setStation(order.getStation());
				   relaBsStationAndAppHis.setStartTime(new Timestamp(System.currentTimeMillis()));//开始时间
				   relaBsStationAndAppHis.setEndTime(DateUtil.formatDateToTimestamp(endtime, DateUtil.FULL_DATE_FORMAT));
				   relaBsStationAndAppHis.setIsDeleted(Constants.IS_NOT_DELETED);
				   relaBsStationAndAppHis.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndAppHis.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				   relaBsStationAndAppHis.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   relaBsStationAndAppHis.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   
				   relaBsStaAppHisService.save(relaBsStationAndAppHis);
				   
				   
			   }
			   
			  
		    }
		    else if(OrderController.PAGE_OPERORTYPE_REJECT.equals(operortype))
		    {//财管订单列表审批驳回
		       directFlag = "0";
			   OrderNextStatus orderNextStatus = orderStatusService.getOrderNextStatusBycurrentStatusId(currentStatus,directFlag);
			   currentStatus = orderNextStatus.getNextStatusId();
		    }
		    else if(OrderController.PAGE_OPERORTYPE_STOP.equals(operortype))
		    {//财管订单列表不通过
			   currentStatus = OrderController.ORDER_STOP;//置订单状态为“不通过”的状态码
		    }
		    
		   order.setStatus(currentStatus);
		   order.setStatusTime(new Timestamp(System.currentTimeMillis()));
		   order.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
		   order.setModifyTime(new Timestamp(System.currentTimeMillis()));
		   orderService.update(order);
		   
		   //由于状态变化，将变化状态存入到状态流程跟踪表中
			this.saveFoundOrderStatus(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,order);
			
			resultBean.setStatus("success");
			resultBean.setMessage("审批订单成功!");
			
			return resultBean;
		}
	 
	 /**
	  * 
	 * @Description: 生成订单code(全局唯一)（日期+流水号）
	 * @author bann@sdfcp.com
	 * @date 2015年11月17日 下午4:09:05
	  */
	 @RequestMapping(value = "/generateOrdercode", method = RequestMethod.POST)
		public @ResponseBody Map<String,Object>  generateOrdercode(
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
	* @Description:生成订单编码 
	* //规则：年月日(yyyyMMdd)+6位流水号
	* @author bann@sdfcp.com
	* @date 2015年11月18日 上午10:31:02
	 */
	 private  synchronized String codeGenertor()
	 {
		 
		 StringBuffer orderCode = new StringBuffer();
		//获取当前年月日
		 String date = "";
		 Date dd  = Calendar.getInstance().getTime();
		 date = DateUtil.formatDate(dd, DateUtil.FULL_DATE_FORMAT);
		 String year = date.substring(0, 4);//半包，不包括最大位数值
		 String month = date.substring(5, 7);
		 String day = date.substring(8, 10);
		 orderCode.append(year).append(month).append(day);
		 
		 //验证当天是否已生成订单
		//放置分页参数
			Pageable pageable = new PageRequest(0,10000);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
/*			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
*/			
			params.add(orderCode+"%");//根据订单名称模糊查询
			buffer.append(" code like ?").append(params.size());
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("code", "desc");//大号的code排在前面
			
			QueryResult<Orders> orderlist = orderService.getOrdersList(Orders.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(orderlist.getResultList().size()>0)
			{
				String maxCode = orderlist.getResultList().get(0).getCode();
				String weihao = maxCode.substring(8, maxCode.length());
				int num = Integer.parseInt(weihao);
				String newNum = (++num)+"";
				int needLen = (OrderController.SERIAL_NUM_LEN-newNum.length());
				for(int i=0;i<needLen;i++)
				{
					newNum = "0"+newNum;
				}
				orderCode.append(newNum);
			}
			else
			{//当天还没有生成订单号
				orderCode.append("000001");
			}
			
		 
			return orderCode.toString();
	 }
	 
	 /**
	  * 
	 * @Description: 校验订单名称唯一性
	 * @author bann@sdfcp.com
	 * @date 2015年11月16日 下午2:58:17
	  */
	 @RequestMapping(value = "/checkOrderName", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkOrderName(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="name",required=false) String name,
				ModelMap model,HttpSession httpSession) throws Exception {
			
			ResultBean resultBean = new ResultBean ();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,100000);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
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
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<Orders> orderlist = orderService.getOrdersList(Orders.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(orderlist.getResultList().size()>0)
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
			Role roleFManger = roleService.getRoleById(Constants.ROLE_FINANCIAL_MANAGER_ID);
			
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
			
			if(roles.contains(roleFManger))
			{
				resultBean.setFinancialManager(true);
				resultBean.setMessage(code);
			}
			else
			{
				resultBean.setFinancialManager(false);
			}
			
		 	
		 	return resultBean;
		}
	
	 /**
	  * 
	 * @Description: TODO(获取站点列表数据) 
	 * @author bann@sdfcp.com
	 * @date 2015年11月25日 下午2:39:11
	  */
	 /*@RequestMapping(value = "/getStationList", method = RequestMethod.POST)
		public @ResponseBody List<StationDto> getStationList(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	
		 	List<Station> stations = new ArrayList<Station>();
		 	//获取当前登录人员的用户信息
			String code = LoginUtils.getAuthenticatedUserCode(httpSession);//登录用户的code
		 	String userId = LoginUtils.getAuthenticatedUserId(httpSession);
			*//***根据登录人员的和站点关联的字段，查询当前登录用户的下属站点列表***//*
			List<Station> stations2 = new ArrayList<Station>();
			stations2 = stationService.getStationByAgentId(userId);
			List<StationDto> stationDtos = new ArrayList<StationDto>();
			String stationtypeText = "";
			
			for (Station station : stations2) {
				
				if(station.getStationType().equals(Constants.LOTTERY_TYPE_FC))
			 	{
					stationtypeText = "福彩";
			 	}
			 	else
			 		if(station.getStationType().equals(Constants.LOTTERY_TYPE_TC))
				 	{
			 			stationtypeText = "体彩";
				 	}
				
				StationDto stationDto = new StationDto();
				stationDto.setId(station.getId());
				stationDto.setStationNumber(stationtypeText+"--"+station.getStationNumber());
				
				stationDtos.add(stationDto);
			}
		 	
			
		 	return stationDtos;
		}*/
	 
	 
	/* *//**
	  * 
	 * @Description: 获取当前站点的其他类别的站点
	 * @author bann@sdfcp.com
	 * @date 2015年12月1日 下午1:58:41
	  *//*
	 @RequestMapping(value = "/getOtherStations", method = RequestMethod.POST)
		public @ResponseBody List<StationDto> getOtherStations(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	List<StationDto> stationDtos = new ArrayList<StationDto>();
		 	
		 	//1.获取当前选中站点详情
		 	Station station = stationService.getSationById(id);
		 	
		 	String stationType = station.getStationType();
		 	String owner  = station.getOwner();//获取站主姓名
		 	String telephone = station.getOwnerTelephone();//获取站主联系电话
		 	String stationtypeText = "";
		 	
		 	//若是福彩类型，则查询体彩站点，若为体彩类型，则查询福彩站点
		 	if(stationType.equals(Constants.LOTTERY_TYPE_FC))
		 	{
		 		stationType = Constants.LOTTERY_TYPE_TC;
		 		stationtypeText = "体彩";
		 	}
		 	else
		 		if(stationType.equals(Constants.LOTTERY_TYPE_TC))
			 	{
			 		stationType = Constants.LOTTERY_TYPE_FC;
			 		stationtypeText = "福彩";
			 	}
		 	
		 	//2.根据站主姓名和联系电话获取当前站主其他类别站点（规定：站主姓名和站主联系电话可以唯一确定一个站主）
		 	
		 	List<Station> stations = stationService.getStationByStationTypeAndOwnerAndOwnertelephone
		 			(stationType, owner, telephone);
		 	
		 	for (Station station2 : stations) {
				
				StationDto stationDto = new StationDto();
				stationDto.setId(station2.getId());
				stationDto.setStationNumber(stationtypeText+"--"+station2.getStationNumber());
				
				stationDtos.add(stationDto);
			}
		 	
		 	
		 	return stationDtos;
		}
	 */
	 
	 /**
	  * 
	 * @Title: getXufeiAppList
	 * @Description: 获取当前通行证可以进行续费的应用数据
	 * @param @param stationId
	 * @param @param province
	 * @param @param city
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return Map<String,Object>    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/getXufeiAppList", method = RequestMethod.GET)
		public @ResponseBody Map<String ,Object> getXufeiAppList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="stationId",required=false) String stationId,
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="city",required=false) String city,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	Map<String ,Object> resultData = new HashMap<String, Object>();
		 	
		 	//放置分页参数
			Pageable pageable = new PageRequest(page-1,rows);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			Station station = stationService.getSationById(stationId);
			String sprovince = station.getProvinceCode();
			String scity = station.getCityCode();
			String lotteryType = station.getStationType();//获取通行证的彩种
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			/*//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			params.add("1");//正在使用的应用
			buffer.append(" and status = ?").append(params.size());
			
			params.add(AppController.APP_STATUS_SJ);//正在上架的应用
			buffer.append(" and app.appStatus = ?").append(params.size());
			
			//连接查询条件
			if(null != stationId&&!"".equals(stationId.trim()))
			{
				params.add(stationId);
				buffer.append(" and station.id = ?").append(params.size());
			}
		 	
			
			
			QueryResult<RelaBsStationAndApp> applist = relaBsStaAppService.getRelaBsStationAndAppList(RelaBsStationAndApp.class,
					buffer.toString(), params.toArray(),orderBy, pageable);*/
			
			QueryResult<RelaBsStationAndApp> applist = appService.getAppOfXufei(RelaBsStationAndApp.class,
					buffer.toString(), params.toArray(),orderBy, pageable,sprovince,scity,stationId,lotteryType);
					
			List<RelaBsStationAndApp> sapps = applist.getResultList();
		 	
			List<RenewAppDTO> renewAppDTOs = new ArrayList<RenewAppDTO>();
			
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
				
				//上次购买此应用的剩余使用时间(endtime-当前时间)
				long currentTime = System.currentTimeMillis();
				long endtime = sapp.getEndTime().getTime();
				int betweenDays = DateUtil.daysBetween(currentTime, endtime);
				
				renewAppDTO.setSurplusDays(betweenDays+"");
				
				renewAppDTOs.add(renewAppDTO);
			}
			
			resultData.put("rows", renewAppDTOs);
			resultData.put("total", applist.getTotalCount());
		 	
		 	return resultData;
		}
	 
	 /**
	  * 
	 * @Title: getFufeiAppList
	 * @Description: 获取当前通行证可以购买的付费应用数据
	 * @param @param stationId
	 * @param @param province
	 * @param @param city
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return Map<String,Object>    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/getFufeiAppList", method = RequestMethod.GET)
		public @ResponseBody Map<String ,Object> getFufeiAppList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="stationId",required=false) String stationId,
				@RequestParam(value="province",required=false) String province,//站点省份（暂不使用）
				@RequestParam(value="city",required=false) String city,//站点市（暂不使用）
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	Map<String ,Object> resultData = new HashMap<String, Object>();
		 	
			//放置分页参数
			Pageable pageable = new PageRequest(page-1,rows);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			Station station = stationService.getSationById(stationId);
			String sprovince = station.getProvinceCode();
			String scity = station.getCityCode();
			String lotteryType = station.getStationType();//获取通行证的彩种
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			/*//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			params.add(AppController.APP_STATUS_SJ);//正在上架的应用,只有正在上架的应用可以购买
			buffer.append(" and appStatus = ?").append(params.size());
			
			//连接查询条件
			if(null != sprovince&&!"".equals(sprovince.trim()))
			{
				params.add(sprovince);
				buffer.append(" and province = ?").append(params.size());
			}
			
			if(null != scity&&!"".equals(scity.trim()))
			{
				List<String> paraArr = new ArrayList<String> ();
				paraArr.add(scity);
				paraArr.add(Constants.CITY_ALL);
				params.add(paraArr);
				buffer.append(" and city in ?").append(params.size());
			}
		 	
			
			
			//获取全部上架的应用列表
			  QueryResult<App> applist = appService.getAppList(App.class,
					buffer.toString(), params.toArray(),orderBy, pageable);*/
			
			//获取全部上架的付费的应用列表
			QueryResult<App> applist = appService.getAppOfFufei(App.class, buffer.toString(), params.toArray(), orderBy, pageable, sprovince, scity,lotteryType);
					
			List<App> apps = applist.getResultList();
			int totalrow = applist.getTotalCount();//applist.getTotalRecord();jsql的返回数据总值是getTotalRecord
			
			//将实体转换为dto
			List<AppDTO> appDTOs = appService.toRDTOS(apps);
			
			resultData.put("rows", appDTOs);
			resultData.put("total", totalrow);
		 	
		 	
		 	return resultData;
		}
	 
	 
	 /**
	  * 
	 * @Title: getUserYearDiscounts
	 * @Description: 获取使用年限折扣表数据
	 * @param @param id
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return List<UserYearDiscount>    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/getUserYearDiscounts", method = RequestMethod.POST)
		public @ResponseBody List<UserYearDiscount> getUserYearDiscounts(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	List<UserYearDiscount> discounts = new ArrayList<UserYearDiscount>();
		 	
		 	discounts = appUPriceService.findAll();
		 	
		 	return discounts;
		}
				
	 /**
	  * 
	 * @Title: calculateSellprice
	 * @Description: 计算当前通行证购买当前应用所选择的使用年限下的销售价格
	 * @param @param stationId
	 * @param @param province
	 * @param @param city
	 * @param @param appId
	 * @param @param useYear
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/calculateSellprice", method = RequestMethod.GET)
		public @ResponseBody ResultBean calculateSellprice(
				@RequestParam(value="stationId",required=false) String stationId,
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="city",required=false) String city,
				@RequestParam(value="appId",required=false) String appId,
				@RequestParam(value="useYearId",required=false) String useYearId,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	ResultBean resultBean = new ResultBean();
		 	
		 	String price = "";
		 	
		 	//获取当前通行证的区域
		 	Station station = stationService.getSationById(stationId);
		 	String sprovince = station.getProvinceCode();
		 	String scity = station.getCityCode();
		 	
		 	
		 	//获取应用的单价
		 	App app = appService.getAppById(appId);
		 	String morenPrice = app.getAppMoney();//当前通行证想要购买的应用的默认单价
		 	//查询通行证所在区域是否有特殊定价
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
		 	
		 	//获取折扣信息
		 	UserYearDiscount uydiscount = appUPriceService.getUserYearDiscountById(useYearId);
		 	
		 	String userYear = uydiscount.getUseYear();//购买的使用年限（1年或者2年）
		 	int discount = uydiscount.getDiscount();
		 	
		 	Double result = (Double.parseDouble(price)*Integer.parseInt(userYear)*discount)/100;
		 	
		 	resultBean.setMessage(result.toString());//返回计算结果
		 	
		 	return resultBean;
		}
	 
	 
	 
	 
	 
}
