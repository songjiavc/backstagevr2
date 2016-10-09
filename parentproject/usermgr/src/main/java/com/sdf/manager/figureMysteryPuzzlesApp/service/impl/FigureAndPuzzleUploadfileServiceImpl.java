package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAPuzzleUploadfile;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzleUploadfileRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleUploadfileService;

@Service("figureAndPuzzleUploadfileService")
@Transactional(propagation=Propagation.REQUIRED)
public class FigureAndPuzzleUploadfileServiceImpl implements
		FigureAndPuzzleUploadfileService {
	
	@Autowired
	private FigureAndPuzzleUploadfileRepository figureAndPuzzleUploadfileRepository;

	public FigureAPuzzleUploadfile getFigureAPuzzleUploadfileByNewsUuid(
			String newsUuid) {
		return figureAndPuzzleUploadfileRepository.getFigureAPuzzleUploadfileByNewsUuid(newsUuid);
	}

	public void save(FigureAPuzzleUploadfile entity) {

		figureAndPuzzleUploadfileRepository.save(entity);
	}

	public void update(FigureAPuzzleUploadfile entity) {
		figureAndPuzzleUploadfileRepository.save(entity);

	}

	public void delete(FigureAPuzzleUploadfile entity) {
		figureAndPuzzleUploadfileRepository.delete(entity);

	}

	public FigureAPuzzleUploadfile getFigureAPuzzleUploadfileById(int id) {
		return figureAndPuzzleUploadfileRepository.getFigureAPuzzleUploadfileById(id);
	}

	public List<FigureAPuzzleUploadfile> getFigureAPuzzleUploadfilesByNewsUuid(
			String newsUuid) {
		return figureAndPuzzleUploadfileRepository.getFigureAPuzzleUploadfilesByNewsUuid(newsUuid);
	}

}
