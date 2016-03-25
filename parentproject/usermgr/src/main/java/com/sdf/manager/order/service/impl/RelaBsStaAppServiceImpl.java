package com.sdf.manager.order.service.impl;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.repository.RelaBsStaAppRepository;
import com.sdf.manager.order.service.RelaBsStaAppService;

@Service("relaBsStaAppService")
@Transactional(propagation = Propagation.REQUIRED)
public class RelaBsStaAppServiceImpl implements RelaBsStaAppService {

	@Autowired
	private RelaBsStaAppRepository relaBsStaAppRepository;
	
	public void save(RelaBsStationAndApp entity) {
		relaBsStaAppRepository.save(entity);
	}

	public void update(RelaBsStationAndApp entity) {
		relaBsStaAppRepository.save(entity);

	}

	public List<RelaBsStationAndApp> getRelaBsStationAndAppByStationId(
			String stationId) {
		return relaBsStaAppRepository.getRelaBsStationAndAppByStationId(stationId);
	}

	public RelaBsStationAndApp getRelaBsStationAndAppByStationIdAndAppId(
			String stationId, String appId) {
		return relaBsStaAppRepository.getRelaBsStationAndAppByStationIdAndAppId(stationId, appId);
	}

	public QueryResult<RelaBsStationAndApp> getRelaBsStationAndAppList(
			Class<RelaBsStationAndApp> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		
		QueryResult<RelaBsStationAndApp> relaSAndApplist = relaBsStaAppRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		return relaSAndApplist;
	}

	public List<RelaBsStationAndApp> getRelaBsStationAndAppByStatusAndEndTime(
			String status, Timestamp endTime) {
		return relaBsStaAppRepository.getRelaBsStationAndAppByStatusAndEndTime(status, endTime);
	}

}
