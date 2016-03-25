package com.sdf.manager.order.dto;


/**
 * 
 * @ClassName: RenewAppDTO
 * @Description: 可以续费的应用数据
 * @author: banna
 * @date: 2016年2月3日 下午5:41:28
 */
public class RenewAppDTO {

	
	private String appName;
	
	private String appId;
	
	private String provinceName;
	
	private String cityName;
	
	private String lastPurchaseTime;//上次购买时间
	
	private String endTime;//有效期结束时间
	
	private String surplusDays;//剩余使用天数
	
	private String province;
	
	private String city;
	
	

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
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

	public String getLastPurchaseTime() {
		return lastPurchaseTime;
	}

	public void setLastPurchaseTime(String lastPurchaseTime) {
		this.lastPurchaseTime = lastPurchaseTime;
	}

	public String getSurplusDays() {
		return surplusDays;
	}

	public void setSurplusDays(String surplusDays) {
		this.surplusDays = surplusDays;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
	
	
}
