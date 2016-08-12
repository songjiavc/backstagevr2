package com.sdf.manager.weixin.dto;


/**
 * 补录方案dto
 * @author Administrator
 *
 */
public class LotteryPlayBuluPlanDTO 
{
	private String id;
	
	private String planName;//方案名称
	
	private String startNumber;
	
	private String endNumber;
	
	
	private String numOrChar;
	
	
	private String otherPlan;
	
	
	private String otherNum;//其他需要计算的字段和方法
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	
	

	public String getOtherNum() {
		return otherNum;
	}

	public void setOtherNum(String otherNum) {
		this.otherNum = otherNum;
	}

	public String getPlanName() {
		return planName;
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
	
	
}
