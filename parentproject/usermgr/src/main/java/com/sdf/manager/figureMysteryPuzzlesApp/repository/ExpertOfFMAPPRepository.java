package com.sdf.manager.figureMysteryPuzzlesApp.repository;




import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;

public interface ExpertOfFMAPPRepository extends GenericRepository<ExpertsOfFMPAPP, String> {
	
	
	@Query("select u from ExpertsOfFMPAPP u where u.isDeleted ='1' and u.id =?1 ")
	public ExpertsOfFMPAPP getExpertsOfFMPAPPById(String id);
	
	
	
}
