package com.bs.outer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FollowLastIssueBean {
	
	@Id
	@Column(name="ISSUE_NUMBER")
	private String issueNumber;

	public String getIssueNumber() {
		return issueNumber;
	}

	public void setIssueNumber(String issueNumber) {
		this.issueNumber = issueNumber;
	}
}
