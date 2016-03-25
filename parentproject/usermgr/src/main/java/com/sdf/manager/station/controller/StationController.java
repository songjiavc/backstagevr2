package com.sdf.manager.station.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
import com.sdf.manager.appversion.dto.AppversionDTO;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.appversion.entity.RelaBsCurAppverAndSta;
import com.sdf.manager.appversion.service.AppversionService;
import com.sdf.manager.appversion.service.RelaBsCurAvSService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.exception.BizException;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.LoginUtils;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.station.application.dto.StationDto;
import com.sdf.manager.station.application.dto.StationFormDto;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.user.entity.Role;
import com.sdf.manager.user.service.UserService;


/** 
  * @ClassName: StationController 
  * @Description: 站点管理控制层
  * @author songj@sdfcp.com
  * @date 2015年11月13日 上午9:09:45 
  *  
  */
@Controller
@RequestMapping("/station")
public class StationController {
	
		@Autowired
		private UserService userService;
	
		@Autowired
		private StationService stationService;
	
		@Autowired
		private ProvinceService provinceService;
	
		@Autowired
		private CityService cityService;
		
		@Autowired
		private AppversionService appversionService;
		
		@Autowired
		private AppService appService;
		
		@Autowired
		private RelaBsCurAvSService relaBsCurAvSService;
	 
	 	/** 
	 	  * @Description: 站点管理菜单入口
	 	  * @author songj@sdfcp.com
	 	  * @date 2015年12月9日 上午9:35:52 
	 	  * @param request
	 	  * @param model
	 	  * @param httpSession
	 	  * @return 
	 	  */
	 	@RequestMapping("/stationmanager.action")
		public String initStationManager(HttpServletRequest request,ModelMap model,HttpSession httpSession) {
	 		boolean flag = false;
	 		String userId = LoginUtils.getAuthenticatedUserId(httpSession);
	 		List<Role> roles = userService.findRolesByUserId(userId);
	 		for(Role role : roles){
	 			if(Constants.ROLE_SCDL_CODE.equals(role.getCode())){
	 				flag = true;
	 			}
	 		}
	 		request.setAttribute("flag", flag);
	 		request.setAttribute("userId", userId);
	 		return "station/stationmanager";
		}
	 
	 
	 /**
	 * 
	 * @param userName
	 * @param password
	 * @param model
	 * @return
	 * @throws Exception
	 */ 
	@SuppressWarnings("finally")
	@RequestMapping(value = "/saveOrUpdate", method = RequestMethod.POST)
	public @ResponseBody ResultBean saveOrUpdate(
			StationFormDto stationFormDto,
			ModelMap model,HttpSession httpSession)   {
			ResultBean resultBean = new ResultBean();
			try{
				String userId = LoginUtils.getAuthenticatedUserCode(httpSession);
				stationService.saveOrUpdate(stationFormDto,userId);
				resultBean.setMessage("操作成功!");
				resultBean.setStatus("success");
			}catch(BizException bizEx){
				resultBean.setMessage(bizEx.getMessage());
				resultBean.setStatus("failure");
			}
			catch (Exception e) {
				resultBean.setMessage("操作异常!");
				resultBean.setStatus("failure");
				e.printStackTrace();
			}finally{
				return resultBean;
			}
	}
	
