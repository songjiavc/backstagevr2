package com.sdf.manager.figureMysteryPuzzlesApp.service;

import java.util.List;

import com.sdf.manager.ad.entity.StationAdNextStatus;
import com.sdf.manager.ad.entity.StationAdStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleNextStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleStatus;

public interface FigureAndPuzzleStatusService {

	/**
	 * 
	 * @Title: getFigureAndPuzzleStatusByStatusId
	 * @Description: 根据状态位获取状态详情
	 * @author:banna
	 * @return: StationAdStatus
	 */
	public FigureAndPuzzleStatus getFigureAndPuzzleStatusByStatusId(String statusId);
	
	/**
	 * 
	 * @Title: getFigureAndPuzzleStatusByParentStatus
	 * @Description: 根据父级状态获取其下属的状态
	 * @author:banna
	 * @return: List<StationAdStatus>
	 */
	public List<FigureAndPuzzleStatus> getFigureAndPuzzleStatusByParentStatus(String parentStatus);
	
	
	/**
	 * 
	 * @Title: getFigureAndPuzzleNextStatusBycurrentStatusId
	 * @Description: 根据当前状态位和审批方向标志位获取下一状态
	 * @author:banna
	 * @return: StationAdNextStatus
	 */
	public FigureAndPuzzleNextStatus getFigureAndPuzzleNextStatusBycurrentStatusId(String currentStatusId,String directionFlag);
}
