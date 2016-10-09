package com.sdf.manager.figureMysteryPuzzlesApp.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.sdf.manager.ad.entity.AppAdAndArea;
import com.sdf.manager.user.entity.BaseEntiry;



/**
 * 图谜、字谜表实体
 * @author Administrator
 *
 */
@Entity
@Table(name="T_BYL_FIGURE_AND_PUZZLES")
public class FigureAndPuzzles extends BaseEntiry implements Serializable 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3615418749236492229L;


	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	@Column(name="NAME", length=45)
	private String name;//发布的图谜字谜的名称
	
	@Column(name="LOTTERY_TYPE", length=45)
	private String lotteryType;//彩种（1：体彩  2:福彩）
	
	
	@Column(name="PLAY_NAME", length=45)
	private String playName;//彩种玩法名称（例如：3d，双色球等等）
	
	@Column(name="FIGURE_OR_PUZZLES")
	private String figureOrPuzzles;//专家发布的是图谜、字谜或全部1：图谜，2：字谜 (专家发布时只可以选择是发布图谜或字谜)
	
	
	@Column(name="IS_COMPANY")
	private String isCompany;//是否为公司虚拟专家发布的图谜、字谜，1：是 0：否（若为公司虚拟专家算法生成的图谜字谜标记都是1，其他专家添加的都是0）
	
	
	
	@Column(name="WORD_IN_IMG")
	private String wordInImg;//图中字图片对应的附件的newsUuid，可以对应多个图中字图片(图片加载时不需要有顺序)
	
	
	@Column(name="FIGURE_IMG")
	private String figureImg;//图谜图片附件id
	
	
	
	//一个字谜类型可以对应多个字谜
	@ManyToOne  
    @JoinColumn(name = "PUZZLE_TYPE_ID", referencedColumnName = "id")
	private PuzzlesType puzzlesType;
	
	//当前图谜字谜对应的底板id
	@ManyToOne  
    @JoinColumn(name = "FLOOR_FIGURE_AND_PUZZLE_ID", referencedColumnName = "id")
	private FloorOfFigureAndPuzzles floorOfFigureAndPuzzles;

	
	@Column(name="FLOOR_UPLOADID")
	private String floorUploadid;//底板附件id
	
	/**
	 * 一个图谜字谜可以发布给多个区域
	 */
	@OneToMany(mappedBy = "figureAndPuzzle", fetch = FetchType.LAZY) 
	private List<FigureAndPuzzleAndArea> figureAndPuzzleAndAreas;
	
	
	

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

	public PuzzlesType getPuzzlesType() {
		return puzzlesType;
	}

	public void setPuzzlesType(PuzzlesType puzzlesType) {
		this.puzzlesType = puzzlesType;
	}

	public FloorOfFigureAndPuzzles getFloorOfFigureAndPuzzles() {
		return floorOfFigureAndPuzzles;
	}

	public void setFloorOfFigureAndPuzzles(
			FloorOfFigureAndPuzzles floorOfFigureAndPuzzles) {
		this.floorOfFigureAndPuzzles = floorOfFigureAndPuzzles;
	}

	public String getFloorUploadid() {
		return floorUploadid;
	}

	public void setFloorUploadid(String floorUploadid) {
		this.floorUploadid = floorUploadid;
	}

	public List<FigureAndPuzzleAndArea> getFigureAndPuzzleAndAreas() {
		return figureAndPuzzleAndAreas;
	}

	public void setFigureAndPuzzleAndAreas(
			List<FigureAndPuzzleAndArea> figureAndPuzzleAndAreas) {
		this.figureAndPuzzleAndAreas = figureAndPuzzleAndAreas;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
