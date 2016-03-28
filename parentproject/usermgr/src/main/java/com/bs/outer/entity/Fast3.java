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
public class Fast3 {
	
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
	@Column(name="THREE_SUM")
	private int threeSum;
	@Column(name="THREE_SPAN")
	private int threeSpan;
	@Column(name="BIG_COUNT")
	private int bigCount;
	@Column(name="SMALL_COUNT")
	private int smallCount;
	@Column(name="ODD_COUNT")
	private int oddCount;
	@Column(name="EVEN_COUNT")
	private int evenCount;
	@Column(name="NUM_STATUS")
	private int numStatus;

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

	public int getThreeSum() {
		return threeSum;
	}

	public void setThreeSum(int threeSum) {
		this.threeSum = threeSum;
	}

	public int getThreeSpan() {
		return threeSpan;
	}

	public void setThreeSpan(int threeSpan) {
		this.threeSpan = threeSpan;
	}

	public int getBigCount() {
		return bigCount;
	}

	public void setBigCount(int bigCount) {
		this.bigCount = bigCount;
	}

	public int getSmallCount() {
		return smallCount;
	}

	public void setSmallCount(int smallCount) {
		this.smallCount = smallCount;
	}

	public int getOddCount() {
		return oddCount;
	}

	public void setOddCount(int oddCount) {
		this.oddCount = oddCount;
	}

	public int getEvenCount() {
		return evenCount;
	}

	public void setEvenCount(int evenCount) {
		this.evenCount = evenCount;
	}

	public int getNumStatus() {
		return numStatus;
	}

	public void setNumStatus(int numStatus) {
		this.numStatus = numStatus;
	}

}