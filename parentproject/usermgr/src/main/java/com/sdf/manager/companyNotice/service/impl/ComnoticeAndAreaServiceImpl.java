package com.sdf.manager.companyNotice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.companyNotice.entity.ComnoticeAndArea;
import com.sdf.manager.companyNotice.repository.ComnoticeAndAreaRepository;
import com.sdf.manager.companyNotice.service.ComnoticeAndAreaService;

@Service("comnoticeAndAreaService")
@Transactional(propagation=Propagation.REQUIRED)
public class ComnoticeAndAreaServiceImpl implements ComnoticeAndAreaService {
	
	@Autowired
	private ComnoticeAndAreaRepository comnoticeAndAreaRepository;

	public void save(ComnoticeAndArea entity) {

		comnoticeAndAreaRepository.save(entity);
	}

	public void update(ComnoticeAndArea entity) {

		comnoticeAndAreaRepository.save(entity);
	}

	public void delete(ComnoticeAndArea entity) {

		comnoticeAndAreaRepository.delete(entity);
	}

	public List<ComnoticeAndArea> getComnoticeAndAreaById(String comnoticeId) {
		return comnoticeAndAreaRepository.getComnoticeAndAreaById(comnoticeId);
	}

}
