package com.sdf.manager.ad.service;

import java.util.List;

import com.sdf.manager.ad.entity.FoundStationAdStatus;

public interface FoundStationAdStatusService {

	public FoundStationAdStatus getFoundStationAdStatusById(String id);
	
	public List<FoundStationAdStatus> getFoundStationAdStatusByOrderId(String orderId);
	
	public void save(FoundStationAdStatus entity);
}
