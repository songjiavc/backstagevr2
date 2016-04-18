package com.bs.outer.service.impl;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bs.outer.service.StationAdService;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.repository.AdvertisementRepository;
import com.sdf.manager.common.util.QueryResult;

@Service("stationAdService")
@Transactional(propagation=Propagation.REQUIRED)
public class StationAdServiceImpl implements StationAdService {
	
	@Autowired
	private AdvertisementRepository advertisementRepository;
	
	public QueryResult<Advertisement> getAdvertisementOfStaApply(
			Class<Advertisement> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable,String province,String city,String lotteryType) {
		StringBuffer sql = new StringBuffer("SELECT u.* FROM ((T_BS_APP_AD u LEFT JOIN RELA_BS_APPAD_AND_APP app ON u.ID=app.APP_AD_ID)  "+
				"	 LEFT JOIN RELA_BS_APPAD_AND_UGROUP au ON u.ID=au.APP_AD_ID) LEFT JOIN RELA_BS_APPAD_AND_AREA aarea ON u.id = aarea.APP_AD_ID WHERE u.IS_DELETED='1'  "+
				"	AND u.AD_STATUS='1' "+
				"   AND u.AD_END_TIME>=CURDATE() AND u.AD_START_TIME<=CURDATE() ");//AND u.LOTTERY_TYPE='"+lotteryType+"'"
		
			sql.append(" AND aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"' GROUP BY u.id");
		QueryResult<Advertisement> userObj = advertisementRepository.
			getScrollDataBySql(Advertisement.class,sql.toString(), queryParams, pageable);
		return userObj;
	}

}
