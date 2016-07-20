package com.sdf.manager.weixin.controller;

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
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.weixin.dto.WXCommonProblemDTO;
import com.sdf.manager.weixin.entity.WXCommonProblem;
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
public class WeixinController 
{
	private Logger logger = LoggerFactory.getLogger(WeixinController.class);
	
	
	@Autowired
	private WeixinService weixinService;
	
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
	
	
	
}
