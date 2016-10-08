package com.sdf.manager.figureMysteryPuzzlesApp.controller;

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
import com.sdf.manager.figureMysteryPuzzlesApp.dto.ExpertsOfFMPAPPDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;
import com.sdf.manager.figureMysteryPuzzlesApp.service.ExpertOfFMAPPService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleStatusService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleUploadfileService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FoundFigureAndPuzzleStatusService;

/**
 * 图谜字谜应用后台控制层
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/fmpApp")
public class FigureMPuzzlesAppController 
{
	//声明log
	Logger logger = LoggerFactory.getLogger(FigureMPuzzlesAppController.class);
	
	@Autowired
	private FigureAndPuzzleStatusService figureAndPuzzleStatusService;
	
	@Autowired
	private FigureAndPuzzleUploadfileService figureAndPuzzleUploadfileService;
	
	@Autowired
	private FoundFigureAndPuzzleStatusService foundFigureAndPuzzleStatusService;
	
	
	@Autowired
	private ExpertOfFMAPPService expertOfFMAPPService;
	
	
	/**
	 * 根据id获取图谜字谜专家数据
	 * @param id
	 * @param model
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getDetailExpertOfFMAPP", method = RequestMethod.GET)
	public @ResponseBody ExpertsOfFMPAPPDTO getDetailExpertOfFMAPP(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		ExpertsOfFMPAPP expertsOfFMPAPP = expertOfFMAPPService.getExpertsOfFMPAPPById(id);
		
		ExpertsOfFMPAPPDTO expertsOfFMPAPPDTO = expertOfFMAPPService.toDTO(expertsOfFMPAPP);
		
		logger.info("获取图谜字谜专家详情，id="+id);
		
		return expertsOfFMPAPPDTO;
	}
	
	/**
	 * 根据模糊查询条件查询图谜字谜专家列表数据
	 * @param page
	 * @param rows
	 * @param name
	 * @param provinceCode
	 * @param cityCode
	 * @param lotteryType
	 * @param figureOrPuzzles
	 * @param model
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/getExpertList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getExpertList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="provinceCode",required=false) String provinceCode,
			@RequestParam(value="cityCode",required=false) String cityCode,
			@RequestParam(value="lotteryType",required=false) String lotteryType,
			@RequestParam(value="figureOrPuzzles",required=false) String figureOrPuzzles,//1：图谜，2：字谜  0：全部 
			ModelMap model,HttpSession httpSession) throws Exception
	{
		Map<String,Object> result = new HashMap<String, Object>();
		
		//放置分页参数
		Pageable pageable = new PageRequest(page-1,rows);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		//连接查询条件
		if(null != name&&!"".equals(name.trim()))
		{
			params.add("%"+name+"%");
			buffer.append(" and name like ?").append(params.size());
		}
		
		
		if(null != provinceCode&&!"".equals(provinceCode.trim()))
		{
			params.add(provinceCode);
			buffer.append(" and provinceCode = ?").append(params.size());
		}
		
		
		if(null != lotteryType&&!"".equals(lotteryType.trim()))
		{
			params.add(lotteryType);
			buffer.append(" and lotteryType = ?").append(params.size());
		}
		
		
		if(null != cityCode&&!"".equals(cityCode.trim()))
		{
			params.add(cityCode);
			buffer.append(" and cityCode = ?").append(params.size());
		}
		
		if(null != figureOrPuzzles&&!"".equals(figureOrPuzzles.trim()))
		{
			params.add(figureOrPuzzles);
			buffer.append(" and figureOrPuzzles = ?").append(params.size());
		}
		
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("createrTime", "desc");
		
		QueryResult<ExpertsOfFMPAPP> exQueryResult = expertOfFMAPPService.getExpertsOfFMPAPPList(ExpertsOfFMPAPP.class,
				buffer.toString(), params.toArray(),orderBy, pageable);
				
		List<ExpertsOfFMPAPP> expertsOfFMPAPPs = exQueryResult.getResultList();
		Long totalrow = exQueryResult.getTotalRecord();
		
		//将实体转换为dto
		List<ExpertsOfFMPAPPDTO> expertsOfFMPAPPDTOs = expertOfFMAPPService.toRDTOS(expertsOfFMPAPPs);
		
		result.put("rows", expertsOfFMPAPPDTOs);
		result.put("total", totalrow);
	 	
		
		
		
		return result;
	}
	
	/**
	 * 保存或修改图谜字谜专家数据
	 * @param id
	 * @param code
	 * @param name
	 * @param password
	 * @param telephone
	 * @param provinceCode
	 * @param cityCode
	 * @param lotteryType
	 * @param address
	 * @param figureOrPuzzles
	 * @param model
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.GET)
	public @ResponseBody ResultBean saveOrUpdate(
			@RequestParam(value="id",required=false) String id,
			@RequestParam(value="code",required=false) String code,
			@RequestParam(value="name",required=false) String name,
			@RequestParam(value="password",required=false) String password,
			@RequestParam(value="telephone",required=false) String telephone,
			@RequestParam(value="provinceCode",required=false) String provinceCode,
			@RequestParam(value="cityCode",required=false) String cityCode,
			@RequestParam(value="lotteryType",required=false) String lotteryType,
			@RequestParam(value="address",required=false) String address,
			@RequestParam(value="figureOrPuzzles",required=false) String figureOrPuzzles,
			ModelMap model,HttpSession httpSession) throws Exception
	{
	   ResultBean resultBean = new ResultBean ();
	   
	   
	   ExpertsOfFMPAPP expertsOfFMPAPP = expertOfFMAPPService.getExpertsOfFMPAPPById(id);
	   
	   if(null != expertsOfFMPAPP)
	   {//修改图谜字谜专家数据
		   expertsOfFMPAPP.setName(name);
		   expertsOfFMPAPP.setPassword(password);
		   expertsOfFMPAPP.setTelephone(telephone);
		   expertsOfFMPAPP.setProvinceCode(provinceCode);
		   expertsOfFMPAPP.setCityCode(cityCode);
		   expertsOfFMPAPP.setLotteryType(lotteryType);
		   expertsOfFMPAPP.setAddress(address);
		   expertsOfFMPAPP.setFigureOrPuzzles(figureOrPuzzles);
		   expertsOfFMPAPP.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
		   expertsOfFMPAPP.setModifyTime(new Timestamp(System.currentTimeMillis()));
		   
		   logger.info("修改图谜字谜专家数据，id="+id);
		   
		   expertOfFMAPPService.update(expertsOfFMPAPP);
		   
		   resultBean.setMessage("修改专家信息成功!");
		   resultBean.setStatus("success");
	   }
	   else
	   {//添加图谜字谜专家数据
		   expertsOfFMPAPP = new ExpertsOfFMPAPP();
		   expertsOfFMPAPP.setCode(code);//专家登录名在添加之后就不可以进行修改了，且全局有效数据唯一
		   expertsOfFMPAPP.setName(name);
		   expertsOfFMPAPP.setPassword(password);
		   expertsOfFMPAPP.setTelephone(telephone);
		   expertsOfFMPAPP.setProvinceCode(provinceCode);
		   expertsOfFMPAPP.setCityCode(cityCode);
		   expertsOfFMPAPP.setLotteryType(lotteryType);
		   expertsOfFMPAPP.setAddress(address);
		   expertsOfFMPAPP.setFigureOrPuzzles(figureOrPuzzles);
		   expertsOfFMPAPP.setIsDeleted(Constants.IS_NOT_DELETED);
		   expertsOfFMPAPP.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
		   expertsOfFMPAPP.setCreaterTime(new Timestamp(System.currentTimeMillis()));
		   expertsOfFMPAPP.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
		   expertsOfFMPAPP.setModifyTime(new Timestamp(System.currentTimeMillis()));
		   
		   logger.info("添加图谜字谜专家数据，code="+code);
		   
		   expertOfFMAPPService.save(expertsOfFMPAPP);
		   
		   resultBean.setMessage("添加专家信息成功!");
		   resultBean.setStatus("success");
		   
		   
	   }
	   
	 
	   return resultBean;
	}
	
	/**
	 * 删除图谜字谜专家信息
	 * @param ids
	 * @param model
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	 @RequestMapping(value = "/deleteExpertsOfFMPAPPs", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteExpertsOfFMPAPPs(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		ExpertsOfFMPAPP expertsOfFMPAPP;
		 for (String id : ids) 
			{
			 	expertsOfFMPAPP = expertOfFMAPPService.getExpertsOfFMPAPPById(id);
			 	if(null != expertsOfFMPAPP)
			 	{
			 		expertsOfFMPAPP.setIsDeleted(Constants.IS_DELETED);
			 		expertsOfFMPAPP.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		expertsOfFMPAPP.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		expertOfFMAPPService.update(expertsOfFMPAPP);
			 		
			 		 //日志输出
					 logger.info("删除图谜字谜专家数据--id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	
	/**
	 * 校验图谜字谜专家code全局有效数据唯一
	 * @param id
	 * @param code
	 * @param model
	 * @param httpSession
	 * @return
	 * @throws Exception
	 */
	 @RequestMapping(value = "/checkExpertCode", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkExpertCode(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="code",required=false) String code,
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
			
			if(null != code && !"".equals(code))
			{
				params.add(code);
				buffer.append(" and code = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<ExpertsOfFMPAPP> exQueryResult = expertOfFMAPPService.getExpertsOfFMPAPPList(ExpertsOfFMPAPP.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(exQueryResult.getResultList().size()>0)
			{
				resultBean.setExist(true);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
			}
			else
			{
				resultBean.setExist(false);
			}
			
			return resultBean;
			
		}
	
	
	
	
}
