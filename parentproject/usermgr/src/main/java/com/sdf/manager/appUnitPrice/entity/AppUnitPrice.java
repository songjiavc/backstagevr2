package com.sdf.manager.appUnitPrice.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.user.entity.BaseEntiry;

/**
 * 
 * @ClassName: AppUnitPrice
 * @Description: 应用区域单价表(需要应用区域单价模块进行维护，可以根据应用列表选择区域进行单价设定)
 * @author: banna
 * @date: 2016年2月2日 上午9:37:19
 */
@Entity
@Table(name="T_BS_APP_PRICE_AND_AREA")
public class AppUnitPrice extends BaseEntiry{

	
	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	//省
	@Column(name="PROVINCE", length=45)
	private String province;
	//市
	@Column(name="CITY", length=45)
	private String city;
	
	//应用区域单价
	@Column(name="UNIT_PRICE", length=45)
	private String unitPrice;
	
	@Column(name="PRICE_TYPE", length=45)
	private String priceType;//单价类别，0：app默认单价，在创建app时写入的初始单价，之后也会跟随app的单价变化而变化 1：自定义单价，通过应用区域单价模块进行设置的单价，不随应用单价的变化而变化 
	
	@ManyToOne  
	@JoinColumn(name = "APP_ID", referencedColumnName = "id")
	private App app;//与“应用表”关联
	
	

	public String getPriceType() {
		return priceType;
	}

	public void setPriceType(String priceType) {
		this.priceType = priceType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
	
	
}
