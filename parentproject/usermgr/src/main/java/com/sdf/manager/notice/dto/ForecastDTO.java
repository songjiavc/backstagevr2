package com.sdf.manager.notice.dto;

import java.sql.Timestamp;


public class ForecastDTO {

private String id;
	
	private String forecastName;//预测信息名称
	
	
	private String forecastContent;//预测信息内容
	
	private String lotteryType;//预测彩种
	
	
	private String appAreaProvince;
	//市
	private String appAreaCity;
	//区
	private String appArea;
	
	private String provinceName;
	
	private String cityName;
	
	private String countryName;
	
	
	private Timestamp startTime;
	
	private Timestamp endTime;
	
	private String startTimestr;
	
	private String endTimestr;
	
	private String createTime;
	
	
	
	
	

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

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getForecastName() {
		return forecastName;
	}

	public void setForecastName(String forecastName) {
		this.forecastName = forecastName;
	}

	public String getForecastContent() {
		return forecastContent;
	}

	public void setForecastContent(String forecastContent) {
		this.forecastContent = forecastContent;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getAppAreaProvince() {
		return appAreaProvince;
	}

	public void setAppAreaProvince(String appAreaProvince) {
		this.appAreaProvince = appAreaProvince;
	}

	public String getAppAreaCity() {
		return appAreaCity;
	}

	public void setAppAreaCity(String appAreaCity) {
		this.appAreaCity = appAreaCity;
	}

	public String getAppArea() {
		return appArea;
	}

	public void setAppArea(String appArea) {
		this.appArea = appArea;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
