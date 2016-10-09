package com.sdf.manager.figureMysteryPuzzlesApp.entity;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;




@Entity
@Table(name="T_BYL_FOUND_FIGURE_AND_PUZZLE_STATUS")
public class FoundFigureAndPuzzleStatus {

	@Id
	@Column(name="ID", nullable=false, length=45)
	@GenericGenerator(name="idGenerator", strategy="uuid")//uuid由机器生成的主键
	@GeneratedValue(generator="idGenerator")	
	private String id;
	
	
	@Column(name="STATUS", length=2)
	private String status;//状态
	
	@Column(name="REASON")
	private String reason;//如果审批驳回，添加驳回理由
	
	@Column(name="STATUS_SJ", length=11)
	private Timestamp statusSj;//操作状态时间
	
	@Column(name="CREATOR", length=45)
	private String creator;//状态操作人
	
    @ManyToOne  
    @JoinColumn(name = "FIGURE_AND_PUZZLE_ID", referencedColumnName = "id")
    private FigureAndPuzzles figureAndPuzzles;
    
    
    
    



	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public FigureAndPuzzles getFigureAndPuzzles() {
		return figureAndPuzzles;
	}

	public void setFigureAndPuzzles(FigureAndPuzzles figureAndPuzzles) {
		this.figureAndPuzzles = figureAndPuzzles;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Timestamp getStatusSj() {
		return statusSj;
	}

	public void setStatusSj(Timestamp statusSj) {
		this.statusSj = statusSj;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	
	
}
