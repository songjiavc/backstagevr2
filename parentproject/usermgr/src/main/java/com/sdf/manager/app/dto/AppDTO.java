package com.sdf.manager.app.dto;

public class AppDTO {
	private String id;
	
	private String appCode;//应用编码
	
	private String appName;//应用名称
	
	private String appNameWithProvince;//带省份的应用名称
	
	private String appStatus;//应用状态(0:待上架1:上架2:下架3:更新)
	
	private String appTypeName;//应用状态名称
	
	private String appDeveloper;//应用开发商
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	private String createrPerson;//创建人
	
	//省
	private String province;
	//市
	private String city;
	//区
	private String country;
	
	//省名称
	private String provinceName;
	//市名称
	private String cityName;
	//区名称
	private String countryName;
	
	private String appMoney;//应用的默认单价
	
	private String lotteryType;
	
	

	

	public String getAppNameWithProvince() {
		return appNameWithProvince;
	}

	public void setAppNameWithProvince(String appNameWithProvince) {
		this.appNameWithProvince = appNameWithProvince;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getAppMoney() {
		return appMoney;
	}

	public void setAppMoney(String appMoney) {
		this.appMoney = appMoney;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppCode() {
		return appCode;
	}

	public void setAppCode(String appCode) {
		this.appCode = appCode;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}


	public String getAppStatus() {
		return appStatus;
	}

	public void setAppStatus(String appStatus) {
		this.appStatus = appStatus;
	}

	public String getAppTypeName() {
		return appTypeName;
	}

	public void setAppTypeName(String appTypeName) {
		this.appTypeName = appTypeName;
	}

	public String getAppDeveloper() {
		return appDeveloper;
	}

	public void setAppDeveloper(String appDeveloper) {
		this.appDeveloper = appDeveloper;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterPerson() {
		return createrPerson;
	}

	public void setCreaterPerson(String createrPerson) {
		this.createrPerson = createrPerson;
	}
	
	
	
	
}
