package com.sdf.manager.ad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.entity.FoundStationAdStatus;
import com.sdf.manager.ad.repository.FoundStationAdStatusRepository;
import com.sdf.manager.ad.service.FoundStationAdStatusService;

@Service("foundStationAdStatusService")
@Transactional(propagation=Propagation.REQUIRED)
public class FoundStationAdStatusServiceImpl implements FoundStationAdStatusService 
{
  
	@Autowired
	private FoundStationAdStatusRepository foundStationAdStatusRepository;
	
	public FoundStationAdStatus getFoundStationAdStatusById(String id) {
		return foundStationAdStatusRepository.getFoundStationAdStatusId(id);
	}

	public List<FoundStationAdStatus> getFoundStationAdStatusByOrderId(
			String orderId) {
		return foundStationAdStatusRepository.getFoundStationAdStatusByOrderId(orderId);
	}

	public void save(FoundStationAdStatus entity) {
		
		foundStationAdStatusRepository.save(entity);
	}

}
