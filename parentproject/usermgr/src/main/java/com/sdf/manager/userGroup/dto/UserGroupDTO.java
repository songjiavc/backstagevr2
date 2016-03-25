package com.sdf.manager.userGroup.dto;



public class UserGroupDTO {

	private String id;
	
	private String userGroupName;//通行证组名称
	
	
	private String userGroupCode;//通行证组编码
	
	private String userGroupDescription;//通行证组描述
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	private String createrPerson;//创建人

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}

	public String getUserGroupCode() {
		return userGroupCode;
	}

	public void setUserGroupCode(String userGroupCode) {
		this.userGroupCode = userGroupCode;
	}

	public String getUserGroupDescription() {
		return userGroupDescription;
	}

	public void setUserGroupDescription(String userGroupDescription) {
		this.userGroupDescription = userGroupDescription;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterPerson() {
		return createrPerson;
	}

	public void setCreaterPerson(String createrPerson) {
		this.createrPerson = createrPerson;
	}
	
	
	
	
}
