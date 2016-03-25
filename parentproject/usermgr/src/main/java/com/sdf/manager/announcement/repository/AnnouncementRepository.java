package com.sdf.manager.announcement.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.common.repository.GenericRepository;

public interface AnnouncementRepository extends GenericRepository<Announcement, String>{

	@Query("select u from Announcement u where u.isDeleted='1' and u.id =?1")
	public Announcement getAnnouncementById(String id);
}
