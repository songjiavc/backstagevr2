package com.sdf.manager.appUnitPrice.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.appUnitPrice.dto.AppUnitPriceDTO;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.entity.UserYearDiscount;
import com.sdf.manager.common.util.QueryResult;

public interface AppUPriceService {

	public void save(AppUnitPrice entity);
	
	public void update(AppUnitPrice entity);
	
	public AppUnitPrice getAppUnitPriceById(String id);
	
	public QueryResult<AppUnitPrice> getAppUnitPriceList(Class<AppUnitPrice> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public  List<AppUnitPriceDTO> toRDTOS(List<AppUnitPrice> entities);
	
	
	public  AppUnitPriceDTO toDTO(AppUnitPrice entity);
	
	/**
	 * 
	* @Title: findAll
	* @Description: 获取所有的年限折扣数据
	* @param @return    设定文件
	* @return List<UserYearDiscount>    返回类型
	* @author banna
	* @throws
	 */
	public List<UserYearDiscount> findAll();
	
	/**
	 * 
	* @Title: getUserYearDiscountById
	* @Description: 根据id获取使用年限折扣数据
	* @param @param id
	* @param @return    设定文件
	* @return UserYearDiscount    返回类型
	* @author banna
	* @throws
	 */
	public UserYearDiscount getUserYearDiscountById(String id);
	
	public AppUnitPrice getAppUnitPriceByAppIdAndProvinceAndCity(String appId,String province,String city);
	
	/**
	 * 
	 * @Title: getAppUnitPriceByAppIdAndProvinceAndCityNotType
	 * @Description: 获取没有单价类型区分的单价数据
	 * @author:banna
	 * @return: AppUnitPrice
	 */
	public AppUnitPrice getAppUnitPriceByAppIdAndProvinceAndCityNotType(String appId,String province,String city);
}
