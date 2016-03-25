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
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.user.entity.BaseEntiry;


@Entity
@Table(name="T_BS_FORECAST_INFO")
public class ForecastMessage extends BaseEntiry{

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="FORECAST_NAME", length=45)
	private String forecastName;//预测信息名称
	
	
	@Column(name="FORECAST_CONTENT", length=255)
	private String forecastContent;//预测信息内容
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//预测彩种
	
	
	//省
	@Column(name="APP_AREA_PROVINCE", length=45)
	private String appAreaProvince;
	//市
	@Column(name="APP_AREA_CITY", length=45)
	private String appAreaCity;
	//区
	@Column(name="APP_AREA", length=45)
	private String appArea;
	
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	
	/**
	 * 预测信息与应用公告的关联关系
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	 @JoinTable(name = "RELA_BS_APPNOTICE_AND_FORECAST", 
     joinColumns = {  @JoinColumn(name = "FORECAST_ID", referencedColumnName = "id")  }, 
     inverseJoinColumns = {@JoinColumn(name = "APP_NOTICE_ID", referencedColumnName = "id") })
	private List<Notice> notices;
	
	

	public List<Notice> getNotices() {
		return notices;
	}

	public void setNotices(List<Notice> notices) {
		this.notices = notices;
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
	
	
	
	
}
