package com.bs.outer.entity;

public class FollowNumberTempBean implements  Comparable<FollowNumberTempBean>{

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public Integer number;

	public Integer getFollowNumber() {
		return followNumber;
	}

	public void setFollowNumber(Integer followNumber) {
		this.followNumber = followNumber;
	}

	public Integer followNumber;



	public Integer getFollowCount() {
		return followCount;
	}

	public void setFollowCount(Integer followCount) {
		this.followCount = followCount;
	}


	private int followCount;

	public int compareTo(FollowNumberTempBean o) {
		int flag = -1;
		flag = o.getFollowCount().compareTo(this.getFollowCount());
		return flag;
	}

}
