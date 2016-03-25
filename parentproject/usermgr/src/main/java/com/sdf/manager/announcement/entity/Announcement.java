package com.sdf.manager.announcement.entity;

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

import com.sdf.manager.user.entity.BaseEntiry;
import com.sdf.manager.userGroup.entity.UserGroup;


@Entity
@Table(name="T_BS_ANNOUNCEMENT")
public class Announcement extends BaseEntiry{

	@Id
	@Column(name="ID", nullable=false, length=45)
//	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
//	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="ANNOUNCEMENT_NAME", length=45)
	private String announcementName;//通告名称
	
	
	@Column(name="ANNOUNCEMENT_CONTENT", length=255)
	private String announcementContent;//通告内容
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//彩种
	
	@Column(name="START_TIME")
	private Timestamp startTime;
	
	@Column(name="END_TIME")
	private Timestamp endTime;
	

	@Column(name="ANNOUNCE_STATUS", length=45)
	private String announceStatus;//通告状态，0:保存1：发布
	
	/**
	 * 通告与通行证组的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_ANN_AND_UGROUP", 
            joinColumns = {  @JoinColumn(name = "ANNOUNCE_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id") })
	private List<UserGroup> userGroups;
	
	
	/**
	 * 通告与“通告与区域关联表”的关联
	 */
	@OneToMany(mappedBy = "announcement", fetch = FetchType.LAZY) 
	private List<AnnouncementAndArea> announcementAndAreas;
	
	


	public List<AnnouncementAndArea> getAnnouncementAndAreas() {
		return announcementAndAreas;
	}


	public void setAnnouncementAndAreas(
			List<AnnouncementAndArea> announcementAndAreas) {
		this.announcementAndAreas = announcementAndAreas;
	}


	public List<UserGroup> getUserGroups() {
		return userGroups;
	}


	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getAnnouncementName() {
		return announcementName;
	}


	public void setAnnouncementName(String announcementName) {
		this.announcementName = announcementName;
	}


	public String getAnnouncementContent() {
		return announcementContent;
	}


	public void setAnnouncementContent(String announcementContent) {
		this.announcementContent = announcementContent;
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


	public String getAnnounceStatus() {
		return announceStatus;
	}


	public void setAnnounceStatus(String announceStatus) {
		this.announceStatus = announceStatus;
	}

	
	

	
	
}
