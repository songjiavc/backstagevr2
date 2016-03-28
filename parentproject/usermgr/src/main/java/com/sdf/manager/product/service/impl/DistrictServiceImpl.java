package com.sdf.manager.product.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.product.entity.District;
import com.sdf.manager.product.repository.DistrictRepository;
import com.sdf.manager.product.service.DistrictService;

@Service("districtService")
@Transactional(propagation= Propagation.REQUIRED)
public class DistrictServiceImpl implements DistrictService {
	
	@Autowired
	private DistrictRepository districtRepository;

	public List<District> findDistrictesOfCity(String cityId) {
		return districtRepository.findDistrictesOfCityCode(cityId);
	}

	public District getDistrictByAcode(String acode) {
		return districtRepository.getDistrictByAode(acode);
	}

}
