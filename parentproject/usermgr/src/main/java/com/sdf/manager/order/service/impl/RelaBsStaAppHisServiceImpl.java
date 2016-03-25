package com.sdf.manager.order.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.order.repository.RelaBsStaAppHisRepository;
import com.sdf.manager.order.service.RelaBsStaAppHisService;

@Service("relaBsStaAppHisService")
@Transactional(propagation = Propagation.REQUIRED)
public class RelaBsStaAppHisServiceImpl implements RelaBsStaAppHisService {

	@Autowired
	private RelaBsStaAppHisRepository relaBsStaAppHisRepository;
	
	public void save(RelaBsStationAndAppHis entity) {

		relaBsStaAppHisRepository.save(entity);
	}

	public void update(RelaBsStationAndAppHis entity) {

		relaBsStaAppHisRepository.save(entity);
	}

}
