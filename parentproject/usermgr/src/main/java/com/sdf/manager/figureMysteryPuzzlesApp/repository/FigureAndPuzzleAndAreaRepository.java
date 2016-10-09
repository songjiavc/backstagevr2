package com.sdf.manager.figureMysteryPuzzlesApp.repository;




import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleAndArea;

/**
 * 图谜字谜的发布区域实体的操作资源库层
 * @author Administrator
 *
 */
public interface FigureAndPuzzleAndAreaRepository extends GenericRepository<FigureAndPuzzleAndArea, String> {
	
	
	@Query("select u from FigureAndPuzzleAndArea u where  u.figureAndPuzzle.id =?1")
	public List<FigureAndPuzzleAndArea> getFigureAndPuzzleAndAreaById(String figureAndPuzzleId);
	
	
	
}
