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
public class ThreeDTiming {
	
	@Id
	@Column(name="ID", nullable=false, length=45)
	private int id;
	@Column(name="ISSUE_NUMBER")
	private String issueNumber;
	@Column(name="NO1")
	private Integer no1;
	@Column(name="NO2")
	private Integer no2;
	@Column(name="NO3")
	private Integer no3;
	@Column(name="TEST_NUM")
	private String testNum;

	public String getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}

	

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Integer getNo1() {
		return no1;
	}

	public void setNo1(Integer no1) {
		this.no1 = no1;
	}

	public Integer getNo2() {
		return no2;
	}

	public void setNo2(Integer no2) {
		this.no2 = no2;
	}

	public Integer getNo3() {
		return no3;
	}

	public void setNo3(Integer no3) {
		this.no3 = no3;
	}

	public String getTestNum() {
		return testNum;
	}

	public void setTestNum(String testNum) {
		this.testNum = testNum;
	}
	
	

}