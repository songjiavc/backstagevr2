package com.sdf.manager.station.application.dto;


/** 
  * @ClassName: StationFormDto 
  * @Description: 添加修改使用的dto
  * @author songj@sdfcp.com
  * @date 2015年11月17日 上午9:55:48 
  *  
  */
public class StationFormDto {
	//主键
	private String id;
	/** 
	  * @Fields addFormAgent : 代理名称
	  */ 
	private String addFormAgent;
	//站点登录号
	private String addFormStationCode;
	//站点类型
	private String addFormStationStyle;
	//站点号
	private String addFormStationNumber;
	//省
	private String addFormProvince;
	//市
	private String addFormCity;
	//区
	private String addFormRegion;
	//详细地址
	private String addFormAddress;
	//站主姓名
	private String addFormName;
	//站主电话
	private String addFormTelephone;
	//密码
	private String password;
	//确认密码
	private String confirmPassword;
	
	private String macAddr;//mac地址
	
	
	public String getMacAddr() {
		return macAddr;
	}
	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddFormAgent() {
		return addFormAgent;
	}
	public void setAddFormAgent(String addFormAgent) {
		this.addFormAgent = addFormAgent;
	}
	public String getAddFormStationCode() {
		return addFormStationCode;
	}
	public void setAddFormStationCode(String addFormStationCode) {
		this.addFormStationCode = addFormStationCode;
	}
	public String getAddFormStationStyle() {
		return addFormStationStyle;
	}
	public void setAddFormStationStyle(String addFormStationStyle) {
		this.addFormStationStyle = addFormStationStyle;
	}
	public String getAddFormStationNumber() {
		return addFormStationNumber;
	}
	public void setAddFormStationNumber(String addFormStationNumber) {
		this.addFormStationNumber = addFormStationNumber;
	}
	public String getAddFormProvince() {
		return addFormProvince;
	}
	public void setAddFormProvince(String addFormProvince) {
		this.addFormProvince = addFormProvince;
	}
	public String getAddFormCity() {
		return addFormCity;
	}
	public void setAddFormCity(String addFormCity) {
		this.addFormCity = addFormCity;
	}
	public String getAddFormRegion() {
		return addFormRegion;
	}
	public void setAddFormRegion(String addFormRegion) {
		this.addFormRegion = addFormRegion;
	}
	public String getAddFormAddress() {
		return addFormAddress;
	}
	public void setAddFormAddress(String addFormAddress) {
		this.addFormAddress = addFormAddress;
	}
	public String getAddFormName() {
		return addFormName;
	}
	public void setAddFormName(String addFormName) {
		this.addFormName = addFormName;
	}
	public String getAddFormTelephone() {
		return addFormTelephone;
	}
	public void setAddFormTelephone(String addFormTelephone) {
		this.addFormTelephone = addFormTelephone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
