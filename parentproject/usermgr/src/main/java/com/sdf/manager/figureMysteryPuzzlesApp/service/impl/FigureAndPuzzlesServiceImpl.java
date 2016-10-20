package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeDTiming;
import com.bs.outer.repository.ShuangSQRepository;
import com.bs.outer.repository.ThreeDTimingRepository;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.controller.FigureMPuzzlesAppController;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzleStatus;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.PuzzlesType;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzlesRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.ExpertOfFMAPPService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzleStatusService;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzlesService;
import com.sdf.manager.order.entity.OrderStatus;

@Service("figureAndPuzzlesService")
@Transactional(propagation=Propagation.REQUIRED)
public class FigureAndPuzzlesServiceImpl implements FigureAndPuzzlesService {

	@Autowired
	private FigureAndPuzzlesRepository figureAndPuzzlesRepository;
	
	@Autowired
	private FigureAndPuzzleStatusService figureAndPuzzleStatusService;
	
	@Autowired
	private ShuangSQRepository shuangSQRepository;
	
	@Autowired
	private ThreeDTimingRepository threeDTimingRepository;
	
	@Autowired
	private ExpertOfFMAPPService expertOfFMAPPService;
	
	public FigureAndPuzzlesDTO toDTO(FigureAndPuzzles entity) 
	{
		FigureAndPuzzlesDTO dto = new FigureAndPuzzlesDTO();
		
		try {
			BeanUtils.copyProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getCreater())
			{
				ExpertsOfFMPAPP expertsOfFMPAPP = expertOfFMAPPService.getExpertsOfFMPAPPByCode(entity.getCreater());
				dto.setCreaterName(expertsOfFMPAPP.getName());
			}
			
			
			//若当前底板是字谜的底板，则一定有字谜类型，这时要将字谜类型的id返回
			if(null != entity.getFigureOrPuzzles() && FigureMPuzzlesAppController.ZIMI_FLAG.equals(entity.getFigureOrPuzzles()))
			{
				PuzzlesType puzzlesType = entity.getPuzzlesType();
				if(null != puzzlesType)
				{
					dto.setPuzzlesTypeId(puzzlesType.getId());
					dto.setPuzzlesTypeName(puzzlesType.getTypeName());
				}
				else
				{
					dto.setPuzzlesTypeName("图片字谜");
					
				}
				
				
			}
			
			
			if(null != entity.getFloorOfFigureAndPuzzles())
			{
				dto.setFloorOfFigureAndPuzzlesId(entity.getFloorOfFigureAndPuzzles().getId());
				dto.setFigureOrPuzzlesName(entity.getFloorOfFigureAndPuzzles().getFloorName());
			}
			
			
			if(null != entity.getStatus())
			{
				String statusName = "";
				FigureAndPuzzleStatus figureAndPuzzleStatus = figureAndPuzzleStatusService.
						getFigureAndPuzzleStatusByStatusId(entity.getStatus());
				statusName = figureAndPuzzleStatus.getStatusName();
				dto.setStatusName(statusName);
			}
			
			if(null != entity.getFigureOrPuzzles())
			{
				String fop = entity.getFigureOrPuzzles();
				if("0".equals(fop))
				{
					dto.setFigureOrPuzzlesName("全部");
				}
				else
					if("1".equals(fop))
					{
						dto.setFigureOrPuzzlesName("图谜");
					}
					else
						if("2".equals(fop))
						{
							dto.setFigureOrPuzzlesName("字谜");
						}
			}
			
			
			
			
			
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		
		
		return dto;
	}

	public List<FigureAndPuzzlesDTO> toRDTOS(List<FigureAndPuzzles> entities) {
		
		List<FigureAndPuzzlesDTO> dtos = new ArrayList<FigureAndPuzzlesDTO>();
		
		for (FigureAndPuzzles entity : entities) {
			
			FigureAndPuzzlesDTO dto = this.toDTO(entity);
			
			dtos.add(dto);
			
		}
		
		
		return dtos;
	}

	public void save(FigureAndPuzzles entity) {

		figureAndPuzzlesRepository.save(entity);
	}

	public void update(FigureAndPuzzles entity)
	{
		figureAndPuzzlesRepository.save(entity);
	}

	public QueryResult<FigureAndPuzzles> getFigureAndPuzzlesList(
			Class<FigureAndPuzzles> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<FigureAndPuzzles> eQueryResult = figureAndPuzzlesRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams, orderby, pageable);
		
		return eQueryResult;
	}

	public FigureAndPuzzles getFigureAndPuzzlesById(String id) {
		return figureAndPuzzlesRepository.getFigureAndPuzzlesById(id);
	}

	public FigureAndPuzzles getFigureAndPuzzlesByFAPCode(String fAPCode) 
	{
		return figureAndPuzzlesRepository.getFigureAndPuzzlesByFAPCode(fAPCode);
	}
	
	public List<ShuangSQ> getShuangSQKaijiang(Date ct){
		 StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7 FROM analysis.T_DATA_BASE_SHUANG ");
		 if(null != ct)
	 		{
	 			execSql.append(" WHERE CREATE_TIME>'"+ct+"' ");
	 		}
	 		execSql.append(" ORDER BY ISSUE_NUMBER DESC LIMIT 10");
			Object[] queryParams = new Object[]{
			};
			List<ShuangSQ> shuangSQList = shuangSQRepository.getEntityListBySql(ShuangSQ.class,execSql.toString(), queryParams);
			return shuangSQList;
	 }
	
	 public List<ThreeDTiming> get3DNumKaijiang(Date ct){
		 StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,TEST_NUM FROM analysis.T_DATA_BASE_3D ");
			 if(null != ct)
	 		{
	 			execSql.append(" WHERE CREATE_TIME>'"+ct+"' ");
	 		}
	 		execSql.append(" ORDER BY ISSUE_NUMBER DESC LIMIT 10");
			Object[] queryParams = new Object[]{
			};
			List<ThreeDTiming> threeDList =threeDTimingRepository.getEntityListBySql(ThreeDTiming.class,execSql.toString(), queryParams);
			return threeDList;
	  }
	 

}
