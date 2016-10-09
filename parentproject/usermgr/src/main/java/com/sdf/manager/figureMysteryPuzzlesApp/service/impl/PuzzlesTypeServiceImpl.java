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
import com.sdf.manager.figureMysteryPuzzlesApp.dto.PuzzleTypeDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.PuzzlesType;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.PuzzleTypeRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.PuzzlesTypeService;

@Service("puzzlesTypeService")
@Transactional(propagation=Propagation.REQUIRED)
public class PuzzlesTypeServiceImpl implements PuzzlesTypeService 
{
	@Autowired
	private PuzzleTypeRepository puzzleTypeRepository;

	public PuzzlesType getPuzzlesTypeById(String id) 
	{
		return puzzleTypeRepository.getPuzzlesTypeById(id);
	}

	public void save(PuzzlesType entity) 
	{
		puzzleTypeRepository.save(entity);
	}

	public void update(PuzzlesType entity) 
	{
		puzzleTypeRepository.save(entity);
		
	}

	public PuzzleTypeDTO toDTO(PuzzlesType entity) 
	{
		PuzzleTypeDTO dto = new PuzzleTypeDTO();
		
		try {
			BeanUtils.copyProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			
		} catch (IllegalAccessException e)
		{
			e.printStackTrace();
		} catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}
		
		
		return dto;
	}

	public List<PuzzleTypeDTO> toRDTOS(List<PuzzlesType> entities) 
	{
		List<PuzzleTypeDTO> dtos = new ArrayList<PuzzleTypeDTO>();
		
		for (PuzzlesType entity : entities) 
		{
			PuzzleTypeDTO dto = this.toDTO(entity);
			dtos.add(dto);
		}
		
		return dtos;
	}

	public QueryResult<PuzzlesType> getPuzzlesTypeList(
			Class<PuzzlesType> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<PuzzlesType> eQueryResult = puzzleTypeRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return eQueryResult;
	}
}
