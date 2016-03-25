/*package com.sdf.manager.order.entity;

import java.io.Serializable;
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
import org.springframework.data.annotation.Transient;

import com.sdf.manager.goods.entity.Goods;
import com.sdf.manager.goods.entity.RelaSdfGoodProduct;
import com.sdf.manager.user.entity.BaseEntiry;

@Entity
@Table(name="T_SDF_ORDERS")
public class Ordersold extends BaseEntiry implements Serializable 
{
	*//** 
	  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	  *//* 
	private static final long serialVersionUID = 5207440618824306396L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="STATION_ID", length=45)
	private String stationId;//站点号码：关联用户表的站点类数据
	
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
	private String price;//订单总价
	
	@Column(name="CREATOR", length=45)
	private String creator;//订单创建人
	
	
	@Transient
	//表间关联的主控方的配置
	//@JoinTable描述了多对多关系的数据表关系。name属性指定中间表名称，joinColumns定义中间表与Role表的外键关系。
    //中间表RELA_SDF_AUTHORITY_ROLE的ROLE_ID列是Teacher表的主键列对应的外键列，inverseJoinColumns属性定义了中间表与另外一端(Authority)的外键关系。
    //属性referencedColumnName标注的是所关联表中的字段名，若不指定则使用的所关联表的主键字段名作为外键。 
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_SDF_ORDER_GOOD", 
            joinColumns = {  @JoinColumn(name = "ORDER_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "GOOD_ID", referencedColumnName = "id") })
	private List<Goods> goods ;
	
	
	@OneToMany(mappedBy = "orders", fetch = FetchType.LAZY) 
	private List<FoundOrderStatus> foundOrderStatus;
	
	
	
	
	
	
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

	public List<Goods> getGoods() {
		return goods;
	}

	public void setGoods(List<Goods> goods) {
		this.goods = goods;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStationId() {
		return stationId;
	}

	public void setStationId(String stationId) {
		this.stationId = stationId;
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
*/