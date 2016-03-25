package com.sdf.manager.notice.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.notice.entity.ForecastMessage;

public interface ForecastRepository extends GenericRepository<ForecastMessage, String>{

	@Query("select u from ForecastMessage u where u.isDeleted='1' and u.id =?1")
	public ForecastMessage getForecastMessageById(String id);
}
