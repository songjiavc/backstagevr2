package com.sdf.manager.proxyFromCweb.dto;


public class ApplyProxyDTO {

	private String id;
	
	
	private String name;
	
	private String address;
	
	private String province;//是否为系统级数据，不可以进行删除等操作
	
	
	private String city;
	
	private String remark;
	
	private String isConnect;
	
	private String telephone;
	
	private String status;//代理申请数据状态，0：未回访 1：回访不符合 2：回访符合 
	
	private String createTime;
	
	private String statusName;
	
	private String provinceName;
	
	
	private String cityName;
	
	
	private String isConnectName;
	
	private String proxyId;
	
	
	

	public String getProxyId() {
		return proxyId;
	}

	public void setProxyId(String proxyId) {
		this.proxyId = proxyId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getIsConnectName() {
		return isConnectName;
	}

	public void setIsConnectName(String isConnectName) {
		this.isConnectName = isConnectName;
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

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	
}
