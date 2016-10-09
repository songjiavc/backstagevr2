package com.sdf.manager.figureMysteryPuzzlesApp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.user.entity.BaseEntiry;



/**
 *字谜类型表实体（基础表）
 * @author Administrator
 *
 */
@Entity
@Table(name="T_BYL_PUZZLES_TYPE")
public class PuzzlesType extends BaseEntiry implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3910134844632669318L;

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="TYPE_NAME")
	private String typeName;//字谜类型名称（例如，四字三句半（最多每行4个字））
	
	@Column(name="TYPE_COL")
	private String typeCol;//字谜行数
	
	@Column(name="TYPE_COL_WORDS_NUM")
	private String typeColWordsNum;//字谜每行最多字数
	
	@Column(name="TYPE_WORDS_NUM")
	private String typeWordsNum;
	//字谜字数（按照字谜行数和字谜每行最多字数自动计算，例如：4行，最多10字，则是最多40个字，在选择模板添加字谜时按照这个约束字数（包括标点符号），在应用中按行数和最多字数在底板上加载字谜）

	//当前设计是：一个字谜类型可以对应多个种类的底板，例如：三句半4行10字类型的字谜对应的模板类型有，大树背景模板，河流背景模板等等，具体模板是什么模板体现在模板名称中
	@OneToMany(mappedBy = "puzzlesType", fetch = FetchType.LAZY) 
	private List<FloorOfFigureAndPuzzles> floorOfFigureAndPuzzles;
	
	/**
	 * 一个字谜类型对应多个图谜字谜数据
	 */
	@OneToMany(mappedBy = "puzzlesType", fetch = FetchType.LAZY) 
	private List<FigureAndPuzzles> figureAndPuzzles;
	
	
	
	public List<FigureAndPuzzles> getFigureAndPuzzles() {
		return figureAndPuzzles;
	}

	public void setFigureAndPuzzles(List<FigureAndPuzzles> figureAndPuzzles) {
		this.figureAndPuzzles = figureAndPuzzles;
	}

	public List<FloorOfFigureAndPuzzles> getFloorOfFigureAndPuzzles() {
		return floorOfFigureAndPuzzles;
	}

	public void setFloorOfFigureAndPuzzles(
			List<FloorOfFigureAndPuzzles> floorOfFigureAndPuzzles) {
		this.floorOfFigureAndPuzzles = floorOfFigureAndPuzzles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getTypeCol() {
		return typeCol;
	}

	public void setTypeCol(String typeCol) {
		this.typeCol = typeCol;
	}

	public String getTypeColWordsNum() {
		return typeColWordsNum;
	}

	public void setTypeColWordsNum(String typeColWordsNum) {
		this.typeColWordsNum = typeColWordsNum;
	}

	public String getTypeWordsNum() {
		return typeWordsNum;
	}

	public void setTypeWordsNum(String typeWordsNum) {
		this.typeWordsNum = typeWordsNum;
	}
	
	
	
	
	
	
	
}
