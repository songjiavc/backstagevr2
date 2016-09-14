package com.sdf.manager.ad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.common.repository.GenericRepository;

public interface UploadfileRepository extends GenericRepository<Uploadfile, String>{

	@Query("select u from Uploadfile u where u.isDeleted='1' and  u.newsUuid =?1")
	public Uploadfile getUploadfileByNewsUuid(String newsUuid);
	
	@Query("select u from Uploadfile u where u.isDeleted='1' and  u.newsUuid =?1")
	public List<Uploadfile> getUploadfilesByNewsUuid(String newsUuid);
	
	@Query("select u from Uploadfile u where u.isDeleted='1' and  u.id =?1")
	public Uploadfile getUploadfileById(int id);
}
