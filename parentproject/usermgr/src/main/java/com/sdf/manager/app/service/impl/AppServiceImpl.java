package com.sdf.manager.app.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.app.controller.AppController;
import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.repository.AppRepository;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.repository.RelaBsStaAppRepository;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;

@Service("appService")
@Transactional(propagation=Propagation.REQUIRED)
public class AppServiceImpl implements AppService {
	
	
	@Autowired
	private AppRepository appRepository;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private RelaBsStaAppRepository relaBsStaAppRepository;
	
	/**
	 * 
	* @Title: save
	* @Description: 保存应用实体内容
	* @Author : banna
	* @param @param entity    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void save(App entity)
	{
		appRepository.save(entity);
	}
	
	/**
	 * 
	* @Title: update
	* @Description: 修改应用实体内容
	* @Author : banna
	* @param @param entity    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void update(App entity)
	{
		appRepository.save(entity);
	}

	/**
	 * 
	* @Title: getAppById
	* @Description: 根据id获取应用内容
	* @Author : banna
	* @param @param id
	* @param @return    设定文件
	* @throws
	 */
	public App getAppById(String id) {
		
		App app = appRepository.getAppById(id);
		
		return app;
	}
	
	/**
	 * 
	* @Title: getAppList
	* @Description: 根据筛选条件获取应用列表数据
	* @Author : banna
	* @param @param entityClass
	* @param @param whereJpql
	* @param @param queryParams
	* @param @param orderby
	* @param @param pageable
	* @param @return    设定文件
	* @return QueryResult<App>    返回类型
	* @throws
	 */
	public QueryResult<App> getAppList(Class<App> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable)
	{
		
		QueryResult<App> appList = appRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return appList;
	}
	
	public QueryResult<App> getAppOfFufei(Class<App> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String province,String city,String lotteryType,String stationId) {
		String sql = " SELECT u.* FROM T_BS_APPLICATION u LEFT JOIN RELA_BS_STATION_AND_APP rbsaa  ON u.ID= rbsaa.APP_ID "//left join的默认属性是outer，所以left join与left outer join效果相同
						+ "	AND rbsaa.IS_DELETED='1' AND rbsaa.STATUS='1' AND rbsaa.STATION_ID='"+stationId+"' "
						+ "  LEFT JOIN T_BS_APP_PRICE_AND_AREA tbs ON u.ID=tbs.APP_ID "
						+ " WHERE u.IS_DELETED='1' AND tbs.IS_DELETED='1'  AND u.APP_STATUS='1' AND u.PROVINCE='"+province+"' AND u.CITY IN ('"+Constants.CITY_ALL+"','"+city+"') "
						+ " 	AND  tbs.CITY='"+city+"' AND tbs.UNIT_PRICE>0 AND u.LOTTERY_TYPE='"+lotteryType+"' "
						+ " AND rbsaa.ID IS NULL  ";//
		QueryResult<App> appQueryResult = appRepository.
			getScrollDataBySql(App.class,sql, queryParams, pageable);
		return appQueryResult;
	}
	
	/**
	 * 
	 * @Title: getAppOfUninstall
	 * @Description: 获取当前通行证未安装的但是可以安装的应用数据
	 * @author:banna
	 */
	public QueryResult<App> getAppOfUninstall(Class<App> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String province,String city,String lotteryType,String installappIds) {
		StringBuffer sql = new StringBuffer("  SELECT u.* FROM T_BS_APPLICATION u  WHERE u.IS_DELETED='1' "+
					 "   AND u.APP_STATUS='"+AppController.APP_STATUS_SJ+"' AND u.PROVINCE='"+province+"' AND u.CITY IN ('"+Constants.CITY_ALL+"','"+city+"')  	"+
					 "    AND u.LOTTERY_TYPE='"+lotteryType+"' ");
		if(null!=installappIds && installappIds.length()>0)
		{
			sql.append("  AND u.ID NOT IN ("+installappIds+")");
		}
		
		QueryResult<App> appQueryResult = appRepository.
			getScrollDataBySql(App.class,sql.toString(), queryParams, pageable);
		return appQueryResult;
	}
	
	public QueryResult<RelaBsStationAndApp> getAppOfXufei(Class<RelaBsStationAndApp> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String province,String city,String stationId
			,String lotteryType) {
		String sql = " SELECT tbsap.* FROM T_BS_APPLICATION u LEFT JOIN T_BS_APP_PRICE_AND_AREA tbs ON u.ID=tbs.APP_ID  "+
				"  LEFT JOIN RELA_BS_STATION_AND_APP tbsap ON u.ID=tbsap.APP_ID "+
				" WHERE u.IS_DELETED='1' AND tbs.IS_DELETED='1' AND tbsap.IS_DELETED='1' AND tbsap.STATUS='1'   "+
				" AND u.APP_STATUS='1' AND tbsap.STATION_ID='"+stationId+"'  AND u.PROVINCE='"+province+"' AND u.CITY IN ('"+Constants.CITY_ALL+"','"+city+"')  "+
				" 	AND  tbs.CITY='"+city+"' AND tbs.UNIT_PRICE>0 AND u.LOTTERY_TYPE='"+lotteryType+"'";//"AND u.LOTTERY_TYPE='"+lotteryType+"'",应用彩种的关联关系还没有连接
		QueryResult<RelaBsStationAndApp> appQueryResult = relaBsStaAppRepository.
			getScrollDataBySql(RelaBsStationAndApp.class,sql, queryParams, pageable);
		return appQueryResult;
	}
	
	public  AppDTO toDTO(App entity) {
		AppDTO dto = new AppDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getProvince())//省级区域
			{
				Province province = new Province();
				province = provinceService.getProvinceByPcode(entity.getProvince());
				
				dto.setProvinceName(null != province?province.getPname():"");
				dto.setAppNameWithProvince(entity.getAppName()+" "+(null != province?province.getPname():""));
				
			}
			if(null != entity.getCity())//市级区域
			{
				if(Constants.CITY_ALL.equals(entity.getCity()))
				{
					dto.setCityName(Constants.CITY_ALL_NAME);
				}
				else
				{
					City city = new City();
					city = cityService.getCityByCcode(entity.getCity());
					dto.setCityName(null != city?city.getCname():"");
				}
				
			}
			
			if(null != entity.getAppStatus()&& !"".equals(entity.getAppStatus()))
			{
				String appStatus = entity.getAppStatus();
				String appTypeName ="";
				if(AppController.APP_STATUS_SJ.equals(appStatus))
				{
					appTypeName = "上架";
				}
				else 
					if(AppController.APP_STATUS_DSJ.equals(appStatus))
					{
						appTypeName = "待上架";
					}
					else 
						if(AppController.APP_STATUS_XJ.equals(appStatus))
						{
							appTypeName = "下架";
						}
						else 
							if(AppController.APP_STATUS_GX.equals(appStatus))
							{
								appTypeName = "更新";
							}
				dto.setAppTypeName(appTypeName);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

	
	public  List<AppDTO> toRDTOS(List<App> entities) {
		List<AppDTO> dtos = new ArrayList<AppDTO>();
		AppDTO dto;
		for (App entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public App getAppByAppName(String appName) {
		return appRepository.getAppByAppName(appName);
	}
			
}