	@RequestMapping(value = "/getStationList", method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getStationList(
			@RequestParam(value="page",required=false) int page,
			@RequestParam(value="rows",required=false) int rows,
			@RequestParam(value="searchFormNumber",required=false) String searchFormNumber,//模糊查询所选省
			@RequestParam(value="searchFormStyle",required=false) String searchFormStyle,//模糊查询所选省
			@RequestParam(value="searchFormName",required=false) String searchFormName,//模糊查询所选省
			@RequestParam(value="searchFormTelephone",required=false) String searchFormTelephone,//模糊查询所选省
			@RequestParam(value="searchFormProvince",required=false) String searchFormProvince,//模糊查询所选省
			@RequestParam(value="searchFormCity",required=false) String searchFormCity,//模糊查询所选市
			@RequestParam(value="searchFormAgent",required=false) String searchFormAgent,//模糊查询所选市
			ModelMap model,HttpSession httpSession) throws Exception
	{
		Map<String,Object> returnData = new HashMap<String, Object>();
		Pageable pageable = new PageRequest(page-1, rows);
		//参数
		StringBuffer buffer = new StringBuffer();
		
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("id", "desc");
		List<Object> params = new ArrayList<Object>();
		//只查询未删除数据
		params.add(Constants.IS_NOT_DELETED);//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		if(null != searchFormNumber && !"".equals(searchFormNumber))
		{
			params.add(searchFormNumber);//根据站点号
			buffer.append(" and stationNumber = ?").append(params.size());
		}
		if(null != searchFormStyle && !"".equals(searchFormStyle))
		{
			params.add(searchFormStyle);//根据省份查询产品数据
			buffer.append(" and stationType = ?").append(params.size());
		}
		if(null != searchFormProvince && !"".equals(searchFormProvince)&& !Constants.PROVINCE_ALL.equals(searchFormProvince))
		{
			params.add(searchFormProvince);//根据省份查询产品数据
			buffer.append(" and provinceCode = ?").append(params.size());
		}
		if(null != searchFormCity && !"".equals(searchFormCity)&& !Constants.CITY_ALL.equals(searchFormCity))
		{
			params.add(searchFormCity);//根据省份查询产品数据
			buffer.append(" and cityCode = ?").append(params.size());
		}
		if(null != searchFormName && !"".equals(searchFormName))
		{
			params.add("%"+searchFormName+"%");//根据产品描述模糊查询产品数据
			buffer.append(" and owner  like ?").append(params.size());
		}
		
		if(null != searchFormTelephone && !"".equals(searchFormTelephone))
		{
			params.add(searchFormTelephone);//根据产品描述模糊查询产品数据
			buffer.append(" and ownerTelephone = ?").append(params.size());
		}
		
		if(null != searchFormAgent && !"".equals(searchFormAgent))
		{
			params.add(searchFormAgent);//根据产品描述模糊查询产品数据
			buffer.append(" and agentId = ?").append(params.size());
		}
		QueryResult<Station> stationList = stationService.getStationList(Station.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		List<Station> stations = stationList.getResultList();
		List<StationDto> stationDtos = this.toDtos(stations);
		Long totalrow = stationList.getTotalRecord();
		returnData.put("rows", stationDtos);
		returnData.put("total", totalrow);
		return returnData;
	}
	@RequestMapping(value = "/getStationDetail", method = RequestMethod.GET)
	public @ResponseBody StationFormDto getDetailAccount(
			@RequestParam(value="id",required=true) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		StationFormDto stationFormDto = new StationFormDto();
		Station station = stationService.getSationById(id);
		stationFormDto.setId(station.getId());
		stationFormDto.setAddFormAgent(station.getAgentId());
		stationFormDto.setAddFormStationCode(station.getCode());
		stationFormDto.setAddFormName(station.getOwner());
		stationFormDto.setAddFormStationNumber(station.getStationNumber());
		stationFormDto.setAddFormProvince(station.getProvinceCode());
		stationFormDto.setAddFormCity(station.getCityCode());
		stationFormDto.setAddFormRegion(station.getRegionCode());
		stationFormDto.setAddFormStationStyle(station.getStationType());
		stationFormDto.setAddFormAddress(station.getAddress());
		stationFormDto.setAddFormTelephone(station.getOwnerTelephone());
		stationFormDto.setPassword(station.getPassword());
		stationFormDto.setConfirmPassword(station.getPassword());
		return stationFormDto;
	}
	
	
	/**
	  * 
	 * @Description: 校验产品编码唯一性
	 * @author bann@sdfcp.com
	 * @date 2015年11月4日 下午2:58:17
	  */
	 @RequestMapping(value = "/checkCode", method = RequestMethod.POST)
	public @ResponseBody ResultBean  checkCode(
				@RequestParam(value="id",required=false) String id,
				@RequestParam(value="code",required=false) String code,
				ModelMap model,HttpSession httpSession) throws Exception {
		 
		 	ResultBean resultBean = new ResultBean ();
			
			//放置分页参数
			Pageable pageable = new PageRequest(0,10000);
			
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
			
			QueryResult<Station> plist = stationService.getStationList(Station.class, buffer.toString(), params.toArray(),
					orderBy, pageable);
			
			if(plist.getResultList().size()>0)
			{
				resultBean.setExist(true);//若查询的数据条数大于0，则当前输入值已存在，不符合唯一性校验
			}
			else
			{
				resultBean.setExist(false);
			}
			
			return resultBean;
			
		}
	
	

	@SuppressWarnings("finally")
	@RequestMapping(value = "/deleteStationByIds", method = RequestMethod.POST)
	public @ResponseBody ResultBean deleteAccount(
			@RequestParam(value="ids",required=false) String[] ids,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		try {
			String userId = LoginUtils.getAuthenticatedUserCode(httpSession);
			stationService.deleteStationByIds(ids,userId);
			resultBean.setStatus("success");
			resultBean.setMessage("删除成功!");
		}catch(BizException bizEx){
			resultBean.setStatus("failure");
			resultBean.setMessage(bizEx.getMessage());
		}catch (Exception e) {
			resultBean.setStatus("failure");
			resultBean.setMessage(e.getMessage());
		}finally{
			return resultBean;
		}
	}
	
	
	public List<StationDto> toDtos(List<Station> stations){
		List<StationDto> dtoList = new ArrayList<StationDto>();
		for(Station station : stations){
			StationDto stationDto = new StationDto();
			stationDto = this.toDto(station);
			dtoList.add(stationDto);
		}
		return dtoList;
	}
	
	public StationDto toDto(	Station station){
		StationDto stationDto = new StationDto();
		stationDto.setId(station.getId());
		stationDto.setStationCode(station.getCode());
		stationDto.setStationNumber(station.getStationNumber());
		stationDto.setName(station.getOwner());
		stationDto.setTelephone(station.getOwnerTelephone());
		stationDto.setStationStyle("1".equals(station.getStationType()) ?"体彩":"福彩");
		//处理实体中的特殊转换值
		if(null != station.getCreaterTime())//创建时间
		{
			stationDto.setCreateTime(DateUtil.formatDate(station.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
		}
		if(null != station.getProvinceCode())//省级区域
		{
			Province province = new Province();
			province = provinceService.getProvinceByPcode(station.getProvinceCode());
			stationDto.setProvince(null != province?province.getPname():"");
		}
		if(null != station.getCityCode())//市级区域
		{
			if(Constants.CITY_ALL.equals(station.getCityCode()))
			{
				stationDto.setCity(Constants.CITY_ALL_NAME);
			}
			else
			{
				City city = new City();
				city = cityService.getCityByCcode(station.getCityCode());
				stationDto.setCity(null != city?city.getCname():"");
			}
		}
		return stationDto;
	}
	
	/**
	 * 
	* @Title: getAppversOfStation
	* @Description: TODO:获取当前通行证拥有的应用版本权限
	* @Author : banna
	* @param @param id
	* @param @param model
	* @param @param httpSession
	* @param @return
	* @param @throws Exception    设定文件
	* @return List<AppversionDTO>    返回类型
	* @throws
	 */
	@RequestMapping(value = "/getAppversOfStation", method = RequestMethod.GET)
	public @ResponseBody List<AppversionDTO> getAppversOfStation(
			@RequestParam(value="id",required=true) String id,
			ModelMap model,HttpSession httpSession) throws Exception
	{
		List<AppversionDTO> appversionDTOs = new ArrayList<AppversionDTO>();
		
		Station station = stationService.getSationById(id);
		
		List<RelaBsCurAppverAndSta> relaBsCurAppverAndStas = station.getRelaBsCurAppverAndStas();
		
		List<Appversion> appversions = new ArrayList<Appversion>();
		for (RelaBsCurAppverAndSta relaBsCurAppverAndSta : relaBsCurAppverAndStas) {
		
			appversions.add(relaBsCurAppverAndSta.getAppversion());
		}
		
		appversionDTOs = appversionService.toRDTOS(appversions);
		
		
		return appversionDTOs;
	}
	
	@RequestMapping(value = "/connectAppVersions", method = RequestMethod.GET)
	public @ResponseBody ResultBean getAppversOfStation(
			@RequestParam(value="id",required=true) String id,//关联的通行证id
			@RequestParam(value="appversions",required=true) String[] appversions,//待关联的应用版本信息
			ModelMap model,HttpSession httpSession) throws Exception
	{
		ResultBean resultBean = new ResultBean();
		
		List<Appversion> appversionsList = new ArrayList<Appversion>();
		//整理关联的应用版本数据，去除应用数据
		for (String appversionId : appversions) {
			
			Appversion appversion = appversionService.getAppversionById(appversionId);
			
			if(null != appversion)
			{
				appversionsList.add(appversion);
			}
			
		}
		
		
		
		for (Appversion appversion : appversionsList) {
			
			String appId = appversion.getApp().getId();//获取当前应用版本的所属应用id
			
			App app = appService.getAppById(appId);
			
			Station station  = stationService.getSationById(id);//根据id获取当前通行证的信息
			
			//1.放入“当前通行证使用版本记录表”
			RelaBsCurAppverAndSta rcur = relaBsCurAvSService.getRelaBsCurAppverAndStaByStationIdAndAppId(id, appId);
			
			if(null != rcur)
			{//当前通行证之前使用过这个应用的其他版本
				rcur.setAppversion(appversion);
				rcur.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				rcur.setModifyTime(new Timestamp(System.currentTimeMillis()));
			}
			else
			{
				rcur = new RelaBsCurAppverAndSta();
				rcur.setApp(app);
				rcur.setAppversion(appversion);
				rcur.setStation(station);
				rcur.setSerialNum("0");
				rcur.setCreater(LoginUtils.getAuthenticatedUserCode(httpSession));
				rcur.setCreaterTime(new Timestamp(System.currentTimeMillis()));
				rcur.setModify(LoginUtils.getAuthenticatedUserCode(httpSession));
				rcur.setModifyTime(new Timestamp(System.currentTimeMillis()));
				rcur.setIsDeleted("1");//有效数据标记位
			}
			
			
			
			//2.放入“历史通行证使用版本记录表”
		}
		
		
		
		
		return resultBean;
	}
	
	
	
	
	
	
	
	
	
	
}
