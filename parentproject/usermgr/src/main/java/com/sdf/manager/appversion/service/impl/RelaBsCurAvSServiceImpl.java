package com.sdf.manager.appversion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.appversion.entity.RelaBsCurAppverAndSta;
import com.sdf.manager.appversion.repository.RelaBsCurAvSRepository;
import com.sdf.manager.appversion.service.RelaBsCurAvSService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RelaBsCurAvSServiceImpl implements RelaBsCurAvSService {

	@Autowired
	private RelaBsCurAvSRepository relaBsCurAvSRepository;

	public void save(RelaBsCurAppverAndSta entity) {
		
		relaBsCurAvSRepository.save(entity);
	}

	public void update(RelaBsCurAppverAndSta entity) {
		relaBsCurAvSRepository.save(entity);
		
	}

	public RelaBsCurAppverAndSta getRelaBsCurAppverAndStaById(String id) {
		return relaBsCurAvSRepository.getRelaBsCurAppverAndStaById(id);
	}

	public RelaBsCurAppverAndSta getRelaBsCurAppverAndStaByStationIdAndAppId(
			String stationId, String appId) {
		return relaBsCurAvSRepository.getRelaBsCurAppverAndStaByStationIdAndAppId(stationId, appId);
	}
	
	
	
}
