package com.sdf.manager.figureMysteryPuzzlesApp.repository;




import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;

/**
 * 图谜字谜实体资源库层
 * @author Administrator
 *
 */
public interface FigureAndPuzzlesRepository extends GenericRepository<FigureAndPuzzles, String> {
	
	
	@Query("select u from FigureAndPuzzles u where u.isDeleted ='1' and u.id =?1 ")
	public FigureAndPuzzles getFigureAndPuzzlesById(String id);
	
	@Query("select u from FigureAndPuzzles u where u.isDeleted ='1' and u.fAPCode =?1 ")
	public FigureAndPuzzles getFigureAndPuzzlesByFAPCode(String fAPCode);
	
}
