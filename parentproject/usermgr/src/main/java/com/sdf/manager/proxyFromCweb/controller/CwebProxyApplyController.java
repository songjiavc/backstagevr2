package com.sdf.manager.proxyFromCweb.controller;

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

import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.proxyFromCweb.dto.ApplyProxyDTO;
import com.sdf.manager.proxyFromCweb.entity.ApplyProxy;
import com.sdf.manager.proxyFromCweb.service.ApplyProxyService;


@Controller
@RequestMapping("cWebProxy")
public class CwebProxyApplyController 
{
	
	Logger logger = LoggerFactory.getLogger(CwebProxyApplyController.class);
	
	@Autowired
	private ApplyProxyService applyProxyService;
	
	
	public static final String NOT_REVRIW_STATUS = "0";//代理申请数据状态，0：未回访
	public static final String REVRIW_NOT_CONFORM_STATUS = "1";//代理申请数据状态，1：回访不符合  
	public static final String REVRIW_CONFORM_STATUS = "2";//代理申请数据状态， 2：回访符合 
	
	/**
	 * 
	 * @Title: getDetailApplyProxy
	 * @Description: 获取代理申请信息详情
	 * @author:banna
	 * @return: ApplyProxyDTO
	 */
	@RequestMapping(value = "/getDetailApplyProxy", method = RequestMethod.GET)
	public @ResponseBody ApplyProxyDTO getDetailApplyProxy(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		ApplyProxy applyProxy = applyProxyService.getApplyProxyById(id);
		
		ApplyProxyDTO applyProxyDTO  = applyProxyService.toDTO(applyProxy);
		
		
		return applyProxyDTO;
	}
	
	/**
	 * 删除代理申请信息
	 * @Title: deleteApplyProxys
	 * @Description: TODO
	 * @author:banna
	 * @return: ResultBean
	 */
	 @RequestMapping(value = "/deleteApplyProxys", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteApplyProxys(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 ApplyProxy applyProxy;
		 for (String id : ids) 
			{
			 	applyProxy = applyProxyService.getApplyProxyById(id);
			 	if(null != applyProxy)
			 	{
			 		applyProxy.setIsDeleted("0");
			 		applyProxy.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		applyProxy.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		applyProxyService.update(applyProxy);
			 		
			 		 //日志输出
					 logger.info("删除应用--代理申请信息id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * @Title: updateApplyProxys
	  * @Description: 更新代理申请数据状态
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/updateApplyProxys", method = RequestMethod.POST)
		public @ResponseBody ResultBean  updateApplyProxys(
				@RequestParam(value="ids",required=false) String[] ids,
				@RequestParam(value="operatype",required=false) String operatype,//2：回访符合1”回访不符合 
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 ApplyProxy applyProxy;
		 
		 if(CwebProxyApplyController.REVRIW_CONFORM_STATUS.equals(operatype))//回访符合操作
		 {
			 for (String id : ids) 
				{
				 	applyProxy = applyProxyService.getApplyProxyById(id);
				 	if(null != applyProxy)
				 	{
				 		applyProxy.setStatus(CwebProxyApplyController.REVRIW_CONFORM_STATUS);
				 		applyProxy.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				 		applyProxy.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 		applyProxyService.update(applyProxy);
				 		
				 		 //日志输出
						 logger.info("更新状态--代理申请信息id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   
				 	}
				}
		 }
		 else if(CwebProxyApplyController.REVRIW_NOT_CONFORM_STATUS.equals(operatype))//回访不符合操作
		 {
			 for (String id : ids) 
				{
				 	applyProxy = applyProxyService.getApplyProxyById(id);
				 	if(null != applyProxy)
				 	{
				 		applyProxy.setStatus(CwebProxyApplyController.REVRIW_NOT_CONFORM_STATUS);
				 		applyProxy.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				 		applyProxy.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 		applyProxyService.update(applyProxy);
				 		
				 		 //日志输出
						 logger.info("更新状态--代理申请信息id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   
				 	}
				}
		 }
		 
		 
		 String returnMsg = "更新成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	
	/**
	 * 
	 * @Title: getApplyProxyList
	 * @Description: 获取代理申请数据列表
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	 @RequestMapping(value = "/getApplyProxyList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getApplyProxyList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="province",required=false) String province,//省份
				@RequestParam(value="city",required=false) String city,//市
				@RequestParam(value="status",required=false) String status,//代理申请数据状态
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
			
			
			/**
			 * 根据省市筛选应用数据，当前是应用广告模块在使用这个功能，start
			 */
			if(null != province&&!"".equals(province.trim())&&!Constants.PROVINCE_ALL.equals(province))
			{
				params.add(province);
				buffer.append(" and province = ?").append(params.size());
			}
			
			if(null != city&&!"".equals(city.trim()))
			{
				/*List<String> paraArr = new ArrayList<String> ();
				paraArr.add(city);
				paraArr.add(Constants.CITY_ALL);*/
				params.add(city);
				buffer.append(" and city = ?").append(params.size());
			}
			
			if(null != status&&!"".equals(status.trim()))
			{
				params.add(status);
				buffer.append(" and status = ?").append(params.size());
			}
			
			/**
			 * 根据省市筛选应用数据，当前是应用广告模块在使用这个功能，end
			 */
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<ApplyProxy> queryResult = applyProxyService.getApplyProxyList(ApplyProxy.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<ApplyProxy> applylist = queryResult.getResultList();
			Long totalrow = queryResult.getTotalRecord();
			
			//将实体转换为dto
			List<ApplyProxyDTO> applyProxyDTOs = applyProxyService.toRDTOS(applylist);
			
			returnData.put("rows", applyProxyDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
}
