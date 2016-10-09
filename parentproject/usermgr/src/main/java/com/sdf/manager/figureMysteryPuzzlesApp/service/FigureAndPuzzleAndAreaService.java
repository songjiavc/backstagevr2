package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.List;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleAndArea;

public interface FigureAndPuzzleAndAreaService 	
{	
	public void save(FigureAndPuzzleAndArea entity);
	
	
	public void update(FigureAndPuzzleAndArea entity);
	
	
	public void delete(FigureAndPuzzleAndArea entity);
	
	
	public List<FigureAndPuzzleAndArea> getFigureAndPuzzleAndAreaById(String figureAndPuzzleId);

}
