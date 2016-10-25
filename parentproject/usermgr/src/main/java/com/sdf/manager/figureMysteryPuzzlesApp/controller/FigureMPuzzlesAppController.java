package com.sdf.manager.figureMysteryPuzzlesApp.controller;

import java.io.File;
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

import com.alibaba.fastjson.JSONObject;
import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeDTiming;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.entity.AppAdAndArea;
import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.ExpertsOfFMPAPPDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FloorOfFigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.PuzzleTypeDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAPuzzleUploadfile;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleAndArea;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleNextStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FloorOfFigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FoundFigureAndPuzzleStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.PuzzlesType;
import com.sdf.manager.figureMysteryPuzzlesApp.service.ExpertOfFMAPPService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleAndAreaService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleStatusService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleUploadfileService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzlesService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FloorOfFigureAndPuzzlesService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FoundFigureAndPuzzleStatusService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.PuzzlesTypeService;
import com.sdf.manager.order.controller.OrderController;
import com.sdf.manager.order.entity.FoundOrderStatus;
import com.sdf.manager.order.entity.OrderNextStatus;
import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.service.CityService;

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
	
	@Autowired
	private FloorOfFigureAndPuzzlesService floorOfFigureAndPuzzlesService;
	
	@Autowired
	private FigureAndPuzzlesService figureAndPuzzlesService;
	
	@Autowired
	private FigureAndPuzzleAndAreaService figureAndPuzzleAndAreaService;
	
	@Autowired
	private CityService cityService;
	
	
	
	
	public static final String TUMI_FLAG = "1";//图谜标记
	public static final String ZIMI_FLAG = "2";//字谜标记
	
	 public static final String OPERORTYPE_SAVE = "0";//专家编辑页面，保存
	 public static final String OPERORTYPE_SAVEANDCOMMIT = "1";//专家编辑页面，保存并提交
	 
	 public static final String DIRECTION_GO = "1";//前进方向标志位
	 /***图谜字谜状态静态变量 start***/
	 public static final String EXPERT_SAVE_FAP = "01";//专家保存图谜字谜的状态码
	 public static final String FIGURE_AND_PUZZLE_FINISH = "21";//审批完成且已归档的图谜字谜状态
	 public static final String FIGURE_AND_PUZZLE_STOP = "31";//审批不通过，终止图谜字谜审批状态
	 /***图谜字谜状态静态变量 end***/
	 
	 public static final String PAGE_OPERORTYPE_SAVE = "1";//专家图谜字谜列表，提交
	 public static final String PAGE_OPERORTYPE_PASS = "2";//公司人员审批图谜字谜列表，审批通过
	 public static final String PAGE_OPERORTYPE_REJECT = "3";//公司人员审批图谜字谜列表，审批驳回
	 public static final String PAGE_OPERORTYPE_STOP = "4";//公司人员审批图谜字谜列表，不通过
	 
	
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
			if((null!=puzzlesType.getFloorOfFigureAndPuzzles()&&puzzlesType.getFloorOfFigureAndPuzzles().size()>0) 
					|| (null != puzzlesType.getFigureAndPuzzles() && puzzlesType.getFigureAndPuzzles().size()>0))
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
	  * 
	 * @Title: getAllPuzzlesType 
	 * @Description: 获取字谜类型数据列表
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件 
	 * @author banna
	 * @date 2016年10月10日 上午8:44:06 
	 * @return Map<String,Object>    返回类型 
	 * @throws
	  */
	 @RequestMapping(value = "/getAllPuzzlesType", method = RequestMethod.POST)
		public @ResponseBody List<PuzzleTypeDTO> getAllPuzzlesType(
				ModelMap model,HttpSession httpSession) throws Exception
		{
//			Map<String,Object> result = new HashMap<String, Object>();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("typeName", "desc");
			
			QueryResult<PuzzlesType> exQueryResult = puzzleTypeService.getPuzzlesTypeList(PuzzlesType.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<PuzzlesType> puzzlesTypes = exQueryResult.getResultList();
			
			//将实体转换为dto
			List<PuzzleTypeDTO> puzzleTypeDTOs = puzzleTypeService.toRDTOS(puzzlesTypes);
			
//			result.put("puzzleTypes", puzzleTypeDTOs);
			
			
			return puzzleTypeDTOs;
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
		 
		 
		 /**
			 *********** 4.图谜字谜底板管理模块 ***********
			 **/
		 
		 /**
		  * 
		 * @Title: saveSingleFAPAppFujian 
		 * @Description: 图谜字谜应用上传单个附件方法（一个newsUuid只对应一个附件）
		 * @param @param realname
		 * @param @param filename
		 * @param @param uplId
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月10日 上午8:52:01 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/saveSingleFAPAppFujian", method = RequestMethod.GET)
			public @ResponseBody ResultBean  saveSingleFAPAppFujian(
					@RequestParam(value="realname",required=false) String realname,
					@RequestParam(value="filename",required=false) String filename,
					@RequestParam(value="uplId",required=false) String uplId,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 String type=getExt(filename);
			 String uploadfilepath = "/uploadFAPAppImg/";//图谜字谜应用图片路径
			 
			 FigureAPuzzleUploadfile uploadfile = figureAndPuzzleUploadfileService.getFigureAPuzzleUploadfileByNewsUuid(uplId);
			 //因为一个应用只能有一个图片附件，所以当这个upId有数据的话就进行修改操作，如果没有数据就创建数据
			 if(null != uploadfile)
			 {
				 //①：因为广告图片只有一个附件，所以在上传其他附件替换上一个附件时，要先把上一个附件文件删除
				 String savePath = httpSession.getServletContext().getRealPath("");//获取项目根路径
			     savePath = savePath +File.separator+ "uploadFAPAppImg"+File.separator;
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
	        		logger.info("saveSingleFAPAppFujian==删除原附件文件数据--附件id="+uploadfile.getId()+"--操作人= 图谜字谜专家");
	        	}
			    //删除附件e
				 
				 //②：保存新的附件文件
				 uploadfile.setUploadFileName(filename);
				 uploadfile.setUploadRealName(realname);
				 uploadfile.setUploadfilepath(uploadfilepath);
				 uploadfile.setUploadContentType(type);
				 
				 //添加修改时间跟踪
				 uploadfile.setModify(uploadfile.getNewsUuid());//放置附件关联uuid
				 uploadfile.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 
				 figureAndPuzzleUploadfileService.update(uploadfile);
			 }
			 else
			 {
				 uploadfile = new FigureAPuzzleUploadfile();
				 uploadfile.setNewsUuid(uplId);
				 uploadfile.setUploadFileName(filename);
				 uploadfile.setUploadRealName(realname);
				 uploadfile.setUploadfilepath(uploadfilepath);
				 uploadfile.setUploadContentType(type);
				
				 //添加修改时间跟踪
				 uploadfile.setCreater(uploadfile.getNewsUuid());//放置附件关联uuid
				 uploadfile.setModify(uploadfile.getNewsUuid());//放置附件关联uuid
				 uploadfile.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				 uploadfile.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 uploadfile.setIsDeleted(Constants.IS_NOT_DELETED);
				 
				 figureAndPuzzleUploadfileService.save(uploadfile);
			 }
			 
			 resultBean.setStatus("success");
			 
			 logger.info("上传图谜字谜应用单个附件成功，附件id="+uplId+"==附件文件存储名称="+filename);
			 
			 return resultBean;
			 
		 }
		 
		 /**
		  * 
		 * @Title: saveMoreFAPAppFujian 
		 * @Description: 图谜字谜应用上传多个附件方法（一个newsUuid对应多个附件）
		 * @param @param realname
		 * @param @param filename
		 * @param @param uplId
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月10日 上午8:52:45 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/saveMoreFAPAppFujian", method = RequestMethod.GET)
			public @ResponseBody ResultBean  saveMoreFAPAppFujian(
					@RequestParam(value="realname",required=false) String realname,
					@RequestParam(value="filename",required=false) String filename,
					@RequestParam(value="uplId",required=false) String uplId,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 String type=getExt(filename);
			 String uploadfilepath = "/uploadFAPAppImg/";//图谜字谜应用图片路径
			 
			 FigureAPuzzleUploadfile uploadfile = new FigureAPuzzleUploadfile();
			 uploadfile.setNewsUuid(uplId);
			 uploadfile.setUploadFileName(filename);
			 uploadfile.setUploadRealName(realname);
			 uploadfile.setUploadfilepath(uploadfilepath);
			 uploadfile.setUploadContentType(type);
			 
			 //添加修改时间跟踪
			 uploadfile.setCreater(uploadfile.getNewsUuid());//放置附件关联uuid
			 uploadfile.setModify(uploadfile.getNewsUuid());//放置附件关联uuid
			 uploadfile.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			 uploadfile.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 uploadfile.setIsDeleted(Constants.IS_NOT_DELETED);
			 
			 figureAndPuzzleUploadfileService.save(uploadfile);
			 
			 resultBean.setStatus("success");
			 
			 logger.info("上传图谜字谜附件成功，附件id="+uplId+"==附件文件存储名称="+filename);
			 
			 return resultBean;
			 
		 }
		 
		 
		 /**
		  * 
		  * @Title: getExt
		  * @Description: 获取附件类型的后缀
		  * @author:banna
		  * @return: String
		  */
		 private String getExt(String fileName) {
				return fileName.substring(fileName.lastIndexOf("."));
			}
		 
		 /**
		  * 
		 * @Title: getDetailFloorOfFAPApp 
		 * @Description:根据id获取图谜字谜底板数据
		 * @param @param id
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月10日 上午9:03:36 
		 * @return FloorOfFigureAndPuzzlesDTO    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getDetailFloorOfFAPApp", method = RequestMethod.GET)
			public @ResponseBody FloorOfFigureAndPuzzlesDTO getDetailFloorOfFAPApp(@RequestParam(value="id",required=false) String id,
					ModelMap model,HttpSession httpSession) throws Exception
			{
				
				FloorOfFigureAndPuzzles floorOfFigureAndPuzzles = floorOfFigureAndPuzzlesService.getFloorOfFigureAndPuzzlesById(id);
				
				FloorOfFigureAndPuzzlesDTO floorOfFigureAndPuzzlesDTO = floorOfFigureAndPuzzlesService.toDTO(floorOfFigureAndPuzzles);
				
				logger.info("获取图谜字谜底板详情，id="+id);
				
				return floorOfFigureAndPuzzlesDTO;
			}
		 
		 /**
		  * 
		 * @Title: getFloorOfFAPAppList 
		 * @Description: 根据条件筛选图谜字谜底板数据
		 * @param @param page
		 * @param @param rows
		 * @param @param floorName
		 * @param @param figureOrPuzzles
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月10日 上午9:41:14 
		 * @return Map<String,Object>    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getFloorOfFAPAppList", method = RequestMethod.GET)
			public @ResponseBody Map<String,Object> getFloorOfFAPAppList(
					@RequestParam(value="page",required=false) int page,
					@RequestParam(value="rows",required=false) int rows,
					@RequestParam(value="floorName",required=false) String floorName,
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
				if(null != floorName&&!"".equals(floorName.trim()))
				{
					params.add("%"+floorName+"%");
					buffer.append(" and floorName like ?").append(params.size());
				}
				
				
				if(null != figureOrPuzzles&&!"".equals(figureOrPuzzles.trim()))
				{
					params.add(figureOrPuzzles);
					buffer.append(" and figureOrPuzzles = ?").append(params.size());
				}
				
				//排序
				LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
				orderBy.put("createrTime", "desc");
				
				QueryResult<FloorOfFigureAndPuzzles> exQueryResult = floorOfFigureAndPuzzlesService.getFloorOfFigureAndPuzzlesList
						(FloorOfFigureAndPuzzles.class,
						buffer.toString(), params.toArray(),orderBy, pageable);
						
				List<FloorOfFigureAndPuzzles> expertsOfFMPAPPs = exQueryResult.getResultList();
				Long totalrow = exQueryResult.getTotalRecord();
				
				//将实体转换为dto
				List<FloorOfFigureAndPuzzlesDTO> floorOfFigureAndPuzzlesDTOs = floorOfFigureAndPuzzlesService.toRDTOS(expertsOfFMPAPPs);
				
				result.put("rows", floorOfFigureAndPuzzlesDTOs);
				result.put("total", totalrow);
			 	
				
				
				
				return result;
			}
		 
		 /**
		  * 
		 * @Title: saveOrUpdateFloorOfFAPApp 
		 * @Description: 添加或修改图谜字谜底板数据
		 * @param @param id
		 * @param @param floorName
		 * @param @param figureOrPuzzles
		 * @param @param floorDescription
		 * @param @param floorImg
		 * @param @param puzzlesTypeId
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月10日 上午10:06:31 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/saveOrUpdateFloorOfFAPApp", method = RequestMethod.GET)
			public @ResponseBody ResultBean saveOrUpdateFloorOfFAPApp(
					@RequestParam(value="id",required=false) String id,
					@RequestParam(value="floorName",required=false) String floorName,
					@RequestParam(value="figureOrPuzzles",required=false) String figureOrPuzzles,
					@RequestParam(value="floorDescription",required=false) String floorDescription,
					@RequestParam(value="floorImg",required=false) String floorImg,
					@RequestParam(value="puzzlesTypeId",required=false) String puzzlesTypeId,//若当前的底板是字谜底板，这个字段放置的是字谜类型的id
					ModelMap model,HttpSession httpSession) throws Exception
			{
			   ResultBean resultBean = new ResultBean ();
			   
			   FloorOfFigureAndPuzzles floorOfFigureAndPuzzles = floorOfFigureAndPuzzlesService.getFloorOfFigureAndPuzzlesById(id);
			   if(null != floorOfFigureAndPuzzles)
			   {//修改图谜字谜底板数据
				   
				   floorOfFigureAndPuzzles.setFloorName(floorName);
				   
//				   floorOfFigureAndPuzzles.setFigureOrPuzzles(figureOrPuzzles);//图谜字谜类型修改时不可进行修改
				   floorOfFigureAndPuzzles.setFloorDescription(floorDescription);
				   floorOfFigureAndPuzzles.setFloorImg(floorImg);
				   floorOfFigureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   floorOfFigureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   
				   PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(puzzlesTypeId);
				   floorOfFigureAndPuzzles.setPuzzlesType(puzzlesType);
				   
				   
				   logger.info("修改图谜字谜底板数据，id="+id);
				   
				   floorOfFigureAndPuzzlesService.update(floorOfFigureAndPuzzles);
				   
				   resultBean.setMessage("修改图谜字谜底板成功!");
				   resultBean.setStatus("success");
			   }
			   else
			   {//添加图谜字谜专家数据
				   floorOfFigureAndPuzzles = new FloorOfFigureAndPuzzles();
				   floorOfFigureAndPuzzles.setFloorName(floorName);
				   floorOfFigureAndPuzzles.setFigureOrPuzzles(figureOrPuzzles);
				   floorOfFigureAndPuzzles.setFloorDescription(floorDescription);
				   floorOfFigureAndPuzzles.setFloorImg(floorImg);
				   
				   PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(puzzlesTypeId);
				   floorOfFigureAndPuzzles.setPuzzlesType(puzzlesType);
				   
				   floorOfFigureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				   floorOfFigureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   floorOfFigureAndPuzzles.setIsDeleted(Constants.IS_NOT_DELETED);
				   floorOfFigureAndPuzzles.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
				   floorOfFigureAndPuzzles.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				   
				   logger.info("添加图谜字谜底板数据");
				   
				   floorOfFigureAndPuzzlesService.save(floorOfFigureAndPuzzles);
				   
				   resultBean.setMessage("添加图谜字谜底板成功!");
				   resultBean.setStatus("success");
				   
				   
			   }
			   
			 
			   return resultBean;
			}
		 
		 /**
		  * 
		 * @Title: deleteFloorOfFAPApp 
		 * @Description: 删除图谜字谜底板数据
		 * @param @param ids
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月10日 上午10:09:38 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/deleteFloorOfFAPApp", method = RequestMethod.POST)
			public @ResponseBody ResultBean  deleteFloorOfFAPApp(
					@RequestParam(value="ids",required=false) String[] ids,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 
			 //获取项目根路径
			 String savePath = httpSession.getServletContext().getRealPath("");
		     savePath = savePath +File.separator+ "uploadFAPAppImg"+File.separator;
			 
			 
			 FloorOfFigureAndPuzzles floorOfFigureAndPuzzles = null;
			 //删除附件文件相关s
			List<FigureAPuzzleUploadfile> uploadfiles;
			 File dirFile = null;
			 boolean deleteFlag = false;//删除附件flag
			//删除附件文件相关e
			 
			 for (String id : ids) 
				{
				 	floorOfFigureAndPuzzles = floorOfFigureAndPuzzlesService.getFloorOfFigureAndPuzzlesById(id);
				 	if(null != floorOfFigureAndPuzzles)
				 	{
				 		//删除附件s
				 		//1.获取附件
				 		uploadfiles = figureAndPuzzleUploadfileService.getFigureAPuzzleUploadfilesByNewsUuid(floorOfFigureAndPuzzles.getFloorImg());
				 		if(null != uploadfiles && uploadfiles.size()>0)
				 		{
				 			for (FigureAPuzzleUploadfile uploadfile : uploadfiles)
				 			{
				 				//2.删除附件
						 		dirFile = new File(savePath+uploadfile.getUploadRealName());
						 		logger.info("待删除文件路径："+dirFile);
						        // 如果dir对应的文件不存在，或者不是一个目录，则退出
						 		deleteFlag = dirFile.delete();
					        	if(deleteFlag)
					        	{//删除附件(清空附件关联newsUuid)
					        		uploadfile.setModify(uploadfile.getNewsUuid());//放置附件关联uuid
					   			 	uploadfile.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   			 	uploadfile.setNewsUuid("");
					   			 	uploadfile.setIsDeleted(Constants.IS_DELETED);
					        		figureAndPuzzleUploadfileService.update(uploadfile);
					        		logger.info("删除图谜字谜底板附件数据--附件id="+uploadfile.getId()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					        	}
					        	else
					        	{
					        		 logger.error("图谜字谜底板数据id为："+floorOfFigureAndPuzzles.getId()+"的数据没有文件");
					        	}
						        	
						      //删除附件e
							}
				 			
				 		}
				 		//对图谜字谜底板数据做删除处理
				 		floorOfFigureAndPuzzles.setIsDeleted(Constants.IS_DELETED);
				 		floorOfFigureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				 		floorOfFigureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 		floorOfFigureAndPuzzles.setPuzzlesType(null);
				 		floorOfFigureAndPuzzlesService.update(floorOfFigureAndPuzzles);
				 		
				 		
				 		 //日志输出
						 logger.info("删除图谜字谜底板版本--图谜字谜底板id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
					   
				 	}
				}
			 String returnMsg = "删除成功!";
			 resultBean.setStatus("success");
			 resultBean.setMessage(returnMsg);
			 
			 return resultBean;
		 }
		 
		 
		 /**
		  * 
		 * @Title: deleteImgsByNewsuuid 
		 * @Description: 根据newsUuid删除图谜字谜附件数据
		 * @param @param newsUuid
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月11日 上午9:51:11 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/deleteImgsByNewsuuid", method = RequestMethod.GET)
			public @ResponseBody ResultBean deleteImgsByNewsuuid(
					@RequestParam(value="newsUuid",required=false) String newsUuid,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 if(null != newsUuid)
			 {
				 List<FigureAPuzzleUploadfile> uploadfiles = figureAndPuzzleUploadfileService.getFigureAPuzzleUploadfilesByNewsUuid(newsUuid);
				 
				
				 
				 //删除
				 if(null != uploadfiles)
				 {
					//①：删除附件的数据时要把当前附件数据对于的附件文件也删除
					 String savePath = httpSession.getServletContext().getRealPath("");//获取项目根路径
				    
				     //删除附件文件相关s
					 File dirFile = null;
					 boolean deleteFlag = false;//删除附件flag
					 
					 for (FigureAPuzzleUploadfile uploadfile : uploadfiles) 
					 {
						 	savePath = savePath +uploadfile.getUploadfilepath();
						 	//2.删除附件
					 		dirFile = new File(savePath+uploadfile.getUploadRealName());
					 		logger.info("待删除文件路径："+dirFile);
					        // 如果dir对应的文件不存在，或者不是一个目录，则退出
				        	deleteFlag = dirFile.delete();
				        	if(deleteFlag)
				        	{//删除附件(清空附件关联newsUuid)
				        		logger.info("deleteImgsByNewsuuid==删除原附件文件数据--附件id="+uploadfile.getId());
				        	}
						    //删除附件e
					   		 uploadfile.setModify(uploadfile.getNewsUuid());//放置附件关联uuid
					   		 uploadfile.setModifyTime(new Timestamp(System.currentTimeMillis()));
					   		 uploadfile.setIsDeleted(Constants.IS_DELETED);//删除标记
		//						 uploadfileService.delete(uploadfile);
					   		figureAndPuzzleUploadfileService.update(uploadfile);
					 }
					 
					
				 }
				
				 
				 //TODO:删除文件附件图片
				 
				 resultBean.setUseFlag(true);
			 }
			
			 return resultBean;
		 }
		 
		
		 
		 /**
		  * 
		 * @Title: deleteImg 
		 * @Description: 根据id删除附件数据
		 * @param @param id
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月11日 上午10:24:50 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/deleteImg", method = RequestMethod.GET)
			public @ResponseBody ResultBean deleteImg(
					@RequestParam(value="id",required=false) String id,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 if(null != id)
			 {
				 FigureAPuzzleUploadfile uploadfile = figureAndPuzzleUploadfileService.getFigureAPuzzleUploadfileById(Integer.parseInt(id));
				 
				
				 
				 //删除
				 if(null != uploadfile)
				 {
					//①：删除附件的数据时要把当前附件数据对于的附件文件也删除
					 String savePath = httpSession.getServletContext().getRealPath("");//获取项目根路径
				     savePath = savePath +uploadfile.getUploadfilepath();
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
		        		logger.info("deleteImg==删除原附件文件数据--附件id="+uploadfile.getId()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
		        	}
				    //删除附件e
			   		 uploadfile.setModify(uploadfile.getNewsUuid());//放置附件关联uuid
			   		 uploadfile.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   		 uploadfile.setIsDeleted(Constants.IS_DELETED);//删除标记
//					 uploadfileService.delete(uploadfile);
			   		figureAndPuzzleUploadfileService.update(uploadfile);
				 }
				
				 
				 //TODO:删除文件附件图片
				 
				 resultBean.setUseFlag(true);
			 }
			
			 return resultBean;
		 }
		 
		/**
		 * 
		* @Title: getImgsByNewsuuid 
		* @Description:根据newsUuid获取其对应的有效的图片列表
		* @param @param newsUuid
		* @param @param model
		* @param @param httpSession
		* @param @return
		* @param @throws Exception    设定文件 
		* @author banna
		* @date 2016年10月11日 上午10:05:18 
		* @return Map<String,Object>    返回类型 
		* @throws
		 */
		 @RequestMapping(value = "/getImgsByNewsuuid", method = RequestMethod.GET)
			public @ResponseBody Map<String,Object> getImgsByNewsuuid(
					@RequestParam(value="newsUuid",required=false) String newsUuid,
					ModelMap model,HttpSession httpSession) throws Exception 
		{
			 
			 Map<String,Object> result = new HashMap<String, Object>();
			 
			 if(null != newsUuid)
			 {
				 List<FigureAPuzzleUploadfile> uploadfiles = figureAndPuzzleUploadfileService.getFigureAPuzzleUploadfilesByNewsUuid(newsUuid);
				 
				 result.put("imgList", uploadfiles);
			 }
			 
			 return result;
			 
		 }
		 
		 /**
		  * 
		 * @Title: getImgsById 
		 * @Description:根据id获取附件数据 
		 * @param @param newsUuid
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月18日 下午1:50:11 
		 * @return Map<String,Object>    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getImgById", method = RequestMethod.GET)
			public @ResponseBody Map<String,Object> getImgById(
					@RequestParam(value="id",required=false) String id,
					ModelMap model,HttpSession httpSession) throws Exception 
		{
			 
			 Map<String,Object> result = new HashMap<String, Object>();
			 
			 if(null != id && !"".equals(id))
			 {
				 FigureAPuzzleUploadfile figureAPuzzleUploadfile = figureAndPuzzleUploadfileService.getFigureAPuzzleUploadfileById(Integer.parseInt(id));
				 result.put("uploadfile", figureAPuzzleUploadfile);
			 }
			 
			 return result;
			 
		 }
		 
		 /**
		  * 
		 * @Title: getFloorOfFAPAppOfPuzzlesType 
		 * @Description: 获取当前字谜类型对应的图谜字谜底板数据
		 * @param @param id
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月14日 下午4:40:19 
		 * @return List<FloorOfFigureAndPuzzlesDTO>    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getFloorOfFAPAppOfPuzzlesType", method = RequestMethod.POST)
			public @ResponseBody List<FloorOfFigureAndPuzzlesDTO> getFloorOfFAPAppOfPuzzlesType(@RequestParam(value="id",required=false) String id,
					ModelMap model,HttpSession httpSession) throws Exception
			{
			 	List<FloorOfFigureAndPuzzlesDTO> floorOfFigureAndPuzzlesDTOs = 
			 			new ArrayList<FloorOfFigureAndPuzzlesDTO>();
			 
				PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(id);
				
				if(null != puzzlesType)
				{
					List<FloorOfFigureAndPuzzles> floorOfFigureAndPuzzles = puzzlesType.getFloorOfFigureAndPuzzles();
					
					floorOfFigureAndPuzzlesDTOs = floorOfFigureAndPuzzlesService.toRDTOS(floorOfFigureAndPuzzles);
					
					logger.info("获取当前字谜类型对应的底板数据数据，id="+id);
				}
				
				
				
				return floorOfFigureAndPuzzlesDTOs;
			}
		 
		
		 
		 
		 /**
			 *********** 5.专家管理及发布图谜、字谜模块 ***********
			 **/
		 
		 
		 /**
		  * 
		 * @Title: getDetailFigureAndPuzzles 
		 * @Description: 根据id获取图谜字谜dto数据
		 * @param @param id
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月13日 下午1:36:00 
		 * @return FigureAndPuzzlesDTO    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getDetailFigureAndPuzzles", method = RequestMethod.GET)
			public @ResponseBody FigureAndPuzzlesDTO getDetailFigureAndPuzzles(@RequestParam(value="id",required=false) String id,
					ModelMap model,HttpSession httpSession) throws Exception
			{
				
				FigureAndPuzzles figureAndPuzzles = figureAndPuzzlesService.getFigureAndPuzzlesById(id);
				
				FigureAndPuzzlesDTO figureAndPuzzlesDTO = figureAndPuzzlesService.toDTO(figureAndPuzzles);
				
				logger.info("获取图谜字谜详情，id="+id);
				
				return figureAndPuzzlesDTO;
			}
		 
		 /**
		  * 
		 * @Title: getFigureAndPuzzlesList 
		 * @Description: 模糊查询获取图谜字谜列表数据
		 * @param @param page
		 * @param @param rows
		 * @param @param name
		 * @param @param lotteryType
		 * @param @param figureOrPuzzles
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月13日 下午1:39:15 
		 * @return Map<String,Object>    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getFigureAndPuzzlesList", method = RequestMethod.GET)
			public @ResponseBody Map<String,Object> getFigureAndPuzzlesList(
					@RequestParam(value="page",required=false) int page,
					@RequestParam(value="rows",required=false) int rows,
					@RequestParam(value="name",required=false) String name,
					@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种（1：体彩  2:福彩）
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
				
				
				if(null != figureOrPuzzles&&!"".equals(figureOrPuzzles.trim()))
				{
					params.add(figureOrPuzzles);
					buffer.append(" and figureOrPuzzles = ?").append(params.size());
				}
				
				
				if(null != lotteryType&&!"".equals(lotteryType.trim()))
				{
					params.add(lotteryType);
					buffer.append(" and lotteryType = ?").append(params.size());
				}
				
				//排序
				LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
				orderBy.put("createrTime", "desc");
				
				QueryResult<FigureAndPuzzles> exQueryResult = figureAndPuzzlesService.getFigureAndPuzzlesList
						(FigureAndPuzzles.class,
						buffer.toString(), params.toArray(),orderBy, pageable);
						
				List<FigureAndPuzzles> expertsOfFMPAPPs = exQueryResult.getResultList();
				Long totalrow = exQueryResult.getTotalRecord();
				
				//将实体转换为dto
				List<FigureAndPuzzlesDTO> figureAndPuzzlesDTOs = figureAndPuzzlesService.toRDTOS(expertsOfFMPAPPs);
				
				result.put("rows", figureAndPuzzlesDTOs);
				result.put("total", totalrow);
			 	
				
				
				
				return result;
			}
		 
		 /**
		  * 
		 * @Title: getFigureAndPuzzlesListOfExpert 
		 * @Description: 获取当前专家发布的图谜字谜数据 
		 * @param @param page
		 * @param @param rows
		 * @param @param name
		 * @param @param lotteryType
		 * @param @param figureOrPuzzles
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月14日 上午8:58:25 
		 * @return Map<String,Object>    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/getFigureAndPuzzlesListOfExpert", method = RequestMethod.GET)
			public @ResponseBody Map<String,Object> getFigureAndPuzzlesListOfExpert(
					@RequestParam(value="page",required=false) int page,
					@RequestParam(value="rows",required=false) int rows,
					@RequestParam(value="name",required=false) String name,
					@RequestParam(value="lotterytype",required=false) String lotterytype,//彩种（1：体彩  2:福彩）
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
				
				//获取当前登录专家所发布的图谜字谜数据
				params.add(this.getAuthenticatedExpertCode(httpSession));
				buffer.append(" and creater = ?").append(params.size());
				
				//连接查询条件
				if(null != name&&!"".equals(name.trim()))
				{
					params.add("%"+name+"%");
					buffer.append(" and name like ?").append(params.size());
				}
				
				
				if(null != figureOrPuzzles&&!"".equals(figureOrPuzzles.trim()))
				{
					params.add(figureOrPuzzles);
					buffer.append(" and figureOrPuzzles = ?").append(params.size());
				}
				
				
				if(null != lotterytype&&!"".equals(lotterytype.trim()))
				{
					params.add(lotterytype);
					buffer.append(" and lotteryType = ?").append(params.size());
				}
				
				//排序
				LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
				orderBy.put("createrTime", "desc");
				
				QueryResult<FigureAndPuzzles> exQueryResult = figureAndPuzzlesService.getFigureAndPuzzlesList
						(FigureAndPuzzles.class,
						buffer.toString(), params.toArray(),orderBy, pageable);
						
				List<FigureAndPuzzles> expertsOfFMPAPPs = exQueryResult.getResultList();
				Long totalrow = exQueryResult.getTotalRecord();
				
				//将实体转换为dto
				List<FigureAndPuzzlesDTO> figureAndPuzzlesDTOs = figureAndPuzzlesService.toRDTOS(expertsOfFMPAPPs);
				
				result.put("rows", figureAndPuzzlesDTOs);
				result.put("total", totalrow);
			 	
				
				
				
				return result;
			}
		 
		 
		 /**
		  * 
		 * @Title: saveOrUpdateFigureAndPuzzles 
		 * @Description: 保存或修改图谜字谜数据
		 * @param @param id
		 * @param @param name
		 * @param @param lotteryType
		 * @param @param figureOrPuzzles
		 * @param @param isCompany
		 * @param @param wordInImg
		 * @param @param status
		 * @param @param puzzlesTypeId
		 * @param @param floorOfFigureAndPuzzlesId
		 * @param @param areadata
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月13日 下午1:44:35 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/saveOrUpdateFigureAndPuzzles", method = RequestMethod.GET)
			public @ResponseBody ResultBean saveOrUpdateFigureAndPuzzles(
					@RequestParam(value="id",required=false) String id,
					@RequestParam(value="name",required=false) String name,
					@RequestParam(value="playName",required=false) String playName,//彩种玩法
					@RequestParam(value="lotteryType",required=false) String lotteryType,
					@RequestParam(value="figureOrPuzzles",required=false) String figureOrPuzzles,
					@RequestParam(value="isCompany",required=false) String isCompany,//是否为公司虚拟专家发布的图谜、字谜，1：是 0：否（若为公司虚拟专家算法生成的图谜字谜标记都是1，其他专家添加的都是0）
					@RequestParam(value="wordInImg",required=false) String wordInImg,//图中字图片对应的附件的newsUuid，可以对应多个图中字图片(图片加载时不需要有顺序)
					@RequestParam(value="status",required=false) String status,
					@RequestParam(value="puzzlesTypeId",required=false) String puzzlesTypeId,
					@RequestParam(value="floorOfFigureAndPuzzlesId",required=false) String floorOfFigureAndPuzzlesId,
					@RequestParam(value="floorUploadid",required=false) String floorUploadid,//底板图片附件数据id(floor表对应的附件的)
					@RequestParam(value="puzzleContent",required=false) String puzzleContent,//输入文字形式的字谜的字谜内容
					@RequestParam(value="figureImg",required=false) String figureImg,//上传的图谜/字谜图片对应的newsUuid
					@RequestParam(value="zimiStatus",required=false) String  zimiStatus,//字谜类型，0：输入文字,1：上传字谜图片
					@RequestParam(value="playNum",required=false) String  playNum,//当前图谜字谜是给彩种玩法的哪期使用的
					@RequestParam(value="operatype",required=false) String operatype,//0:保存 1：保存并提交
//					@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,//审批完成时添加区域信息
					ModelMap model,HttpSession httpSession) throws Exception
			{
			   ResultBean resultBean = new ResultBean ();
			   
			  /* //提取绑定的区域数据
			   JSONObject areas = JSONObject.parseObject(areadata);
			   List<String> cityIds =  (List<String>) areas.get("keys");*/
			   
			   FigureAndPuzzles figureAndPuzzles = figureAndPuzzlesService.getFigureAndPuzzlesById(id);
			   
			   if(null != figureAndPuzzles)
			   {
				   logger.info("修改图谜字谜数据，id="+id);
				   
				   
				   figureAndPuzzles.setName(name);
				   /*figureAndPuzzles.setPlayName(playName);//修改图谜、字谜时不可以修改字段*/
				  /* figureAndPuzzles.setLotteryType(lotteryType);//修改图谜、字谜时不可以修改字段*/
//				   figureAndPuzzles.setIsCompany("0");//非公司虚拟发布
				   /*figureAndPuzzles.setFigureOrPuzzles(figureOrPuzzles);//修改时不可以修改图谜/字谜类型*/
				   
				   String lastFigureOrPuzzles = figureAndPuzzles.getFigureOrPuzzles();
				   if(this.TUMI_FLAG.equals(lastFigureOrPuzzles))
				   {//图谜
					   figureAndPuzzles.setWordInImg(wordInImg);
					   figureAndPuzzles.setFigureImg(figureImg);
					   figureAndPuzzles.setZimiStatus(null);//图谜没有字谜状态数据
				   }
				   else
				   {
					   String lastZimiStatus = figureAndPuzzles.getZimiStatus();//修改字谜前的字谜类别
					   if(this.ZIMI_FLAG.equals(lastFigureOrPuzzles))
					   {//字谜
						   if("0".equals(zimiStatus))
						   {//输入文字字谜
							   figureAndPuzzles.setZimiStatus(zimiStatus);
							   figureAndPuzzles.setPuzzleContent(puzzleContent);
							   //添加关联的字谜类型数据
							   PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(puzzlesTypeId);
							   figureAndPuzzles.setPuzzlesType(puzzlesType);
							   
							   //添加关联的底板数据
							   FloorOfFigureAndPuzzles floorOfFigureAndPuzzles = floorOfFigureAndPuzzlesService
									   .getFloorOfFigureAndPuzzlesById(floorOfFigureAndPuzzlesId);
							   figureAndPuzzles.setFloorOfFigureAndPuzzles(floorOfFigureAndPuzzles);
							   
							   figureAndPuzzles.setFloorUploadid(floorUploadid);
							   
							   if(!lastZimiStatus.equals(zimiStatus))
							   {//若字谜类型改变，则要删除之前的数据
								   figureAndPuzzles.setFigureImg(null);//清空字谜图片附件id
								   this.deleteImgsByNewsuuid(figureImg, model, httpSession);
							   }
						   }
						   else 
							   if("1".equals(zimiStatus))
							   {//上传字谜图片
								   figureAndPuzzles.setWordInImg(wordInImg);
								   figureAndPuzzles.setFigureImg(figureImg);
								   figureAndPuzzles.setZimiStatus(zimiStatus);//图谜没有字谜状态数据
								   
								   if(!lastZimiStatus.equals(zimiStatus))
								   {//若字谜类型改变，则要删除之前的数据
									   figureAndPuzzles.setPuzzleContent(null);//清空之前的字谜内容
									   figureAndPuzzles.setPuzzlesType(null);//清空之前关联的字谜类型
									   figureAndPuzzles.setFloorOfFigureAndPuzzles(null);//清空之前关联的底板数据
									   figureAndPuzzles.setFloorUploadid(null);//清空之前关联的底板附件id数据
								   }
							   }
					   }
				   }
				   
				 //当前图谜字谜的状态处理
				   String currentStatus = figureAndPuzzles.getStatus();//获取当前图谜字谜数据的状态
				   if(FigureMPuzzlesAppController.OPERORTYPE_SAVE.equals(operatype))
				   {//保存
					   currentStatus = figureAndPuzzles.getStatus();
				   }
				   else if(FigureMPuzzlesAppController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
				   {
					   //根据当前状态获取下一状态
					   //专家提交图谜字谜，状态前进标记位
					   String directionFlag = "1";
					   FigureAndPuzzleNextStatus figureAndPuzzleNextStatus = 
							   figureAndPuzzleStatusService.
							   getFigureAndPuzzleNextStatusBycurrentStatusId(currentStatus, directionFlag);
					   
					   currentStatus = figureAndPuzzleNextStatus.getNextStatusId();//专家发布图谜字谜保存并提交
					   
				   }
				   
				   figureAndPuzzles.setStatus(currentStatus);//专家发布图谜字谜，保存并提交操作
				   figureAndPuzzles.setStatusTime(new Timestamp(System.currentTimeMillis()));
				   
				   
				   figureAndPuzzles.setModify(this.getAuthenticatedExpertCode(httpSession));
				   figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   figureAndPuzzlesService.update(figureAndPuzzles);
				   
				   
				   if(FigureMPuzzlesAppController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
				   {
					 //由于状态变化，将变化状态存入到状态流程跟踪表中
					   this.saveFoundFigureAndPuzzleStatus(this.getAuthenticatedExpertCode(httpSession)
							   ,currentStatus,figureAndPuzzles,"");
				   }
				   
				   resultBean.setMessage("修改图谜字谜成功!");
				   resultBean.setStatus("success");
			   }
			   else
			   {
				   figureAndPuzzles = new FigureAndPuzzles();
				   
				   String fApCode = this.codeGenertor();
				   
				   figureAndPuzzles.setfAPCode(fApCode);
				   figureAndPuzzles.setName(name);
				   figureAndPuzzles.setPlayName(playName);
				   figureAndPuzzles.setLotteryType(lotteryType);
				   figureAndPuzzles.setIsCompany("0");//非公司虚拟发布
				   figureAndPuzzles.setFigureOrPuzzles(figureOrPuzzles);//放置当前是图谜or字谜
				   figureAndPuzzles.setPlayNum(playNum);//当前图谜字谜给哪期彩种玩法使用
				   
				   if(this.TUMI_FLAG.equals(figureOrPuzzles))
				   {//图谜
					   figureAndPuzzles.setWordInImg(wordInImg);
					   figureAndPuzzles.setFigureImg(figureImg);
					   figureAndPuzzles.setZimiStatus(null);//图谜没有字谜状态数据
				   }
				   else
				   {
					   if(this.ZIMI_FLAG.equals(figureOrPuzzles))
					   {//字谜
						   if("0".equals(zimiStatus))
						   {//输入文字字谜
							   figureAndPuzzles.setZimiStatus(zimiStatus);
							   figureAndPuzzles.setPuzzleContent(puzzleContent);
							   //添加关联的字谜类型数据
							   PuzzlesType puzzlesType = puzzleTypeService.getPuzzlesTypeById(puzzlesTypeId);
							   figureAndPuzzles.setPuzzlesType(puzzlesType);
							   
							   //添加关联的底板数据
							   FloorOfFigureAndPuzzles floorOfFigureAndPuzzles = floorOfFigureAndPuzzlesService
									   .getFloorOfFigureAndPuzzlesById(floorOfFigureAndPuzzlesId);
							   figureAndPuzzles.setFloorOfFigureAndPuzzles(floorOfFigureAndPuzzles);
							   
							   figureAndPuzzles.setFloorUploadid(floorUploadid);
						   }
						   else 
							   if("1".equals(zimiStatus))
							   {//上传字谜图片
								   figureAndPuzzles.setWordInImg(wordInImg);
								   figureAndPuzzles.setFigureImg(figureImg);
								   figureAndPuzzles.setZimiStatus(zimiStatus);//图谜没有字谜状态数据
							   }
					   }
				   }
				   
				   
				   //当前图谜字谜的状态处理
				   String currentStatus = "01";
				   if(FigureMPuzzlesAppController.OPERORTYPE_SAVE.equals(operatype))
				   {
					   currentStatus = OrderController.PROXY_SAVE_ORDER;//购买商品形成订单时，代理保存的状态
				   }
				   else if(FigureMPuzzlesAppController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
				   {
					   String directionFlag = "1";
					   FigureAndPuzzleNextStatus figureAndPuzzleNextStatus = 
							   figureAndPuzzleStatusService.
							   getFigureAndPuzzleNextStatusBycurrentStatusId(currentStatus, directionFlag);
					   
					   currentStatus = figureAndPuzzleNextStatus.getNextStatusId();//专家发布图谜字谜保存并提交
				   }
				   figureAndPuzzles.setStatus(currentStatus);//专家发布图谜字谜，保存并提交操作
				   figureAndPuzzles.setStatusTime(new Timestamp(System.currentTimeMillis()));
				   
				   figureAndPuzzles.setModify(this.getAuthenticatedExpertCode(httpSession));
				   figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				   figureAndPuzzles.setIsDeleted(Constants.IS_NOT_DELETED);
				   figureAndPuzzles.setCreater(this.getAuthenticatedExpertCode(httpSession));
				   figureAndPuzzles.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				   
				   logger.info("添加图谜字谜数据");
				   
				   figureAndPuzzlesService.save(figureAndPuzzles);
				   
				 //跟踪图谜字谜状态，添加图谜字谜状态到图谜字谜状态跟踪表中
				   
				   /*finishSaveFAP用处：用来做订单跟踪表与订单表的数据关联，因为若不获取，
				   	则当前操作的订单还不存在，无法存入其状态(订单编码也是全局唯一的！！！)*/
				   FigureAndPuzzles finishSaveFAP = figureAndPuzzlesService.getFigureAndPuzzlesByFAPCode(fApCode);
				   
				   if(FigureMPuzzlesAppController.OPERORTYPE_SAVE.equals(operatype))
				   {
					   currentStatus = FigureMPuzzlesAppController.EXPERT_SAVE_FAP;//
					   this.saveFoundFigureAndPuzzleStatus
					   (this.getAuthenticatedExpertCode(httpSession),currentStatus,finishSaveFAP,"");
				   }
				   else if(FigureMPuzzlesAppController.OPERORTYPE_SAVEANDCOMMIT.equals(operatype))
				   { /**若专家在发布图谜字谜时保存并提交到公司审批人员处审批时，当前图谜字谜是有两个状态变化的，
				   		所以要向图谜字谜状态跟踪表中放入两条数据
				   	*/
					  //1.保存“专家保存图谜字谜”状态跟踪数据
					   this.saveFoundFigureAndPuzzleStatus
					   (this.getAuthenticatedExpertCode(httpSession),FigureMPuzzlesAppController.EXPERT_SAVE_FAP,finishSaveFAP,"");
					   
					   //2.保存“提交公司人员审批”状态跟踪数据；若提交图谜字谜到公司人员处审批，则当前状态为提交后的状态
					   this.saveFoundFigureAndPuzzleStatus
					   (this.getAuthenticatedExpertCode(httpSession),currentStatus,finishSaveFAP,"");
				   }
				   
				   resultBean.setMessage("添加图谜字谜成功!");
				   resultBean.setStatus("success");
				   
				   //日志输出
				   logger.info("添加图谜字谜--图谜字谜code="+fApCode+"--操作人="+this.getAuthenticatedExpertCode(httpSession));
			   }
			   
			   
			   
			   return resultBean;
			   
			}
		 
		 
		 /**
		  * 
		 * @Title: deleteFigureAndPuzzle 
		 * @Description: 专家删除图谜字谜数据
		 * @param @param ids
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月13日 下午2:59:10 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/deleteFigureAndPuzzle", method = RequestMethod.POST)
			public @ResponseBody ResultBean  deleteFigureAndPuzzle(
					@RequestParam(value="ids",required=false) String[] ids,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 
			 //获取项目根路径
			 String savePath = httpSession.getServletContext().getRealPath("");
		     savePath = savePath +File.separator+ "uploadFAPAppImg"+File.separator;
			 
			 
			 FigureAndPuzzles figureAndPuzzles = null;
			 List<FigureAndPuzzleAndArea> figureAndPuzzleAndAreas = 
					 new ArrayList<FigureAndPuzzleAndArea>();
			 
			 for (String id : ids) 
				{
				 	figureAndPuzzles = figureAndPuzzlesService.getFigureAndPuzzlesById(id);
				 	if(null != figureAndPuzzles)
				 	{
				 		//删除关联区域表数据
				 		List<FigureAndPuzzleAndArea> beforeFigureAndPuzzleAndAreas = 
				 				figureAndPuzzles.getFigureAndPuzzleAndAreas();
				 		for (FigureAndPuzzleAndArea figureAndPuzzleAndArea : beforeFigureAndPuzzleAndAreas) 
				 		{
				 			figureAndPuzzleAndAreaService.delete(figureAndPuzzleAndArea);
				 			logger.info("删除图谜字谜与区域关联数据--关联id="+figureAndPuzzleAndArea.getId()+"==图谜字谜id="+id+""
				 					+ "--操作人="+this.getAuthenticatedExpertCode(httpSession));
						}
				 		figureAndPuzzles.setFigureAndPuzzleAndAreas(figureAndPuzzleAndAreas);//将要删除的图谜字谜数据的区域取消关联
				 		//对图谜字谜数据做删除处理
//				 		figureAndPuzzles.setFigureAndPuzzleAndAreas(null);
				 		figureAndPuzzles.setFloorOfFigureAndPuzzles(null);
				 		figureAndPuzzles.setPuzzlesType(null);
				 		figureAndPuzzles.setIsDeleted(Constants.IS_DELETED);
				 		figureAndPuzzles.setModify(this.getAuthenticatedExpertCode(httpSession));
				 		figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 		figureAndPuzzlesService.update(figureAndPuzzles);
				 		
				 		
				 		 //日志输出
						 logger.info("删除图谜字谜--图谜字谜id="+id+"--操作人="+this.getAuthenticatedExpertCode(httpSession));
					   
				 	}
				}
			 String returnMsg = "删除成功!";
			 resultBean.setStatus("success");
			 resultBean.setMessage(returnMsg);
			 
			 return resultBean;
		 }
		 
		 /**
		  * 
		 * @Title: deleteFigureAndPuzzleByCompany 
		 * @Description: 公司删除图谜字谜数据 
		 * @param @param ids
		 * @param @param model
		 * @param @param httpSession
		 * @param @return
		 * @param @throws Exception    设定文件 
		 * @author banna
		 * @date 2016年10月19日 下午3:27:19 
		 * @return ResultBean    返回类型 
		 * @throws
		  */
		 @RequestMapping(value = "/deleteFigureAndPuzzleByCompany", method = RequestMethod.POST)
			public @ResponseBody ResultBean  deleteFigureAndPuzzleByCompany(
					@RequestParam(value="ids",required=false) String[] ids,
					ModelMap model,HttpSession httpSession) throws Exception {
			 
			 ResultBean resultBean = new ResultBean();
			 
			 //获取项目根路径
			 String savePath = httpSession.getServletContext().getRealPath("");
		     savePath = savePath +File.separator+ "uploadFAPAppImg"+File.separator;
			 
			 
			 FigureAndPuzzles figureAndPuzzles = null;
			 List<FigureAndPuzzleAndArea> figureAndPuzzleAndAreas = 
					 new ArrayList<FigureAndPuzzleAndArea>();
			 
			 for (String id : ids) 
				{
				 	figureAndPuzzles = figureAndPuzzlesService.getFigureAndPuzzlesById(id);
				 	if(null != figureAndPuzzles)
				 	{
				 		//删除关联区域表数据
				 		List<FigureAndPuzzleAndArea> beforeFigureAndPuzzleAndAreas = 
				 				figureAndPuzzles.getFigureAndPuzzleAndAreas();
				 		for (FigureAndPuzzleAndArea figureAndPuzzleAndArea : beforeFigureAndPuzzleAndAreas) 
				 		{
				 			figureAndPuzzleAndAreaService.delete(figureAndPuzzleAndArea);
				 			logger.info("删除图谜字谜与区域关联数据--关联id="+figureAndPuzzleAndArea.getId()+"==图谜字谜id="+id+""
				 					+ "--操作人="+LoginUtils.getAuthenticatedUserCode(httpSession));
						}
				 		figureAndPuzzles.setFigureAndPuzzleAndAreas(figureAndPuzzleAndAreas);//将要删除的图谜字谜数据的区域取消关联
				 		//对图谜字谜数据做删除处理
//				 		figureAndPuzzles.setFigureAndPuzzleAndAreas(null);
				 		figureAndPuzzles.setFloorOfFigureAndPuzzles(null);
				 		figureAndPuzzles.setPuzzlesType(null);
				 		figureAndPuzzles.setIsDeleted(Constants.IS_DELETED);
				 		figureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				 		figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
				 		figureAndPuzzlesService.update(figureAndPuzzles);
				 		
				 		
				 		 //日志输出
						 logger.info("删除图谜字谜--图谜字谜id="+id+"--操作人="+LoginUtils.getAuthenticatedUserCode(httpSession));
					   
				 	}
				}
			 String returnMsg = "删除成功!";
			 resultBean.setStatus("success");
			 resultBean.setMessage(returnMsg);
			 
			 return resultBean;
		 }
		 
		 /**
		  * 
		 * @Title: saveFoundFigureAndPuzzleStatus 
		 * @Description: 保存图谜字谜状态跟踪表数据
		 * @param @param creator
		 * @param @param currentStatus
		 * @param @param figureAndPuzzles    设定文件 
		 * @author banna
		 * @date 2016年10月13日 下午2:12:24 
		 * @return void    返回类型 
		 * @throws
		  */
		 private void saveFoundFigureAndPuzzleStatus(String creator,String currentStatus,FigureAndPuzzles figureAndPuzzles,String rejectReson)
		 {
			 FoundFigureAndPuzzleStatus foundFigureAndPuzzleStatus = new FoundFigureAndPuzzleStatus();
			 foundFigureAndPuzzleStatus.setCreator(creator);
			 foundFigureAndPuzzleStatus.setStatus(currentStatus);
			 foundFigureAndPuzzleStatus.setStatusSj(new Timestamp(System.currentTimeMillis()));
			 foundFigureAndPuzzleStatus.setFigureAndPuzzles(figureAndPuzzles);
			 foundFigureAndPuzzleStatus.setReason(rejectReson);//驳回状态的驳回理由
			 foundFigureAndPuzzleStatusService.save(foundFigureAndPuzzleStatus);
		 }
		 
		 /**
		  * 
		 * @Description: 生成图谜字谜code(全局唯一)（日期+流水号）
		 * @author bann@sdfcp.com
		 * @date 2015年11月17日 下午4:09:05
		  */
		 @RequestMapping(value = "/generateFAPAppcode", method = RequestMethod.POST)
			public @ResponseBody Map<String,Object>  generateFAPAppcode(
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
			* @Description:生成图谜字谜编码 
			* //规则：年月日(yyyyMMdd)+6位流水号
			* @author bann@sdfcp.com
			* @date 2015年11月18日 上午10:31:02
			 */
			 private  synchronized String codeGenertor()
			 {
				 
				 StringBuffer fAPCode = new StringBuffer();
				//获取当前年月日
				 String date = "";
				 Date dd  = Calendar.getInstance().getTime();
				 date = DateUtil.formatDate(dd, DateUtil.FULL_DATE_FORMAT);
				 String year = date.substring(0, 4);//半包，不包括最大位数值
				 String month = date.substring(5, 7);
				 String day = date.substring(8, 10);
				 fAPCode.append(year).append(month).append(day);
				 
				 //验证当天是否已生成图谜字谜
				//放置分页参数
					Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
					
					//参数
					StringBuffer buffer = new StringBuffer();
					List<Object> params = new ArrayList<Object>();
					
		/*			//只查询未删除数据
					params.add("1");//只查询有效的数据
					buffer.append(" isDeleted = ?").append(params.size());
		*/			
					params.add(fAPCode+"%");//根据订单名称模糊查询
					buffer.append(" fAPCode like ?").append(params.size());
					
					//排序
					LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
					orderBy.put("fAPCode", "desc");//大号的code排在前面
					
					QueryResult<FigureAndPuzzles> queryResult = figureAndPuzzlesService.getFigureAndPuzzlesList
							(FigureAndPuzzles.class, buffer.toString(), params.toArray(),
							orderBy, pageable);
					
					if(queryResult.getResultList().size()>0)
					{
						String maxCode = queryResult.getResultList().get(0).getfAPCode();
						String weihao = maxCode.substring(8, maxCode.length());
						int num = Integer.parseInt(weihao);
						String newNum = (++num)+"";
						int needLen = (OrderController.SERIAL_NUM_LEN-newNum.length());
						for(int i=0;i<needLen;i++)
						{
							newNum = "0"+newNum;
						}
						fAPCode.append(newNum);
					}
					else
					{//当天还没有生成图谜字谜号
						fAPCode.append("000001");
					}
					
				 
					return fAPCode.toString();
			 }
			 
			 
			 /**
			  * 
			 * @Title: approveFigureAndPuzzle 
			 * @Description: 审批图谜字谜 
			 * @param @param fApId
			 * @param @param operortype
			 * @param @param areadata
			 * @param @param model
			 * @param @param httpSession
			 * @param @return
			 * @param @throws Exception    设定文件 
			 * @author banna
			 * @date 2016年10月13日 下午2:50:20 
			 * @return ResultBean    返回类型 
			 * @throws
			  */
			 @RequestMapping(value = "/approveFigureAndPuzzle", method = RequestMethod.POST)
				public @ResponseBody ResultBean approveFigureAndPuzzle(
						@RequestParam(value="fApId",required=false) String fApId,
						@RequestParam(value="operortype",required=false) String operortype,
						@RequestParam(value="areadata",required=false) String areadata,//绑定的区域数据list,
						@RequestParam(value="rejectReson",required=false) String rejectReson,//审批驳回理由
						ModelMap model,HttpSession httpSession) throws Exception
				{
					ResultBean resultBean = new ResultBean();
					
					FigureAndPuzzles figureAndPuzzles = figureAndPuzzlesService.getFigureAndPuzzlesById(fApId);
				    String currentStatus = figureAndPuzzles.getStatus();
				    String directFlag = "1";
				    
				    if(FigureMPuzzlesAppController.PAGE_OPERORTYPE_SAVE.equals(operortype))
				    {//专家审批通过
				    	FigureAndPuzzleNextStatus figureAndPuzzleNextStatus = 
				    			figureAndPuzzleStatusService.
				    			getFigureAndPuzzleNextStatusBycurrentStatusId(currentStatus, directFlag);
				    	currentStatus = figureAndPuzzleNextStatus.getNextStatusId();
				    	
				    	figureAndPuzzles.setModify(this.getAuthenticatedExpertCode(httpSession));
					    figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
					  //由于状态变化，将变化状态存入到状态流程跟踪表中
						this.saveFoundFigureAndPuzzleStatus
						(this.getAuthenticatedExpertCode(httpSession),currentStatus,figureAndPuzzles,"");
				    }
				    else if(FigureMPuzzlesAppController.PAGE_OPERORTYPE_PASS.equals(operortype))
				    {//公司审批图谜字谜列表审批通过
				    	FigureAndPuzzleNextStatus figureAndPuzzleNextStatus = 
				    			figureAndPuzzleStatusService.
				    			getFigureAndPuzzleNextStatusBycurrentStatusId(currentStatus, directFlag);
					   currentStatus = figureAndPuzzleNextStatus.getNextStatusId();
					  
					   //TODO:公司审批通过后，保存当前图谜字谜发布的区域
					   //TODO:提取绑定的区域数据
					   JSONObject areas = JSONObject.parseObject(areadata);
					   List<String> cityIds =  (List<String>) areas.get("keys");
					   
					   
					   //整理并放置区域数据
					   List<FigureAndPuzzleAndArea> figureAndPuzzleAndAreas
					   = new ArrayList<FigureAndPuzzleAndArea>();
					   for (String cityId : cityIds) {
						
						   FigureAndPuzzleAndArea aaa = new FigureAndPuzzleAndArea();
						   City city = cityService.getCityByCcode(cityId);
						   aaa.setFigureAndPuzzle(figureAndPuzzles);
						   aaa.setCity(city.getCcode());
						   aaa.setProvince(city.getProvinceCode());
						   
						   figureAndPuzzleAndAreaService.save(aaa);
						   
						   figureAndPuzzleAndAreas.add(aaa);
					   }
					   figureAndPuzzles.setFigureAndPuzzleAndAreas(figureAndPuzzleAndAreas);//添加图谜字谜和发布区域的关联
//					   figureAndPuzzlesService.update(figureAndPuzzles);
					   figureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					   figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
					 //由于状态变化，将变化状态存入到状态流程跟踪表中
						this.saveFoundFigureAndPuzzleStatus
						(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,figureAndPuzzles,"");
					  
				    }
				    else if(FigureMPuzzlesAppController.PAGE_OPERORTYPE_REJECT.equals(operortype))
				    {//公司审批图谜字谜列表审批驳回
				       directFlag = "0";
				       FigureAndPuzzleNextStatus figureAndPuzzleNextStatus = 
				    			figureAndPuzzleStatusService.
				    			getFigureAndPuzzleNextStatusBycurrentStatusId(currentStatus, directFlag);
					   currentStatus = figureAndPuzzleNextStatus.getNextStatusId();
					   
					   figureAndPuzzles.setRejectReason(rejectReson);//审批驳回操作，录入驳回理由
					   figureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					   figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
					 //由于状态变化，将变化状态存入到状态流程跟踪表中
						this.saveFoundFigureAndPuzzleStatus
						(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,figureAndPuzzles,rejectReson);
				    }
				    else if(FigureMPuzzlesAppController.PAGE_OPERORTYPE_STOP.equals(operortype))
				    {//公司审批列表不通过
					   currentStatus = FigureMPuzzlesAppController.FIGURE_AND_PUZZLE_STOP;//置图谜字谜状态为“不通过”的状态码
					   
					   
					   figureAndPuzzles.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
					   figureAndPuzzles.setModifyTime(new Timestamp(System.currentTimeMillis()));
					 //由于状态变化，将变化状态存入到状态流程跟踪表中
						this.saveFoundFigureAndPuzzleStatus
						(LoginUtils.getAuthenticatedUserCode(httpSession),currentStatus,figureAndPuzzles,"");
				    }
				    
				    figureAndPuzzles.setStatus(currentStatus);
				    figureAndPuzzles.setStatusTime(new Timestamp(System.currentTimeMillis()));
				    figureAndPuzzlesService.update(figureAndPuzzles);
				   
					
					resultBean.setStatus("success");
					resultBean.setMessage("审批图谜字谜成功!");
					
					return resultBean;
				}
			 
			 /**
			  * 
			 * @Title: getAreasOfFigureAndPuzzle 
			 * @Description: 获取当前图谜字谜发布的区域
			 * @param @param id
			 * @param @param model
			 * @param @param httpSession
			 * @param @return
			 * @param @throws Exception    设定文件 
			 * @author banna
			 * @date 2016年10月13日 下午3:17:03 
			 * @return List<String>    返回类型 
			 * @throws
			  */
			 @RequestMapping(value = "/getAreasOfFigureAndPuzzle", method = RequestMethod.GET)
				public @ResponseBody List<String>  getAreasOfFigureAndPuzzle(
						@RequestParam(value="id",required=false) String id,
						ModelMap model,HttpSession httpSession) throws Exception {
				 
				 FigureAndPuzzles figureAndPuzzles = figureAndPuzzlesService.getFigureAndPuzzlesById(id);
				 
				 List<FigureAndPuzzleAndArea> figureAndPuzzleAndAreas = figureAndPuzzles.getFigureAndPuzzleAndAreas();
				 
				 List<String> cityIds = new ArrayList<String>();
				 
				 for (FigureAndPuzzleAndArea figureAndPuzzleAndArea : figureAndPuzzleAndAreas) {
					
					 cityIds.add(figureAndPuzzleAndArea.getCity());
				}
				 
				 return cityIds;
				 
			 }
			 
			 /**
			  * 
			 * @Title: getPlaynumOfPlayname 
			 * @Description: 根据彩种玩法获取当前即将开奖的期号
			 * @param @param playName
			 * @param @param model
			 * @param @param httpSession
			 * @param @return
			 * @param @throws Exception    设定文件 
			 * @author banna
			 * @date 2016年10月20日 上午9:41:38 
			 * @return List<String>    返回类型 
			 * @throws
			  */
			 @RequestMapping(value = "/getPlaynumOfPlayname", method = RequestMethod.GET)
				public @ResponseBody Map<String,Object>  getPlaynumOfPlayname(
						@RequestParam(value="playName",required=false) String playName,
						ModelMap model,HttpSession httpSession) throws Exception {
				 
				 Map<String,Object> result = new HashMap<String, Object>();
				 
				 String playNum = "";//用户返回的期号
				 String issueNum = "";//最近一期已经开奖的期号后3位
				 String yearNum = "";//最近一期已经开奖的年份前4位
				 String threeDname = "3D";//3D名称
				 String ssQName = "双色球";//"双色球"名称
				 if(threeDname.equals(playName))
				 {
					 List<ThreeDTiming> threeDTimings = figureAndPuzzlesService.get3DNumKaijiang(null);
					 ThreeDTiming threeDTiming = threeDTimings.get(0);
					 yearNum = threeDTiming.getIssueNumber().substring(0, 4);
					 issueNum  = threeDTiming.getIssueNumber().substring(4, 7);
					 
				 }
				 else
					 if(ssQName.equals(playName))
					 {
						 List<ShuangSQ> shuangSQs = figureAndPuzzlesService.getShuangSQKaijiang(null);
						 ShuangSQ shuangSQ = shuangSQs.get(0);
						 yearNum = shuangSQ.getIssueNumber().substring(0, 4);
						 issueNum = shuangSQ.getIssueNumber().substring(4, 7);
					 }
				 
				 if(null != issueNum)
				 {
					 int calPlaynum = Integer.parseInt(issueNum)+1;
					 
					 String calPlaynumStr = calPlaynum+"";
					 
					 if(calPlaynumStr.length()<3)//期号流水号不足3位
					 {
						 int zeroNum = 3-calPlaynumStr.length();//需要补位的0的个数
						 for(int i=0;i<zeroNum;i++)
						 {
							 calPlaynumStr = "0" + calPlaynumStr;
						 }
					 }
					 
					 playNum = yearNum + calPlaynumStr;
				 }
				 
				 result.put("playNum", playNum);
				 
				 return result;
				 
			 }
		 
}
