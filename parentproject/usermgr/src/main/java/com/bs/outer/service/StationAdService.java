package com.bs.outer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.common.util.QueryResult;

public interface StationAdService {

	/**
	 * 
	 * @Title: getAdvertisementOfStaApply
	 * @Description: 市中心获取通行证提交的待审批的通行证应用广告数据
	 * @author:banna
	 * @return: QueryResult<Advertisement>
	 */
	public QueryResult<Advertisement> getAdvertisementOfStaApply(
			Class<Advertisement> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable,String province,String city,String lotteryType,String adStatus);
	
}
