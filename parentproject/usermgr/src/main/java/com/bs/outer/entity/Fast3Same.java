package com.bs.outer.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Entity;
@Entity
public class Fast3Same{
	
	@Id
	@Column(name="ID")
	private int id;
	
	@Column(name="CURRENT_ISSUE")
	private String currentIssue;
	
	@Column(name="LOTTORY_NUMBER")
	private String lottoryNumber;
	
	@Column(name="NEXT_ISSUE")
	private String nextIssue;
	
	@Column(name="NEXT_LOTTORY_NUMBER")
	private String nextLottoryNumber;
	
	@Column(name="CREATE_TIME")
	private Date createTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCurrentIssue() {
		return currentIssue;
	}

	public void setCurrentIssue(String currentIssue) {
		this.currentIssue = currentIssue;
	}

	public String getLottoryNumber() {
		return lottoryNumber;
	}

	public void setLottoryNumber(String lottoryNumber) {
		this.lottoryNumber = lottoryNumber;
	}
	
	public String getNextIssue() {
		return nextIssue;
	}

	public void setNextIssue(String nextIssue) {
		this.nextIssue = nextIssue;
	}

	public String getNextLottoryNumber() {
		return nextLottoryNumber;
	}

	public void setNextLottoryNumber(String nextLottoryNumber) {
		this.nextLottoryNumber = nextLottoryNumber;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
