package com.sdf.manager.figureMysteryPuzzlesApp.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;





@Entity
@Table(name="T_BYL_FIGURE_AND_PUZZLE_NEXT_STATUS")
public class FigureAndPuzzleNextStatus {

	@Id
	@Column(name="ID", nullable=false, length=11)
	private String id;
	
	
	@Column(name="CURRENT_STATUS_ID", length=2)
	private String currentStatusId;//当前流程状态ID(小状态)
	
	@Column(name="NEXT_STATUS_ID", length=2)
	private String nextStatusId;//下一流程状态码(小状态)
	
	@Column(name="DIRECTION_FLAG", length=2)
	private String directionFlag;//流向标志：:0:Backward 1:Forward 2:复审通过

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCurrentStatusId() {
		return currentStatusId;
	}

	public void setCurrentStatusId(String currentStatusId) {
		this.currentStatusId = currentStatusId;
	}

	public String getNextStatusId() {
		return nextStatusId;
	}

	public void setNextStatusId(String nextStatusId) {
		this.nextStatusId = nextStatusId;
	}

	public String getDirectionFlag() {
		return directionFlag;
	}

	public void setDirectionFlag(String directionFlag) {
		this.directionFlag = directionFlag;
	}
	
    
    
	

	
	
}
