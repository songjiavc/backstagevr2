package com.sdf.manager.weixin.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
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
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.weixin.dto.LotteryPlayBuluPlanDTO;
import com.sdf.manager.weixin.dto.LotteryPlayDTO;
import com.sdf.manager.weixin.dto.WXCommonProblemDTO;
import com.sdf.manager.weixin.entity.LotteryPlay;
import com.sdf.manager.weixin.entity.LotteryPlayBulufangan;
import com.sdf.manager.weixin.entity.WXCommonProblem;
import com.sdf.manager.weixin.service.LotteryPlayBuLuPlanService;
import com.sdf.manager.weixin.service.LotteryPlayService;
import com.sdf.manager.weixin.service.WeixinService;


/**
 * 
* @ClassName: WeixinController 
* @Description: 后台管理平台，微信管理模块 
* @author banna
* @date 2016年7月19日 上午9:58:11 
*
 */
@Controller
@RequestMapping("weixincontrol")
public class WeixinController extends GlobalExceptionHandler
{
	private Logger logger = LoggerFactory.getLogger(WeixinController.class);
	
	
	@Autowired
	private WeixinService weixinService;
	
	@Autowired
	private LotteryPlayService lotteryPlayService;
	
	@Autowired
	private LotteryPlayBuLuPlanService lotteryPlayBuLuPlanService;
	
	
	
	@Autowired
	private ProvinceService proviceService;
	
	/******1.常见问题管理模块******/
	
