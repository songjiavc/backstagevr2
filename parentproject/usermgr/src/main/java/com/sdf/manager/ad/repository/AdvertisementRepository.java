package com.sdf.manager.ad.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.common.repository.GenericRepository;

public interface AdvertisementRepository extends GenericRepository<Advertisement, String>{

	@Query("select u from Advertisement u where u.isDeleted='1' and u.id =?1")
	public Advertisement getAdvertisementById(String id);
	
	@Query("select u from Advertisement u where u.isDeleted='1' and u.adStatus =?1")
	public List<Advertisement> getAdvertisementByAdStatus(String adStatus);
}
