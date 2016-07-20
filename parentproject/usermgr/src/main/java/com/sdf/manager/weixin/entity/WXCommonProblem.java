package com.sdf.manager.weixin.entity;


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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.user.entity.BaseEntiry;

/**
 * 
* @ClassName: WXCommentsProblem 
* @Description: 微信公众号使用的，自助菜单的“常见问题”对应的实体类
* @author banna
* @date 2016年7月19日 上午8:37:01 
*
 */
@Entity
@Table(name="T_BS_WEIXIN_COMMEON_PROBLEM")
public class WXCommonProblem extends BaseEntiry{

	
	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="TITLE", length=45)
	private String title;//标题
	
	
	@Column(name="CONTENT", length=500)
	private String content;//问题内容
	
	
	
	//自关联多对多关系配置
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_WXCP_AND_WXCP", 
            joinColumns = {  @JoinColumn(name = "P_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "F_ID", referencedColumnName = "id") })
	private List<WXCommonProblem> commentsProblems;
	



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



	public List<WXCommonProblem> getCommentsProblems() {
		return commentsProblems;
	}



	public void setCommentsProblems(List<WXCommonProblem> commentsProblems) {
		this.commentsProblems = commentsProblems;
	}



	
	
	
	
}
