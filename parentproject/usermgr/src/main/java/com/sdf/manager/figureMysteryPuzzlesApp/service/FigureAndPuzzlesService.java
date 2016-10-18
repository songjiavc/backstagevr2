package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FloorOfFigureAndPuzzles;

public interface FigureAndPuzzlesService 
{
	public  FigureAndPuzzlesDTO toDTO(FigureAndPuzzles entity) ;
	
	public  List<FigureAndPuzzlesDTO> toRDTOS(List<FigureAndPuzzles> entities);
	
	public void save(FigureAndPuzzles entity);
	
	public void update(FigureAndPuzzles entity);
	
	public QueryResult<FigureAndPuzzles> getFigureAndPuzzlesList(Class<FigureAndPuzzles> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public FigureAndPuzzles getFigureAndPuzzlesById(String id);	
	
	public FigureAndPuzzles getFigureAndPuzzlesByFAPCode(String fAPCode);
}
