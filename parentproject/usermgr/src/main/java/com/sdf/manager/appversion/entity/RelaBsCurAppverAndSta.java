package com.sdf.manager.appversion.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.user.entity.BaseEntiry;


@Entity
@Table(name="T_BS_CURRENT_VERSION")
public class RelaBsCurAppverAndSta extends BaseEntiry{

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@ManyToOne  
	@JoinColumn(name = "STATION_ID", referencedColumnName = "id")
	private Station station;//与“通行证表”关联
	
	@ManyToOne  
	@JoinColumn(name = "APP_ID", referencedColumnName = "id")
	private App app;//与“应用表”关联
	
	@ManyToOne  
	@JoinColumn(name = "VERSION_ID", referencedColumnName = "id")
	private Appversion appversion;//与“应用版本表”关联
	
	@Column(name="SERIAL_NUM", length=45)
	private String serialNum;//流水号

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public Appversion getAppversion() {
		return appversion;
	}

	public void setAppversion(Appversion appversion) {
		this.appversion = appversion;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	
	
	
	
	
	
}
