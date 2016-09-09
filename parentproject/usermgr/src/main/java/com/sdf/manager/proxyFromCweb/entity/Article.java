package com.sdf.manager.proxyFromCweb.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Transient;

import com.sdf.manager.user.entity.BaseEntiry;


/** 
  * @ClassName: Article 
  * @Description:公司网站文章实体
  * @date 2015年9月23日 下午5:27:11 
  *  
  */
@Entity
@Table(name="T_BYL_ARTICLE")
public class Article extends BaseEntiry implements Serializable 
{
	
	
	/** 
	  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	  */ 
	private static final long serialVersionUID = -6158098346204363173L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="TITLE")
	private String title;//文章标题
	
	@Column(name="CONTENT",length=16777216)//指定为这个长度后，数据库会自动映射成longtext类型
	private String content;//文章内容
	
	@Column(name="IMG")
	private String img;//文章图片

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
	
	
	
	
	
}
