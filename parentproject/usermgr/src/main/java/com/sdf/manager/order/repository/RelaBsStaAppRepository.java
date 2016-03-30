package com.sdf.manager.order.repository;


import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.order.entity.RelaBsStationAndApp;

public interface RelaBsStaAppRepository extends GenericRepository<RelaBsStationAndApp, String> {
	
	@Query("select u from RelaBsStationAndApp u where  u.isDeleted ='1' and  u.station.id =?1")
	public List<RelaBsStationAndApp> getRelaBsStationAndAppByStationId(String stationId);
	
	@Query("select u from RelaBsStationAndApp u where  u.isDeleted ='1' and u.status = '1' and  u.station.id =?1 and  u.app.id =?2")
	public RelaBsStationAndApp getRelaBsStationAndAppByStationIdAndAppId(String stationId,String appId);
	
	/**
	 * 
	 * @Title: getRelaBsStationAndAppByStatusAndEndTime
	 * @Description: TODO:获取有效的且正在使用的且有效结束时间小于传入参数的通行证和应用的数据
	 * @author:banna
	 * @return: List<RelaBsStationAndApp>
	 */
	@Query("select u from RelaBsStationAndApp u where  u.isDeleted ='1' and  u.status =?1 and u.endTime<?2")
	public List<RelaBsStationAndApp> getRelaBsStationAndAppByStatusAndEndTime(String status,Timestamp endTime);
	
}
