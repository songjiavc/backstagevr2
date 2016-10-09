package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.List;

import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAPuzzleUploadfile;

public interface FigureAndPuzzleUploadfileService {

	public FigureAPuzzleUploadfile getFigureAPuzzleUploadfileByNewsUuid(String newsUuid);
	
	public void save(FigureAPuzzleUploadfile entity);
	
	public void update(FigureAPuzzleUploadfile entity);
	
	public void delete(FigureAPuzzleUploadfile entity);
	
	public FigureAPuzzleUploadfile getFigureAPuzzleUploadfileById(int id);
	
	public List<FigureAPuzzleUploadfile> getFigureAPuzzleUploadfilesByNewsUuid(String newsUuid);
	
	
}
