package com.sdf.manager.station.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.station.entity.Station;

/** 
  * @ClassName: AuthRepository 
  * @Description: 权限 
  * @author songj@sdfcp.com
  * @date 2015年9月25日 上午8:24:21 
  *  
  */
public interface StationRepository extends GenericRepository<Station, String>  {
	
	/** 
	  * @Description: 判断code是否有重复
	  * @author songj@sdfcp.com
	  * @date 2015年10月15日 下午1:18:37 
	  * @param code
	  * @return 
	  */
	@Query("select u from Station u where u.code =?1 and u.isDeleted=1")
	public Station getStationByCode(String code);
	
	/**
	 * 
	 * @Title: getStationById
	 * @Description: 根据id获取通行证数据
	 * @author:banna
	 * @return: Station
	 */
	@Query("select u from Station u where u.id =?1 and u.isDeleted=1")
	public Station getStationById(String id);
	
	/**
	 * 
	* @Description: 根据上级代理id获取其下属的站点数据
	* @author bann@sdfcp.com
	* @date 2015年12月1日 上午9:09:23
	 */
	@Query("select u from Station u where  u.isDeleted=1 and u.agentId =?1 ")
	public List<Station> getStationByAgentId(String agentId);
	
	/**
	 * 
	* @Description: 获取当前站主信息对应的其他彩种的站点
	* @author bann@sdfcp.com
	* @date 2015年12月1日 下午2:05:03
	 */
	@Query("select u from Station u where  u.isDeleted=1 and u.stationType =?1 and owner =?2 and ownerTelephone =?3")
	public List<Station> getStationByStationTypeAndOwnerAndOwnertelephone
				(String stationType,String owner,String ownerTelephone);
	
	
	
	
}
