package com.sdf.manager.ad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.AppAdAndArea;
import com.sdf.manager.common.repository.GenericRepository;

public interface AppAdAndAreaRepository extends GenericRepository<AppAdAndArea, String>{

	@Query("select u from AppAdAndArea u where  u.advertisement.id =?1")
	public List<AppAdAndArea> getAppAdAndAreaById(String adId);
}
