package com.sdf.manager.weixin.dto;


/**
 * 补录信息dto
 * @author Administrator
 *
 */
public class LotteryPlayDTO 
{
	private String id;
	
	private String code;
	
	private String name;//彩种名称
	
	private String province;//彩种省份
	
	private String provinceName;//彩种名称，1：体彩，2：福彩  
	
	private String CorrespondingTable;//彩种对应的补录表
	
	private String lotteryNumber;//开奖号码个数(例如：11选5，开奖号码是5个号码，这个字段就是5)
	
	private String issueNumLen;//期号长度
	
	
	private String lotteryType;//彩种，1：体彩，2：福彩  
	
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	private String ltblPlaneId;//补录方案id
	
	
	


	public String getIssueNumLen() {
		return issueNumLen;
	}

	public void setIssueNumLen(String issueNumLen) {
		this.issueNumLen = issueNumLen;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getLtblPlaneId() {
		return ltblPlaneId;
	}

	public void setLtblPlaneId(String ltblPlaneId) {
		this.ltblPlaneId = ltblPlaneId;
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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCorrespondingTable() {
		return CorrespondingTable;
	}

	public void setCorrespondingTable(String correspondingTable) {
		CorrespondingTable = correspondingTable;
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
