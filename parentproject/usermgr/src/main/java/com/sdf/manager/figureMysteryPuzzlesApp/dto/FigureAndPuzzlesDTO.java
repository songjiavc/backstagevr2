package com.sdf.manager.figureMysteryPuzzlesApp.dto;

import java.sql.Timestamp;


public class FigureAndPuzzlesDTO 
{
	private String id;
	
	private String fAPCode;//图谜字谜编码
	
	private String name;//发布的图谜字谜的名称
	
	private String lotteryType;//彩种（1：体彩  2:福彩）
	
	
	private String figureOrPuzzles;//专家发布的是图谜、字谜或全部1：图谜，2：字谜 (专家发布时只可以选择是发布图谜或字谜)
	
	private String figureOrPuzzlesName;//“图谜”，“字谜”
	
	
	private String isCompany;//是否为公司虚拟专家发布的图谜、字谜，1：是 0：否（若为公司虚拟专家算法生成的图谜字谜标记都是1，其他专家添加的都是0）
	
	private String playName;//彩种玩法名称（例如：3d，双色球等等）
	
	private String playNum;//当前图谜字谜发布给玩法的第多少期对应的期号，这个数据由系统生成填充，在当前彩种开奖前，则只可以发布即将开奖这期的图谜字谜
	
	private String wordInImg;//图中字图片对应的附件的newsUuid，可以对应多个图中字图片
	
	private String puzzleContent;//字谜文字内容（专家发布字谜使用）
	
	
	private String puzzlesTypeId;//对应的字谜类型的id
	
	private String puzzlesTypeName;//字谜类型名称
	
	private String floorUploadid;//底板附件id
	
	private String zimiStatus;//字谜类型，0：输入文字,1：上传字谜图片
	
	private String floorOfFigureAndPuzzlesId;//对应的底板id
	
	private String floorOfFigureAndPuzzlesName;//对应的底板name
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	private String figureImg;//图谜图片附件id
	
	
	private String status;
	private String statusName;
	
	private Timestamp statusTime;
	
	private String rejectReason;//驳回理由

	
	
	
	public String getRejectReason() {
		return rejectReason;
	}


	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}


	public String getPlayNum() {
		return playNum;
	}


	public void setPlayNum(String playNum) {
		this.playNum = playNum;
	}


	public String getFloorOfFigureAndPuzzlesName() {
		return floorOfFigureAndPuzzlesName;
	}


	public void setFloorOfFigureAndPuzzlesName(String floorOfFigureAndPuzzlesName) {
		this.floorOfFigureAndPuzzlesName = floorOfFigureAndPuzzlesName;
	}


	public String getZimiStatus() {
		return zimiStatus;
	}


	public void setZimiStatus(String zimiStatus) {
		this.zimiStatus = zimiStatus;
	}


	public String getPuzzleContent() {
		return puzzleContent;
	}


	public void setPuzzleContent(String puzzleContent) {
		this.puzzleContent = puzzleContent;
	}


	public String getPuzzlesTypeName() {
		return puzzlesTypeName;
	}


	public void setPuzzlesTypeName(String puzzlesTypeName) {
		this.puzzlesTypeName = puzzlesTypeName;
	}


	public String getFigureOrPuzzlesName() {
		return figureOrPuzzlesName;
	}


	public void setFigureOrPuzzlesName(String figureOrPuzzlesName) {
		this.figureOrPuzzlesName = figureOrPuzzlesName;
	}


	public String getfAPCode() {
		return fAPCode;
	}


	public void setfAPCode(String fAPCode) {
		this.fAPCode = fAPCode;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public String getStatusName() {
		return statusName;
	}


	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}


	public Timestamp getStatusTime() {
		return statusTime;
	}


	public void setStatusTime(Timestamp statusTime) {
		this.statusTime = statusTime;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPlayName() {
		return playName;
	}


	public void setPlayName(String playName) {
		this.playName = playName;
	}


	public String getFigureImg() {
		return figureImg;
	}


	public void setFigureImg(String figureImg) {
		this.figureImg = figureImg;
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


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
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


	public String getIsCompany() {
		return isCompany;
	}


	public void setIsCompany(String isCompany) {
		this.isCompany = isCompany;
	}


	public String getWordInImg() {
		return wordInImg;
	}


	public void setWordInImg(String wordInImg) {
		this.wordInImg = wordInImg;
	}


	public String getPuzzlesTypeId() {
		return puzzlesTypeId;
	}


	public void setPuzzlesTypeId(String puzzlesTypeId) {
		this.puzzlesTypeId = puzzlesTypeId;
	}


	public String getFloorUploadid() {
		return floorUploadid;
	}


	public void setFloorUploadid(String floorUploadid) {
		this.floorUploadid = floorUploadid;
	}


	public String getFloorOfFigureAndPuzzlesId() {
		return floorOfFigureAndPuzzlesId;
	}


	public void setFloorOfFigureAndPuzzlesId(String floorOfFigureAndPuzzlesId) {
		this.floorOfFigureAndPuzzlesId = floorOfFigureAndPuzzlesId;
	}
	
	
	
	
	
}
