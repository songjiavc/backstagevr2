package com.sdf.manager.proxyFromCweb.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.user.entity.BaseEntiry;


/**
 * 
 * @ClassName: ApplyProxy
 * @Description: 申请代理实体
 * @author: banna
 * @date: 2016年5月4日 下午2:01:09
 */
@Entity
@Table(name="T_BYL_APPLY_PROXY")
public class ApplyProxy extends BaseEntiry implements Serializable 
{
	
	
	/** 
	  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	  */ 
	private static final long serialVersionUID = 7692132908491601627L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	
	@Column(name="NAME",length=45)
	private String name;
	
	@Column(name="ADDRESS",length=45)
	private String address;
	
	@Column(name="PROVINCE")
	private String province;//是否为系统级数据，不可以进行删除等操作
	
	
	@Column(name="CITY")
	private String city;
	
	@Column(name="REMARK",length=500)
	private String remark;
	
	@Column(name="IS_CONNECT",length=10)
	private String isConnect;
	
	@Column(name="TELEPHONE",length=100)
	private String telephone;
	
	@Column(name="PROXY_ID",length=100)
	private String proxyId;//若回访符合后则要生成对应的代理信息
	
	@Column(name="STATUS",length=10)
	private String status;//代理申请数据状态，0：未回访 1：回访不符合 2：回访符合 
	
	
	

	public String getProxyId() {
		return proxyId;
	}

	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getIsConnect() {
		return isConnect;
	}

	public void setIsConnect(String isConnect) {
		this.isConnect = isConnect;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	
	
	
	
}
