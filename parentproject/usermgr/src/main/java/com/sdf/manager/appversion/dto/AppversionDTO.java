package com.sdf.manager.appversion.dto;

public class AppversionDTO {

	
	private String id;
	
	private String appVersionCode;//应用版本编码
	
	private String appVersionName;//应用版本名称
	
	private String versionCode;//版本号
	
	private String versionFlowId;//版本流水号
	
	private String appVersionUrl;//应用版本安装包位置
	
	private String appVersionStatus;//应用版本状态标记(0:待上架1:上架2:下架3:更新)
	
	private String appVersionStatusName;//应用版本状态名称
	
	private String appDeveloper;//应用开发商
			
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	private String createrPerson;//创建人
	
	
	
	private String appName;//所属应用名称
	
	private String appId;//所属应用id
	
	

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppVersionStatusName() {
		return appVersionStatusName;
	}

	public void setAppVersionStatusName(String appVersionStatusName) {
		this.appVersionStatusName = appVersionStatusName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppVersionCode() {
		return appVersionCode;
	}

	public void setAppVersionCode(String appVersionCode) {
		this.appVersionCode = appVersionCode;
	}

	public String getAppVersionName() {
		return appVersionName;
	}

	public void setAppVersionName(String appVersionName) {
		this.appVersionName = appVersionName;
	}

	public String getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersionFlowId() {
		return versionFlowId;
	}

	public void setVersionFlowId(String versionFlowId) {
		this.versionFlowId = versionFlowId;
	}

	public String getAppVersionUrl() {
		return appVersionUrl;
	}

	public void setAppVersionUrl(String appVersionUrl) {
		this.appVersionUrl = appVersionUrl;
	}

	public String getAppVersionStatus() {
		return appVersionStatus;
	}

	public void setAppVersionStatus(String appVersionStatus) {
		this.appVersionStatus = appVersionStatus;
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
