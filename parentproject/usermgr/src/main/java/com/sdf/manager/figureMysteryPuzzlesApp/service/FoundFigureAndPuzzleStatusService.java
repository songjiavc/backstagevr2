package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.List;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FoundFigureAndPuzzleStatus;

public interface FoundFigureAndPuzzleStatusService {

	public FoundFigureAndPuzzleStatus getFoundFigureAndPuzzleStatusById(String id);
	
	public List<FoundFigureAndPuzzleStatus> getFoundFigureAndPuzzleStatusByFigureAndPuzzlesId(String orderId);
	
	public void save(FoundFigureAndPuzzleStatus entity);
}
