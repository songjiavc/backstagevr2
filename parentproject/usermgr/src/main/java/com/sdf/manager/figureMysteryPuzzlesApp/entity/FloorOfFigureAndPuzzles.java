package com.sdf.manager.figureMysteryPuzzlesApp.entity;

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
 * 图谜、字谜底板表实体
 * @author Administrator
 *
 */
@Entity
@Table(name="T_BYL_FLOOR_OF_FIGURE_AND_PUZZLES")
public class FloorOfFigureAndPuzzles extends BaseEntiry implements Serializable 
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -666749332723416091L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="FLOOR_NAME")
	private String floorName;//底板名称（大体描述底板的类型，例如，三句半4行10字大树模板）
	
	@Column(name="FIGURE_OR_PUZZLES")
	private String figureOrPuzzles;//专家发布的是图谜、字谜或全部1：图谜，2：字谜  0：全部 
	
	 //2016-10-25 ADD
	@Column(name="START_COORDINATE")
	private String startCoordinate;//开始坐标
	 //2016-10-25 ADD
	@Column(name="FONT_CSS")
	private String fontCss;//字体样式
	
	@Column(name="FLOOR_DESCRIPTION")
	private String floorDescription;//底板的具体描述
	
	@Column(name="FLOOR_IMG")
	private String floorImg;//底板的图片newUUid，对应的是FigureAPuzzleUploadfile的newsUuid，一个newsUuid可以对应多个图片，因为是一种底板类型可以有多种底板
	
	//一个字谜类型可以对应多个种类的底板
	@ManyToOne  
    @JoinColumn(name = "PUZZLE_TYPE_ID", referencedColumnName = "id")
	private PuzzlesType puzzlesType;
	
	

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

	public PuzzlesType getPuzzlesType() {
		return puzzlesType;
	}

	public void setPuzzlesType(PuzzlesType puzzlesType) {
		this.puzzlesType = puzzlesType;
	}
	
	
	
	
	
	
	
	
	
	
}
