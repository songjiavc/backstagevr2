package com.sdf.manager.user.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccountBean 
{
	private String id;
	
	private String code;
	
	private String name;
	
	private String password;
	
	private String parentId;
	
	private String parentName;
	
	private String status;
	
	private List<UserRelaRoleBean> roles = new ArrayList<UserRelaRoleBean>();

	private String telephone;
	
	private String creater;
	
	private String createrTime;
	
	//省
	private String province;
	//市
	private String city;
	//区
	private String country;
	
	
	
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterTime() {
		return createrTime;
	}

	public void setCreaterTime(String createrTime) {
		this.createrTime = createrTime;
	}

	public List<UserRelaRoleBean> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRelaRoleBean> roles) {
		this.roles = roles;
	}

	
	
}
