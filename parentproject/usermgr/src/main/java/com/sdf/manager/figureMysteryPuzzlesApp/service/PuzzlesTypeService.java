package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.PuzzleTypeDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.PuzzlesType;

public interface PuzzlesTypeService {

	public PuzzlesType getPuzzlesTypeById(String id);
	
	public void save(PuzzlesType entity);
	
	public void update(PuzzlesType entity);
	
	
	public  PuzzleTypeDTO toDTO(PuzzlesType entity) ;
	
	public  List<PuzzleTypeDTO> toRDTOS(List<PuzzlesType> entities);
	
	public QueryResult<PuzzlesType> getPuzzlesTypeList(Class<PuzzlesType> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
}
