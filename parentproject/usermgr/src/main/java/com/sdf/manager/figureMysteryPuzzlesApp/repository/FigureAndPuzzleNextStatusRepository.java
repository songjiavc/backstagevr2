package com.sdf.manager.figureMysteryPuzzlesApp.repository;




import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleNextStatus;

public interface FigureAndPuzzleNextStatusRepository extends GenericRepository<FigureAndPuzzleNextStatus, String> {
	
	
	/**
	 * 
	* @Description: 根据当前状态和方向值获取下一状态
	* @author bann@sdfcp.com
	* @date 2015年11月19日 下午1:28:44
	 */
	@Query("select u from FigureAndPuzzleNextStatus u where u.currentStatusId =?1 and u.directionFlag =?2 ")
	public FigureAndPuzzleNextStatus getFigureAndPuzzleNextStatusBycurrentStatusId(String currentStatusId,String directionFlag);
	
	
	
}
