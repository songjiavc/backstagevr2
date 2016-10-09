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
import com.sdf.manager.figureMysteryPuzzlesApp.dto.PuzzleTypeDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.PuzzlesType;
import com.sdf.manager.figureMysteryPuzzlesApp.service.ExpertOfFMAPPService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleStatusService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleUploadfileService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FoundFigureAndPuzzleStatusService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.PuzzlesTypeService;
import com.sdf.manager.user.bean.AccountBean;
import com.sdf.manager.user.entity.User;

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
	private PuzzlesTypeService puzzleTypeService;
	
	
	@Autowired
	private ExpertOfFMAPPService expertOfFMAPPService;
	
	
	public static final String TUMI_FLAG = "1";//图谜标记
	public static final String ZIMI_FLAG = "2";//字谜标记
	
	
	/**
	 *********** 1.图谜字谜专家管理模块 ***********
	 **/
	
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
	
	 
	 /**
	  ********* 2.字谜类型管理模块*********
	  * 
	  **/
	 
	 /**
	  * 根据id获取字谜类型详情数据
	  * @param id
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getDetailPuzzlesType", method = RequestMethod.GET)
		public @ResponseBody PuzzleTypeDTO getDetailPuzzlesType(@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			
			PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(id);
			
			PuzzleTypeDTO dto = puzzleTypeService.toDTO(puzzlesType);
			
			logger.info("获取字谜类型详情数据，id="+id);
			
			return dto;
		}
	
	 /**
	  * 获取字谜类型数据列表
	  * @param page
	  * @param rows
	  * @param typeName
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getPuzzlesTypeList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getPuzzlesTypeList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="typeName",required=false) String typeName,
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
			if(null != typeName&&!"".equals(typeName.trim()))
			{
				params.add("%"+typeName+"%");
				buffer.append(" and typeName like ?").append(params.size());
			}
			
			
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<PuzzlesType> exQueryResult = puzzleTypeService.getPuzzlesTypeList(PuzzlesType.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<PuzzlesType> puzzlesTypes = exQueryResult.getResultList();
			Long totalrow = exQueryResult.getTotalRecord();
			
			//将实体转换为dto
			List<PuzzleTypeDTO> puzzleTypeDTOs = puzzleTypeService.toRDTOS(puzzlesTypes);
			
			result.put("rows", puzzleTypeDTOs);
			result.put("total", totalrow);
		 	
			
			
			return result;
		}
		
	 /**
	  * 保存或修改字谜类型数据
	  * @param id
	  * @param typeName
	  * @param typeCol
	  * @param typeColWordsNum
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/saveOrUpdatePuzzleType", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdatePuzzleType(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="typeName",required=false) String typeName,
				@RequestParam(value="typeCol",required=false) String typeCol,
				@RequestParam(value="typeColWordsNum",required=false) String typeColWordsNum,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   
		   PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(id);
		   
		   if(null != puzzlesType)
		   {//修改字谜类型数据
			   puzzlesType.setTypeName(typeName);
			   puzzlesType.setTypeCol(typeCol);
			   puzzlesType.setTypeColWordsNum(typeColWordsNum);
			   
			   int typeWordsNum = 0;
			   if(null != typeCol && null != typeColWordsNum)
			   {
				   typeWordsNum = Integer.parseInt(typeCol) * Integer.parseInt(typeColWordsNum);//计算字谜最多字数
			   }
			   puzzlesType.setTypeWordsNum(typeWordsNum+"");
			   puzzlesType.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   puzzlesType.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   logger.info("修改字谜类型数据，id="+id);
			   
			   puzzleTypeService.update(puzzlesType);
			   
			   resultBean.setMessage("修改字谜类型成功!");
			   resultBean.setStatus("success");
		   }
		   else
		   {//添加图谜字谜专家数据
			   puzzlesType = new PuzzlesType();
			   puzzlesType.setTypeName(typeName);
			   puzzlesType.setTypeCol(typeCol);
			   puzzlesType.setTypeColWordsNum(typeColWordsNum);
			   
			   int typeWordsNum = 0;
			   if(null != typeCol && null != typeColWordsNum)
			   {
				   typeWordsNum = Integer.parseInt(typeCol) * Integer.parseInt(typeColWordsNum);//计算字谜最多字数
			   }
			   puzzlesType.setTypeWordsNum(typeWordsNum+"");
			   puzzlesType.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   puzzlesType.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   puzzlesType.setIsDeleted(Constants.IS_NOT_DELETED);
			   puzzlesType.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   puzzlesType.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   
			   logger.info("添加字谜类型数据");
			   
			   puzzleTypeService.save(puzzlesType);
			   
			   resultBean.setMessage("添加字谜类型成功!");
			   resultBean.setStatus("success");
			   
			   
		   }
		   
		 
		   return resultBean;
		}
	 
	 
	 /**
	  * 删除字谜类型数据
	  * @param ids
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/deletePuzzleType", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deletePuzzleType(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		PuzzlesType puzzlesType;
		 for (String id : ids) 
			{
			 	puzzlesType = puzzleTypeService.getPuzzlesTypeById(id);
			 	if(null != puzzlesType)
			 	{
			 		puzzlesType.setIsDeleted(Constants.IS_DELETED);
			 		puzzlesType.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		puzzlesType.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		puzzleTypeService.update(puzzlesType);
			 		
			 		 //日志输出
					 logger.info("删除字谜类型数据--id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	
	 /**
	  * 判断字谜类型是否可以删除
	  * @param id
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/checkCouldDeleted", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkCouldDeleted(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
			
			ResultBean resultBean = new ResultBean ();
			
			PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(id);
			
			
			//若字谜类型的关联数据都还有关联的数据
			if(puzzlesType.getFloorOfFigureAndPuzzles().size()>0 || puzzlesType.getFigureAndPuzzles().size()>0)
			{
				resultBean.setExist(false);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
			}
			else
			{
				resultBean.setExist(true);
			}
			
			return resultBean;
			
		}
	 
	/**
	 * 
	* @Title: checkTypeName 
	* @Description: 校验字谜类型名称全局唯一
	* @param @param id
	* @param @param typeName
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @author banna
	* @date 2016年10月9日 上午10:38:50 
	* @return ResultBean    返回类型 
	* @throws
	 */
	 @RequestMapping(value = "/checkTypeName", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkTypeName(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="typeName",required=false) String typeName,
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
			
			if(null != typeName && !"".equals(typeName))
			{
				params.add(typeName);
				buffer.append(" and typeName = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<PuzzlesType> exQueryResult = puzzleTypeService.getPuzzlesTypeList
					(PuzzlesType.class, buffer.toString(), params.toArray(),
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
	 
	 
	 /**
	  ********* 3.专家发布图谜字谜登录模块*********
	  * 
	  **/
	 
	 /**
	  * 
	 * @Title: expertLogin 
	 * @Description: 图谜字谜专家登录方法
	 * @param @param httpSession
	 * @param @param code
	 * @param @param password
	 * @param @param model
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @author banna
	 * @date 2016年10月9日 上午11:05:56 
	 * @return String    返回类型 
	 * @throws
	  */
	 @RequestMapping(value = "/expertLogin", method = RequestMethod.POST)
		public String expertLogin(HttpSession httpSession,
				@RequestParam(value="code",required=false) String code,//登录名是user表中的code
				@RequestParam(value="password",required=false) String password,
				ModelMap model) throws Exception {
			

			String message ="success";
			
			ExpertsOfFMPAPP expertsOfFMPAPP = expertOfFMAPPService.getExpertsOfFMPAPPByCode(code);
			if(null == expertsOfFMPAPP)
			{
				message = "0";//当前登录名不存在，请确认后登录!
			}
			else if("".equals(password))
			{
				message = "1";//登录密码不可以为空!
			}
			else if(null != expertsOfFMPAPP && !expertsOfFMPAPP.getPassword().equals(password))
			{
				message = "2";//登录密码不正确，请确认后登录!
			}
			else
			{
				//向session中写入登录的专家信息
				this.setLoginExpertMessage(httpSession, code, password, expertsOfFMPAPP.getName(),expertsOfFMPAPP.getId());
			}
			
			 //日志输出
			   logger.info("图谜字谜专家登录：登录信息专家登录名："+code+"密码："+password+"登录状态："+message);
			   
			
			
			model.addAttribute("message", message);
			return "figureAndPuzzleApp/figureAndPuzzleOfExpert";//登录成功后返回专家的图谜字谜管理页面
		}
	 
	 /**
	  * 
	 * @Title: updateExpertPassword 
	 * @Description: 修改图谜字谜专家登录密码
	 * @param @param newPassword
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @author banna
	 * @date 2016年10月9日 上午11:45:47 
	 * @return ResultBean    返回类型 
	 * @throws
	  */
	 @RequestMapping(value = "/updateExpertPassword", method = RequestMethod.POST)
		public @ResponseBody ResultBean  updateExpertPassword(
				@RequestParam(value="password",required=true) String newPassword,
				ModelMap model,HttpSession httpSession) throws Exception{
			ResultBean resultBean = new ResultBean();
			try {
				String userCode = this.getAuthenticatedExpertCode(httpSession);//获取当前登录的专家code
				ExpertsOfFMPAPP expertsOfFMPAPP = expertOfFMAPPService.getExpertsOfFMPAPPByCode(userCode);//根据当前专家的登录名获取专家信息
				expertsOfFMPAPP.setPassword(newPassword);//修改专家登录密码为新的登录密码
				expertOfFMAPPService.update(expertsOfFMPAPP);
				resultBean.setStatus("success");
				resultBean.setMessage("密码修改成功!");
			}catch (Exception e) {
				resultBean.setStatus("failure");
				resultBean.setMessage(e.getMessage());
			}finally{
				return resultBean;
			}
		}
		
	
	 
	 /**
		 * 图谜字谜专家登出
		 * @return
		 */
		@RequestMapping(value = "/logout.action", method = RequestMethod.GET)
		public String logout(@RequestParam(value="alertmsg",required=false) String alertmsg,
				ModelMap model)
		{
			String indexPage = "figureAndPuzzleApp/index";
			
			model.addAttribute("alertmsg", alertmsg);
			return indexPage;
		}
		
		/**
		 * 获取登录的专家信息
		* @Title: getLoginExpertmsg 
		* @Description: TODO(这里用一句话描述这个方法的作用) 
		* @param @param model
		* @param @param httpSession
		* @param @return
		* @param @throws Exception    设定文件 
		* @author banna
		* @date 2016年10月9日 上午11:34:36 
		* @return ResultBean    返回类型 
		* @throws
		 */
		@RequestMapping(value = "/getLoginExpertmsg", method = RequestMethod.POST)
		public @ResponseBody ResultBean getLoginExpertmsg(
				ModelMap model,HttpSession httpSession) throws Exception
		{
			ResultBean resultBean = new ResultBean();
			
			String name = this.getAuthenticatedExpertName(httpSession);
			
			resultBean.setMessage(name);
			
			return resultBean;
		}
		
		/**
		 * 
		* @Title: setLoginExpertMessage 
		* @Description:向session中放置当前登录的专家信息 
		* @param @param session
		* @param @param userCode
		* @param @param password
		* @param @param name
		* @param @param userId    设定文件 
		* @author banna
		* @date 2016年10月9日 上午11:53:10 
		* @return void    返回类型 
		* @throws
		 */
		public  void setLoginExpertMessage(HttpSession session,
				 String userCode,String password,String name,String userId)
		 {
			ExpertsOfFMPAPPDTO expertsOfFMPAPPDTO = new ExpertsOfFMPAPPDTO();
			expertsOfFMPAPPDTO.setCode(userCode);
			expertsOfFMPAPPDTO.setPassword(password);
			expertsOfFMPAPPDTO.setName(name);
			expertsOfFMPAPPDTO.setId(userId);
			
			//将session登录信息放置到session 
			session.setAttribute("expertBean", expertsOfFMPAPPDTO);
			
			
		 }
		
		/**
		 * 
		* @Title: getAuthenticatedExpertName 
		* @Description: 从session中获取当前登录的专家姓名
		* @param @param session
		* @param @return    设定文件 
		* @author banna
		* @date 2016年10月9日 上午11:53:36 
		* @return String    返回类型 
		* @throws
		 */
		public  String getAuthenticatedExpertName(HttpSession session){
			 String name = null; 
			
			 ExpertsOfFMPAPPDTO expertsOfFMPAPPDTO = (ExpertsOfFMPAPPDTO)session.getAttribute("expertBean");
			 
			 name = expertsOfFMPAPPDTO.getName();
			 
			 return name;
		 }
		
		/**
		 * 
		* @Title: getAuthenticatedUserCode 
		* @Description: 从session中获取当前登录的专家登录名 
		* @param @param session
		* @param @return    设定文件 
		* @author banna
		* @date 2016年10月9日 上午11:53:55 
		* @return String    返回类型 
		* @throws
		 */
		 public  String getAuthenticatedExpertCode(HttpSession session){
			 String code = null; 
			
			 ExpertsOfFMPAPPDTO expertsOfFMPAPPDTO = (ExpertsOfFMPAPPDTO)session.getAttribute("expertBean");
			 
			 code = expertsOfFMPAPPDTO.getCode();
			 
			 return code;
		 }
		
	
}
