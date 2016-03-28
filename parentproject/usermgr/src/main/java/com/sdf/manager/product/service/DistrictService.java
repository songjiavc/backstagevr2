package com.sdf.manager.product.service;

import java.util.List;

import com.sdf.manager.product.entity.District;

public interface DistrictService {

	/**
	 * 
	* @Description: 根据省份id查询市数据 
	* @author bann@sdfcp.com
	* @date 2015年11月4日 上午9:14:52
	 */
	public List<District> findDistrictesOfCity(String cityId);
	
	/**
	 * 
	* @Description: 根据cityCode查询城市信息 
	* @author bann@sdfcp.com
	* @date 2015年11月5日 上午9:31:11
	 */
	public District getDistrictByAcode(String acode);
}
