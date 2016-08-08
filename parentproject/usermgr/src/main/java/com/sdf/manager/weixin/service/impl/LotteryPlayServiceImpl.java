package com.sdf.manager.weixin.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.weixin.dto.LotteryPlayDTO;
import com.sdf.manager.weixin.entity.LotteryPlay;
import com.sdf.manager.weixin.repository.LotteryPlayPlanRepository;
import com.sdf.manager.weixin.repository.LotteryPlayRepository;
import com.sdf.manager.weixin.service.LotteryPlayService;

@Service("lotteryPlayService")
@Transactional(propagation=Propagation.REQUIRED)
public class LotteryPlayServiceImpl implements LotteryPlayService 
{
	@Autowired
	private LotteryPlayRepository lotteryPlayRepository;//补录信息
	
	
	@Autowired
	private LotteryPlayPlanRepository lotteryPlayPlanRepository;//补录号码方案
	
	@Autowired
	private ProvinceService provinceService;


	public void save(LotteryPlay entity) {
		lotteryPlayRepository.save(entity);
	}


	public void update(LotteryPlay entity) {
		lotteryPlayRepository.save(entity);
		
	}


	public QueryResult<LotteryPlay> getLotteryPlayList(
			Class<LotteryPlay> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<LotteryPlay> qResult = lotteryPlayRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return qResult;
	}


	public List<LotteryPlayDTO> toRDTOS(List<LotteryPlay> entities) {
		List<LotteryPlayDTO> dtos = new ArrayList<LotteryPlayDTO>();
		LotteryPlayDTO dto;
		for (LotteryPlay entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}


	public LotteryPlayDTO toDTO(LotteryPlay entity) {
		LotteryPlayDTO dto = new LotteryPlayDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getLotteryPlayBulufangan())//放置补录方案id
			{
				dto.setLtblPlaneId(entity.getLotteryPlayBulufangan().getId());
			}
			
			if(null != entity.getProvince())
			{
				Province province = new Province();
				province = provinceService.getProvinceByPcode(entity.getProvince());
				dto.setProvinceName(null != province?province.getPname():"");
			}
			
			
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}


	public LotteryPlay getLotteryPlayById(String id) {
		return lotteryPlayRepository.getLotteryPlayById(id);
	}
	
	
	
	
	
	
}
