package com.bs.outer.dto;

import java.util.List;

/** 
 * @ClassName: StationDto 
 * @Description: 前后台传递数据 
 * @author songj@sdfcp.com
 * @date 2015年11月13日 下午2:02:07 
 *  
 */
public class StationOuterDTO {

	
		//主键
		private String id;
		//站点登录号
		private String stationCode;
		//站点号
		private String stationNumber;
		//省份
		private String province;
		//市
		private String city;
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
		
		private List<AppListDTO> appList;
		
		
		
		
		public List<AppListDTO> getAppList() {
			return appList;
		}
		public void setAppList(List<AppListDTO> appList) {
			this.appList = appList;
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
		
		
}
