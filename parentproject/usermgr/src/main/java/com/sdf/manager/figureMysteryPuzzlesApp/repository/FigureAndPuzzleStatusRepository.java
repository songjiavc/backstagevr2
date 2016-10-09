package com.sdf.manager.figureMysteryPuzzlesApp.repository;


import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.ad.entity.StationAdStatus;
import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleStatus;

public interface FigureAndPuzzleStatusRepository extends GenericRepository<FigureAndPuzzleStatus, String> {
	
	/**
	 * 
	* @Description:根据状态id获取当前站点应用广告审批状态信息 
	* @author bann@sdfcp.com
	* @date 2015年11月18日 下午3:27:17
	 */
	@Query("select u from FigureAndPuzzleStatus u where u.statusId =?1")
	public FigureAndPuzzleStatus getFigureAndPuzzleStatusById(String statusId);
	
	/**
	 * 
	* @Description: 根据父级状态获取其下属的子级应用广告审批状态
	* @author bann@sdfcp.com
	* @date 2015年11月18日 下午3:27:33
	 */
	@Query("select u from FigureAndPuzzleStatus u where u.parentStatus =?1")
	public List<FigureAndPuzzleStatus> getFigureAndPuzzleStatusByParentStatus(String parentStatus);
}
