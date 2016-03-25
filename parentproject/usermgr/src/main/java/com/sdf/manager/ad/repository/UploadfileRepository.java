package com.sdf.manager.ad.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.common.repository.GenericRepository;

public interface UploadfileRepository extends GenericRepository<Uploadfile, String>{

	@Query("select u from Uploadfile u where  u.newsUuid =?1")
	public Uploadfile getUploadfileByNewsUuid(String newsUuid);
}
