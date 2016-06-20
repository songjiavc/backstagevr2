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
			Pageable pageable,String province,String city,String lotteryType,String adStatus,String adName) {
		/*//查询max数据所在行数据错误
		 * StringBuffer sql = new StringBuffer("SELECT u.ID,u.CREATER,MAX(u.CREATER_TIME) AS CREATER_TIME ,u.IS_DELETED,u.MODIFY,u.MODIFY_TIME,u.AD_STATUS,u.AD_TIME,u.AD_TYPE,u.APP_WORD, "+
					"	u.APP_AD_NAME,u.APP_IMG_URL,u.AD_END_TIME,u.IMG_OR_WORD,u.AD_START_TIME,u.AD_FONT_COLOR,u.CREATOR_STATION,u.STATION_AD_STATUS,u.STATION_AD_STATUS_TIME "+
					 " FROM ((T_BS_APP_AD u LEFT JOIN RELA_BS_APPAD_AND_APP app ON u.ID=app.APP_AD_ID)  "+
				"	 LEFT JOIN RELA_BS_APPAD_AND_UGROUP au ON u.ID=au.APP_AD_ID) LEFT JOIN T_SDF_STATIONS aarea ON u.CREATOR_STATION = aarea.ID WHERE u.IS_DELETED='1'  "+
				"	  AND u.AD_TYPE='0' "+
				"   AND u.AD_END_TIME>=CURDATE() AND u.AD_START_TIME<=CURDATE() AND aarea.STATION_TYPE='"+lotteryType+"' ");//
		
			if(null!=adStatus&&!"".equals(adStatus))
			{
				sql.append("  AND u.AD_STATUS='"+adStatus+"' ");
			}
			
			if(null != adName && !"".equals(adName))
			{
				sql.append("  AND u.APP_AD_NAME like '%"+adName+"%' ");
			}
		
			sql.append(" AND aarea.PROVINCE_CODE='"+province+"' AND aarea.CITY_CODE='"+city+"' GROUP BY u.CREATOR_STATION");*/
		StringBuffer sql = new StringBuffer("SELECT ad.* FROM (SELECT MAX(u.CREATER_TIME) AS CREATER_TIME,u.CREATOR_STATION  "+
				 " FROM ((T_BS_APP_AD u LEFT JOIN RELA_BS_APPAD_AND_APP app ON u.ID=app.APP_AD_ID)  "+
			"	 LEFT JOIN RELA_BS_APPAD_AND_UGROUP au ON u.ID=au.APP_AD_ID) LEFT JOIN T_SDF_STATIONS aarea ON u.CREATOR_STATION = aarea.ID WHERE u.IS_DELETED='1'  "+
			"	  AND u.AD_TYPE='0' "+
			"   AND u.AD_END_TIME>=CURDATE() AND u.AD_START_TIME<=NOW() AND aarea.STATION_TYPE='"+lotteryType+"' ");//
	
		if(null!=adStatus&&!"".equals(adStatus))
		{
			sql.append("  AND u.AD_STATUS='"+adStatus+"' ");
		}
		
		if(null != adName && !"".equals(adName))
		{
			sql.append("  AND u.APP_AD_NAME like '%"+adName+"%' ");
		}
	
		sql.append(" AND aarea.PROVINCE_CODE='"+province+"' AND aarea.CITY_CODE='"+city+"' GROUP BY u.CREATOR_STATION,app.APP_ID) m  "
				+ " LEFT JOIN T_BS_APP_AD ad ON  m.CREATER_TIME= ad.CREATER_TIME AND m.CREATOR_STATION = ad.CREATOR_STATION");
			
		QueryResult<Advertisement> userObj = advertisementRepository.
				getScrollDataByGroupBySql(Advertisement.class,sql.toString(), queryParams, pageable);/*※带group by的sql的执行方法，
		和getScrollDataBySql的区别在于对统计总的数据条数的处理，getScrollDataBySql统计总数据量会去掉group by，这种方法导致的结构是统计错误，因为带group by的sql要
		的总数据量是group by分组后的组数而不是group by分组内统计*/
		return userObj;
	}
	
	

	
	
}
