package com.sdf.manager.order.service;



import com.sdf.manager.order.entity.RelaBsStationAndAppHis;

public interface RelaBsStaAppHisService {

	/**
	 * 
	* @Title: save
	* @Description: 保存通行证应用历史记录表数据
	* @param @param entity    设定文件
	* @return void    返回类型
	* @author banna
	* @throws
	 */
	public void save(RelaBsStationAndAppHis entity);
	
	/**
	 * 
	* @Title: update
	* @Description: 更新通行证应用历史记录表数据
	* @param @param entity    设定文件
	* @return void    返回类型
	* @author banna
	* @throws
	 */
	public void update(RelaBsStationAndAppHis entity);
	
	
	
}
