package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FloorOfFigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FloorOfFigureAndPuzzles;

public interface FloorOfFigureAndPuzzlesService 
{
	public  FloorOfFigureAndPuzzlesDTO toDTO(FloorOfFigureAndPuzzles entity) ;
	
	public  List<FloorOfFigureAndPuzzlesDTO> toRDTOS(List<FloorOfFigureAndPuzzles> entities);
	
	public void save(FloorOfFigureAndPuzzles entity);
	
	public void update(FloorOfFigureAndPuzzles entity);
	
	public QueryResult<FloorOfFigureAndPuzzles> getFloorOfFigureAndPuzzlesList(Class<FloorOfFigureAndPuzzles> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public FloorOfFigureAndPuzzles getFloorOfFigureAndPuzzlesById(String id);
}
