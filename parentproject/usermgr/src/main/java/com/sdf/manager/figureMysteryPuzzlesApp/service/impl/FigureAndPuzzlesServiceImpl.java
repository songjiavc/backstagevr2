package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.controller.FigureMPuzzlesAppController;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FigureAndPuzzlesRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FigureAndPuzzlesService;

@Service("figureAndPuzzlesService")
@Transactional(propagation=Propagation.REQUIRED)
public class FigureAndPuzzlesServiceImpl implements FigureAndPuzzlesService {

	@Autowired
	private FigureAndPuzzlesRepository figureAndPuzzlesRepository;
	
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
			
			
			//若当前底板是字谜的底板，则一定有字谜类型，这时要将字谜类型的id返回
			if(null != entity.getFigureOrPuzzles() && FigureMPuzzlesAppController.ZIMI_FLAG.equals(entity.getFigureOrPuzzles()))
			{
				dto.setPuzzlesTypeId(entity.getPuzzlesType().getId());
			}
			
			
			if(null != entity.getFloorOfFigureAndPuzzles())
			{
				dto.setFloorOfFigureAndPuzzlesId(entity.getFloorOfFigureAndPuzzles().getId());
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

}
