package com.bs.outer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/** 
  * @ClassName: Fast3Analysis 
  * @Description:  
  * @author songjia
  * @date Feb 25, 2016 1:42:50 PM 
  *  
  */
@Entity
public class Fast3Analysis{
	@Id
	@Column(name="ISSUE_NUMBER")
	private String issueNumber;
	
	@Column(name="GROUP_NUMBER")
	private int groupNumber;
	
	@Column(name="CURRENT_MISS")
	private Integer currentMiss;
	
	@Column(name="MAX_MISS")
	private int maxMiss;
	
	@Column(name="TYPE")
	private int type;

	public String getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}

	public int getGroupNumber() {
		return groupNumber;
	}

	public void setGroupNum(int groupNumber) {
		this.groupNumber = groupNumber;
	}

	public Integer getCurrentMiss() {
		return currentMiss;
	}

	public void setCurrentMiss(Integer currentMiss) {
		this.currentMiss = currentMiss;
	}

	public int getMaxMiss() {
		return maxMiss;
	}

	public void setMaxMiss(int maxMiss) {
		this.maxMiss = maxMiss;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}	
}