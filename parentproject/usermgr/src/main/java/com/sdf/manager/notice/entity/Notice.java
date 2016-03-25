package com.sdf.manager.notice.entity;

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
@Table(name="T_BS_APP_NOTICE")
public class Notice extends BaseEntiry{

	@Id
	@Column(name="ID", nullable=false, length=45)
//	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
//	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="APP_NOTICE_NAME", length=45)
	private String appNoticeName;//应用公告名称
	
	
	@Column(name="APP_NOTICE_WORD", length=255)
	private String appNoticeWord;//应用公告内容
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//彩种
	
	@Column(name="NOTICE_STARTTIME")
	private Timestamp startTime;
	
	@Column(name="NOTICE_ENDTIME")
	private Timestamp endTime;
	
	@Column(name="APP_CATEGORY", length=45)
	private String appCategory;//公告类别,0:省中心公告1：市中心公告2：公司普通公告3：公司开奖公告4：公司预测公告
	
	@Column(name="NOTICE_FONT_COLOR", length=45)
	private String noticeFontColor;//公告的字体颜色，与公告类别对应

	
	@Column(name="NOTICE_STATUS", length=45)
	private String noticeStatus;//应用公告的状态，0:保存1：发布
	
	/**
	 * 
	 * @Title: getId
	 * @Description: 应用公告与应用的关联
	 * @author:banna
	 * @return: String
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_NOTICE_AND_APP", 
            joinColumns = {  @JoinColumn(name = "APP_NOTICE_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "APP_ID", referencedColumnName = "id") })
	private List<App> apps;
	
	/**
	 * 应用公告与通行证组的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_NOTICE_AND_UGROUP", 
            joinColumns = {  @JoinColumn(name = "APP_NOTICE_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id") })
	private List<UserGroup> userGroups;
	
	/**
	 * 应用公告与预测信息关联表
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_APPNOTICE_AND_FORECAST", 
            joinColumns = {  @JoinColumn(name = "APP_NOTICE_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "FORECAST_ID", referencedColumnName = "id") })
	private List<ForecastMessage> forecastMessages;
	
	/**
	 * 应用公告与“应用公告与区域关联表”的关联
	 */
	@OneToMany(mappedBy = "notice", fetch = FetchType.LAZY) 
	private List<AppNoticeAndArea> appNoticeAndAreas;
	
	
	public String getNoticeFontColor() {
		return noticeFontColor;
	}

	public void setNoticeFontColor(String noticeFontColor) {
		this.noticeFontColor = noticeFontColor;
	}

	public List<AppNoticeAndArea> getAppNoticeAndAreas() {
		return appNoticeAndAreas;
	}

	public void setAppNoticeAndAreas(List<AppNoticeAndArea> appNoticeAndAreas) {
		this.appNoticeAndAreas = appNoticeAndAreas;
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

	public List<ForecastMessage> getForecastMessages() {
		return forecastMessages;
	}

	public void setForecastMessages(List<ForecastMessage> forecastMessages) {
		this.forecastMessages = forecastMessages;
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

	
	
	
	
}
