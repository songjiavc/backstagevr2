package com.sdf.manager.ad.repository;




import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.StationAdNextStatus;
import com.sdf.manager.common.repository.GenericRepository;

public interface StationAdNextStatusRepository extends GenericRepository<StationAdNextStatus, String> {
	
	
	/**
	 * 
	* @Description: 根据当前状态和方向值获取下一状态
	* @author bann@sdfcp.com
	* @date 2015年11月19日 下午1:28:44
	 */
	@Query("select u from StationAdNextStatus u where u.currentStatusId =?1 and u.directionFlag =?2 ")
	public StationAdNextStatus getStationAdNextStatusBycurrentStatusId(String currentStatusId,String directionFlag);
	
	
	
}
