package com.sdf.manager.order.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;

public interface RelaBsStaAppHisRepository extends GenericRepository<RelaBsStationAndAppHis, String> {
	
	@Query("select u from RelaBsStationAndAppHis u where  u.isDeleted ='1' and  u.station.id =?1")
	public List<RelaBsStationAndAppHis> getRelaBsStationAndAppHisByOrderId(String stationId);
	
}
