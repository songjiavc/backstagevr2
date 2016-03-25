package com.sdf.manager.app.entity;


import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.appversion.entity.RelaBsCurAppverAndSta;
import com.sdf.manager.appversion.entity.RelaBsHisAppverAndSta;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.user.entity.BaseEntiry;

/**
 * 
* @ClassName: App
* @Description: 应用表实体
* @author banna
* @date 2016年1月25日 下午3:27:31
*
 */
@Entity
@Table(name="T_BS_APPLICATION")
public class App extends BaseEntiry{

	
	@Id
	@Column(name="ID", nullable=false, length=45)
//	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
//	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="APP_CODE", length=45)
	private String appCode;//应用编码
	
	
	@Column(name="APP_NAME", length=45)
	private String appName;//应用名称
	
	@Column(name="APP_STATUS", length=45)
	private String appStatus;//应用状态(0:待上架1:上架2:下架3:更新)
	
	
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
	
	//应用默认单价
	@Column(name="APP_MONEY", length=45)
	private String appMoney;
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//彩种
	
	//与“应用版本表”关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<Appversion> appVersions;
	
	//与“当前通行证使用版本记录表”关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<RelaBsCurAppverAndSta> relaBsCurAppverAndStas;

	//与"历史通行证使用版本记录表"关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<RelaBsHisAppverAndSta> relaBsHisAppverAndStas;
	
	
	//与“应用区域单价表”关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<AppUnitPrice> appUnitPrices;
	
	//应用与“订单表”关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<Orders> orders;
	
	//应用与“通行证应用表”的关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<RelaBsStationAndApp> relaBsStationAndApps;
	
	//应用与“通行证应用表历史记录表”的关联
	@OneToMany(mappedBy = "app", fetch = FetchType.LAZY) 
	private List<RelaBsStationAndAppHis> relaBsStationAndAppHis;
	
	/**
	 * 
	 * @Title: getId
	 * @Description: 应用广告与应用的关联
	 * @author:banna
	 * @return: String
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private List<Advertisement> advertisements;
	
	/**
	 * 应用公告与应用的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	private List<Notice> notices;
	
	
	
	
	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}

	public List<Advertisement> getAdvertisements() {
		return advertisements;
	}

	public void setAdvertisements(List<Advertisement> advertisements) {
		this.advertisements = advertisements;
	}

	public List<RelaBsStationAndApp> getRelaBsStationAndApps() {
		return relaBsStationAndApps;
	}

	public void setRelaBsStationAndApps(
			List<RelaBsStationAndApp> relaBsStationAndApps) {
		this.relaBsStationAndApps = relaBsStationAndApps;
	}

	public List<RelaBsStationAndAppHis> getRelaBsStationAndAppHis() {
		return relaBsStationAndAppHis;
	}

	public void setRelaBsStationAndAppHis(
			List<RelaBsStationAndAppHis> relaBsStationAndAppHis) {
		this.relaBsStationAndAppHis = relaBsStationAndAppHis;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public List<AppUnitPrice> getAppUnitPrices() {
		return appUnitPrices;
	}

	public void setAppUnitPrices(List<AppUnitPrice> appUnitPrices) {
		this.appUnitPrices = appUnitPrices;
	}

	public String getAppMoney() {
		return appMoney;
	}

	public void setAppMoney(String appMoney) {
		this.appMoney = appMoney;
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


	public List<Appversion> getAppVersions() {
		return appVersions;
	}


	public void setAppVersions(List<Appversion> appVersions) {
		this.appVersions = appVersions;
	}


	public String getAppDeveloper() {
		return appDeveloper;
	}


	public void setAppDeveloper(String appDeveloper) {
		this.appDeveloper = appDeveloper;
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




	
	
	
}
