package com.sdf.manager.figureMysteryPuzzlesApp.repository;




import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.PuzzlesType;

public interface PuzzleTypeRepository extends GenericRepository<PuzzlesType, String> {
	
	
	@Query("select u from PuzzlesType u where u.isDeleted ='1' and u.id =?1 ")
	public PuzzlesType getPuzzlesTypeById(String id);
	
	
	
}
