package com.sdf.manager.figureMysteryPuzzlesApp.dto;

/**
 * 字谜类型实体的dto
 * @author Administrator
 *
 */
public class PuzzleTypeDTO {

	
	private String id;
	
	private String typeName;//字谜类型名称（例如，四字三句半（最多每行4个字））
	
	private String typeCol;//字谜行数
	
	
	private String typeColWordsNum;//字谜每行最多字数
	
	//字谜字数（按照字谜行数和字谜每行最多字数自动计算，例如：4行，最多10字，则是最多40个字，在选择模板添加字谜时按照这个约束字数（包括标点符号），在应用中按行数和最多字数在底板上加载字谜）
	private String typeWordsNum;
	
	
	private String createTime;//创建时间
	
	private String creater;//创建人

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
