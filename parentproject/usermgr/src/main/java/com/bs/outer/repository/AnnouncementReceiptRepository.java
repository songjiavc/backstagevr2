package com.bs.outer.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.bs.outer.entity.AnnouncementReceipt;
import com.sdf.manager.common.repository.GenericRepository;

public interface AnnouncementReceiptRepository extends GenericRepository<AnnouncementReceipt, String>{

	/**
	 * 
	 * @Title: getAnnouncementReceiptByStatus
	 * @Description: 获取指定状态的通告回执表数据
	 * @author:banna
	 * @return: List<AnnouncementReceipt>
	 */
	@Query("select u from AnnouncementReceipt u where u.status=?1")
	public List<AnnouncementReceipt> getAnnouncementReceiptByStatus(String status);
	
	/**
	 * 
	 * @Title: getAnnouncementReceiptByStatusAndEndTimeAndStationId
	 * @Description: 获取当前通行证的未到有效期的且未读的通告回执表数据
	 * @author:banna
	 * @return: List<AnnouncementReceipt>
	 */
	@Query("select u from AnnouncementReceipt u where u.status=?1 AND u.endTime>=?2 AND u.stationId=?3")
	public List<AnnouncementReceipt> getAnnouncementReceiptByStatusAndEndTimeAndStationId(String status,Timestamp endTime,String stationId);
}
