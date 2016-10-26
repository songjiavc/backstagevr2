package com.sdf.manager.figureMysteryPuzzlesApp.dto;




/**
 * 图谜字谜底板实体的dto
 * @author Administrator
 *
 */
public class FloorOfFigureAndPuzzlesDTO
{

	private String id;
	
	private String floorName;//底板名称（大体描述底板的类型，例如，三句半4行10字大树模板）
	
	private String figureOrPuzzles;//专家发布的是图谜、字谜或全部1：图谜，2：字谜  0：全部 
	
	private String figureOrPuzzlesName;//“图谜”，“字谜”
	
	private String floorDescription;//底板的具体描述
	
	private String floorImg;
	
	private String startCoordinate;//开始坐标
	
	private String fontCss;//字体样式
	
	//一个字谜类型可以对应多个种类的底板
	private String puzzlesTypeId;//若当前底板是属于某个字谜类型的，则这个字段放置对应的这个字谜类型的id
	
	
	
	private String createTime;//创建时间
	
	private String creater;//创建人
	
	

	public String getStartCoordinate() {
		return startCoordinate;
	}

	public void setStartCoordinate(String startCoordinate) {
		this.startCoordinate = startCoordinate;
	}

	public String getFontCss() {
		return fontCss;
	}

	public void setFontCss(String fontCss) {
		this.fontCss = fontCss;
	}

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

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getFigureOrPuzzles() {
		return figureOrPuzzles;
	}

	public void setFigureOrPuzzles(String figureOrPuzzles) {
		this.figureOrPuzzles = figureOrPuzzles;
	}

	public String getFloorDescription() {
		return floorDescription;
	}

	public void setFloorDescription(String floorDescription) {
		this.floorDescription = floorDescription;
	}

	public String getFloorImg() {
		return floorImg;
	}

	public void setFloorImg(String floorImg) {
		this.floorImg = floorImg;
	}

	public String getPuzzlesTypeId() {
		return puzzlesTypeId;
	}

	public void setPuzzlesTypeId(String puzzlesTypeId) {
		this.puzzlesTypeId = puzzlesTypeId;
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
