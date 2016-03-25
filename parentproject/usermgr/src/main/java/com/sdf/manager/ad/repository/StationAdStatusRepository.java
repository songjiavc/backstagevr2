package com.sdf.manager.ad.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.StationAdStatus;
import com.sdf.manager.common.repository.GenericRepository;

public interface StationAdStatusRepository extends GenericRepository<StationAdStatus, String> {
	
	/**
	 * 
	* @Description:根据状态id获取当前站点应用广告审批状态信息 
	* @author bann@sdfcp.com
	* @date 2015年11月18日 下午3:27:17
	 */
	@Query("select u from StationAdStatus u where u.statusId =?1")
	public StationAdStatus getStationAdStatusById(String statusId);
	
	/**
	 * 
	* @Description: 根据父级状态获取其下属的子级应用广告审批状态
	* @author bann@sdfcp.com
	* @date 2015年11月18日 下午3:27:33
	 */
	@Query("select u from StationAdStatus u where u.parentStatus =?1")
	public List<StationAdStatus> getStationAdStatusByParentStatus(String parentStatus);
}
