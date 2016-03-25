package com.sdf.manager.appversion.service;

import com.sdf.manager.appversion.entity.RelaBsCurAppverAndSta;

public interface RelaBsCurAvSService {

	
	public void save(RelaBsCurAppverAndSta entity);
	
	public void update(RelaBsCurAppverAndSta entity);
	
	public RelaBsCurAppverAndSta getRelaBsCurAppverAndStaById(String id);
	
	/**
	 * 
	* @Title: getRelaBsCurAppverAndStaByStationIdAndAppId
	* @Description: 根据通行证id和应用id查找当前通行证是否之前使用过该应用
	* @Author : banna
	* @param @param stationId
	* @param @param appId
	* @param @return    设定文件
	* @return RelaBsCurAppverAndSta    返回类型
	* @throws
	 */
	public RelaBsCurAppverAndSta getRelaBsCurAppverAndStaByStationIdAndAppId(String stationId,String appId);
}
