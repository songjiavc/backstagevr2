package com.sdf.manager.announcement.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.announcement.controller.AnnouncementController;
import com.sdf.manager.announcement.dto.AnnouncementDTO;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.announcement.repository.AnnouncementRepository;
import com.sdf.manager.announcement.service.AnnouncementService;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;

@Service("announcementService")
@Transactional(propagation = Propagation.REQUIRED)
public class AnnouncementServiceImpl implements AnnouncementService {
	
	@Autowired
	private AnnouncementRepository announcementRepository;

	public void save(Announcement entity) {

		announcementRepository.save(entity);
	}

	public void update(Announcement entity) {

		announcementRepository.save(entity);
	}

	public Announcement getAnnouncementById(String id) {
		return announcementRepository.getAnnouncementById(id);
	}

	public List<AnnouncementDTO> toRDTOS(List<Announcement> entities) {
		List<AnnouncementDTO> dtos = new ArrayList<AnnouncementDTO>();
		AnnouncementDTO dto;
		for (Announcement entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public AnnouncementDTO toDTO(Announcement entity) {
		AnnouncementDTO dto = new AnnouncementDTO();
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
			
			if(null != entity.getAnnounceStatus())
			{
				if(AnnouncementController.ANNOUNCEMENT_FB.equals(entity.getAnnounceStatus()))
				{
					dto.setAnnounceStatusName("发布");
				}
				else
					if(AnnouncementController.ANNOUNCEMENT_BC.equals(entity.getAnnounceStatus()))
					{
						dto.setAnnounceStatusName("保存");
					}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

	public QueryResult<Announcement> getAnnouncementList(Class<Announcement> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
		QueryResult<Announcement> announcementList = announcementRepository.
				getScrollDataByJpql(entityClass, whereJpql, queryParams, orderby, pageable);
		
		return announcementList;
	}

}
