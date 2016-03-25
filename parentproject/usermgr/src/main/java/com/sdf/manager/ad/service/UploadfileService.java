package com.sdf.manager.ad.service;

import com.sdf.manager.ad.entity.Uploadfile;

public interface UploadfileService {

	public Uploadfile getUploadfileByNewsUuid(String newsUuid);
	
	public void save(Uploadfile entity);
	
	public void update(Uploadfile entity);
	
	
}
