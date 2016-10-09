package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleAndArea;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzleAndAreaRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleAndAreaService;

@Service("figureAndPuzzleAndAreaService")
@Transactional(propagation = Propagation.REQUIRED)
public class FigureAndPuzzleAndAreaServiceImpl implements
		FigureAndPuzzleAndAreaService {
	
	@Autowired
	private FigureAndPuzzleAndAreaRepository figureAndPuzzleAndAreaRepository;

	public void save(FigureAndPuzzleAndArea entity) 
	{
		figureAndPuzzleAndAreaRepository.save(entity);
	}

	public void update(FigureAndPuzzleAndArea entity) {

		figureAndPuzzleAndAreaRepository.save(entity);
	}

	public void delete(FigureAndPuzzleAndArea entity) {

		figureAndPuzzleAndAreaRepository.delete(entity);
	}

	public List<FigureAndPuzzleAndArea> getFigureAndPuzzleAndAreaById(
			String figureAndPuzzleId) {
		return figureAndPuzzleAndAreaRepository.getFigureAndPuzzleAndAreaById(figureAndPuzzleId);
	}

}
