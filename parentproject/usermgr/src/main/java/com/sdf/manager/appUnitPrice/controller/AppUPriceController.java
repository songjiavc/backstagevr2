package com.sdf.manager.appUnitPrice.controller;

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

import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appUnitPrice.dto.AppUnitPriceDTO;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.service.AppUPriceService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;

@Controller
@RequestMapping("appUprice")
public class AppUPriceController extends GlobalExceptionHandler {
	
	Logger logger = LoggerFactory.getLogger(AppUPriceController.class);
	
	@Autowired
	private AppUPriceService appUPriceService;
	
	@Autowired
	private AppService appService;//应用管理模块的service层
	
	/**
	 * 
	* @Title: getDetailAppUprice
	* @Description:获取应用区域单价详情数据
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return AppUnitPriceDTO    返回类型
	* @author banna
	* @throws
	 */
	@RequestMapping(value = "/getDetailAppUprice", method = RequestMethod.GET)
	public @ResponseBody AppUnitPriceDTO getDetailAppUprice(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		AppUnitPrice appUnitPrice = appUPriceService.getAppUnitPriceById(id);
		
		AppUnitPriceDTO appUnitPriceDTO = appUPriceService.toDTO(appUnitPrice);
		
		
		return appUnitPriceDTO;
	}
	
	/**
	 * 
	* @Title: getAppUnitPriceList
	* @Description: 根据筛选条件获取应用区域单价数据
	* @param @param page
	* @param @param rows
	* @param @param appId
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
	 @RequestMapping(value = "/getAppUnitPriceList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getAppUnitPriceList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="appId",required=false) String appId,//应用id
				@RequestParam(value="province",required=false) String province,//区域省
				@RequestParam(value="city",required=false) String city,//区域市
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
			
			params.add("1");//只获取自定义单价数据
			buffer.append(" and  priceType = ?").append(params.size());
			
			//连接查询条件
			if(null != appId & !"".equals(appId))
			{
				params.add(appId);
				buffer.append(" and app.id = ?").append(params.size());
			}
			
			if(null != city & !"".equals(city)&&!Constants.CITY_ALL.equals(city))
			{
				params.add(city);
				buffer.append(" and city = ?").append(params.size());
			}
			
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<AppUnitPrice> appupricelist = appUPriceService.getAppUnitPriceList(AppUnitPrice.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<AppUnitPrice> appuPrices = appupricelist.getResultList();
			Long totalrow = appupricelist.getTotalRecord();
			
			//将实体转换为dto
			List<AppUnitPriceDTO> appUpriceDTOs = appUPriceService.toRDTOS(appuPrices);
			
			returnData.put("rows", appUpriceDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	 
	 /**
	  * 
	 * @Title: saveOrUpdate
	 * @Description: 保存或修改应用区域单价数据
	 * @param @param id
	 * @param @param appId
	 * @param @param unitPrice
	 * @param @param province
	 * @param @param city
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="appId",required=false) String appId,
				@RequestParam(value="unitPrice",required=false) String unitPrice,
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="city",required=false) String city,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   AppUnitPrice appUnitPrice = appUPriceService.getAppUnitPriceByAppIdAndProvinceAndCityNotType(appId, province, city);//根据应用id，省份id和城市id获取没有单价类别区分的区域单价数据
		   
		   if(null != appUnitPrice)
		   {//应用区域单价不为空，进行修改操作
			   
//			   App app = appService.getAppById(appId);
			   
			   appUnitPrice.setUnitPrice(unitPrice);
//			   appUnitPrice.setApp(app);
			   appUnitPrice.setPriceType("1");//若之前存在的数据为省份单价，则在应用区域单价模块中变更为自定义单价
			   appUnitPrice.setProvince(province);
			   appUnitPrice.setCity(city);
			   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   appUPriceService.update(appUnitPrice);
			   
			   resultBean.setMessage("修改应用区域单价信息成功!");
			   resultBean.setStatus("success");
			  
			   //日志输出
				 logger.info("修改应用区域单价数据--应用区域单价id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   appUnitPrice = new AppUnitPrice();
			   App app = appService.getAppById(appId);
			   
			   appUnitPrice.setUnitPrice(unitPrice);
			   appUnitPrice.setApp(app);
			   appUnitPrice.setProvince(province);
			   appUnitPrice.setCity(city);
			   appUnitPrice.setPriceType("1");//单价类别，0：app默认单价，在创建app时写入的初始单价，之后也会跟随app的单价变化而变化 1：自定义单价，通过应用区域单价模块进行设置的单价，不随应用单价的变化而变化 
			   appUnitPrice.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   appUnitPrice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   appUnitPrice.setIsDeleted("1");//有效数据标记位
			   
			   appUPriceService.save(appUnitPrice);
			   
			   resultBean.setMessage("添加应用区域单价信息成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加应用区域单价数据--应用区域单价省份="+province+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 
	 /**
	  * 
	 * @Title: deleteAppUprices
	 * @Description: 删除区域单价数据
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/deleteAppUprices", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteAppUprices(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 AppUnitPrice appUnitPrice;
		 for (String id : ids) 
			{
			 	appUnitPrice = appUPriceService.getAppUnitPriceById(id);
			 	if(null != appUnitPrice)
			 	{
			 		appUnitPrice.setIsDeleted("0");
			 		appUnitPrice.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		appUnitPrice.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		appUPriceService.update(appUnitPrice);
			 		
			 		 //日志输出
					 logger.info("删除应用区域单价信息--应用区域单价数据id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 
	 /**
	  * 
	 * @Title: checkAppUprices
	 * @Description:校验是否已经为此应用在当前区域设置过单价
	 * @param @param AppId
	 * @param @param province
	 * @param @param city
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/checkAppUprices", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkAppUprices(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="appId",required=false) String appId,
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="city",required=false) String city,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 //放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			params.add("1");//只获取自定义单价数据,自定义单价的数据才算是设置过单价的数据
			buffer.append(" and  priceType = ?").append(params.size());
			
			if(null != id && !"".equals(id.trim()))
			{
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//连接查询条件
			if(null != appId & !"".equals(appId))
			{
				params.add(appId);
				buffer.append(" and app.id = ?").append(params.size());
			}
			
			if(null != city & !"".equals(city))
			{
				params.add(city);
				buffer.append(" and city = ?").append(params.size());
			}
			
			if(null != province & !"".equals(province))
			{
				params.add(province);
				buffer.append(" and province = ?").append(params.size());
			}
			
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<AppUnitPrice> appupricelist = appUPriceService.getAppUnitPriceList(AppUnitPrice.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<AppUnitPrice> appuPrices = appupricelist.getResultList();
		 
		 if(appuPrices.size()>0)
		 {
			 resultBean.setExist(true);
		 }
		 else
		 {
			 resultBean.setExist(false);
		 }
		 
		 return resultBean;
		 
	 }
	 
}
