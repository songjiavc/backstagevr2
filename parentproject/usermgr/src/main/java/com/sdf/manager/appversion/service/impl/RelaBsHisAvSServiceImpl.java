package com.sdf.manager.appversion.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.appversion.entity.RelaBsHisAppverAndSta;
import com.sdf.manager.appversion.repository.RelaBsHisAvSRepository;
import com.sdf.manager.appversion.service.RelaBsHisAvSService;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RelaBsHisAvSServiceImpl implements RelaBsHisAvSService {

	@Autowired
	private RelaBsHisAvSRepository relaBsHisAvSRepository;

	public void save(RelaBsHisAppverAndSta entity) {
		
		relaBsHisAvSRepository.save(entity);
	}

	public void update(RelaBsHisAppverAndSta entity) {

		relaBsHisAvSRepository.save(entity);
	}

	public RelaBsHisAppverAndSta getRelaBsHisAppverAndStaById(String id) {
		return relaBsHisAvSRepository.getRelaBsHisAppverAndStaById(id);
	}

	
	
}
