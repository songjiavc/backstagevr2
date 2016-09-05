package com.sdf.manager.ad.service;

import java.util.List;

import com.sdf.manager.ad.entity.Uploadfile;

public interface UploadfileService {

	public Uploadfile getUploadfileByNewsUuid(String newsUuid);
	
	public void save(Uploadfile entity);
	
	public void update(Uploadfile entity);
	
	public void delete(Uploadfile entity);
	
	public Uploadfile getUploadfileById(int id);
	
	public List<Uploadfile> getUploadfilesByNewsUuid(String newsUuid);
	
	
}
