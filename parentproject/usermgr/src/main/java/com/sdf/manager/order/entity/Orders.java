package com.sdf.manager.order.entity;

import java.io.Serializable;
import java.sql.Timestamp;
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
import com.sdf.manager.appUnitPrice.entity.UserYearDiscount;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.user.entity.BaseEntiry;

@Entity
@Table(name="T_SDF_ORDERS")
public class Orders extends BaseEntiry implements Serializable 
{
	private static final long serialVersionUID = 5207440618824306396L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="CODE", length=45)
	private String code;//订单编号全系统唯一（日期+流水号）
	
	@Column(name="NAME", length=45)
	private String name;//订单名称
	
	@Column(name="TRANS_COST", length=45)
	private String transCost;//运费
	
	@Column(name="PAY_MODE", length=45)
	private String payMode;//支付方式，下拉框
	
	@Column(name="RECEIVE_ADDR", length=200)
	private String receiveAddr;//收件人地址
	
	@Column(name="RECEIVE_TELE", length=45)
	private String receiveTele;//收件人电话
	
	@Column(name="STATUS", length=4)
	private String status;//订单状态(0:新建;1:待审核;2:审核通过;3:不通过;4:撤销订单;)
	
	@Column(name="STATUS_TIME")
	private Timestamp statusTime;//订单状态(0:新建;1:待审核;2:审核通过;3:不通过;4:撤销订单;)
	
	@Column(name="PRICE", length=45)
	private String price;//成交价格
	
	@Column(name="CREATOR", length=45)
	private String creator;//订单创建人
	
	
	
	@OneToMany(mappedBy = "orders", fetch = FetchType.LAZY) 
	private List<FoundOrderStatus> foundOrderStatus;
	
	//Data:2016/2/2 ADD
	@ManyToOne  
	@JoinColumn(name = "STATION_ID", referencedColumnName = "id")
	private Station station;//与“通行证表”关联
	
	//Data:2016/2/2 ADD
	@ManyToOne  
	@JoinColumn(name = "APP_ID", referencedColumnName = "id")
	private App app;//与“应用表”关联
	
	
	//Data:2016/2/2 ADD
	@ManyToOne  
	@JoinColumn(name = "USER_YEARS", referencedColumnName = "id")
	private UserYearDiscount userYearDiscount;
	
	
	
	public UserYearDiscount getUserYearDiscount() {
		return userYearDiscount;
	}

	public void setUserYearDiscount(UserYearDiscount userYearDiscount) {
		this.userYearDiscount = userYearDiscount;
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

	public List<FoundOrderStatus> getFoundOrderStatus() {
		return foundOrderStatus;
	}

	public void setFoundOrderStatus(List<FoundOrderStatus> foundOrderStatus) {
		this.foundOrderStatus = foundOrderStatus;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTransCost() {
		return transCost;
	}

	public void setTransCost(String transCost) {
		this.transCost = transCost;
	}

	public String getPayMode() {
		return payMode;
	}

	public void setPayMode(String payMode) {
		this.payMode = payMode;
	}

	public String getReceiveAddr() {
		return receiveAddr;
	}

	public void setReceiveAddr(String receiveAddr) {
		this.receiveAddr = receiveAddr;
	}

	public String getReceiveTele() {
		return receiveTele;
	}

	public void setReceiveTele(String receiveTele) {
		this.receiveTele = receiveTele;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(Timestamp statusTime) {
		this.statusTime = statusTime;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	
	
	
	
	
	
}
