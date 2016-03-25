package com.sdf.manager.companyNotice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.announcement.entity.AnnouncementAndArea;
import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.companyNotice.entity.ComnoticeAndArea;

public interface ComnoticeAndAreaRepository extends GenericRepository<ComnoticeAndArea, String>{
	@Query("select u from ComnoticeAndArea u where  u.companyNotice.id =?1")
	public List<ComnoticeAndArea> getComnoticeAndAreaById(String comnoticeId);
}
