package com.bs.outer.service.impl;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bs.outer.entity.AnnouncementReceipt;
import com.bs.outer.repository.AnnouncementReceiptRepository;
import com.bs.outer.service.AnnouncementReceiptService;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.common.util.QueryResult;

@Service("announcementReceiptService")
@Transactional(propagation = Propagation.REQUIRED)
public class AnnouncementReceiptServiceImpl implements
		AnnouncementReceiptService {

	@Autowired
	private AnnouncementReceiptRepository announcementReceiptRepository;
	
	
	public List<AnnouncementReceipt> getAnnouncementReceiptByStatus(
			String status) {
		return announcementReceiptRepository.getAnnouncementReceiptByStatus(status);
	}

	public List<AnnouncementReceipt> getAnnouncementReceiptByStatusAndEndTimeAndStationId(
			String status, Timestamp endTime, String stationId) {
		return announcementReceiptRepository.getAnnouncementReceiptByStatusAndEndTimeAndStationId(status, endTime, stationId);
	}

	public void save(AnnouncementReceipt entity) {

		announcementReceiptRepository.save(entity);
	}

	public void update(AnnouncementReceipt entity) {

		announcementReceiptRepository.save(entity);
	}
	
	public QueryResult<AnnouncementReceipt> getAnnouncementReceiptsList(Class<AnnouncementReceipt> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
		QueryResult<AnnouncementReceipt> announcementReceiptList = announcementReceiptRepository.
				getScrollDataByJpql(entityClass, whereJpql, queryParams, orderby, pageable);
		
		return announcementReceiptList;
	}

}
