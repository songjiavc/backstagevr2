package com.sdf.manager.ad.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.FoundStationAdStatus;
import com.sdf.manager.common.repository.GenericRepository;

public interface FoundStationAdStatusRepository extends GenericRepository<FoundStationAdStatus, String> {
	
	/**
	 * 
	* @Description:根据id获取状态详情
	* @author bann@sdfcp.com
	* @date 2015年11月18日 下午3:27:17
	 */
	@Query("select u from FoundStationAdStatus u where u.id =?1")
	public FoundStationAdStatus getFoundStationAdStatusId(String id);
	
	
	/**
	 * 
	* @Description:根据应用广告id获取当前站点应用广告的所有状态流程 
	* @author bann@sdfcp.com
	* @date 2015年11月19日 上午9:19:49
	 */
	@Query("select u from FoundStationAdStatus u where u.advertisement.id =?1")
	public List<FoundStationAdStatus> getFoundStationAdStatusByOrderId(String adId);
	
}
