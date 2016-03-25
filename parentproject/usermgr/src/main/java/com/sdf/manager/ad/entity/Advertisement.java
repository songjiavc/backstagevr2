package com.sdf.manager.ad.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.user.entity.BaseEntiry;
import com.sdf.manager.userGroup.entity.UserGroup;


@Entity
@Table(name="T_BS_APP_AD")
public class Advertisement extends BaseEntiry{

	@Id
	@Column(name="ID", nullable=false, length=45)
//	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
//	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="APP_AD_NAME", length=45)
	private String appAdName;//应用广告名称
	
	
	@Column(name="APP_WORD", length=255)
	private String addWord;//应用广告内容
	
	@Column(name="APP_IMG_URL", length=45)
	private String appImgUrl;//应用图片url
	
	@Column(name="AD_START_TIME")
	private Timestamp startTime;
	
	@Column(name="AD_END_TIME")
	private Timestamp endTime;
	
	@Column(name="AD_TIME", length=45)
	private String adTime;//广告的轮播时长，单位：s
		
	@Column(name="IMG_OR_WORD", length=10)
	private String imgOrWord;//应用广告的展示形式，0：图片 1：文字

	@Column(name="AD_TYPE", length=45)
	private String addType;//广告类别,0：站点广告 1.市中心应用广告2.省中心应用广告3.公司应用广告
	
	@Column(name="AD_FONT_COLOR", length=45)
	private String adFontColor;//广告的字体颜色，与广告类别对应

	@Column(name="AD_STATUS", length=45)
	private String adStatus;//应用广告的状态，0:保存1：发布
	
	@Column(name="CREATOR_STATION", length=45)
	private String creatorStation;//创建站点,若创建的是站点类别的应用广告，则要填充创建站点的字段值
	
	@Column(name="STATION_AD_STATUS", length=45)
	private String stationAdStatus;//通行证发布的应用广告的当前状态
	
	@Column(name="STATION_AD_STATUS_TIME")
	private Timestamp stationAdStatusTime;//通行证发布的应用广告的当前状态更新时间
	
	/**
	 * 
	 * @Title: getId
	 * @Description: 应用广告与应用的关联
	 * @author:banna
	 * @return: String
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_APPAD_AND_APP", 
            joinColumns = {  @JoinColumn(name = "APP_AD_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "APP_ID", referencedColumnName = "id") })
	private List<App> apps;
	
	/**
	 * 应用广告与通行证组的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_APPAD_AND_UGROUP", 
            joinColumns = {  @JoinColumn(name = "APP_AD_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id") })
	private List<UserGroup> userGroups;
	
	/**
	 * 应用广告与“应用广告与区域关联”表的关联
	 */
	@OneToMany(mappedBy = "advertisement", fetch = FetchType.LAZY) 
	private List<AppAdAndArea> appAdAndAreas;
	
	
	
	
	
	

	public Timestamp getStationAdStatusTime() {
		return stationAdStatusTime;
	}

	public void setStationAdStatusTime(Timestamp stationAdStatusTime) {
		this.stationAdStatusTime = stationAdStatusTime;
	}

	public String getStationAdStatus() {
		return stationAdStatus;
	}

	public void setStationAdStatus(String stationAdStatus) {
		this.stationAdStatus = stationAdStatus;
	}

	public String getCreatorStation() {
		return creatorStation;
	}

	public void setCreatorStation(String creatorStation) {
		this.creatorStation = creatorStation;
	}

	public String getAdFontColor() {
		return adFontColor;
	}

	public void setAdFontColor(String adFontColor) {
		this.adFontColor = adFontColor;
	}

	public List<AppAdAndArea> getAppAdAndAreas() {
		return appAdAndAreas;
	}

	public void setAppAdAndAreas(List<AppAdAndArea> appAdAndAreas) {
		this.appAdAndAreas = appAdAndAreas;
	}

	public List<App> getApps() {
		return apps;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	public List<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
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
	
	
	

	
	
}
