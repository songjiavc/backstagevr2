package com.sdf.manager.appversion.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.appversion.entity.RelaBsHisAppverAndSta;
import com.sdf.manager.common.repository.GenericRepository;

public interface RelaBsHisAvSRepository extends GenericRepository<RelaBsHisAppverAndSta, String>{

	
	@Query("select u from RelaBsHisAppverAndSta u where u.isDeleted='1' and u.id =?1")
	public RelaBsHisAppverAndSta getRelaBsHisAppverAndStaById(String id);
}
