package com.bs.outer.entity;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Fast3DanMa{
	
	@Id
	@Column(name="ID")
	private int id;
	
	@Column(name="ISSUE_NUMBER")
	private String issueNumber;
	
	@Column(name="DANMA_ONE")
	private String danmaOne;
	
	@Column(name="DANMA_TWO")
	private String danmaTwo;
	
	@Column(name="DROWN_NUMBER")
	private String drownNumber;
	
	@Column(name="CREATE_TIME")
	private Date createTime;
	
	@Column(name="STATUS")
	private String status;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}


	public String getDanmaOne() {
		return danmaOne;
	}

	public String getDanmaTwo() {
		return danmaTwo;
	}

	public void setDanmaOne(String danmaOne) {
		this.danmaOne = danmaOne;
	}

	public void setDanmaTwo(String danmaTwo) {
		this.danmaTwo = danmaTwo;
	}

	public String getDrownNumber() {
		return drownNumber;
	}

	public void setDrownNumber(String drownNumber) {
		this.drownNumber = drownNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	
}
