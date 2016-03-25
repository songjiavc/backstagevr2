package com.sdf.manager.notice.dto;

import java.sql.Timestamp;




public class NoticeDTO {

	private String id;
	
	private String appNoticeName;//应用公告名称
	
	
	private String appNoticeWord;//应用公告内容
	
	private String lotteryType;//彩种
	
	private String lotteryTypeName;//彩种名称
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private String appCategory;//公告类别

	private String noticeStatus;//应用公告的状态，0:保存1：发布
	
	private String noticeStatusName;//应用公告的状态名称，0:保存1：发布
	
	private String startTimestr;
	
	private String endTimestr;
	
	private String createTime;
	
	
	private String noticeFontColor;//公告的字体颜色，与公告类别对应
	

	public String getNoticeFontColor() {
		return noticeFontColor;
	}

	public void setNoticeFontColor(String noticeFontColor) {
		this.noticeFontColor = noticeFontColor;
	}

	public String getLotteryTypeName() {
		return lotteryTypeName;
	}

	public void setLotteryTypeName(String lotteryTypeName) {
		this.lotteryTypeName = lotteryTypeName;
	}

	public String getNoticeStatusName() {
		return noticeStatusName;
	}

	public void setNoticeStatusName(String noticeStatusName) {
		this.noticeStatusName = noticeStatusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppNoticeName() {
		return appNoticeName;
	}

	public void setAppNoticeName(String appNoticeName) {
		this.appNoticeName = appNoticeName;
	}

	public String getAppNoticeWord() {
		return appNoticeWord;
	}

	public void setAppNoticeWord(String appNoticeWord) {
		this.appNoticeWord = appNoticeWord;
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

	public String getAppCategory() {
		return appCategory;
	}

	public void setAppCategory(String appCategory) {
		this.appCategory = appCategory;
	}

	public String getNoticeStatus() {
		return noticeStatus;
	}

	public void setNoticeStatus(String noticeStatus) {
		this.noticeStatus = noticeStatus;
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
