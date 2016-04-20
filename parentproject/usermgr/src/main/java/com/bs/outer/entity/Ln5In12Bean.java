package com.bs.outer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
@Entity
public class Ln5In12Bean{
	
	@Id
	@Column(name="ID")
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

	public int getId() {
		return id;
	}

	public String getIssueNumber() {
		return issueNumber;
	}

	public int getNo1() {
		return no1;
	}

	public int getNo2() {
		return no2;
	}

	public int getNo3() {
		return no3;
	}

	public int getNo4() {
		return no4;
	}

	public int getNo5() {
		return no5;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}

	public void setNo1(int no1) {
		this.no1 = no1;
	}

	public void setNo2(int no2) {
		this.no2 = no2;
	}

	public void setNo3(int no3) {
		this.no3 = no3;
	}

	public void setNo4(int no4) {
		this.no4 = no4;
	}

	public void setNo5(int no5) {
		this.no5 = no5;
	}

	
}
