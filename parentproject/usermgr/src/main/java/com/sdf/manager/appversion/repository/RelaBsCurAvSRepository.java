package com.sdf.manager.appversion.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.appversion.entity.RelaBsCurAppverAndSta;
import com.sdf.manager.common.repository.GenericRepository;

public interface RelaBsCurAvSRepository extends GenericRepository<RelaBsCurAppverAndSta, String>{

	
	@Query("select u from RelaBsCurAppverAndSta u where u.isDeleted='1' and u.id =?1")
	public RelaBsCurAppverAndSta getRelaBsCurAppverAndStaById(String id);
	
	@Query("select u from RelaBsCurAppverAndSta u where u.isDeleted='1' and u.station.id =?1 and u.app.id =?2")
	public RelaBsCurAppverAndSta getRelaBsCurAppverAndStaByStationIdAndAppId(String stationId,String appId);
}
