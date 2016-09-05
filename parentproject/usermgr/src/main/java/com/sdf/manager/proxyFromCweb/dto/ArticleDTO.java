package com.sdf.manager.proxyFromCweb.dto;

public class ArticleDTO {

	private String id;
	
	private String title;//文章标题
	
	private String content;//文章内容
	
	private String img;//文章图片
	
	private String creater;
	
	/** 
	  * @Fields createrTime : TODO(用一句话描述这个变量表示什么) 
	  */ 
	private String createrTime;

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

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
	}

	public String getCreaterTime() {
		return createrTime;
	}

	public void setCreaterTime(String createrTime) {
		this.createrTime = createrTime;
	}
	
	
	
	
}
