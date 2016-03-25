package com.sdf.manager.ad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.entity.StationAdNextStatus;
import com.sdf.manager.ad.entity.StationAdStatus;
import com.sdf.manager.ad.repository.StationAdNextStatusRepository;
import com.sdf.manager.ad.repository.StationAdStatusRepository;
import com.sdf.manager.ad.service.StationAdStatusService;

@Service("stationAdStatusService")
@Transactional(propagation=Propagation.REQUIRED)
public class StationAdStatusServiceImpl implements StationAdStatusService {
	
	@Autowired
	private StationAdStatusRepository stationAdStatusRepository;
	
	@Autowired
	private StationAdNextStatusRepository stationAdNextStatusRepository;

	public StationAdStatus getStationAdStatusByStatusId(String statusId) {
		return stationAdStatusRepository.getStationAdStatusById(statusId);
	}

	public List<StationAdStatus> getStationAdStatusByParentStatus(
			String parentStatus) {
		return stationAdStatusRepository.getStationAdStatusByParentStatus(parentStatus);
	}

	
	public StationAdNextStatus getStationAdNextStatusBycurrentStatusId(
			String currentStatusId, String directionFlag) {
		return stationAdNextStatusRepository.getStationAdNextStatusBycurrentStatusId(currentStatusId, directionFlag);
	}

}
