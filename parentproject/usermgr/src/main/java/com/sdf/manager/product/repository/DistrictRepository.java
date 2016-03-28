package com.sdf.manager.product.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.product.entity.District;

public interface DistrictRepository extends GenericRepository<District, String> {

	/**
	 * 
	* @Description: 根据ccode查询区信息
	* @author bann@sdfcp.com
	* @date 2015年11月5日 上午9:32:22
	 */
	@Query("select u from District u where  u.acode =?1")
	public District getDistrictByAode(String acode);
	
	/**
	 * 
	 * @Title: findDistrictesOfCityCode
	 * @Description: 根据城市code获取其下属的区域信息
	 * @author:banna
	 * @return: List<District>
	 */
	@Query("select u from District u where  u.cityCode =?1")
	public List<District> findDistrictesOfCityCode(String cityCode);
}
