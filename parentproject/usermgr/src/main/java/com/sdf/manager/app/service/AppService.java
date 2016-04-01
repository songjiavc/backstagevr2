package com.sdf.manager.app.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.order.entity.RelaBsStationAndApp;

public interface AppService {

	public void save(App entity);
	
	public void update(App entity);
	
	
	public App getAppById(String id);
	
	
	public  List<AppDTO> toRDTOS(List<App> entities);
	
	
	public  AppDTO toDTO(App entity);
	
	public QueryResult<App> getAppList(Class<App> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	/**
	 * 
	 * @Title: getAppOfFufei
	 * @Description: 获取当前通行证所在区域的付费应用
	 * @author:banna
	 * @return: QueryResult<App>
	 */
	public QueryResult<App> getAppOfFufei(Class<App> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String province,String city,String lotteryType);
	
	/**
	 * 
	 * @Title: getAppOfXufei
	 * @Description: 获取当前通行证所在区域的续费应用
	 * @author:banna
	 * @return: QueryResult<App>
	 */
	public QueryResult<RelaBsStationAndApp> getAppOfXufei(Class<RelaBsStationAndApp> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String province,String city,String stationId,String lotteryType);
	
	/**
	 * 
	 * @Title: getAppOfUninstall
	 * @Description: 获取符合当前区域和彩种条件的但是并未在当前一体机安装的应用数据
	 * @author:banna
	 * @return: QueryResult<App>
	 */
	public QueryResult<App> getAppOfUninstall(Class<App> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String province,String city,String lotteryType,String installappIds);
	
}
