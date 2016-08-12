package com.sdf.manager.weixin.entity;

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

import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.user.entity.BaseEntiry;


/** 
  * @ClassName: LotteryPlayBulufangan 
  * @Description: 彩票补录方案
  * @author banna
  * @date 2015年9月23日 下午5:27:11 
  *  
  */
@Entity
@Table(name="T_BYL_LOTTERYPLAY_BULUFANGAN")
public class LotteryPlayBulufangan extends BaseEntiry implements Serializable 
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
	
	@Column(name="PLAN_NAME", length=45)
	private String planName;//方案名称
	
	@Column(name="START_NUMBER", length=45)
	private String startNumber;//号码选择范围的开始号码是几，11选5是1开始，时时彩是0开始，用来确认前台的补录方案号码以几开始
	
	@Column(name="END_NUMBER", length=45)
	private String endNumber;//号码选择范围的结束号码是几，11选5是11结束，时时彩是9结束，用来确认前台的补录方案号码以几结束
	
	@Column(name="NUM_OR_CHAR", length=45)
	private String numOrChar;//彩种玩法是数字还是汉字，因为有的彩种玩法是汉字的，用此字段来区分，（0：数字，1：汉字或其他）
	
	@Column(name="OTHER_PLAN", length=255)
	private String otherPlan;//若为其他玩法类型，存储其方案
	
	@Column(name="OTHER_NUM", length=255)
	private String otherNum;//其他需要计算的字段和方法
	
	
	
	//与“补录方案表”关联
	@OneToMany(mappedBy = "lotteryPlayBulufangan", fetch = FetchType.LAZY) 
	private List<LotteryPlay> lotteryPlays;

	/**
	 * 一个方案对应多个补录计算字段的关联关系
	 *//*
	@ManyToMany(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinTable(name = "RELA_BYL_LP_AND_LPOTHERNUM", 
    joinColumns = {  @JoinColumn(name = "LP_ID", referencedColumnName = "id")  }, 
    inverseJoinColumns = {@JoinColumn(name = "LP_OTHERNUM_ID", referencedColumnName = "id") })
	private List<LotteryPlayOtherNum> lotteryPlayOtherNums;
	
	
	
	
	public List<LotteryPlayOtherNum> getLotteryPlayOtherNums() {
		return lotteryPlayOtherNums;
	}

	public void setLotteryPlayOtherNums(
			List<LotteryPlayOtherNum> lotteryPlayOtherNums) {
		this.lotteryPlayOtherNums = lotteryPlayOtherNums;
	}*/
	
	
	

	public String getPlanName() {
		return planName;
	}

	public String getOtherNum() {
		return otherNum;
	}

	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}


	public String getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(String startNumber) {
		this.startNumber = startNumber;
	}

	public String getEndNumber() {
		return endNumber;
	}

	public void setEndNumber(String endNumber) {
		this.endNumber = endNumber;
	}

	public String getNumOrChar() {
		return numOrChar;
	}

	public void setNumOrChar(String numOrChar) {
		this.numOrChar = numOrChar;
	}

	public String getOtherPlan() {
		return otherPlan;
	}

	public void setOtherPlan(String otherPlan) {
		this.otherPlan = otherPlan;
	}

	public List<LotteryPlay> getLotteryPlays() {
		return lotteryPlays;
	}

	public void setLotteryPlays(List<LotteryPlay> lotteryPlays) {
		this.lotteryPlays = lotteryPlays;
	}

	
	
	
	
	
}
