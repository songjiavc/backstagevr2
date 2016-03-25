package com.sdf.manager.ad.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;





@Entity
@Table(name="T_BS_STATION_AD_NEXT_STATUS")
public class StationAdNextStatus {

	@Id
	@Column(name="ID", nullable=false, length=11)
	private String id;
	
	
	@Column(name="current_status_id", length=2)
	private String currentStatusId;//当前流程状态ID(小状态)
	
	@Column(name="next_status_id", length=2)
	private String nextStatusId;//下一流程状态码(小状态)
	
	@Column(name="direction_flag", length=2)
	private String directionFlag;//流向标志：:0:Backward 1:Forward 2:复审通过

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentStatusId() {
		return currentStatusId;
	}

	public void setCurrentStatusId(String currentStatusId) {
		this.currentStatusId = currentStatusId;
	}

	public String getNextStatusId() {
		return nextStatusId;
	}

	public void setNextStatusId(String nextStatusId) {
		this.nextStatusId = nextStatusId;
	}

	public String getDirectionFlag() {
		return directionFlag;
	}

	public void setDirectionFlag(String directionFlag) {
		this.directionFlag = directionFlag;
	}
	
    
    
	

	
	
}
