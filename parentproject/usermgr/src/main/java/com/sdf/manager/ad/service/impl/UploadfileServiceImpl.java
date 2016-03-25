package com.sdf.manager.ad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.repository.UploadfileRepository;
import com.sdf.manager.ad.service.UploadfileService;

@Service("uploadfileService")
@Transactional(propagation = Propagation.REQUIRED)
public class UploadfileServiceImpl implements UploadfileService {
	
	@Autowired
	private UploadfileRepository uploadfileRepository;

	public Uploadfile getUploadfileByNewsUuid(String newsUuid) {
		return uploadfileRepository.getUploadfileByNewsUuid(newsUuid);
	}

	public void save(Uploadfile entity) {

		uploadfileRepository.save(entity);
	}

	public void update(Uploadfile entity) {

		uploadfileRepository.save(entity);
	}

}
