package com.sdf.manager.appversion.controller;

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

import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.service.UploadfileService;
import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appversion.dto.AppversionDTO;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.appversion.service.AppversionService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.GlobalExceptionHandler;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.goods.controller.GoodsController;


@Controller
@RequestMapping("appversion")
public class AppversionController extends GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(AppversionController.class);
	
	
	@Autowired
	private AppversionService appversionService;
	
	@Autowired
	private AppService appService;
	
	@Autowired
	private UploadfileService uploadfileService;
	
	/**应用版本管理模块的默认值**/
	public static final String APP_V_STATUS_DSJ = "0";//应用版本状态(0:待上架)
	public static final String APP_V_STATUS_SJ = "1";//应用版本状态(1:上架)
	public static final String APP_V_STATUS_XJ = "2";//应用版本状态(02:下架)
	public static final String APP_V_STATUS_GX = "3";//应用版本状态(3:更新)
	
	
	/**
	 * 
	* @Title: getDetailAppversion
	* @Description:根据id获取应用版本实体数据
	* @Author : banna
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return AppversionDTO    返回类型
	* @throws
	 */
	@RequestMapping(value = "/getDetailAppversion", method = RequestMethod.GET)
	public @ResponseBody AppversionDTO getDetailAppversion(@RequestParam(value="id",required=false) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		
		Appversion appversion = appversionService.getAppversionById(id);
		
		AppversionDTO appversionDTO = appversionService.toDTO(appversion);
		
		return appversionDTO;
	}
	
	/**
	 * 
	* @Title: getAppversionList
	* @Description: 根据筛选条件获取应用版本数据
	* @Author : banna
	* @param @param page
	* @param @param rows
	* @param @param appversionCode
	* @param @param appversionType
	* @param @param appName
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	 @RequestMapping(value = "/getAppversionList", method = RequestMethod.GET)
		public @ResponseBody Map<String,Object> getAppversionList(
				@RequestParam(value="page",required=false) int page,
				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="appversionCode",required=false) String appversionCode,//应用编码
				@RequestParam(value="appversionType",required=false) String appversionType,//应用状态
				@RequestParam(value="appName",required=false) String appName,//应用版本名称
				@RequestParam(value="appId",required=false) String appId,//所属应用id
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
			if(null != appversionCode&&!"".equals(appversionCode.trim()))
			{
				params.add("%"+appversionCode+"%");
				buffer.append(" and appVersionCode like ?").append(params.size());
			}
			
			if(null != appName&&!"".equals(appName.trim()))//根据应用版本名称查询
			{
				params.add("%"+appName+"%");
				buffer.append(" and appVersionName like ?").append(params.size());
			}
		 	
			if(null != appId&&!"".equals(appId.trim()))//根据所属应用查询
			{
				params.add(appId);
				buffer.append(" and app.id = ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<Appversion> appversionlist = appversionService.getAppversionList(Appversion.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<Appversion> appsversions = appversionlist.getResultList();
			Long totalrow = appversionlist.getTotalRecord();
			
			//将实体转换为dto
			List<AppversionDTO> appvDTOs = appversionService.toRDTOS(appsversions);
			
			returnData.put("rows", appvDTOs);
			returnData.put("total", totalrow);
		 	
		 	return returnData;
		}
	
	 /**
	  * 
	 * @Title: saveOrUpdate
	 * @Description: 保存或修改应用版本数据
	 * @Author : banna
	 * @param @param id
	 * @param @param appVersionCode
	 * @param @param appVersionName
	 * @param @param versionCode
	 * @param @param appVersionUrl
	 * @param @param appVersionStatus
	 * @param @param appDeveloper
	 * @param @param appId
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
				@RequestParam(value="appVersionCode",required=false) String appVersionCode,
				@RequestParam(value="appVersionName",required=false) String appVersionName,
				@RequestParam(value="versionCode",required=false) String versionCode,
				@RequestParam(value="appVersionUrl",required=false) String appVersionUrl,
				@RequestParam(value="appVersionStatus",required=false) String appVersionStatus,
				@RequestParam(value="appDeveloper",required=false) String appDeveloper,
				@RequestParam(value="appId",required=false) String appId,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		   ResultBean resultBean = new ResultBean ();
		   
		   Appversion appversion = appversionService.getAppversionById(id);
		   
		   
		   if(null != appversion)
		   {//应用版本数据不为空，则进行修改操作
			   
			   App appParent = appService.getAppById(appId);//获取父级应用id
			   
			   appversion.setApp(appParent);//所属的应用
			   appversion.setAppVersionName(appVersionName);
			   appversion.setAppVersionCode(appVersionCode);
			   appversion.setAppVersionUrl(appVersionUrl);
			   appversion.setAppVersionStatus(appVersionStatus);
			   appversion.setAppDeveloper(appDeveloper);
			   appversion.setVersionCode(versionCode);
			   
			   appversion.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   appversion.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   
			   appversionService.update(appversion);
			   
			   resultBean.setMessage("修改应用版本信息成功!");
			   resultBean.setStatus("success");
			  
			   //日志输出
				 logger.info("修改应用版本--应用版本id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   else
		   {
			   appversion = new Appversion();
			   App appParent = appService.getAppById(appId);//获取父级应用id
			   
			   appversion.setApp(appParent);//所属的应用
			   appversion.setAppVersionName(appVersionName);
			   appversion.setAppVersionCode(appVersionCode);
			   appversion.setAppVersionUrl(appVersionUrl);
			   appversion.setAppVersionStatus(appVersionStatus);
			   appversion.setAppDeveloper(appDeveloper);
			   appversion.setVersionCode(versionCode);
			   String versionFlowId = this.generateFlowId(appId);
			   appversion.setVersionFlowId(versionFlowId);
			   
			   appversion.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
			   appversion.setCreaterTime(new Timestamp(System.currentTimeMillis()));
			   appversion.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			   appversion.setModifyTime(new Timestamp(System.currentTimeMillis()));
			   appversion.setIsDeleted("1");
			   
			   appversionService.save(appversion);
			   
			   resultBean.setMessage("添加应用版本信息成功!");
			   resultBean.setStatus("success");
			   
			 //日志输出
			logger.info("添加应用版本--应用版本code="+appVersionCode+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
			   
		   }
		   
		   
		   
		   
		   return resultBean;
		}
	 
	 
	 private String generateFlowId(String appId)
	 {
		 String versionFlowId = "";
		 
		/* Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		params.add(appId);
		buffer.append(" app.id = ?").append(params.size());
		
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("versionFlowId", "desc");//大号的versionFlowId排在前面
		
		QueryResult<Appversion> appverlist = appversionService.getAppversionList(Appversion.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
		 
		if(appverlist.getResultList().size()>0)
		{
			String maxVersionFlowId = appverlist.getResultList().get(0).getVersionFlowId();
			versionFlowId = (Integer.parseInt(maxVersionFlowId)+1)+"";
		}
		else
		{
			versionFlowId = "1";
		}*/
		 
		 versionFlowId = appversionService.findMaxVersionFlowId(appId);//根据应用id获取该应用下有效应用版本数据下的最大版本号
		 
		 if(null != versionFlowId && !"".equals(versionFlowId))
		 {
			 String maxVersionFlowId =(Integer.parseInt(versionFlowId)+1)+"";
			 versionFlowId = maxVersionFlowId;
		 }
		 else
		 {
			 versionFlowId = "1";
		 }
		 
		 return versionFlowId;
	 }
	 
	 /**
	  * 
	 * @Title: deleteAppversions
	 * @Description: 删除应用版本数据
	 * @Author : banna
	 * @param @param ids
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/deleteAppversions", method = RequestMethod.POST)
		public @ResponseBody ResultBean  deleteAppversions(
				@RequestParam(value="ids",required=false) String[] ids,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Appversion appversion;
		 for (String id : ids) 
			{
			 	appversion = appversionService.getAppversionById(id);
			 	if(null != appversion)
			 	{
			 		appversion.setIsDeleted("0");
			 		appversion.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		appversion.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		appversion.setApp(null);//删除应用版本与应用的关联关系,避免在删除应用时校验是否拥有有效的应用版本数据的时候造成错误
			 		appversionService.update(appversion);
			 		
			 		 //日志输出
					 logger.info("删除应用版本--应用版本id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "删除成功!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	 
	 /**
	  * 
	 * @Title: updateAppversionStatus
	 * @Description:更新应用版本数据状态
	 * @Author : banna
	 * @param @param ids
	 * @param @param appVerStatus
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return ResultBean    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/updateAppversionStatus", method = RequestMethod.POST)
		public @ResponseBody ResultBean  updateAppversionStatus(
				@RequestParam(value="ids",required=false) String[] ids,
				@RequestParam(value="appVerStatus",required=false) String appVerStatus,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 ResultBean resultBean = new ResultBean();
		 
		 Appversion appversion;
		 for (String id : ids) 
			{
			 	appversion = appversionService.getAppversionById(id);
			 	if(null != appversion)
			 	{
			 		appversion.setAppVersionStatus(appVerStatus);
			 		appversion.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
			 		appversion.setModifyTime(new Timestamp(System.currentTimeMillis()));
			 		appversionService.update(appversion);
			 		
			 		 //日志输出
					 logger.info("更新应用版本状态--应用版本id="+id+"--操作人="+LoginUtils.getAuthenticatedUserId(httpSession));
				   
			 	}
			}
		 String returnMsg = "更新应用版本状态!";
		 resultBean.setStatus("success");
		 resultBean.setMessage(returnMsg);
		 
		 return resultBean;
				 
		 
	 }
	 
	/**
	 * 
	* @Title: checkAppVersionName
	* @Description: 应用版本相关校验方法
	* @Author : banna
	* @param @param id
	* @param @param name
	* @param @param appId
	* @param @param versionCode
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return ResultBean    返回类型
	* @throws
	 */
	 @RequestMapping(value = "/checkAppVersionName", method = RequestMethod.POST)
		public @ResponseBody ResultBean  checkAppVersionName(
				@RequestParam(value="id",required=false) String id,//用来校验应用版本名称唯一的条件
				@RequestParam(value="name",required=false) String name,//用来校验应用版本名称唯一的条件
				@RequestParam(value="appId",required=false) String appId,//用来校验应用下的版本号唯一的条件
				@RequestParam(value="versionCode",required=false) String versionCode,//用来校验应用下的版本号唯一的条件
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
				buffer.append(" and appVersionName = ?").append(params.size());
			}
			
			
			if(null != id && !"".equals(id))
			{//校验修改中的值的唯一性
				params.add(id);
				buffer.append(" and id != ?").append(params.size());
			}
			
			if(null != versionCode)
			{//校验应用下的应用版本号的唯一性
				params.add(appId);
				buffer.append(" and app.id = ?").append(params.size());
				
				params.add(versionCode);
				buffer.append(" and versionCode = ?").append(params.size());
			}
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			
			QueryResult<Appversion> appverlist = appversionService.getAppversionList(Appversion.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(appverlist.getResultList().size()>0)
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
	 * @Title: getAppcomboList
	 * @Description: 获取应用combobox的填充值
	 * @Author : banna
	 * @param @param page
	 * @param @param rows
	 * @param @param appCode
	 * @param @param appType
	 * @param @param appName
	 * @param @param model
	 * @param @param httpSession
	 * @param @return
	 * @param @throws Exception    设定文件
	 * @return List<AppDTO>    返回类型
	 * @throws
	  */
	 @RequestMapping(value = "/getAppcomboList", method = RequestMethod.GET)
		public @ResponseBody List<AppDTO> getAppcomboList(
//				@RequestParam(value="page",required=false) int page,
//				@RequestParam(value="rows",required=false) int rows,
				@RequestParam(value="appCode",required=false) String appCode,//应用编码
				@RequestParam(value="appType",required=false) String appType,//应用状态
				@RequestParam(value="appName",required=false) String appName,//应用名称
				@RequestParam(value="isAll",required=false) boolean isAll,
				@RequestParam(value="checkCity",required=false) boolean checkCity,
				ModelMap model,HttpSession httpSession) throws Exception
		{
		 	
		 	//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			//只查询未删除数据
			params.add("1");//只查询有效的数据
			buffer.append(" isDeleted = ?").append(params.size());
			
			//连接查询条件
			if(null != appCode&&!"".equals(appCode.trim()))
			{
				params.add("%"+appCode+"%");
				buffer.append(" and appCode like ?").append(params.size());
			}
			
			if(null != appName&&!"".equals(appName.trim()))
			{
				params.add("%"+appName+"%");
				buffer.append(" and appName like ?").append(params.size());
			}
			
			if(checkCity)
			{//移除应用的“区域市”的值不是“all”的值，这类应用是已经指定了指定市级的应用，不需要特殊设置单价
				params.add(Constants.CITY_ALL);
				buffer.append(" and city = ?").append(params.size());
			}
		 	
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("id", "desc");
			
			QueryResult<App> applist = appService.getAppList(App.class,
					buffer.toString(), params.toArray(),orderBy, pageable);
					
			List<App> apps = applist.getResultList();
			Long totalrow = applist.getTotalRecord();
			
			//将实体转换为dto
			List<AppDTO> appDTOs = appService.toRDTOS(apps);
			
		 	if(isAll)
		 	{
		 		AppDTO allAppDto = new AppDTO();
		 		allAppDto.setId("");
		 		allAppDto.setAppName("全部");
		 		appDTOs.add(allAppDto);
		 	}
			
		 	return appDTOs;
		}
	 
	 /**
	  * 
	  * @Title: saveFujian
	  * @Description: 保存应用安装包附件
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
		 String uploadfilepath = "/uploadApkFile/";
		 
		 Uploadfile uploadfile = uploadfileService.getUploadfileByNewsUuid(uplId);
		 
		 //因为一个应用只能有一个图片附件，所以当这个upId有数据的话就进行修改操作，如果没有数据就创建数据
		 if(null != uploadfile)
		 {
			 uploadfile.setUploadFileName(filename);
			 uploadfile.setUploadRealName(realname);
			 uploadfile.setUploadfilepath(uploadfilepath);
			 uploadfile.setUploadContentType(type);
			 
			 uploadfileService.update(uploadfile);
		 }
		 else
		 {
			 uploadfile = new Uploadfile();
			 uploadfile.setNewsUuid(uplId);
			 uploadfile.setUploadFileName(filename);
			 uploadfile.setUploadRealName(realname);
			 uploadfile.setUploadfilepath(uploadfilepath);
			 uploadfile.setUploadContentType(type);
			 
			 uploadfileService.save(uploadfile);
		 }
		 
		 resultBean.setStatus("success");
		 
		 return resultBean;
		 
	 }
	 
	 private String getExt(String fileName) {
			return fileName.substring(fileName.lastIndexOf("."));
		}
	 
	
	/**
	 * 
	* @Title: generateAppVercode
	* @Description: 生成应用版本的编码
	* @Author : banna
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return Map<String,Object>    返回类型
	* @throws
	 */
	 @RequestMapping(value = "/generateAppVercode", method = RequestMethod.POST)
		public @ResponseBody Map<String,Object>  generateAppVercode(
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
	* @Description:生成应用版本编码 
	* //规则：年月日(yyyyMMdd)+6位流水号
	* @author bann@sdfcp.com
	* @date 2015年11月18日 上午10:31:02
	 */
	 private  synchronized String codeGenertor()
	 {
		 
		 StringBuffer appVerCode = new StringBuffer("AppVersion");
		//获取当前年月日
		 String date = "";
		 Date dd  = Calendar.getInstance().getTime();
		 date = DateUtil.formatDate(dd, DateUtil.FULL_DATE_FORMAT);
		 String year = date.substring(0, 4);//半包，不包括最大位数值
		 String month = date.substring(5, 7);
		 String day = date.substring(8, 10);
		 appVerCode.append(year).append(month).append(day);
		 
		 //验证当天是否已生成应用
		//放置分页参数
			Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
			
			//参数
			StringBuffer buffer = new StringBuffer();
			List<Object> params = new ArrayList<Object>();
			
			params.add(appVerCode+"%");//根据应用版本编码模糊查询
			buffer.append(" appVersionCode like ?").append(params.size());
			
			//排序
			LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
			orderBy.put("appVersionCode", "desc");//大号的code排在前面
			
			QueryResult<Appversion> appverlist = appversionService.getAppversionList(Appversion.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(appverlist.getResultList().size()>0)
			{
				String maxCode = appverlist.getResultList().get(0).getAppVersionCode();
				String weihao = maxCode.substring(18, maxCode.length());
				int num = Integer.parseInt(weihao);
				String newNum = (++num)+"";
				int needLen = (GoodsController.SERIAL_NUM_LEN-newNum.length());
				for(int i=0;i<needLen;i++)
				{
					newNum = "0"+newNum;
				}
				appVerCode.append(newNum);
			}
			else
			{//当天还没有生成应用版本编码
				appVerCode.append("000001");
			}
			
		 
			return appVerCode.toString();
	 }

}
