package com.bs.outer.service;

import java.util.List;

import com.bs.outer.dto.FigureAndPuzzlesOuterDTO;
import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeDTiming;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;

public interface FigureAndPuzzleOuterInterfaceService
{
	/**
	 * 
	* @Title: get3DNumKaijiang 
	* @Description: TODO(根据期号获取3d开奖号码数据) 
	* @param @param issuenum
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月27日 上午9:24:12 
	* @return ThreeDTiming    返回类型 
	* @throws
	 */
	public ThreeDTiming get3DNumKaijiang(String issuenum);
	
	/**
	 * 
	* @Title: get3DNumKaijiangTodayInhistory 
	* @Description: TODO(获取当前期号的历史的今天的数据，不包括当前期号) 
	* @param @param subIssuenum
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月27日 上午9:24:27 
	* @return ThreeDTiming    返回类型 
	* @throws
	 */
	public List<ThreeDTiming> get3DNumKaijiangTodayInhistory(String issuenum);
	
	/**
	 * 
	* @Title: getShuangSQNumKaijiang 
	* @Description: TODO(根据期号获取双色球开奖号码数据) 
	* @param @param issuenum
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月27日 上午9:25:41 
	* @return ShuangSQ    返回类型 
	* @throws
	 */
	public ShuangSQ getShuangSQNumKaijiang(String issuenum);
	
	
	/**
	 * 
	* @Title: getShuangSQNumKaijiangTodayInhistory 
	* @Description: TODO(获取当前期号的历史的今天的数据，不包括当前期号) 
	* @param @param subIssuenum
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月27日 上午9:26:04 
	* @return List<ShuangSQ>    返回类型 
	* @throws
	 */
	public List<ShuangSQ> getShuangSQNumKaijiangTodayInhistory(String issuenum);
	
	/**
	 * 
	* @Title: getFigureAndPuzzles 
	* @Description: TODO(根据条件查询图谜字谜数据) 
	* @param @param issuenum
	* @param @param playName
	* @param @param province
	* @param @param city
	* @param @param lotteryType
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月27日 上午9:27:40 
	* @return List<FigureAndPuzzles>    返回类型 
	* @throws
	 */
	public List<FigureAndPuzzles> getFigureAndPuzzles(String issuenum,String playName,String province,String city,String lotteryType);
	
	
	public List<FigureAndPuzzlesOuterDTO> toDTOs(List<FigureAndPuzzles> entities);
	
	
	public FigureAndPuzzlesOuterDTO toDTO(FigureAndPuzzles entity);
}
