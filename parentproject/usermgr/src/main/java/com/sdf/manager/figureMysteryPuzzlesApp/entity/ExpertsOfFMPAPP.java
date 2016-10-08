package com.sdf.manager.figureMysteryPuzzlesApp.entity;

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
@Entity
@Table(name="T_BYL_EXPERTS")
public class ExpertsOfFMPAPP extends BaseEntiry implements Serializable 
{
	
	
	/** 
	  * @Fields serialVersionUID : 使用逆向工程时使用
	  */ 
	private static final long serialVersionUID = 9029294793418739543L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="CODE")
	private String code;//登录名
	
	@Column(name="NAME")
	private String name;//专家姓名
	
	@Column(name="PASSWORD")
	private String password;
	
	@Column(name="TELEPHONE")
	private String telephone;//专家手机
	
	@Column(name="PROVINCE_CODE")
	private String provinceCode;
	
	@Column(name="CITY_CODE")
	private String cityCode;
	
	
	@Column(name="ADDRESS")
	private String address;
	
	
	@Column(name="LOTTERY_TYPE")
	private String lotteryType;//彩种，1：体彩，2：福彩  0：全部 
	
	@Column(name="FIGURE_OR_PUZZLES")
	private String figureOrPuzzles;//专家发布的是图谜、字谜或全部1：图谜，2：字谜  0：全部 

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