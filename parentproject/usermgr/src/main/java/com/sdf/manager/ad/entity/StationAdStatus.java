package com.sdf.manager.ad.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;



@Entity
@Table(name="T_BS_STATION_AD_STATUS")
public class StationAdStatus {

	@Id
	@Column(name="ID", nullable=false, length=11)
	private String id;
	
	@Column(name="statusId", length=2)
	private String statusId;//状态id
	
	@Column(name="statusName", length=20)
	private String statusName;//状态名称
	
	@Column(name="isParent", length=11)
	private int isParent;//子状态标识(1：父状态 0：子状态)
	
	@Column(name="parentStatus", length=2)
	private String parentStatus;//子状态属于哪个父状态 (0:子状态 1：父状态)

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatusId() {
		return statusId;
	}

	public void setStatusId(String statusId) {
		this.statusId = statusId;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public int getIsParent() {
		return isParent;
	}

	public void setIsParent(int isParent) {
		this.isParent = isParent;
	}

	public String getParentStatus() {
		return parentStatus;
	}

	public void setParentStatus(String parentStatus) {
		this.parentStatus = parentStatus;
	}
	
	
}
