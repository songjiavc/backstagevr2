package com.sdf.manager.userGroup.entity;


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

import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.user.entity.BaseEntiry;

/**
 * 
* @ClassName: UserGroup
* @Description: 通行证组实体
* @author banna
* @date 2016年1月25日 下午4:11:46
*
 */
@Entity
@Table(name="T_BS_USER_GROUP")
public class UserGroup extends BaseEntiry{

	
	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="USER_GROUP_NAME", length=45)
	private String userGroupName;//通行证组名称
	
	
	@Column(name="USER_GROUP_CODE", length=45)
	private String userGroupCode;//通行证组编码
	
	@Column(name="USER_GROUP_DESCRIPTION", length=200)
	private String userGroupDescription;//通行证组描述
	
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_STATION_AND_UGROUP", 
            joinColumns = {  @JoinColumn(name = "USER_GROUP_ID", referencedColumnName = "id")  }, 
            inverseJoinColumns = {@JoinColumn(name = "STATION_ID", referencedColumnName = "id") })
	private List<Station> stations;
	
	/**
	 * 通行证组与应用广告的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BS_APPAD_AND_UGROUP", 
    joinColumns = {  @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id")  }, 
    inverseJoinColumns = {@JoinColumn(name = "APP_AD_ID", referencedColumnName = "id") })
	private List<Advertisement> advertisements;
	
	/**
	 * 通行证组与应用公告的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "RELA_BS_NOTICE_AND_UGROUP", 
    joinColumns = {  @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id")  }, 
    inverseJoinColumns = {@JoinColumn(name = "APP_NOTICE_ID", referencedColumnName = "id") })
	private List<Notice> notices;
	
	/**
	 * 通行证组与通告的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "RELA_BS_ANN_AND_UGROUP", 
    joinColumns = {  @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id")  }, 
    inverseJoinColumns = {@JoinColumn(name = "ANNOUNCE_ID", referencedColumnName = "id") })
	private List<Announcement> announcements;
	
	/**
	 * 通行证组与公司公告的关联
	 */
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "RELA_BS_COMNOTICE_AND_UGROUP", 
	    joinColumns = {  @JoinColumn(name = "USERGROUP_ID", referencedColumnName = "id")  }, 
	    inverseJoinColumns = {@JoinColumn(name = "COM_NOTICE_ID", referencedColumnName = "id") })
	private List<CompanyNotice> companyNotices;
	
	


	public List<Announcement> getAnnouncements() {
		return announcements;
	}


	public void setAnnouncements(List<Announcement> announcements) {
		this.announcements = announcements;
	}


	public List<CompanyNotice> getCompanyNotices() {
		return companyNotices;
	}


	public void setCompanyNotices(List<CompanyNotice> companyNotices) {
		this.companyNotices = companyNotices;
	}


	public List<Notice> getNotices() {
		return notices;
	}


	public void setNotices(List<Notice> notices) {
		this.notices = notices;
	}


	public List<Advertisement> getAdvertisements() {
		return advertisements;
	}


	public void setAdvertisements(List<Advertisement> advertisements) {
		this.advertisements = advertisements;
	}


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


	public List<Station> getStations() {
		return stations;
	}


	public void setStations(List<Station> stations) {
		this.stations = stations;
	}
	
	
	
	
	
}
