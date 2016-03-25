package com.sdf.manager.ad.service;

import java.util.LinkedHashMap;
import java.util.List;




import org.springframework.data.domain.Pageable;

import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.common.util.QueryResult;

public interface AdvertisementService {

	/**
	 * 
	 * @Title: getAdvertisementById
	 * @Description: 根据id查询应用广告数据
	 * @author:banna
	 * @return: Advertisement
	 */
	public Advertisement getAdvertisementById(String id);
	
	/**
	 * 
	 * @Title: getAdvertisementByAdStatus
	 * @Description: 根据应用广告状态
	 * @author:banna
	 * @return: List<Advertisement>
	 */
	public List<Advertisement> getAdvertisementByAdStatus(String adStatus);
	
	/**
	 * 
	 * @Title: save
	 * @Description: 保存应用广告实体
	 * @author:banna
	 * @return: void
	 */
	public void save(Advertisement entity);
	
	/**
	 * 
	 * @Title: update
	 * @Description:修改应用广告实体
	 * @author:banna
	 * @return: void
	 */
	public void update(Advertisement entity);
	
	/**
	 * 
	 * @Title: getAdvertisementList
	 * @Description: 根据筛选条件查询应用广告数据
	 * @author:banna
	 * @return: QueryResult<Advertisement>
	 */
	public QueryResult<Advertisement> getAdvertisementList(Class<Advertisement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public  List<AdvertisementDTO> toRDTOS(List<Advertisement> entities);
	
	
	public  AdvertisementDTO toDTO(Advertisement entity);
}
