package com.sdf.manager.weixin.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.user.entity.BaseEntiry;


/** 
  * @ClassName: LotteryPlay 
  * @Description: 获取
  * @author banna
  * @date 2015年9月23日 下午5:27:11 
  *  
  */
@Entity
@Table(name="T_BYL_LOTTERYPLAY")
public class LotteryPlay extends BaseEntiry implements Serializable 
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
	
	@Column(name="CODE", length=45)
	private String code;
	
	@Column(name="NAME", length=45)
	private String name;//彩种名称
	
	@Column(name="PROVINCE", length=45)
	private String province;//彩种省份
	
	@Column(name="CORRESPONDINGTABLE", length=45)
	private String correspondingTable;//彩种对应的补录表
	
	@Column(name="LOTTERY_NUMBER", length=45)
	private String lotteryNumber;//开奖号码个数(例如：11选5，开奖号码是5个号码，这个字段就是5)
	
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//彩种，1：体彩，2：福彩  
	
	//与“补录方案表”关联
	@ManyToOne  
    @JoinColumn(name = "LP_ID", referencedColumnName = "id")
	private LotteryPlayBulufangan lotteryPlayBulufangan;

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


	public LotteryPlayBulufangan getLotteryPlayBulufangan() {
		return lotteryPlayBulufangan;
	}

	public void setLotteryPlayBulufangan(LotteryPlayBulufangan lotteryPlayBulufangan) {
		this.lotteryPlayBulufangan = lotteryPlayBulufangan;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}



	public String getCorrespondingTable() {
		return correspondingTable;
	}

	public void setCorrespondingTable(String correspondingTable) {
		this.correspondingTable = correspondingTable;
	}

	public String getLotteryNumber() {
		return lotteryNumber;
	}

	public void setLotteryNumber(String lotteryNumber) {
		this.lotteryNumber = lotteryNumber;
	}



	public String getLotteryType() {
		return lotteryType;
	}

	public void setLotteryType(String lotteryType) {
		this.lotteryType = lotteryType;
	}
	
	
	
	
	
}
