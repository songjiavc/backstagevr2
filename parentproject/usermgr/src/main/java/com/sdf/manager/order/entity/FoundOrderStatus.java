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




@Entity
@Table(name="T_SDF_FOUND_ORDERS_STATUS")
public class FoundOrderStatus {

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
//	@Column(name="ORDER_ID", length=45, nullable=false)
//	private String orderId;//订单id
	
	@Column(name="STATUS", length=2)
	private String status;//状态
	
	@Column(name="STATUS_SJ", length=11)
	private Timestamp statusSj;//操作状态时间
	
	@Column(name="CREATOR")
	private String creator;//状态操作人
	
    @ManyToOne  
    @JoinColumn(name = "ORDER_ID", referencedColumnName = "id")
    private Orders orders; 
    
    

	public Orders getOrders() {
		return orders;
	}

	public void setOrders(Orders orders) {
		this.orders = orders;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/*public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}*/

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusSj() {
		return statusSj;
	}

	public void setStatusSj(Timestamp statusSj) {
		this.statusSj = statusSj;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	
	
}
