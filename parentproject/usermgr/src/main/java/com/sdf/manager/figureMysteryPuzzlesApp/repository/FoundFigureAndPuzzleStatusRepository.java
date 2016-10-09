package com.sdf.manager.figureMysteryPuzzlesApp.repository;



import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FoundFigureAndPuzzleStatus;

public interface FoundFigureAndPuzzleStatusRepository extends GenericRepository<FoundFigureAndPuzzleStatus, String> {
	
	/**
	 * 
	* @Description:根据id获取状态详情
	* @author bann@sdfcp.com
	* @date 2015年11月18日 下午3:27:17
	 */
	@Query("select u from FoundFigureAndPuzzleStatus u where u.id =?1")
	public FoundFigureAndPuzzleStatus getFoundFigureAndPuzzleStatusById(String id);
	
	
	/**
	 * 
	* @Description:根据图谜字谜id获取当前专家发布的图谜字谜的所有状态流程 
	* @author bann@sdfcp.com
	* @date 2015年11月19日 上午9:19:49
	 */
	@Query("select u from FoundFigureAndPuzzleStatus u where u.figureAndPuzzles.id =?1")
	public List<FoundFigureAndPuzzleStatus> getFoundFigureAndPuzzleStatusByFigureAndPuzzlesId(String figureAndPuzzlesId);
	
}
