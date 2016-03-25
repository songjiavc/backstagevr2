package com.sdf.manager.announcement.dto;

import java.sql.Timestamp;


public class AnnouncementDTO {

private String id;
	
	private String announcementName;//通告名称
	
	
	private String announcementContent;//通告内容
	
	private String lotteryType;//彩种
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	

	private String announceStatus;//通告状态，0:保存1：发布
	
	private String announceStatusName;//通告状态名称，0:保存1：发布
	
	private String startTimestr;
	
	private String endTimestr;
	
	private String createTime;
	
	

	public String getAnnounceStatusName() {
		return announceStatusName;
	}

	public void setAnnounceStatusName(String announceStatusName) {
		this.announceStatusName = announceStatusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAnnouncementName() {
		return announcementName;
	}

	public void setAnnouncementName(String announcementName) {
		this.announcementName = announcementName;
	}

	public String getAnnouncementContent() {
		return announcementContent;
	}

	public void setAnnouncementContent(String announcementContent) {
		this.announcementContent = announcementContent;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getAnnounceStatus() {
		return announceStatus;
	}

	public void setAnnounceStatus(String announceStatus) {
		this.announceStatus = announceStatus;
	}

	public String getStartTimestr() {
		return startTimestr;
	}

	public void setStartTimestr(String startTimestr) {
		this.startTimestr = startTimestr;
	}

	public String getEndTimestr() {
		return endTimestr;
	}

	public void setEndTimestr(String endTimestr) {
		this.endTimestr = endTimestr;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
