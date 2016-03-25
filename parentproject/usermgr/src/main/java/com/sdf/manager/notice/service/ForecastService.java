package com.sdf.manager.notice.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.dto.ForecastDTO;
import com.sdf.manager.notice.entity.ForecastMessage;

public interface ForecastService {

	public void save(ForecastMessage entity);
	
	public void update(ForecastMessage entity);
	
	public ForecastMessage getForecastMessageById(String id);
	
	public QueryResult<ForecastMessage> getForecastList(Class<ForecastMessage> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public  List<ForecastDTO> toRDTOS(List<ForecastMessage> entities);
	
	
	public  ForecastDTO toDTO(ForecastMessage entity);
}
