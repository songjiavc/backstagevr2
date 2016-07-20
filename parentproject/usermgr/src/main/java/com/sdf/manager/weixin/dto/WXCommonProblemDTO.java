package com.sdf.manager.weixin.dto;


public class WXCommonProblemDTO {

	private String id;
	
	private String title;//标题
	
	
	private String content;//问题内容
	
	private String createTime;//创建时间
	
	private String creater;//创建人


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getContent() {
		return content;
	}


	public void setContent(String content) {
		this.content = content;
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
	
	
	
}
