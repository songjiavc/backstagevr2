package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleNextStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzleNextStatusRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzleStatusRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleStatusService;

@Service("figureAndPuzzleStatusService")
@Transactional(propagation=Propagation.REQUIRED)
public class FigureAndPuzzleStatusServiceImpl implements
		FigureAndPuzzleStatusService {
	
	@Autowired
	private FigureAndPuzzleStatusRepository figureAndPuzzleStatusRepository;
	
	@Autowired
	private FigureAndPuzzleNextStatusRepository figureAndPuzzleNextStatusRepository;

	public FigureAndPuzzleStatus getFigureAndPuzzleStatusByStatusId(
			String statusId) {
		return figureAndPuzzleStatusRepository.getFigureAndPuzzleStatusById(statusId);
	}

	public List<FigureAndPuzzleStatus> getFigureAndPuzzleStatusByParentStatus(
			String parentStatus) {
		return figureAndPuzzleStatusRepository.getFigureAndPuzzleStatusByParentStatus(parentStatus);
	}

	public FigureAndPuzzleNextStatus getFigureAndPuzzleNextStatusBycurrentStatusId(
			String currentStatusId, String directionFlag) {
		return figureAndPuzzleNextStatusRepository.getFigureAndPuzzleNextStatusBycurrentStatusId(currentStatusId, directionFlag);
	}

}
