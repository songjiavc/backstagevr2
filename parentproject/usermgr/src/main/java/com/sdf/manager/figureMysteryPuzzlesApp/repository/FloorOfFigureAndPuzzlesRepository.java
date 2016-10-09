package com.sdf.manager.figureMysteryPuzzlesApp.repository;




import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FloorOfFigureAndPuzzles;

/**
 * 
 * 图谜字谜底板资源库层
 * @author Administrator
 *
 */
public interface FloorOfFigureAndPuzzlesRepository extends GenericRepository<FloorOfFigureAndPuzzles, String> {
	
	
	@Query("select u from FloorOfFigureAndPuzzles u where u.isDeleted ='1' and u.id =?1 ")
	public FloorOfFigureAndPuzzles getFloorOfFigureAndPuzzlesById(String id);
	
	
	
}
