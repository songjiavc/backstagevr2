package com.sdf.manager.announcement.dto;

/**
 * 
 * @ClassName: ReceiptStationDTO
 * @Description: 通告回执通行证实体
 * @author: banna
 * @date: 2016年3月31日 上午9:08:58
 */
public class ReceiptStationDTO 
{
	//主键
	private String id;
	//站点登录号
	private String stationCode;
	//站点号
	private String stationNumber;
	//省份
	private String province;
	
	private String provinceName;
	//市
	private String city;
	
	private String cityName;
	//站点类型
	private String stationStyle;
	//站主姓名
	private String name;
	//站主手机号
	private String telephone;
	//创建时间
	private String createTime;
	//所属代理
	private String agent;
	
	private String agentId;//上级代理id
	
	private String provinceCode;//省份编码
	
	private String cityCode;//市编码
	
	private String statusName;
	
	private String statusTime;
	
	private String status;
	
	

	public String getStatusTime() {
		return statusTime;
	}

	public void setStatusTime(String statusTime) {
		this.statusTime = statusTime;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStationCode() {
		return stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	public String getStationNumber() {
		return stationNumber;
	}

	public void setStationNumber(String stationNumber) {
		this.stationNumber = stationNumber;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStationStyle() {
		return stationStyle;
	}

	public void setStationStyle(String stationStyle) {
		this.stationStyle = stationStyle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getAgent() {
		return agent;
	}

	public void setAgent(String agent) {
		this.agent = agent;
	}

	public String getAgentId() {
		return agentId;
	}

	public void setAgentId(String agentId) {
		this.agentId = agentId;
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
		
		
}
