package com.sdf.manager.announcement.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.announcement.entity.AnnouncementAndArea;
import com.sdf.manager.announcement.repository.AnnouncementAndAreaRepository;
import com.sdf.manager.announcement.service.AnnouncementAndAreaService;

@Service("announcementAndAreaService")
@Transactional(propagation=Propagation.REQUIRED)
public class AnnouncementAndAreaServiceImpl implements
		AnnouncementAndAreaService {
	
	@Autowired
	private AnnouncementAndAreaRepository announcementAndAreaRepository;

	public void save(AnnouncementAndArea entity) {

		announcementAndAreaRepository.save(entity);
	}

	public void update(AnnouncementAndArea entity) {

		announcementAndAreaRepository.save(entity);
	}

	public void delete(AnnouncementAndArea entity) {

		announcementAndAreaRepository.delete(entity);
	}

	public List<AnnouncementAndArea> getAnnouncementAndAreaById(
			String announcementId) {
		return announcementAndAreaRepository.getAnnouncementAndAreaById(announcementId);
	}

}
