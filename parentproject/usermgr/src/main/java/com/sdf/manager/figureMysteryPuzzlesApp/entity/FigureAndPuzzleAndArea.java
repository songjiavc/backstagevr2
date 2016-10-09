package com.sdf.manager.figureMysteryPuzzlesApp.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

/**
 * 图谜字谜的区域关联表（在专家发布的图谜字谜通过审批后，审批人员要选择当前图谜字谜发布的区域，发布区域和图谜字谜的关联数据放在这个表里）
 * @author Administrator
 *
 */
@Entity
@Table(name="RELA_BS_FIGURE_AND_PUZZLE_AND_AREA")
public class FigureAndPuzzleAndArea {
	

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@ManyToOne  
    @JoinColumn(name = "FIGURE_AND_PUZZLE_ID", referencedColumnName = "id")
	private FigureAndPuzzles figureAndPuzzle;//与图谜字谜的关联，一个图谜字谜可以发布到多个区域
	
	//省
	@Column(name="PROVINCE", length=45)
	private String province;
	//市
	@Column(name="CITY", length=45)
	private String city;
	//区
	@Column(name="COUNTRY", length=45)
	private String country;
	
	
	
	
	
	public FigureAndPuzzles getFigureAndPuzzle() {
		return figureAndPuzzle;
	}
	public void setFigureAndPuzzle(FigureAndPuzzles figureAndPuzzle) {
		this.figureAndPuzzle = figureAndPuzzle;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	
	

}
