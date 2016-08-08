package com.bs.outer.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class FollowNumberBean  {
	
	@Id
	@Column(name="ID")
	private int id;
	
	@Column(name="NUMBER")
	private int number;
	
	@Column(name="FOLLOW_NUMBER")
	private int followNumber;
	
	@Column(name="FOLLOW_COUNT")
	private int followCount;
	
	@Column(name="THREE_FOLLOW_COUNT")
	private int threeFollowCount;
	
	@Column(name="NO_FOLLOW_COUNT")
	private int noFollowCount;

	@Column(name="THREE_NO_FOLLOW_COUNT")
	private int threeNoFollowCount;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getFollowNumber() {
		return followNumber;
	}

	public void setFollowNumber(int followNumber) {
		this.followNumber = followNumber;
	}

	public int getFollowCount() {
		return followCount;
	}

	public void setFollowCount(int followCount) {
		this.followCount = followCount;
	}

	public int getThreeFollowCount() {
		return threeFollowCount;
	}

	public void setThreeFollowCount(int threeFollowCount) {
		this.threeFollowCount = threeFollowCount;
	}

	public int getNoFollowCount() {
		return noFollowCount;
	}

	public void setNoFollowCount(int noFollowCount) {
		this.noFollowCount = noFollowCount;
	}

	public int getThreeNoFollowCount() {
		return threeNoFollowCount;
	}

	public void setThreeNoFollowCount(int threeNoFollowCount) {
		this.threeNoFollowCount = threeNoFollowCount;
	}
}
