package com.sdf.manager.appversion.service;

import com.sdf.manager.appversion.entity.RelaBsHisAppverAndSta;

public interface RelaBsHisAvSService {

	public void save(RelaBsHisAppverAndSta entity);
	
	public void update(RelaBsHisAppverAndSta entity);
	
	public RelaBsHisAppverAndSta getRelaBsHisAppverAndStaById(String id);
}
