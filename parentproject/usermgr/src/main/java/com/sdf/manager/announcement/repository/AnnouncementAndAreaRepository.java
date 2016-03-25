package com.sdf.manager.announcement.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.announcement.entity.AnnouncementAndArea;
import com.sdf.manager.common.repository.GenericRepository;

public interface AnnouncementAndAreaRepository extends GenericRepository<AnnouncementAndArea, String>{

	@Query("select u from AnnouncementAndArea u where  u.announcement.id =?1")
	public List<AnnouncementAndArea> getAnnouncementAndAreaById(String announcementId);
}
