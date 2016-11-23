package com.sdf.manager.ad.dto;

import java.sql.Timestamp;


public class AdvertisementDTO {

	private String id;
	
	private String appAdName;//应用广告名称
	
	
	private String addWord;//应用广告内容
	
	private String appImgUrl;//应用图片url
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private String adTime;//广告的轮播时长，单位：s
		
	private String imgOrWord;//应用广告的展示形式，0：图片 1：文字

	private String addType;//广告类别,0：站点广告 1.市中心应用广告2.省中心应用广告3.公司应用广告

	private String adStatus;//应用广告的状态，0:保存1：发布
	
	private String adStatusName;//应用广告的状态名称，根据应用广告的状态生成的。
	
	private String startTimestr;
	
	private String endTimestr;
	
	private String createTime;
	
	private String adFontColor;//广告的字体颜色，与广告类别对应
	
	private String stationAdStatus;//通行证发布的应用广告的当前状态
	
	private String stationAdStatusName;//通行证发布的应用广告的当前状态名称
	
	private String stationAdStatusTime;//通行证发布的应用广告的当前状态更新时间
	
	private String stationNum;//发布应用广告所属的通行证的站点号
	
	private String appName;//发布应用广告的通行证选择发布广告的应用的名称
	
	
	
	
	

	public String getStationNum() {
		return stationNum;
	}

	public void setStationNum(String stationNum) {
		this.stationNum = stationNum;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getStationAdStatus() {
		return stationAdStatus;
	}

	public void setStationAdStatus(String stationAdStatus) {
		this.stationAdStatus = stationAdStatus;
	}

	public String getStationAdStatusName() {
		return stationAdStatusName;
	}

	public void setStationAdStatusName(String stationAdStatusName) {
		this.stationAdStatusName = stationAdStatusName;
	}

	public String getStationAdStatusTime() {
		return stationAdStatusTime;
	}

	public void setStationAdStatusTime(String stationAdStatusTime) {
		this.stationAdStatusTime = stationAdStatusTime;
	}

	public String getAdFontColor() {
		return adFontColor;
	}

	public void setAdFontColor(String adFontColor) {
		this.adFontColor = adFontColor;
	}

	public String getAdStatusName() {
		return adStatusName;
	}

	public void setAdStatusName(String adStatusName) {
		this.adStatusName = adStatusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppAdName() {
		return appAdName;
	}

	public void setAppAdName(String appAdName) {
		this.appAdName = appAdName;
	}

	public String getAddWord() {
		return addWord;
	}

	public void setAddWord(String addWord) {
		this.addWord = addWord;
	}

	public String getAppImgUrl() {
		return appImgUrl;
	}

	public void setAppImgUrl(String appImgUrl) {
		this.appImgUrl = appImgUrl;
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

	public String getAdTime() {
		return adTime;
	}

	public void setAdTime(String adTime) {
		this.adTime = adTime;
	}

	public String getImgOrWord() {
		return imgOrWord;
	}

	public void setImgOrWord(String imgOrWord) {
		this.imgOrWord = imgOrWord;
	}

	public String getAddType() {
		return addType;
	}

	public void setAddType(String addType) {
		this.addType = addType;
	}

	public String getAdStatus() {
		return adStatus;
	}

	public void setAdStatus(String adStatus) {
		this.adStatus = adStatus;
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
