package com.sdf.manager.notice.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.dto.NoticeDTO;
import com.sdf.manager.notice.entity.Notice;

public interface NoticeService {

	public void save(Notice entity);
	
	public void update(Notice entity);
	
	public Notice getNoticeById(String id);
	
	public QueryResult<Notice> getForecastList(Class<Notice> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public  List<NoticeDTO> toRDTOS(List<Notice> entities);
	
	
	public  NoticeDTO toDTO(Notice entity);
}
