package com.sdf.manager.appversion.entity;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.user.entity.BaseEntiry;

/**
 * 
* @ClassName: Appversion
* @Description: 应用版本关联表
* @author banna
* @date 2016年1月25日 下午3:35:00
*
 */
@Entity
@Table(name="T_BS_APPLICATION_VERSION")
public class Appversion extends BaseEntiry{

	
	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="APP_VERSION_CODE", length=45)
	private String appVersionCode;//应用版本编码
	
	
	@Column(name="APP_VERSION_NAME", length=45)
	private String appVersionName;//应用版本名称
	
	@Column(name="VERSION_CODE", length=45)
	private String versionCode;//版本号
	
	
	@Column(name="VERSION_FLOW_ID", length=45)
	private String versionFlowId;//版本流水号


	@Column(name="APP_VERSION_URL", length=100)
	private String appVersionUrl;//应用版本安装包位置
	
	@Column(name="APP_VERSION_STATUS", length=45)
	private String appVersionStatus;//应用版本状态标记(0:待上架1:上架2:下架3:更新)
	
//	@Column(name="APP_ID", length=45)
//	private String appId;//应用id
	
	@Column(name="APP_DEVELOPER", length=45)
	private String appDeveloper;//应用开发商
	
	//省
	@Column(name="PROVINCE", length=45)
	private String province;
	//市
	@Column(name="CITY", length=45)
	private String city;
	//区
	@Column(name="COUNTRY", length=45)
	private String country;
	
	@ManyToOne  
    @JoinColumn(name = "APP_ID", referencedColumnName = "id")
	private App app;
	
	//与“当前通行证使用版本记录表”关联
	@OneToMany(mappedBy = "appversion", fetch = FetchType.LAZY) 
	private List<RelaBsCurAppverAndSta> relaBsCurAppverAndStas;

	//与"历史通行证使用版本记录表"关联
	@OneToMany(mappedBy = "appversion", fetch = FetchType.LAZY) 
	private List<RelaBsHisAppverAndSta> relaBsHisAppverAndStas;
	
	
	

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

	public List<RelaBsCurAppverAndSta> getRelaBsCurAppverAndStas() {
		return relaBsCurAppverAndStas;
	}

	public void setRelaBsCurAppverAndStas(
			List<RelaBsCurAppverAndSta> relaBsCurAppverAndStas) {
		this.relaBsCurAppverAndStas = relaBsCurAppverAndStas;
	}

	public List<RelaBsHisAppverAndSta> getRelaBsHisAppverAndStas() {
		return relaBsHisAppverAndStas;
	}

	public void setRelaBsHisAppverAndStas(
			List<RelaBsHisAppverAndSta> relaBsHisAppverAndStas) {
		this.relaBsHisAppverAndStas = relaBsHisAppverAndStas;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
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
	
	
	
}
