package com.sdf.manager.order.service;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.entity.RelaBsStationAndApp;

public interface RelaBsStaAppService {

	public void save(RelaBsStationAndApp entity);
	
	
	public void update(RelaBsStationAndApp entity);
	
	/**
	 * 
	* @Title: getRelaBsStationAndAppByOrderId
	* @Description: 根据
	* @param @param stationId
	* @param @return    设定文件
	* @return List<RelaBsStationAndApp>    返回类型
	* @author banna
	* @throws
	 */
	public List<RelaBsStationAndApp> getRelaBsStationAndAppByStationId(String stationId);
	
	public RelaBsStationAndApp getRelaBsStationAndAppByStationIdAndAppId(String stationId,String appId);
	
	public QueryResult<RelaBsStationAndApp> getRelaBsStationAndAppList(Class<RelaBsStationAndApp> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	/**
	 * 
	 * @Title: getRelaBsStationAndAppByStatusAndEndTime
	 * @Description: TODO:获取有效的且正在使用的且有效结束时间大于传入参数的通行证和应用的数据
	 * @author:banna
	 * @return: List<RelaBsStationAndApp>
	 */
	public List<RelaBsStationAndApp> getRelaBsStationAndAppByStatusAndEndTime(String status,Timestamp endTime);
}
