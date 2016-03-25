package com.sdf.manager.notice.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.dto.ForecastDTO;
import com.sdf.manager.notice.entity.ForecastMessage;
import com.sdf.manager.notice.repository.ForecastRepository;
import com.sdf.manager.notice.service.ForecastService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;


@Service("forecastService")
@Transactional(propagation = Propagation.REQUIRED)
public class ForecastServiceImpl implements ForecastService {

	@Autowired
	private ForecastRepository forecastRepository;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	public void save(ForecastMessage entity)
	{
		forecastRepository.save(entity);
	}

	public void update(ForecastMessage entity) {
		
		forecastRepository.save(entity);
		
	}

	public ForecastMessage getForecastMessageById(String id) {
		return forecastRepository.getForecastMessageById(id);
	}

	public QueryResult<ForecastMessage> getForecastList(
			Class<ForecastMessage> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<ForecastMessage> forecastList = forecastRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return forecastList;
	}

	public List<ForecastDTO> toRDTOS(List<ForecastMessage> entities) {
		List<ForecastDTO> dtos = new ArrayList<ForecastDTO>();
		ForecastDTO dto;
		for (ForecastMessage entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public ForecastDTO toDTO(ForecastMessage entity) {
		ForecastDTO dto = new ForecastDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getAppAreaProvince())//省级区域
			{
				if(Constants.PROVINCE_ALL.equals(entity.getAppAreaProvince()))
				{
					dto.setProvinceName(Constants.PROVINCE_ALL_NAME);
				}
				else
				{
					Province province = new Province();
					province = provinceService.getProvinceByPcode(entity.getAppAreaProvince());
					dto.setProvinceName(null != province?province.getPname():"");
				}
				
			}
			if(null != entity.getAppAreaCity())//市级区域
			{
				if(Constants.CITY_ALL.equals(entity.getAppAreaCity()))
				{
					dto.setCityName(Constants.CITY_ALL_NAME);
				}
				else
				{
					City city = new City();
					city = cityService.getCityByCcode(entity.getAppAreaCity());
					dto.setCityName(null != city?city.getCname():"");
				}
				
			}
			if(null != entity.getStartTime())
			{
				dto.setStartTimestr(DateUtil.formatTimestampToString(entity.getStartTime()));
			}
			
			if(null != entity.getEndTime())
			{
				dto.setEndTimestr(DateUtil.formatTimestampToString(entity.getEndTime()));
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}
	
	
}
