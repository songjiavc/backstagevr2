package com.bs.outer.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 
 * @ClassName: AnnouncementReceipt
 * @Description: 通告回执表
 * @author: banna
 * @date: 2016年3月29日 下午4:29:56
 */
@Entity
@Table(name="T_BS_RECEIPT_OF_ANNOUNCEMENT")
public class AnnouncementReceipt {
	

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name = "ANNOUNCEMENT_ID", length=45)
	private String announcementId;
	
	@Column(name = "STATION_ID", length=45)
	private String stationId;
	
	
	@Column(name="STATUS", length=45)
	private String status;//通告被查看状态，0：未读，1：已读
	
	
	@Column(name="STATUS_TIME")
	private Timestamp statusTime;//通告被查看的时间
	
	@Column(name="END_TIME")
	private Timestamp endTime;//通告有效结束时间
	
	
	
	
	

	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAnnouncementId() {
		return announcementId;
	}


	public void setAnnouncementId(String announcementId) {
		this.announcementId = announcementId;
	}


	public String getStationId() {
		return stationId;
	}


	public void setStationId(String stationId) {
		this.stationId = stationId;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Timestamp getStatusTime() {
		return statusTime;
	}


	public void setStatusTime(Timestamp statusTime) {
		this.statusTime = statusTime;
	}
	
	
	
	

}
