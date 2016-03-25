package com.sdf.manager.notice.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.controller.NoticeController;
import com.sdf.manager.notice.dto.NoticeDTO;
import com.sdf.manager.notice.dto.NoticeDTO;
import com.sdf.manager.notice.entity.ForecastMessage;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.repository.NoticeRepository;
import com.sdf.manager.notice.service.NoticeService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;

@Service("noticeService")
@Transactional(propagation = Propagation.REQUIRED)
public class NoticeServiceImpl implements NoticeService {
	
	@Autowired
	private NoticeRepository noticeRepository;

	public void save(Notice entity) {

		noticeRepository.save(entity);
	}

	public void update(Notice entity) {

		noticeRepository.save(entity);
	}

	public Notice getNoticeById(String id) {
		return noticeRepository.getNoticeById(id);
	}

	public QueryResult<Notice> getForecastList(Class<Notice> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
		QueryResult<Notice> noticeList = noticeRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return noticeList;
	}

	public List<NoticeDTO> toRDTOS(List<Notice> entities) {
		List<NoticeDTO> dtos = new ArrayList<NoticeDTO>();
		NoticeDTO dto;
		for (Notice entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public NoticeDTO toDTO(Notice entity) {
		NoticeDTO dto = new NoticeDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getStartTime())
			{
				dto.setStartTimestr(DateUtil.formatTimestampToString(entity.getStartTime()));
			}
			
			if(null != entity.getEndTime())
			{
				dto.setEndTimestr(DateUtil.formatTimestampToString(entity.getEndTime()));
			}
			
			if(null != entity.getNoticeStatus())
			{
				if(NoticeController.NOTICE_STATUS_FB.equals(entity.getNoticeStatus()))
				{
					dto.setNoticeStatusName("发布");
				}
				else
					if(NoticeController.NOTICE_STATUS_BC.equals(entity.getNoticeStatus()))
					{
						dto.setNoticeStatusName("保存");
					}
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

}
