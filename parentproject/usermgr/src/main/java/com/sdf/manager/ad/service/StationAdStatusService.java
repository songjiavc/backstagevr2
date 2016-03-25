package com.sdf.manager.ad.service;

import java.util.List;

import com.sdf.manager.ad.entity.StationAdNextStatus;
import com.sdf.manager.ad.entity.StationAdStatus;

public interface StationAdStatusService {

	/**
	 * 
	 * @Title: getStationAdStatusByStatusId
	 * @Description: 根据状态位获取状态详情
	 * @author:banna
	 * @return: StationAdStatus
	 */
	public StationAdStatus getStationAdStatusByStatusId(String statusId);
	
	/**
	 * 
	 * @Title: getStationAdStatusByParentStatus
	 * @Description: 根据父级状态获取其下属的状态
	 * @author:banna
	 * @return: List<StationAdStatus>
	 */
	public List<StationAdStatus> getStationAdStatusByParentStatus(String parentStatus);
	
	
	/**
	 * 
	 * @Title: getStationAdNextStatusBycurrentStatusId
	 * @Description: 根据当前状态位和审批方向标志位获取下一状态
	 * @author:banna
	 * @return: StationAdNextStatus
	 */
	public StationAdNextStatus getStationAdNextStatusBycurrentStatusId(String currentStatusId,String directionFlag);
}
