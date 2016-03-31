package com.bs.outer.service;

import java.sql.Timestamp;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.bs.outer.entity.AnnouncementReceipt;
import com.sdf.manager.common.util.QueryResult;

public interface AnnouncementReceiptService {
	/**
	 * 
	 * @Title: getAnnouncementReceiptByStatus
	 * @Description: 获取指定状态的通告回执表数据
	 * @author:banna
	 * @return: List<AnnouncementReceipt>
	 */
	public List<AnnouncementReceipt> getAnnouncementReceiptByStatus(String status);
	/**
	 * 
	 * @Title: getAnnouncementReceiptByStatusAndEndTimeAndStationId
	 * @Description: 获取当前通行证的未到有效期的且未读的通告回执表数据
	 * @author:banna
	 * @return: List<AnnouncementReceipt>
	 */
	public List<AnnouncementReceipt> getAnnouncementReceiptByStatusAndEndTimeAndStationId(String status,Timestamp endTime,String stationId);
	
	/**
	 * 
	 * @Title: save
	 * @Description: 保存通告回执表实体
	 * @author:banna
	 * @return: void
	 */
	public void save(AnnouncementReceipt entity);
	
	/**
	 * 
	 * @Title: update
	 * @Description: 修改通告回执表实体
	 * @author:banna
	 * @return: void
	 */
	public void update(AnnouncementReceipt entity);
	
	public QueryResult<AnnouncementReceipt> getAnnouncementReceiptsList(Class<AnnouncementReceipt> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
}
