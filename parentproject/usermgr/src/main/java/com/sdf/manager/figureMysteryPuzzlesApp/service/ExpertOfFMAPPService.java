package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.ExpertsOfFMPAPPDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;

public interface ExpertOfFMAPPService 
{

	public  ExpertsOfFMPAPPDTO toDTO(ExpertsOfFMPAPP entity) ;
	
	public  List<ExpertsOfFMPAPPDTO> toRDTOS(List<ExpertsOfFMPAPP> entities);
	
	public void save(ExpertsOfFMPAPP entity);
	
	public void update(ExpertsOfFMPAPP entity);
	
	public QueryResult<ExpertsOfFMPAPP> getExpertsOfFMPAPPList(Class<ExpertsOfFMPAPP> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public ExpertsOfFMPAPP getExpertsOfFMPAPPById(String id);
}
