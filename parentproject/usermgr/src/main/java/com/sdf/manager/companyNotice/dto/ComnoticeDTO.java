package com.sdf.manager.companyNotice.dto;

import java.sql.Timestamp;


public class ComnoticeDTO {

	
private String id;
	
	private String comnoticeName;//公司公告名称
	
	
	private String comnoticeContent;//公司公告内容
	
	private String lotteryType;//彩种
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	

	private String comnoticeStatus;//公司公告的状态，0:保存1：发布
	
	private String comnoticeStatusName;//公司公告的状态名称，0:保存1：发布
	
	private String startTimestr;
	
	private String endTimestr;
	
	private String createTime;
	
	

	public String getComnoticeStatusName() {
		return comnoticeStatusName;
	}

	public void setComnoticeStatusName(String comnoticeStatusName) {
		this.comnoticeStatusName = comnoticeStatusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getComnoticeName() {
		return comnoticeName;
	}

	public void setComnoticeName(String comnoticeName) {
		this.comnoticeName = comnoticeName;
	}

	public String getComnoticeContent() {
		return comnoticeContent;
	}

	public void setComnoticeContent(String comnoticeContent) {
		this.comnoticeContent = comnoticeContent;
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

	public String getComnoticeStatus() {
		return comnoticeStatus;
	}

	public void setComnoticeStatus(String comnoticeStatus) {
		this.comnoticeStatus = comnoticeStatus;
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
