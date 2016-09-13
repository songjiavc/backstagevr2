package com.sdf.manager.proxyFromCweb.controller;

import java.io.File;
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

import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.service.UploadfileService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.proxyFromCweb.dto.ArticleDTO;
import com.sdf.manager.proxyFromCweb.entity.Article;
import com.sdf.manager.proxyFromCweb.service.ArticleService;


@Controller
@RequestMapping("article")
public class ArticleController 
{
	
	Logger logger = LoggerFactory.getLogger(ArticleController.class);
	
	@Autowired
	private ArticleService articleService;
	
	@Autowired
	private UploadfileService uploadfileService;
	
	
	
	/**
	 * 
	 * @Title: getDetailArticle
	 * @Description: 获取文章详情
	 * @author:banna
	 * @return: ApplyProxyDTO
	 */
	@RequestMapping(value = "/getDetailArticle", method = RequestMethod.GET)
	public @ResponseBody ArticleDTO getDetailArticle(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		Article article = articleService.getArticleById(id);
		
		ArticleDTO articleDTO  = articleService.toDTO(article);
		
		
		return articleDTO;
	}
	
	/**
	 * 删除文章数据
	 * @Title: deleteApplyProxys
	 * @Description: TODO
	 * @author:banna
	 * @return: ResultBean
	 */
	 @RequestMapping(value = "/deleteArticles", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteArticles(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		//获取项目根路径
		 String savePath = httpSession.getServletContext().getRealPath("");
	     savePath = savePath +File.separator+ "uploadArticleImg"+File.separator;
	     //删除附件文件相关s
		 List<Uploadfile> uploadfiles = null;
		 File dirFile = null;
		 boolean deleteFlag = false;//删除附件flag
		//删除附件文件相关e
		 
		 Article article;
		 for (String id : ids) 
			{
			 	article = articleService.getArticleById(id);
			 	if(null != article)
			 	{
			 		article.setIsDeleted("0");
			 		article.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		article.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		articleService.update(article);
			 		
			 		//删除附件s
			 		//1.获取附件
			 		if(null != article.getImg() && !"".equals(article.getImg()))
			 		{
			 			 uploadfiles = uploadfileService.getUploadfilesByNewsUuid(article.getImg());
				 		//2.删除附件
			 			 Uploadfile uploadfile = null;
			 			 for(int m=0;m<uploadfiles.size();m++)
			 			 {
			 				uploadfile = uploadfiles.get(m);
			 				dirFile = new File(savePath+uploadfile.getUploadRealName());
			 				logger.info("待删除文件路径："+dirFile);
					        // 如果dir对应的文件不存在，或者不是一个目录，则退出
				        	deleteFlag = dirFile.delete();
				        	if(deleteFlag)
				        	{//删除附件(清空附件关联newsUuid)
				        		uploadfile.setNewsUuid("");
				        		uploadfileService.update(uploadfile);
				        		logger.info("删除附件数据--附件id="+uploadfile.getId()+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				        	}
				        	else
				        	{
				        		 logger.error("网站文章id为："+article.getId()+"的数据没有文件");
				        	}
			 				 
			 			 }
				 		
			 		}
			 		
			      //删除附件e
			 		
			 		 //日志输出
					 logger.info("删除文章数据id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 /**
	  * 
	  * 保存或修改文章数据
	  * @param id
	  * @param title
	  * @param content
	  * @param relatedProblems
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
		public @ResponseBody ResultBean saveOrUpdate(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="title",required=false) String title,
				@RequestParam(value="content",required=false) String content,//字段长度过长时，使用post方法提交，可以正常将内容提交到后台
				@RequestParam(value="img",required=false) String img,//图片对应的upload表的newsUuid
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		  Article article = articleService.getArticleById(id);
		   
		   
		   if(null != article)
		   {
			   article.setTitle(title);
			   article.setContent(content);
			   article.setImg(img);
			   article.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   article.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
				
			   
			   articleService.update(article);
			   
			   resultBean.setMessage("修改文章数据成功!");
			   resultBean.setStatus("success");
			   //日志输出
				 logger.info("修改文章数据id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   article = new Article();
			   article.setTitle(title);
			   article.setContent(content);
			   article.setImg(img);
			   
			   article.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   article.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   
			   article.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   article.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   article.setIsDeleted("1");
			   
			   articleService.save(article);
			   
			   logger.info("添加文章数据--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
			   resultBean.setMessage("添加文章数据成功!");
			   resultBean.setStatus("success");
			   
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	
	
	/**
	 * 
	 * @Title: getArticleList
	 * @Description: 获取文章数据列表
	 * @author:banna
	 * @return: Map<String,Object>
	 */
	 @RequestMapping(value = "/getArticleList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getArticleList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="title",required=false) String title,//省份
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
			
			
			
			if(null != title&&!"".equals(title.trim()))
			{
				params.add("%"+title+"%");
				buffer.append(" and title like ?").append(params.size());
			}
			
			
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("createrTime", "desc");
			
			QueryResult<Article> queryResult = articleService.getArticleList(Article.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<Article> articles = queryResult.getResultList();
			Long totalrow = queryResult.getTotalRecord();
			
			//将实体转换为dto
			List<ArticleDTO> articleDTOs = articleService.toRDTOS(articles);
			
			returnData.put("rows", articleDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	 
	 /**
	  * 
	  * @Title: saveFujian
	  * @Description: 保存广告图片附件
	  * @author:banna
	  * @return: ResultBean
	  */
	 @RequestMapping(value = "/saveFujian", method = RequestMethod.GET)
		public @ResponseBody ResultBean  saveFujian(
				@RequestParam(value="realname",required=false) String realname,
				@RequestParam(value="filename",required=false) String filename,
				@RequestParam(value="uplId",required=false) String uplId,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 String type=getExt(filename);
		 String uploadfilepath = "/uploadArticleImg/";//文章图片路径
		 
		 Uploadfile uploadfile = new Uploadfile();
		 uploadfile.setNewsUuid(uplId);
		 uploadfile.setUploadFileName(filename);
		 uploadfile.setUploadRealName(realname);
		 uploadfile.setUploadfilepath(uploadfilepath);
		 uploadfile.setUploadContentType(type);
		 
		 uploadfileService.save(uploadfile);
		 
		 resultBean.setStatus("success");
		 
		 return resultBean;
		 
	 }
	 
	 /**
	  * 
	  * @Title: getExt
	  * @Description: TODO
	  * @author:banna
	  * @return: String
	  */
	 private String getExt(String fileName) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
	 
	 /**
	  * 
	  * @Title: getFileOfAppad
	  * @Description: 根据附件主键获取附件信息
	  * @author:banna
	  * @return: Uploadfile
	  */
	 @RequestMapping(value = "/getFileOfAppad", method = RequestMethod.GET)
		public @ResponseBody List<Uploadfile>  getFileOfAppad(
				@RequestParam(value="uplId",required=false) String uplId,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 List<Uploadfile> uploadfiles = new ArrayList<Uploadfile>();
		 if(!"".equals(uplId))
		 {
			 uploadfiles = uploadfileService.getUploadfilesByNewsUuid(uplId);
			 
	 		 if(uploadfiles.size()==0)
			 {
				 Uploadfile uploadfile = new Uploadfile();
				 uploadfiles.add(uploadfile);
			 }
			 
		 }
		
		 
		 return uploadfiles;
	 }
	 


	 /**
	  * 删除图片附件
	  * @param id
	  * @param model
	  * @param httpSession
	  * @return
	  * @throws Exception
	  */
	 @RequestMapping(value = "/deleteImg", method = RequestMethod.GET)
		public @ResponseBody ResultBean deleteImg(
				@RequestParam(value="id",required=false) String id,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 if(null != id)
		 {
			 Uploadfile uploadfile = uploadfileService.getUploadfileById(Integer.parseInt(id));
			 
			
			 
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
				 
				 uploadfileService.delete(uploadfile);
			 }
			
			 
			 //TODO:删除文件附件图片
			 
			 resultBean.setUseFlag(true);
		 }
		
		 return resultBean;
	 }






}
