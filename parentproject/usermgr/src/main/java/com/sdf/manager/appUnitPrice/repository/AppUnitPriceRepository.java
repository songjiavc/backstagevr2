package com.sdf.manager.appUnitPrice.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.common.repository.GenericRepository;

public interface AppUnitPriceRepository extends GenericRepository<AppUnitPrice, String>{

	@Query("select u from AppUnitPrice u where u.isDeleted='1' and u.id =?1")
	public AppUnitPrice getAppUnitPriceById(String id);
	
	/**
	 *
	 * @Title: getAppUnitPriceByAppIdAndProvinceAndCityNotType
	 * @Description: 获取没有priceType区别的数据
	 * @author:banna
	 * @return: AppUnitPrice
	 */
	@Query("select u from AppUnitPrice u where u.isDeleted='1' and u.app.id =?1 and u.province =?2 and u.city =?3")
	public AppUnitPrice getAppUnitPriceByAppIdAndProvinceAndCityNotType(String appId,String province,String city);
	
	@Query("select u from AppUnitPrice u where u.isDeleted='1' and u.priceType='1' and u.app.id =?1 and u.province =?2 and u.city =?3")
	public AppUnitPrice getAppUnitPriceByAppIdAndProvinceAndCity(String appId,String province,String city);
}
