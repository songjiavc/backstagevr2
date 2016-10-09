package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FoundFigureAndPuzzleStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FoundFigureAndPuzzleStatusRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FoundFigureAndPuzzleStatusService;

@Service("foundFigureAndPuzzleStatusService")
@Transactional(propagation=Propagation.REQUIRED)
public class FoundFigureAndPuzzleStatusServiceImpl implements
		FoundFigureAndPuzzleStatusService {
	
	@Autowired
	private FoundFigureAndPuzzleStatusRepository foundFigureAndPuzzleStatusRepository;

	public FoundFigureAndPuzzleStatus getFoundFigureAndPuzzleStatusById(
			String id) {
		return foundFigureAndPuzzleStatusRepository.getFoundFigureAndPuzzleStatusById(id);
	}

	public List<FoundFigureAndPuzzleStatus> getFoundFigureAndPuzzleStatusByFigureAndPuzzlesId(
			String figureAndPuzzlesId) {
		return foundFigureAndPuzzleStatusRepository.getFoundFigureAndPuzzleStatusByFigureAndPuzzlesId(figureAndPuzzlesId);
	}

	public void save(FoundFigureAndPuzzleStatus entity) {
		
		foundFigureAndPuzzleStatusRepository.save(entity);

	}

}
