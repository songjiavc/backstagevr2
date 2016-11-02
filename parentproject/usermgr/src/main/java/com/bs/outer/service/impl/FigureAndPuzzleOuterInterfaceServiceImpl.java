package com.bs.outer.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bs.outer.dto.FigureAndPuzzlesOuterDTO;
import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeDTiming;
import com.bs.outer.repository.ShuangSQRepository;
import com.bs.outer.repository.ThreeDTimingRepository;
import com.bs.outer.service.FigureAndPuzzleOuterInterfaceService;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzlesRepository;

@Service("figureAndPuzzleOuterInterfaceService")
@Transactional(propagation = Propagation.REQUIRED)
public class FigureAndPuzzleOuterInterfaceServiceImpl implements
		FigureAndPuzzleOuterInterfaceService 
{
	@Autowired
	private ThreeDTimingRepository threeDTimingRepository;
	
	@Autowired
	private ShuangSQRepository shuangSQRepository;
	
	@Autowired
	private FigureAndPuzzlesRepository figureAndPuzzlesRepository;

	public ThreeDTiming get3DNumKaijiang(String issuenum) 
	{
		StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,TEST_NUM FROM analysis.T_DATA_BASE_3D ");
		execSql.append(" WHERE ISSUE_NUMBER = '"+issuenum+"' ");
		Object[] queryParams = new Object[]{
		};
		List<ThreeDTiming> threeDList =threeDTimingRepository.getEntityListBySql(ThreeDTiming.class,execSql.toString(), queryParams);
		
		ThreeDTiming threeDTiming = new ThreeDTiming();
		
		if(null != threeDList && threeDList.size()>0)
		{
			threeDTiming = threeDList.get(0);
		}
		
		return threeDTiming;
	}

	public List<ThreeDTiming> get3DNumKaijiangTodayInhistory(String issuenum) 
	{
		String subIssuenum = issuenum.substring(4, 7);//substring 是包含开始坐标值不包括结束位置值
		StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,TEST_NUM FROM analysis.T_DATA_BASE_3D ");
		execSql.append(" WHERE ISSUE_NUMBER like '%"+subIssuenum+"' AND ISSUE_NUMBER != '"+issuenum+"' ");
		Object[] queryParams = new Object[]{
		};
		List<ThreeDTiming> threeDList =threeDTimingRepository.getEntityListBySql(ThreeDTiming.class,execSql.toString(), queryParams);
		
		
		return threeDList;
	}

	public ShuangSQ getShuangSQNumKaijiang(String issuenum) 
	{
		StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7 FROM analysis.T_DATA_BASE_SHUANG ");
 		execSql.append(" WHERE ISSUE_NUMBER = '"+issuenum+"' ");
		Object[] queryParams = new Object[]{
		};
		List<ShuangSQ> shuangSQList = shuangSQRepository.getEntityListBySql(ShuangSQ.class,execSql.toString(), queryParams);
		
		ShuangSQ shuangSQ = new ShuangSQ();
		
		if(null != shuangSQList && shuangSQList.size()>0)
		{
			shuangSQ = shuangSQList.get(0);
		}
		
		return shuangSQ;
	}

	public List<ShuangSQ> getShuangSQNumKaijiangTodayInhistory(
			String issuenum) 
	{
		String subIssuenum = issuenum.substring(4, 7);//substring 是包含开始坐标值不包括结束位置值
		StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7 FROM analysis.T_DATA_BASE_SHUANG ");
		execSql.append(" WHERE ISSUE_NUMBER like '%"+subIssuenum+"' AND ISSUE_NUMBER != '"+issuenum+"' ");
		Object[] queryParams = new Object[]{
		};
		List<ShuangSQ> shuangSQList = shuangSQRepository.getEntityListBySql(ShuangSQ.class,execSql.toString(), queryParams);
		
		
		return shuangSQList;
	}

	public List<FigureAndPuzzles> getFigureAndPuzzles(String issuenum,
			String playName, String province, String city, String lotteryType) {
		
		//放置分页参数
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);
		
		//参数
		StringBuffer buffer = new StringBuffer();
		List<Object> params = new ArrayList<Object>();
		
		//只查询未删除数据
		params.add("1");//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		//连接期号
		if(null != issuenum)
		{
			params.add(issuenum);
			buffer.append(" and playNum = ?").append(params.size());
		}
		
		
		if(null != playName)
		{
			params.add(playName);
			buffer.append(" and playName = ?").append(params.size());
		}
		
		
		if(null != lotteryType)
		{
			params.add(lotteryType);
			buffer.append(" and lotteryType = ?").append(params.size());
		}
		
		//2016-10-27 write,TODO:暂时先不按区域展示图谜字谜，若有区域限制时，则需要重新构建sql进行查询，因为涉及到多表连接
		
		//排序
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("createrTime", "desc");
		
		QueryResult<FigureAndPuzzles> exQueryResult = figureAndPuzzlesRepository.getScrollDataByJpql
				(FigureAndPuzzles.class,
				buffer.toString(), params.toArray(),orderBy, pageable);
		
		List<FigureAndPuzzles> figureAndPuzzles = exQueryResult.getResultList();
		
		return figureAndPuzzles;
	}
	
	public FigureAndPuzzlesOuterDTO toDTO(FigureAndPuzzles entity)
	{
		FigureAndPuzzlesOuterDTO dto = new FigureAndPuzzlesOuterDTO();
		
		if(null != entity)
		{
			try {
				BeanUtils.copyProperties(dto, entity);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		return dto;
	}
	
	public List<FigureAndPuzzlesOuterDTO> toDTOs(List<FigureAndPuzzles> entities)
	{
		List<FigureAndPuzzlesOuterDTO> dtos = new ArrayList<FigureAndPuzzlesOuterDTO>();
		
		for (FigureAndPuzzles figureAndPuzzles : entities) 
		{
			FigureAndPuzzlesOuterDTO dto = this.toDTO(figureAndPuzzles);
			
			dtos.add(dto);
		}
		
		return dtos;
	}
	
}
