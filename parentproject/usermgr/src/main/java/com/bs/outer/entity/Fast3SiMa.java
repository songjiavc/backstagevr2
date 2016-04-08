package com.bs.outer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
@Entity
public class Fast3SiMa{
	
	@Id
	@Column(name="ID")
	private int id;
	
	@Column(name="YUCE_ISSUE_START")
	private String yuceIssueStart;
	
	@Column(name="YUCE_ISSUE_STOP")
	private String yuceIssueStop;
	
	@Column(name="DROWN_PLAN")
	private String drownPlan;
	
	@Column(name="DROWN_ISSUE_NUMBER")
	private String drownIssueNumber;
	
	@Column(name="DROWN_NUMBER")
	private String drownNumber;
	
	@Column(name="DROWN_CYCLE")
	private int drownCycle;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="CREATE_TIME")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getYuceIssueStart() {
		return yuceIssueStart;
	}

	public void setYuceIssueStart(String yuceIssueStart) {
		this.yuceIssueStart = yuceIssueStart;
	}

	public String getYuceIssueStop() {
		return yuceIssueStop;
	}

	public void setYuceIssueStop(String yuceIssueStop) {
		this.yuceIssueStop = yuceIssueStop;
	}

	public String getDrownPlan() {
		return drownPlan;
	}

	public void setDrownPlan(String drownPlan) {
		this.drownPlan = drownPlan;
	}

	public String getDrownIssueNumber() {
		return drownIssueNumber;
	}

	public void setDrownIssueNumber(String drownIssueNumber) {
		this.drownIssueNumber = drownIssueNumber;
	}

	public String getDrownNumber() {
		return drownNumber;
	}

	public void setDrownNumber(String drownNumber) {
		this.drownNumber = drownNumber;
	}

	public int getDrownCycle() {
		return drownCycle;
	}

	public void setDrownCycle(int drownCycle) {
		this.drownCycle = drownCycle;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
