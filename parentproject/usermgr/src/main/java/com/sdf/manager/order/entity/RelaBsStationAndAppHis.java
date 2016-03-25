package com.sdf.manager.order.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.product.entity.Product;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.user.entity.BaseEntiry;


/**
 * 
 * @ClassName: RelaBsStationAndAppHis
 * @Description: 通行证应用历史记录表(在从表内插入最新的购买或续费记录 )
 * @author: banna
 * @date: 2016年2月3日 上午9:13:24
 */
@Entity
@Table(name="RELA_STATION_AND_APP_HIS")
public class RelaBsStationAndAppHis extends BaseEntiry {

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	//Data:2016/2/2 ADD
	@ManyToOne  
	@JoinColumn(name = "STATION_ID", referencedColumnName = "id")
	private Station station;//与“通行证表”关联
	
	//Data:2016/2/2 ADD
	@ManyToOne  
	@JoinColumn(name = "APP_ID", referencedColumnName = "id")
	private App app;//与“应用表”关联
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	

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
