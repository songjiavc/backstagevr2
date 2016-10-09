package com.sdf.manager.figureMysteryPuzzlesApp.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.figureMysteryPuzzlesApp.dto.ExpertsOfFMPAPPDTO;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.ExpertsOfFMPAPP;
import com.sdf.manager.figureMysteryPuzzlesApp.repository.ExpertOfFMAPPRepository;
import com.sdf.manager.figureMysteryPuzzlesApp.service.ExpertOfFMAPPService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;

@Service("expertOfFMAPPService")
@Transactional(propagation=Propagation.REQUIRED)
public class ExpertOfFMAPPServiceImpl implements ExpertOfFMAPPService 
{
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	
	@Autowired
	private ExpertOfFMAPPRepository expertOfFMAPPRepository;
	
	public  ExpertsOfFMPAPPDTO toDTO(ExpertsOfFMPAPP entity) {
		ExpertsOfFMPAPPDTO dto = new ExpertsOfFMPAPPDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getProvinceCode())//省级区域
			{
				Province province = new Province();
				province = provinceService.getProvinceByPcode(entity.getProvinceCode());
				dto.setProvinceName(null != province?province.getPname():"");
			}
			if(null != entity.getCityCode())//市级区域
			{
				if(Constants.CITY_ALL.equals(entity.getCityCode()))
				{
					dto.setCityName(Constants.CITY_ALL_NAME);
				}
				else
				{
					City city = new City();
					city = cityService.getCityByCcode(entity.getCityCode());
					dto.setCityName(null != city?city.getCname():"");
				}
				
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
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

	
	public  List<ExpertsOfFMPAPPDTO> toRDTOS(List<ExpertsOfFMPAPP> entities) {
		List<ExpertsOfFMPAPPDTO> dtos = new ArrayList<ExpertsOfFMPAPPDTO>();
		ExpertsOfFMPAPPDTO dto;
		for (ExpertsOfFMPAPP entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}
	
	public QueryResult<ExpertsOfFMPAPP> getExpertsOfFMPAPPList(Class<ExpertsOfFMPAPP> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable)
	{
		
		QueryResult<ExpertsOfFMPAPP> eQueryResult = expertOfFMAPPRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return eQueryResult;
	}


	public void save(ExpertsOfFMPAPP entity) 
	{
		expertOfFMAPPRepository.save(entity);
	}


	public void update(ExpertsOfFMPAPP entity) {
		
		expertOfFMAPPRepository.save(entity);
		
	}
	
	public ExpertsOfFMPAPP getExpertsOfFMPAPPById(String id)
	{
		return expertOfFMAPPRepository.getExpertsOfFMPAPPById(id);
	}
}
