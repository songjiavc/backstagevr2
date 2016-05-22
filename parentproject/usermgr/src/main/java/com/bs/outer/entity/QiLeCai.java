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
public class QiLeCai {
	
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
	@Column(name="NO4")
	private int no4;
	@Column(name="NO5")
	private int no5;
	@Column(name="NO6")
	private int no6;
	@Column(name="NO7")
	private int no7;

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

	public int getId() {
		return id;
	}

	public int getNo4() {
		return no4;
	}

	public int getNo5() {
		return no5;
	}

	public int getNo6() {
		return no6;
	}

	public int getNo7() {
		return no7;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setNo4(int no4) {
		this.no4 = no4;
	}

	public void setNo5(int no5) {
		this.no5 = no5;
	}

	public void setNo6(int no6) {
		this.no6 = no6;
	}

	public void setNo7(int no7) {
		this.no7 = no7;
	}
	
	
}