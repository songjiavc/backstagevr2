package com.bs.outer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/** 
  * @ClassName: Fast3 
  * @Description: 快3 
  * @author songj@sdfcp.com
  * @date Feb 22, 2016 4:30:49 PM 
  *  
  */
@Entity
public class ThreeD {
	
	@Id
	@Column(name="ID", nullable=false, length=45)
	private int id;
	@Column(name="ISSUE_NUMBER")
	private String issueNumber;
	@Column(name="NO1")
	private int no1;
	@Column(name="NO2")
	private int no2;
	@Column(name="NO3")
	private int no3;

	public String getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}

	public int getNo1() {
		return no1;
	}

	public void setNo1(int no1) {
		this.no1 = no1;
	}

	public int getNo2() {
		return no2;
	}

	public void setNo2(int no2) {
		this.no2 = no2;
	}

	public int getNo3() {
		return no3;
	}

	public void setNo3(int no3) {
		this.no3 = no3;
	}

}