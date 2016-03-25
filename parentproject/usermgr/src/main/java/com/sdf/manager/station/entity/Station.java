package com.sdf.manager.station.entity;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.appversion.entity.RelaBsCurAppverAndSta;
import com.sdf.manager.appversion.entity.RelaBsHisAppverAndSta;
import com.sdf.manager.order.entity.FoundOrderStatus;
import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.user.entity.BaseEntiry;
import com.sdf.manager.userGroup.entity.UserGroup;


/** 
  * @ClassName: Station 
  * @Description: 站点信息实体 
  * @author songj@sdfcp.com
  * @date 2015年10月21日 上午10:43:25 
  *  
  */
@Entity
@Table(name="T_SDF_STATIONS")
public class Station extends BaseEntiry implements Serializable 
{
	
    
	/** 
	  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	  */ 
	private static final long serialVersionUID = 372148302787546454L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="CODE")
	private String code;
	
	@Column(name="STATION_NUMBER")
	private String stationNumber;
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="PROVINCE_CODE")
	private String provinceCode;
	
	@Column(name="CITY_CODE")
	private String cityCode;
	
	@Column(name="REGION_CODE")
	private String regionCode;
	
	@Column(name="ADDRESS")
	private String address;
	
	@Column(name="OWNER")
	private String owner;
	
	@Column(name="OWNER_TELEPHONE")
	private String ownerTelephone;
	
	@Column(name="STATION_TYPE")
	private String stationType;
	
	@Column(name="STATUS")
	private String status;
	
	@Column(name="AGENT_ID")
	private String agentId;//上级代理id
	
	
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
	@JoinTable(name = "RELA_BS_STATION_AND_UGROUP", 
		joinColumns = {  @JoinColumn(name = "STATION_ID", referencedColumnName = "id")  }, 
		inverseJoinColumns = {@JoinColumn(name = "USER_GROUP_ID", referencedColumnName = "id") })
	private List<UserGroup> userGroups;//station与ugroup为双主控
	


	//与“当前通行证使用版本记录表”关联
	@OneToMany(mappedBy = "station", fetch = FetchType.LAZY) 
	private List<RelaBsCurAppverAndSta> relaBsCurAppverAndStas;
	
	//与"历史通行证使用版本记录表"关联
	@OneToMany(mappedBy = "station", fetch = FetchType.LAZY) 
	private List<RelaBsHisAppverAndSta> relaBsHisAppverAndStas;
	
	
	@OneToMany(mappedBy = "station", fetch = FetchType.LAZY) 
	private List<Orders> orders;
	
	@OneToMany(mappedBy = "station", fetch = FetchType.LAZY) 
	private List<RelaBsStationAndApp> relaBsStationAndApps;//通行证与“通行证应用表”的关联
	
	@OneToMany(mappedBy = "station", fetch = FetchType.LAZY) 
	private List<RelaBsStationAndAppHis> relaBsStationAndAppHis;//通行证与“通行证应用表历史记录表”的关联
	
	

	public List<RelaBsStationAndApp> getRelaBsStationAndApps() {
		return relaBsStationAndApps;
	}

	public void setRelaBsStationAndApps(
			List<RelaBsStationAndApp> relaBsStationAndApps) {
		this.relaBsStationAndApps = relaBsStationAndApps;
	}

	public List<RelaBsStationAndAppHis> getRelaBsStationAndAppHis() {
		return relaBsStationAndAppHis;
	}

	public void setRelaBsStationAndAppHis(
			List<RelaBsStationAndAppHis> relaBsStationAndAppHis) {
		this.relaBsStationAndAppHis = relaBsStationAndAppHis;
	}

	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public List<RelaBsCurAppverAndSta> getRelaBsCurAppverAndStas() {
		return relaBsCurAppverAndStas;
	}

	public void setRelaBsCurAppverAndStas(
			List<RelaBsCurAppverAndSta> relaBsCurAppverAndStas) {
		this.relaBsCurAppverAndStas = relaBsCurAppverAndStas;
	}

	public List<RelaBsHisAppverAndSta> getRelaBsHisAppverAndStas() {
		return relaBsHisAppverAndStas;
	}

	public void setRelaBsHisAppverAndStas(
			List<RelaBsHisAppverAndSta> relaBsHisAppverAndStas) {
		this.relaBsHisAppverAndStas = relaBsHisAppverAndStas;
	}

	public List<UserGroup> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(List<UserGroup> userGroups) {
		this.userGroups = userGroups;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getRegionCode() {
		return regionCode;
	}

	public void setRegionCode(String regionCode) {
		this.regionCode = regionCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(String stationNumber) {
		this.stationNumber = stationNumber;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOwnerTelephone() {
		return ownerTelephone;
	}

	public void setOwnerTelephone(String ownerTelephone) {
		this.ownerTelephone = ownerTelephone;
	}

	public String getStationType() {
		return stationType;
	}

	public void setStationType(String stationType) {
		this.stationType = stationType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
