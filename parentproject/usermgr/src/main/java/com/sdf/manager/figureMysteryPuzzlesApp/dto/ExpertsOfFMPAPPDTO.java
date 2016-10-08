package com.sdf.manager.figureMysteryPuzzlesApp.dto;

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
import org.springframework.data.annotation.Transient;

import com.sdf.manager.user.entity.BaseEntiry;

/** 
  * @ClassName: ExpertsOfFMPAPP 
  * @Description: 图谜字谜应用专家实体
  * @author songj@sdfcp.com
  * @date 2015年9月23日 下午5:27:11 
  *  
  */
public class ExpertsOfFMPAPPDTO 
{
	
	

	private String id;
	
	private String code;//登录名
	
	private String name;//专家姓名
	
	private String password;
	
	private String telephone;//专家手机
	
	private String provinceCode;
	
	private String cityCode;
	
	
	private String address;
	
	
	private String lotteryType;//彩种，1：体彩，2：福彩  0：全部 
	
	private String figureOrPuzzles;//专家发布的是图谜、字谜或全部1：图谜，2：字谜  0：全部 
	
	private String figureOrPuzzlesName;//“图谜”，“字谜”
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	//省名称
	private String provinceName;
	//市名称
	private String cityName;
	//区名称
	private String countryName;

	
	
	
	public String getFigureOrPuzzlesName() {
		return figureOrPuzzlesName;
	}

	public void setFigureOrPuzzlesName(String figureOrPuzzlesName) {
		this.figureOrPuzzlesName = figureOrPuzzlesName;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
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

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCreater() {
		return creater;
	}

	public void setCreater(String creater) {
		this.creater = creater;
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

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}

	public String getFigureOrPuzzles() {
		return figureOrPuzzles;
	}

	public void setFigureOrPuzzles(String figureOrPuzzles) {
		this.figureOrPuzzles = figureOrPuzzles;
	}
	
	/*@Transient
	//@ManyToMany注释表示Teacher是多对多关系的一端。
    //@JoinTable描d述了多对多关系的数据表关系。name属性指定中间表名称，joinColumns定义中间表与Teacher表的外键关系。
    //中间表Teacher_Student的Teacher_ID列是Teacher表的主键列对应的外键列，inverseJoinColumns属性定义了中间表与另外一端(Student)的外键关系。
    @ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_SDF_USER_ROLE", 
            joinColumns = { @JoinColumn(name = "USER_ID",referencedColumnName = "id") }, 
            inverseJoinColumns = { @JoinColumn(name = "ROLE_ID",referencedColumnName = "id") })
	private List<Role> roles;
	
	//只对代理有效，代理范围
	@OneToMany(mappedBy = "agentId", fetch = FetchType.LAZY) 
	private List<AgentRange> agentRanges;
	*/
	
	
	
	
	
	
}