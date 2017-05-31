package com.sdf.manager.notice.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
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

import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.dto.ForecastDTO;
import com.sdf.manager.notice.entity.ForecastMessage;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.service.ForecastService;

@Controller
@RequestMapping("forecast")
public class ForecastController extends GlobalExceptionHandler
{
	
	Logger logger = LoggerFactory.getLogger(ForecastController.class);

	@Autowired
	private ForecastService forecastService;
	
	/**
	 * 
	* @Title: getDetailForecast
	* @Description: 根据id获取预测信息详情
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return ForecastDTO    返回类型
	* @author banna
	* @throws
	 */
	@RequestMapping(value = "/getDetailForecast", method = RequestMethod.GET)
	public @ResponseBody ForecastDTO getDetailForecast(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		ForecastMessage forecastMessage = forecastService.getForecastMessageById(id);
		
		
		ForecastDTO forecastDTO = forecastService.toDTO(forecastMessage);
		
		
		return forecastDTO;
	}
	
	/**
	 * 
	* @Title: getForecastList
	* @Description: 根据筛选条件获取预测信息数据
	* @param @param page
	* @param @param rows
	* @param @param lottertyType
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
	 @RequestMapping(value = "/getForecastList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getForecastList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="lottertyType",required=false) String lottertyType,//彩种
				@RequestParam(value="province",required=false) String province,//省份
				@RequestParam(value="city",required=false) String city,//市
				@RequestParam(value="noticeData",required=false) String noticeData,//noticeData:1,获取的是应用公告中的预测信息数据
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//应用广告的有效结束时间
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
			if(null != lottertyType && !"".equals(lottertyType.trim()))
			{
				params.add(lottertyType);
				buffer.append(" and lotteryType = ?").append(params.size());
			}
			
			if(null != province && !"".equals(province.trim())&&!Constants.PROVINCE_ALL.equals(province))
			{
				params.add(province);
				buffer.append(" and appAreaProvince = ?").append(params.size());
			}
			
			if(null != city && !"".equals(city.trim()) && !Constants.CITY_ALL.equals(city))
			{
				params.add(city);
				buffer.append(" and appAreaCity = ?").append(params.size());
			}
			
			if(null!=noticeData&&"1".equals(noticeData))//获取的若是应用公告的预测数据，则要显示
			{
				Date st =  null;
				if(null != startTime)
					st =  DateUtil.formatStringToDate(startTime.toString(), DateUtil.SIMPLE_DATE_FORMAT);//将前台传过来的字符串日期转换为date时，使用的格式应该与接收的字符串一样，即：若2014-02-01则应该用DateUtil.SIMPLE_DATE_FORMAT，在date和时间戳之间可以跨越格式转换
				
				Date et =  null;
				if(null != endTime)
					et =  DateUtil.formatStringToDate(endTime.toString(), DateUtil.SIMPLE_DATE_FORMAT);
				
				
				if(null != startTime&& !"".equals(startTime.trim()) )//
				{
					params.add(DateUtil.formatDateToTimestamp(st,  DateUtil.FULL_DATE_FORMAT));
					buffer.append(" and startTime >= ?").append(params.size());
					//且开始时间也要小于结束时间
					params.add(DateUtil.formatDateToTimestamp(et,  DateUtil.FULL_DATE_FORMAT));
					buffer.append(" and startTime <= ?").append(params.size());
				}
				
				if(null != endTime && !"".equals(endTime.trim()))//
				{
					params.add(DateUtil.formatDateToTimestamp(et,  DateUtil.FULL_DATE_FORMAT));
					buffer.append(" and endTime >= ?").append(params.size());
				}
			}
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<ForecastMessage> forecastlist = forecastService.getForecastList(ForecastMessage.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<ForecastMessage> forecasts = forecastlist.getResultList();
			Long totalrow = forecastlist.getTotalRecord();
			
			//将实体转换为dto
			List<ForecastDTO> forecastDTOs = forecastService.toRDTOS(forecasts);
			
			returnData.put("rows", forecastDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	 
	 /**
	  * 
	  * @Title: saveOrUpdate
	  * @Description: 保存或修改预测信息数据
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="forecastName",required=false) String forecastName,
				@RequestParam(value="forecastContent",required=false) String forecastContent,
				@RequestParam(value="lotteryType",required=false) String lotteryType,
				@RequestParam(value="appAreaProvince",required=false) String appAreaProvince,
				@RequestParam(value="appAreaCity",required=false) String appAreaCity,
				@RequestParam(value="startTime",required=false) String startTime,
				@RequestParam(value="endTime",required=false) String endTime,//应用的默认单价
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   ForecastMessage forecastMessage = forecastService.getForecastMessageById(id);
		   
		   if(null != forecastMessage)
		   {//预测信息数据不为空，则进行修改操作
			   
//			   forecastMessage.setForecastName(forecastName);
			   forecastMessage.setForecastContent(forecastContent);
			   forecastMessage.setLotteryType(lotteryType);
			   forecastMessage.setAppAreaProvince(appAreaProvince);
			   forecastMessage.setAppAreaCity(appAreaCity);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   forecastMessage.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   forecastMessage.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   forecastMessage.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   forecastMessage.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   forecastService.update(forecastMessage);
			  
			   resultBean.setMessage("修改预测信息成功!");
			   resultBean.setStatus("success");
			   
			   //日志输出
				 logger.info("修改预测信息--预测信息id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   forecastMessage = new ForecastMessage();
			   forecastMessage.setForecastName(forecastName);
			   forecastMessage.setForecastContent(forecastContent);
			   forecastMessage.setLotteryType(lotteryType);
			   forecastMessage.setAppAreaProvince(appAreaProvince);
			   forecastMessage.setAppAreaCity(appAreaCity);
			   Date st = DateUtil.formatStringToDate(startTime, DateUtil.SIMPLE_DATE_FORMAT);
			   forecastMessage.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
			   Date et = DateUtil.formatStringToDate(endTime, DateUtil.SIMPLE_DATE_FORMAT);
			   forecastMessage.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
			   forecastMessage.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   forecastMessage.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   forecastMessage.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   forecastMessage.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   forecastMessage.setIsDeleted("1");//有效数据标记位
			   
			   forecastService.save(forecastMessage);
			   
			   resultBean.setMessage("添加预测信息成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加预测信息--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 
	 * @Title: deleteForecasts
	 * @Description: 晒讯预测信息数据
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @author banna
	 * @throws
	  */
	 @RequestMapping(value = "/deleteForecasts", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteForecasts(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 ForecastMessage forecastMessage;
		 for (String id : ids) 
			{
			 	forecastMessage = forecastService.getForecastMessageById(id);
			 	if(null != forecastMessage)
			 	{
			 		forecastMessage.setIsDeleted("0");
			 		forecastMessage.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		forecastMessage.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		forecastService.update(forecastMessage);
			 		
			 		 //日志输出
					 logger.info("删除预测信息--预测信息id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: checkForcastContact
	  * @Description: 校验预测公告是否和应用广告数据有关联
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/checkForcastContact", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkForcastContact(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 boolean concactNotice = false;
		 
		 ForecastMessage forecastMessage = forecastService.getForecastMessageById(id);
		 
		 List<Notice> notices = forecastMessage.getNotices();
		 
		 
		 if(notices.size()>0)
		 {
			 concactNotice = true;
		 }
		 
		 resultBean.setExist(concactNotice);
		 
		 return resultBean;
	 }
	
}
