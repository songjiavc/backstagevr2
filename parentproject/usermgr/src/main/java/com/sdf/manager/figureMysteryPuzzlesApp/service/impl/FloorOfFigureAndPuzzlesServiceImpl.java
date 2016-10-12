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
import com.sdf.manager.figureMysteryPuzzlesApp.dto.FloorOfFigureAndPuzzlesDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FloorOfFigureAndPuzzles;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.FloorOfFigureAndPuzzlesRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.FloorOfFigureAndPuzzlesService;

@Service("floorOfFigureAndPuzzlesService")
@Transactional(propagation= Propagation.REQUIRED)
public class FloorOfFigureAndPuzzlesServiceImpl implements
		FloorOfFigureAndPuzzlesService {
	
	@Autowired
	private FloorOfFigureAndPuzzlesRepository floorOfFigureAndPuzzlesRepository;

	public FloorOfFigureAndPuzzlesDTO toDTO(FloorOfFigureAndPuzzles entity) 
	{
		FloorOfFigureAndPuzzlesDTO dto = new FloorOfFigureAndPuzzlesDTO();
		
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

	public List<FloorOfFigureAndPuzzlesDTO> toRDTOS(
			List<FloorOfFigureAndPuzzles> entities) {
		List<FloorOfFigureAndPuzzlesDTO> dtos = new ArrayList<FloorOfFigureAndPuzzlesDTO>();
		
		for (FloorOfFigureAndPuzzles entity : entities) 
		{
			FloorOfFigureAndPuzzlesDTO dto = this.toDTO(entity);
			
			dtos.add(dto);
		}
		
		return dtos;
	}

	public void save(FloorOfFigureAndPuzzles entity) {
		floorOfFigureAndPuzzlesRepository.save(entity);
	}

	public void update(FloorOfFigureAndPuzzles entity) {

		floorOfFigureAndPuzzlesRepository.save(entity);
	}

	public QueryResult<FloorOfFigureAndPuzzles> getFloorOfFigureAndPuzzlesList(
			Class<FloorOfFigureAndPuzzles> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<FloorOfFigureAndPuzzles> eQueryResult = floorOfFigureAndPuzzlesRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return eQueryResult;
	}

	public FloorOfFigureAndPuzzles getFloorOfFigureAndPuzzlesById(String id) {
		return floorOfFigureAndPuzzlesRepository.getFloorOfFigureAndPuzzlesById(id);
	}

}