	/**
	 * 
	* @Title: getDetailWxcommonproblem 
	* @Description: 根据id获取微信常见问题详情dto
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @return WXCommonProblemDTO    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/getDetailWxcommonproblem", method = RequestMethod.GET)
	public @ResponseBody WXCommonProblemDTO getDetailWxcommonproblem(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		WXCommonProblem wxCommentsProblem = weixinService.getWXCommentsProblemById(id);
		 
		
		WXCommonProblemDTO wxCommonProblemDTO = weixinService.toDTO(wxCommentsProblem);
		return wxCommonProblemDTO;
	}
	
	/**
	 * 
	* @Title: getRelatedCproblems 
	* @Description: 获取当前常见问题的相关问题数据
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @return List<WXCommonProblemDTO>    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/getRelatedCproblems", method = RequestMethod.GET)
	public @ResponseBody List<WXCommonProblemDTO> getRelatedCproblems(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		WXCommonProblem wxCommentsProblem = weixinService.getWXCommentsProblemById(id);
		List<WXCommonProblemDTO> commonProblemDTOs = weixinService.toRDTOS(wxCommentsProblem.getCommentsProblems());
		
		return commonProblemDTOs;
	}
	
	/**
	 * 
	* @Title: getWXcommonproblemList 
	* @Description:获取公众号常见问题的数据
	* @param @param page
	* @param @param rows
	* @param @param title
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	@RequestMapping(value = "/getWXcommonproblemList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getWXcommonproblemList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="title",required=false) String title,
			@RequestParam(value="id",required=false) String id,
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
		if(null != title&&!"".equals(title.trim()))
		{
			params.add("%"+title+"%");
			buffer.append(" and title like ?").append(params.size());
		}
		
		if(null != id && !"".equals(id))//若查询的是当前常见问题的可以选择的相关问题列表数据时，则不查询本身
		{
			params.add(id);//只查询有效的数据
			buffer.append(" and id != ?").append(params.size());
		}
		
		
	 	
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("createrTime", "desc");
		
		QueryResult<WXCommonProblem> wxlist = weixinService.getWXCommonProblemList(WXCommonProblem.class,
				buffer.toString(), params.toArray(),orderBy, pageable);
				
		List<WXCommonProblem> wxCommentsProblems = wxlist.getResultList();
		Long totalrow = wxlist.getTotalRecord();
		
		//将实体转换为dto
		List<WXCommonProblemDTO> wxCommonProblemDTOs = weixinService.toRDTOS(wxCommentsProblems);
		
		returnData.put("rows", wxCommonProblemDTOs);
		returnData.put("total", totalrow);
	 	
	 	return returnData;
	}
	
	
	/**
	 * 
	* @Title: saveOrUpdate 
	* @Description: 保存或修改公众号常见问题数据
	* @param @param id
	* @param @param title
	* @param @param content
	* @param @param relatedProblems
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
				@RequestParam(value="title",required=false) String title,
				@RequestParam(value="content",required=false) String content,
				@RequestParam(value="relatedProblems",required=false) String[] relatedProblems,//相关问题
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   WXCommonProblem wxCommentsProblem = weixinService.getWXCommentsProblemById(id);
		   
		   
		   if(null != wxCommentsProblem)
		   {
			   wxCommentsProblem.setTitle(title);
			   wxCommentsProblem.setContent(content);
			   wxCommentsProblem.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   wxCommentsProblem.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   //TODO:关联相关问题数据
			   WXCommonProblem rewxCommentsProblem;
			   List<WXCommonProblem> relatedList = new ArrayList<WXCommonProblem>();
			   if(null != relatedProblems && relatedProblems.length>0)
			   {
				   for (String rid : relatedProblems) 
					{
					 	rewxCommentsProblem = weixinService.getWXCommentsProblemById(rid);
					 	if(null != wxCommentsProblem)
					 	{
					 		relatedList.add(rewxCommentsProblem);
					 	}
					 	
					}
			   }
				
				 wxCommentsProblem.setCommentsProblems(relatedList);
			   
			   weixinService.update(wxCommentsProblem);
			   
			   resultBean.setMessage("修改公众号常见问题数据成功!");
			   resultBean.setStatus("success");
			   //日志输出
				 logger.info("修改公众号常见问题id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   wxCommentsProblem = new WXCommonProblem();
			   wxCommentsProblem.setTitle(title);
			   wxCommentsProblem.setContent(content);
			   
			 //TODO:关联相关问题数据
			   WXCommonProblem rewxCommentsProblem;
			   List<WXCommonProblem> relatedList = new ArrayList<WXCommonProblem>();
			   if(null != relatedProblems && relatedProblems.length>0)
			   {
				   for (String rid : relatedProblems) 
					{
					 	rewxCommentsProblem = weixinService.getWXCommentsProblemById(rid);
					 	if(null != wxCommentsProblem)
					 	{
					 		relatedList.add(rewxCommentsProblem);
					 	}
					 	
					}
			   }
				
				 wxCommentsProblem.setCommentsProblems(relatedList);
			   
			   wxCommentsProblem.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   wxCommentsProblem.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   wxCommentsProblem.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   wxCommentsProblem.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   wxCommentsProblem.setIsDeleted("1");
			   
			   weixinService.save(wxCommentsProblem);
			   
			   logger.info("添加公众号常见问题数据--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
			   resultBean.setMessage("添加公众号常见问题数据成功!");
			   resultBean.setStatus("success");
			   
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	
	
	/**
	 * 
	* @Title: deleteWxcommonProblems 
	* @Description: 删除公众号常见问题数据
	* @param @param ids
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @return ResultBean    返回类型 
	* @throws
	 */
	 @RequestMapping(value = "/deleteWxcommonProblems", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteWxcommonProblems(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 WXCommonProblem wxCommentsProblem;
		 List<WXCommonProblem> clearList = new ArrayList<WXCommonProblem>();
		 for (String id : ids) 
			{
			 	wxCommentsProblem = weixinService.getWXCommentsProblemById(id);
			 	if(null != wxCommentsProblem)
			 	{
			 		wxCommentsProblem.setIsDeleted("0");
			 		wxCommentsProblem.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		wxCommentsProblem.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		wxCommentsProblem.setCommentsProblems(clearList);//删除常见问题数据的同时要删除常见问题的相关问题数据
			 		weixinService.update(wxCommentsProblem);
			 		
			 		 //日志输出
					 logger.info("删除公众号常见问题数据--id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	
	/**
	 * 
	* @Title: checkTitle 
	* @Description: 校验公众号常见问题的标题的唯一性
	* @param @param id
	* @param @param title
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件 
	* @return ResultBean    返回类型 
	* @throws
	 */
	 @RequestMapping(value = "/checkTitle", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkTitle(
				@RequestParam(value="id",required=false) String id,//用来校验通行证组名称唯一的条件
				@RequestParam(value="title",required=false) String title,
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
			
			if(null != title && !"".equals(title))
			{
				params.add(title);
				buffer.append(" and title = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<WXCommonProblem> wxQueryResult = weixinService.getWXCommonProblemList(WXCommonProblem.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
			if(wxQueryResult.getResultList().size()>0)
			{
				resultBean.setExist(true);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
			}
			else
			{
				resultBean.setExist(false);
			}
			
			return resultBean;
			
		}
	
	 
	 /******2.补录信息管理模块******/
	 
	 /**
	  * 根据id获取补录信息数据
	  * @param id
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getDetailLotteryPlay", method = RequestMethod.GET)
		public @ResponseBody LotteryPlayDTO getDetailLotteryPlay(@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			LotteryPlay lotteryPlay = lotteryPlayService.getLotteryPlayById(id);
			 
			
			LotteryPlayDTO lotteryPlayDTO = lotteryPlayService.toDTO(lotteryPlay);
			return lotteryPlayDTO;
		}
	 
	 /**
	  * 获取当前补录信息对于的补录方案id
	  * @param id
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getLotteryPlayPlanId", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getLotteryPlayPlanId(@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	Map<String,Object> result = new HashMap<String, Object>();
			LotteryPlay lotteryPlay = lotteryPlayService.getLotteryPlayById(id);
			
			if(null !=lotteryPlay && null != lotteryPlay.getLotteryPlayBulufangan())
			{
				result.put("lpBuluId", lotteryPlay.getLotteryPlayBulufangan().getId());
			}
			else
			{
				result.put("lpBuluId", "");
			}
			
			return result;
		}
	 
	 /**
	  * 获取补录信息数据列表
	  * @param page
	  * @param rows
	  * @param name
	  * @param province
	  * @param lotteryType
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getLotteryPlayList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getLotteryPlayList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="name",required=false) String name,//根据彩种名称
				@RequestParam(value="provinceCode",required=false) String provinceCode,
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种，体彩/福彩
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	Map<String,Object> returnData = new HashMap<String, Object>();
		 	
		 	/**补录的表名处理：之后实在没办法就直接写语句执行sql吧，插入补录数据**/
			
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
			
			if(null != provinceCode && !"".equals(provinceCode)&& !Constants.PROVINCE_ALL.equals(provinceCode))
			{
				params.add(provinceCode);
				buffer.append(" and province = ?").append(params.size());
			}
			
			if(null != lotteryType && !"".equals(lotteryType))
			{
				params.add(lotteryType);
				buffer.append(" and lotteryType = ?").append(params.size());
			}
			
			
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<LotteryPlay> lQueryResult = lotteryPlayService
					.getLotteryPlayList(LotteryPlay.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<LotteryPlay> list = lQueryResult.getResultList();
			Long totalrow = lQueryResult.getTotalRecord();
			
			//将实体转换为dto
			List<LotteryPlayDTO> lotteryPlayDTOs = lotteryPlayService.toRDTOS(list);
			
			returnData.put("rows", lotteryPlayDTOs);
			returnData.put("total", totalrow);
		 	
		 	
		 	return returnData;
		}
	 
	 /**
	  * 保存或修改补录信息数据
	  * @param id
	  * @param code
	  * @param name
	  * @param province
	  * @param correspondingTable
	  * @param lotteryNumber
	  * @param lotteryType
	  * @param lpbuId
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/saveOrUpdateLotteryPlay", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdateLotteryPlay(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="code",required=false) String code,
				@RequestParam(value="name",required=false) String name,
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="correspondingTable",required=false) String correspondingTable,//对应表
				@RequestParam(value="lotteryNumber",required=false) String lotteryNumber,//开奖号码个数
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种，体彩/福彩
				@RequestParam(value="lpbuId",required=false) String lpbuId,//补录方案id
				@RequestParam(value="issueNumLen",required=false) String issueNumLen,//期号长度
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   LotteryPlay lotteryPlay = lotteryPlayService.getLotteryPlayById(id);
		   
		   
		   if(null != lotteryPlay)
		   {
			   
			   lotteryPlay.setCode(code);
			   lotteryPlay.setName(name);
			   lotteryPlay.setProvince(province);
			   lotteryPlay.setCorrespondingTable(correspondingTable);
			   lotteryPlay.setLotteryNumber(lotteryNumber);
			   lotteryPlay.setLotteryType(lotteryType);
			   lotteryPlay.setIssueNumLen(issueNumLen);
			   
			   LotteryPlayBulufangan lotteryPlayBulufangan = lotteryPlayBuLuPlanService.getLotteryPlayBulufanganById(lpbuId);
			   
			   lotteryPlay.setLotteryPlayBulufangan(lotteryPlayBulufangan);//添加补录信息和补录方案的关联关系
			   lotteryPlay.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   lotteryPlay.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   
			   lotteryPlayService.update(lotteryPlay);
			   
			   
			   resultBean.setMessage("修改补录信息数据成功!");
			   resultBean.setStatus("success");
			   //日志输出
				 logger.info("修改补录信息数据id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   lotteryPlay = new LotteryPlay();
			   lotteryPlay.setCode(code);
			   lotteryPlay.setName(name);
			   lotteryPlay.setProvince(province);
			   lotteryPlay.setCorrespondingTable(correspondingTable);
			   lotteryPlay.setLotteryNumber(lotteryNumber);
			   lotteryPlay.setLotteryType(lotteryType);
			   lotteryPlay.setIssueNumLen(issueNumLen);
			   
			   LotteryPlayBulufangan lotteryPlayBulufangan = lotteryPlayBuLuPlanService.getLotteryPlayBulufanganById(lpbuId);
			   
			   lotteryPlay.setLotteryPlayBulufangan(lotteryPlayBulufangan);//添加补录信息和补录方案的关联关系
			   lotteryPlay.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   lotteryPlay.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   lotteryPlay.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   lotteryPlay.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   lotteryPlay.setIsDeleted("1");
			   
			   lotteryPlayService.save(lotteryPlay);
			   
			   
			   logger.info("添加补录信息数据--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
			   resultBean.setMessage("添加补录信息数据成功!");
			   resultBean.setStatus("success");
			   
			   
		   }
		   
		   
		   return resultBean;
		}
	 
	 
	 /**
	  * 删除补录信息数据
	  * @param ids
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/deleteLotteryPlays", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteLotteryPlays(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 LotteryPlay lotteryPlay;
		 for (String id : ids) 
			{
			 	lotteryPlay = lotteryPlayService.getLotteryPlayById(id);
			 	if(null != lotteryPlay)
			 	{
			 		lotteryPlay.setIsDeleted("0");
			 		lotteryPlay.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		lotteryPlay.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		lotteryPlay.setLotteryPlayBulufangan(null);//删除和补录方案的关联关系,置关联字段为null（多对一的删除关联关系的方式；若为多对多的关联管理则可以使用创建新的实体放入关联字段中来删除中间表的关联关系）
			 		
			 		
			 		lotteryPlayService.update(lotteryPlay);
			 		
			 		 //日志输出
					 logger.info("删除补录信息数据--id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	
	 /******3.补录方案管理模块******/
	 
	 /**
	  * 根据id获取补录方案数据
	  * @param id
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getDetailLotteryPlayBuluPlan", method = RequestMethod.GET)
		public @ResponseBody LotteryPlayBuluPlanDTO getDetailLotteryPlayBuluPlan(@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	LotteryPlayBulufangan lotteryPlayBulufangan = lotteryPlayBuLuPlanService.getLotteryPlayBulufanganById(id);
			 
			
		 	LotteryPlayBuluPlanDTO lotteryPlayBuluPlanDTO = lotteryPlayBuLuPlanService.toDTO(lotteryPlayBulufangan);
			return lotteryPlayBuluPlanDTO;
		}
	 
	 /**
	  * 获取补录方案数据列表
	  * @param page
	  * @param rows
	  * @param name
	  * @param province
	  * @param lotteryType
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/getLotteryPlayBuluPlanList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getLotteryPlayBuluPlanList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="numOrChar",required=false) String numOrChar,//根据彩种名称
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	Map<String,Object> returnData = new HashMap<String, Object>();
		 	
			
		 	//放置分页参数
			Pageable pageable = new PageRequest(page-1,rows);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			//连接查询条件
			if(null != numOrChar&&!"".equals(numOrChar.trim()))
			{
				params.add(numOrChar);
				buffer.append(" and numOrChar = ?").append(params.size());
			}
			
			
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("numOrChar", "asc");
			orderBy.put("createrTime", "desc");
			
			QueryResult<LotteryPlayBulufangan> lQueryResult = lotteryPlayBuLuPlanService
					.getLotteryPlayBulufanganList(LotteryPlayBulufangan.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<LotteryPlayBulufangan> list = lQueryResult.getResultList();
			Long totalrow = lQueryResult.getTotalRecord();
			
			//将实体转换为dto
			List<LotteryPlayBuluPlanDTO> lotteryPlayBuluPlanDTOs = lotteryPlayBuLuPlanService.toRDTOS(list);
			
			returnData.put("rows", lotteryPlayBuluPlanDTOs);
			returnData.put("total", totalrow);
		 	
		 	
		 	return returnData;
		}
	  
	 /**
	  * 
	  * 保存或修改补录方案数据
	  * @param id
	  * @param code
	  * @param name
	  * @param province
	  * @param correspondingTable
	  * @param lotteryNumber
	  * @param lotteryType
	  * @param lpbuId
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/saveOrUpdateLotteryPlayBuluPlan", method = RequestMethod.GET)
		public @ResponseBody ResultBean saveOrUpdateLotteryPlayBuluPlan(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="planName",required=false) String planName,
				@RequestParam(value="startNumber",required=false) String startNumber,
				@RequestParam(value="endNumber",required=false) String endNumber,
				@RequestParam(value="numOrChar",required=false) String numOrChar,
				@RequestParam(value="otherPlan",required=false) String otherPlan,
				@RequestParam(value="otherNum",required=false) String otherNum,
				@RequestParam(value="repeatNum",required=false) String repeatNum,//开奖号码是否重复
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   LotteryPlayBulufangan lotteryPlayBulufangan = lotteryPlayBuLuPlanService.getLotteryPlayBulufanganById(id);
		   
		   
		   if(null != lotteryPlayBulufangan)
		   {
			   lotteryPlayBulufangan.setPlanName(planName);
			   lotteryPlayBulufangan.setStartNumber(startNumber);
			   lotteryPlayBulufangan.setEndNumber(endNumber);
			   lotteryPlayBulufangan.setNumOrChar(numOrChar);
			   lotteryPlayBulufangan.setOtherPlan(otherPlan);
			   lotteryPlayBulufangan.setOtherNum(otherNum);
			   lotteryPlayBulufangan.setRepeatNum(repeatNum);
			   
			   lotteryPlayBulufangan.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   lotteryPlayBulufangan.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   
			   lotteryPlayBuLuPlanService.update(lotteryPlayBulufangan);
			   
			   
			   resultBean.setMessage("修改补录方案数据成功!");
			   resultBean.setStatus("success");
			   //日志输出
				 logger.info("修改补录方案数据id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   lotteryPlayBulufangan = new LotteryPlayBulufangan();
			   lotteryPlayBulufangan.setPlanName(planName);
			   lotteryPlayBulufangan.setStartNumber(startNumber);
			   lotteryPlayBulufangan.setEndNumber(endNumber);
			   lotteryPlayBulufangan.setNumOrChar(numOrChar);
			   lotteryPlayBulufangan.setOtherPlan(otherPlan);
			   lotteryPlayBulufangan.setOtherNum(otherNum);
			   lotteryPlayBulufangan.setRepeatNum(repeatNum);
			   
			   
			   lotteryPlayBulufangan.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   lotteryPlayBulufangan.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   lotteryPlayBulufangan.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   lotteryPlayBulufangan.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   lotteryPlayBulufangan.setIsDeleted("1");
			   
			   lotteryPlayBuLuPlanService.save(lotteryPlayBulufangan);
			   
			   
			   logger.info("添加补录方案数据--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
			   resultBean.setMessage("添加补录方案数据成功!");
			   resultBean.setStatus("success");
			   
			   
		   }
		   
		   
		   return resultBean;
		}
	 
	 /**
	  * 删除补录方案数据
	  * @param ids
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/deleteLotteryPlayBuluPlans", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteLotteryPlayBuluPlans(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 LotteryPlayBulufangan lotteryPlayBulufangan ;
		 for (String id : ids) 
			{
			 	lotteryPlayBulufangan = lotteryPlayBuLuPlanService.getLotteryPlayBulufanganById(id);
			 	if(null != lotteryPlayBulufangan)
			 	{
			 		lotteryPlayBulufangan.setIsDeleted("0");
			 		lotteryPlayBulufangan.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		lotteryPlayBulufangan.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		
			 		
			 		lotteryPlayBuLuPlanService.update(lotteryPlayBulufangan);
			 		
			 		 //日志输出
					 logger.info("删除补录方案数据--id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 校验方案名称唯一
	  * @param id
	  * @param name
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/checkPlanName", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkPlanName(
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
				buffer.append(" and planName = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<LotteryPlayBulufangan> alist = lotteryPlayBuLuPlanService.getLotteryPlayBulufanganList(LotteryPlayBulufangan.class, buffer.toString(), params.toArray(),
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
	 
	 
	 /**3.补录数据管理模块**/
	 
	 /**
		 * 获取省份数据
		 * @param provinceCode
		 * @param model
		 * @return
		 */
		@RequestMapping(value = "/getProvinceList", method = RequestMethod.POST)
		public @ResponseBody List<Province> getProvinceList(@RequestParam(value="provinceCode",required=false) String provinceCode,
				ModelMap model)
		{
			List<Province> provinces = new ArrayList<Province>();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<LotteryPlay> result = lotteryPlayService.getProvinceOfLotteryPlayList(LotteryPlay.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<LotteryPlay> lpList = result.getResultList();
			
			for (LotteryPlay lotteryPlay : lpList) 
			{
				Province province = proviceService.getProvinceByPcode(lotteryPlay.getProvince());
				provinces.add(province);
			}
			
			return provinces;
		}
		
		
		//根据省份和类型（体彩、福彩）获取关联的彩票玩法数据
		@RequestMapping(value = "/getContactLottery", method = RequestMethod.POST)
		public @ResponseBody List<LotteryPlayDTO> getContactLottery(
				@RequestParam(value="province",required=false) String province,
				@RequestParam(value="lotteryType",required=false) String lotteryType,
				ModelMap model,HttpSession httpSession) throws Exception
		{
			List<LotteryPlayDTO> lotteryPlays = new ArrayList<LotteryPlayDTO>();
			
			List<LotteryPlay> list = lotteryPlayService.getLotteryPlayByProvinceAndLotteryType(province, lotteryType);
			
			lotteryPlays = lotteryPlayService.toRDTOS(list);
			
			return lotteryPlays;
		}
		
		/**
		 * 获取补录的数据列表
		 * @param province
		 * @param lotteryType
		 * @param model
		 * @param httpSession
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/getNumofMakeupList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getNumofMakeupList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="province",required=false) String province,//省份
				@RequestParam(value="lotteryType",required=false) String lotteryType,//彩种类型
				@RequestParam(value="lotteryPlay",required=false) String lotteryPlay,//
				ModelMap model,HttpSession httpSession) throws Exception
		{
			Map<String,Object> returnData = new HashMap<String, Object>();
			
			if(null != lotteryPlay && !"".equals(lotteryPlay))
			{
				LotteryPlay lotteryPlay2 = lotteryPlayService.getLotteryPlayById(lotteryPlay);//获取补录信息数据\
				LotteryPlayBulufangan lBulufangan = lotteryPlay2.getLotteryPlayBulufangan();
				
				String lotteryNumber = lotteryPlay2.getLotteryNumber();//获取开奖号码个数,用来组装返回数据
				String numOrChar = lBulufangan.getNumOrChar();//获取方案标识
				
				
				Map<String,Object> result = lotteryPlayService.findALL(page, rows, lotteryPlay2.getCorrespondingTable());
				
				
				//组装返回数据
				List<Map<String,Object>> queryList = (List<Map<String, Object>>) result.get("resultList");
				
				List<Map<String,Object>> resultList = new ArrayList<Map<String,Object>>();//返回数据列表list
				//整理数据
				int lNum = 0;//开奖号码个数
				if(null != lotteryNumber && !"".equals(lotteryNumber))
				{
					lNum = Integer.parseInt(lotteryNumber);
				}
				
				SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FULL_DATE_FORMAT);
				
				if("0".equals(numOrChar))//数字方案
				{
					int snum = Integer.parseInt(lBulufangan.getStartNumber());//开始号码
					int endNum = Integer.parseInt(lBulufangan.getEndNumber());//结束号码
					for (Map<String, Object> data : queryList) 
					{
						Map<String , Object> resultData = new HashMap<String, Object>();
						
						Long cl = 0l;
						if(null != data.get("CREATE_TIME") && !"".equals(data.get("CREATE_TIME").toString()))
						{
							resultData.put("CREATE_TIME", data.get("CREATE_TIME").toString().substring(0, 19));
						}
						resultData.put("id", data.get("id"));
						resultData.put("ISSUE_NUMBER", data.get("ISSUE_NUMBER"));
						
						
						
						//整理开奖数据
						StringBuffer kjNum = new StringBuffer();
						int differ = snum - 0;//计算开始号码和0的差，用于生成params，即参数
						for (int i = 0 ; i < lNum ; i++) 
						{
							if(i == (lNum-1))
							{
								kjNum.append(data.get("NO"+(i+differ)));
							}
							else
							{
								kjNum.append(data.get("NO"+(i+differ)) +" ,");
							}
							
						}
						
						resultData.put("kjNum", kjNum);
						resultList.add(resultData);
					}
				}
				else
				{
					for (Map<String, Object> data : queryList) 
					{
						Map<String , Object> resultData = new HashMap<String, Object>();
						
						Long cl = 0l;
						if(null != data.get("CREATE_TIME") && !"".equals(data.get("CREATE_TIME").toString()))
						{
							resultData.put("CREATE_TIME", data.get("CREATE_TIME").toString().substring(0, 19));
						}
						resultData.put("id", data.get("id"));
						resultData.put("ISSUE_NUMBER", data.get("ISSUE_NUMBER"));
						
						//整理开奖数据
						StringBuffer kjNum = new StringBuffer();
//						int differ = snum - 0;//计算开始号码和0的差，用于生成params，即参数
						for (int i = 0 ; i < lNum ; i++) 
						{
							if(i == (lNum-1))
							{
								kjNum.append(data.get("NO"+i));
							}
							else
							{
								kjNum.append(data.get("NO"+i) +" ,");
							}
							
						}
						
						resultData.put("kjNum", kjNum);
						resultList.add(resultData);
					}
				}
				
				
				
				
				returnData.put("rows", resultList);
				returnData.put("total", result.get("totalRecord"));
			}
			
			return returnData;
		}
		
		/**
		 * 删除指定的补录数据
		 * @param ids
		 * @param model
		 * @param httpSession
		 * @return
		 * @throws Exception
		 */
		@RequestMapping(value = "/deleteNumofMakeUp", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteNumofMakeUp(
				@RequestParam(value="ids",required=false) String[] ids,
				@RequestParam(value="lotteryPlay",required=false) String lotteryPlay,
				ModelMap model,HttpSession httpSession) throws Exception 
		{
			ResultBean resultBean = new ResultBean();
			
			LotteryPlay lPlay = lotteryPlayService.getLotteryPlayById(lotteryPlay);
			String tableName = lPlay.getCorrespondingTable();//获取表名
			
			 for (String id : ids) 
				{
				 	lotteryPlayService.deleteById(tableName, id);
				}
			 String returnMsg = "删除成功!";
			 resultBean.setStatus("success");
			 resultBean.setMessage(returnMsg);
			
			return resultBean;
			
		}
	 
}




