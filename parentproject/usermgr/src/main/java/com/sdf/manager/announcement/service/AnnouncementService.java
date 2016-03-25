package com.sdf.manager.announcement.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.announcement.dto.AnnouncementDTO;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.common.util.QueryResult;

public interface AnnouncementService {

	public void save(Announcement entity);
	
	public void update(Announcement entity);
	
	public Announcement getAnnouncementById(String id);
	
	public  List<AnnouncementDTO> toRDTOS(List<Announcement> entities);
	
	
	public  AnnouncementDTO toDTO(Announcement entity);
	
	public QueryResult<Announcement> getAnnouncementList(Class<Announcement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
}
