package com.sdf.manager.figureMysteryPuzzlesApp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAPuzzleUploadfile;

public interface FigureAndPuzzleUploadfileRepository extends GenericRepository<FigureAPuzzleUploadfile, String>{

	@Query("select u from FigureAPuzzleUploadfile u where u.isDeleted='1' and  u.newsUuid =?1")
	public FigureAPuzzleUploadfile getFigureAPuzzleUploadfileByNewsUuid(String newsUuid);
	
	@Query("select u from FigureAPuzzleUploadfile u where u.isDeleted='1' and  u.newsUuid =?1")
	public List<FigureAPuzzleUploadfile> getFigureAPuzzleUploadfilesByNewsUuid(String newsUuid);
	
	@Query("select u from FigureAPuzzleUploadfile u where u.isDeleted='1' and  u.id =?1")
	public FigureAPuzzleUploadfile getFigureAPuzzleUploadfileById(int id);
}
