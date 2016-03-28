package com.sdf.manager.product.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.user.entity.BaseEntiry;

@Entity
@Table(name="T_SDF_AREACOUNTY")
public class District extends BaseEntiry implements Serializable {

	/** 
	  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
	  */ 
	private static final long serialVersionUID = 3995552752646634059L;

	@Id
	@Column(name="AID", nullable=false, length=11)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="Acode")
	private String acode;
	
	@Column(name="Aname")
	private String aname;
	
	@Column(name="CityCode")
	private String cityCode;
	
	@Column(name="ArecordCreationDate")
	private Timestamp arecordCreationDate;
	
	@Column(name="ArecordCreator")
	private String arecordCreator;
	
	@Column(name="ArecordVersion")
	private String arecordVersion;
	
	@Column(name="CrecordCreationDate")
	private Timestamp crecordCreationDate;
	
	@Column(name="CrecordCreator")
	private String crecordCreator;
	
	@Column(name="CrecordVersion")
	private String crecordVersion;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAcode() {
		return acode;
	}

	public void setAcode(String acode) {
		this.acode = acode;
	}

	public String getAname() {
		return aname;
	}

	public void setAname(String aname) {
		this.aname = aname;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public Timestamp getArecordCreationDate() {
		return arecordCreationDate;
	}

	public void setArecordCreationDate(Timestamp arecordCreationDate) {
		this.arecordCreationDate = arecordCreationDate;
	}

	public String getArecordCreator() {
		return arecordCreator;
	}

	public void setArecordCreator(String arecordCreator) {
		this.arecordCreator = arecordCreator;
	}

	public String getArecordVersion() {
		return arecordVersion;
	}

	public void setArecordVersion(String arecordVersion) {
		this.arecordVersion = arecordVersion;
	}

	public Timestamp getCrecordCreationDate() {
		return crecordCreationDate;
	}

	public void setCrecordCreationDate(Timestamp crecordCreationDate) {
		this.crecordCreationDate = crecordCreationDate;
	}

	public String getCrecordCreator() {
		return crecordCreator;
	}

	public void setCrecordCreator(String crecordCreator) {
		this.crecordCreator = crecordCreator;
	}

	public String getCrecordVersion() {
		return crecordVersion;
	}

	public void setCrecordVersion(String crecordVersion) {
		this.crecordVersion = crecordVersion;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	
	
	
}
