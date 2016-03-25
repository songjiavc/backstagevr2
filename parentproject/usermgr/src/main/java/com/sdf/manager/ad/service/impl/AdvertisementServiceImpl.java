package com.sdf.manager.ad.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.entity.StationAdStatus;
import com.sdf.manager.ad.repository.AdvertisementRepository;
import com.sdf.manager.ad.service.AdvertisementService;
import com.sdf.manager.ad.service.StationAdStatusService;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.order.entity.OrderStatus;

@Service("advertisementService")
@Transactional(propagation=Propagation.REQUIRED)
public class AdvertisementServiceImpl implements AdvertisementService {
	
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	@Autowired
	private StationAdStatusService stationAdStatusService;

	public Advertisement getAdvertisementById(String id) 
	{
		return advertisementRepository.getAdvertisementById(id);
	}

	public List<Advertisement> getAdvertisementByAdStatus(String adStatus) {
		return advertisementRepository.getAdvertisementByAdStatus(adStatus);
	}

	public void save(Advertisement entity) {

		advertisementRepository.save(entity);
	}

	public void update(Advertisement entity) {

		advertisementRepository.save(entity);
	}

	public QueryResult<Advertisement> getAdvertisementList(
			Class<Advertisement> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<Advertisement> advertisementList = advertisementRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return advertisementList;
	}

	public List<AdvertisementDTO> toRDTOS(List<Advertisement> entities) {
		List<AdvertisementDTO> dtos = new ArrayList<AdvertisementDTO>();
		AdvertisementDTO dto;
		for (Advertisement entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public AdvertisementDTO toDTO(Advertisement entity) {
		AdvertisementDTO dto = new AdvertisementDTO();
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
			
			if(null != entity.getAdStatus())
			{
				if(AdvertisementController.AD_STATUS_FB.equals(entity.getAdStatus()))
				{
					dto.setAdStatusName("发布");
				}
				else
					if(AdvertisementController.AD_STATUS_BC.equals(entity.getAdStatus()))
					{
						dto.setAdStatusName("保存");
					}
			}
			
			//若当前应用广告为通行证发布的，则要获取当前应用广告的状态
			if(null != entity.getStationAdStatus())
			{
				String statusName = "";
				StationAdStatus stationAdStatus = stationAdStatusService.getStationAdStatusByStatusId(entity.getStationAdStatus());
				statusName = stationAdStatus.getStatusName();
				dto.setStationAdStatusName(statusName);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

}
