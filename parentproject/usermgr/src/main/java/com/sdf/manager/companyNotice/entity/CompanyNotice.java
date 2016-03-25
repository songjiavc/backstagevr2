package com.sdf.manager.companyNotice.entity;

import java.sql.Timestamp;
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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.announcement.entity.AnnouncementAndArea;
import com.sdf.manager.user.entity.BaseEntiry;
import com.sdf.manager.userGroup.entity.UserGroup;


@Entity
@Table(name="T_BS_COM_NOTICE")
public class CompanyNotice extends BaseEntiry{

	@Id
	@Column(name="ID", nullable=false, length=45)
//	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
//	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="COMNOTICE_NAME", length=45)
	private String comnoticeName;//公司公告名称
	
	
	@Column(name="COMNOTICE_CONTENT", length=255)
	private String comnoticeContent;//公司公告内容
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//彩种
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	

	@Column(name="COMNOTICE_STATUS", length=45)
	private String comnoticeStatus;//公司公告的状态，0:保存1：发布
	
	/**
	 * 公司公告与通行证组的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_COMNOTICE_AND_UGROUP", 
            joinColumns = {  @JoinColumn(name = "COM_NOTICE_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id") })
	private List<UserGroup> userGroups;
	
	@OneToMany(mappedBy = "companyNotice", fetch = FetchType.LAZY) 
	private List<ComnoticeAndArea> comnoticeAndAreas;
	
	


	public List<UserGroup> getUserGroups() {
		return userGroups;
	}


	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}


	public List<ComnoticeAndArea> getComnoticeAndAreas() {
		return comnoticeAndAreas;
	}


	public void setComnoticeAndAreas(List<ComnoticeAndArea> comnoticeAndAreas) {
		this.comnoticeAndAreas = comnoticeAndAreas;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getComnoticeName() {
		return comnoticeName;
	}


	public void setComnoticeName(String comnoticeName) {
		this.comnoticeName = comnoticeName;
	}


	public String getComnoticeContent() {
		return comnoticeContent;
	}


	public void setComnoticeContent(String comnoticeContent) {
		this.comnoticeContent = comnoticeContent;
	}


	public String getLotteryType() {
		return lotteryType;
	}


	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}


	public Timestamp getStartTime() {
		return startTime;
	}


	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}


	public Timestamp getEndTime() {
		return endTime;
	}


	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}


	public String getComnoticeStatus() {
		return comnoticeStatus;
	}


	public void setComnoticeStatus(String comnoticeStatus) {
		this.comnoticeStatus = comnoticeStatus;
	}

	
	
	

	
	
}
