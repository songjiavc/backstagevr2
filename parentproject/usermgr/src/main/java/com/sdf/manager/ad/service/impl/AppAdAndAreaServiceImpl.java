package com.sdf.manager.ad.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.entity.AppAdAndArea;
import com.sdf.manager.ad.repository.AppAdAndAreaRepository;
import com.sdf.manager.ad.service.AppAdAndAreaService;

@Service("appAdAndAreaService")
@Transactional(propagation=Propagation.REQUIRED)
public class AppAdAndAreaServiceImpl implements AppAdAndAreaService {
	
	@Autowired
	private AppAdAndAreaRepository appAdAndAreaRepository;

	public void save(AppAdAndArea entity) {

		appAdAndAreaRepository.save(entity);
	}

	public void update(AppAdAndArea entity) {
		appAdAndAreaRepository.save(entity);

	}

	public void delete(AppAdAndArea entity) {
		appAdAndAreaRepository.delete(entity);

	}

	public List<AppAdAndArea> getAppAdAndAreaById(String adId) {
		return appAdAndAreaRepository.getAppAdAndAreaById(adId);
	}

}
