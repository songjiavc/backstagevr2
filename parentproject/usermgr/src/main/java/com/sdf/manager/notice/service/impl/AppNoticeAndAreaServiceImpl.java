package com.sdf.manager.notice.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.notice.entity.AppNoticeAndArea;
import com.sdf.manager.notice.repository.AppNoticeAndAreaRepository;
import com.sdf.manager.notice.service.AppNoticeAndAreaService;

@Service("appNoticeAndAreaService")
@Transactional(propagation=Propagation.REQUIRED)
public class AppNoticeAndAreaServiceImpl implements AppNoticeAndAreaService {
	
	@Autowired
	private AppNoticeAndAreaRepository appNoticeAndAreaRepository;

	public void save(AppNoticeAndArea entity) {

		appNoticeAndAreaRepository.save(entity);
	}

	public void update(AppNoticeAndArea entity) {

		appNoticeAndAreaRepository.save(entity);
	}

	public void delete(AppNoticeAndArea entity) {

		appNoticeAndAreaRepository.delete(entity);
	}

	public List<AppNoticeAndArea> getAppNoticeAndAreaById(String noticeId) {
		return appNoticeAndAreaRepository.getAppNoticeAndAreaById(noticeId);
	}

}
