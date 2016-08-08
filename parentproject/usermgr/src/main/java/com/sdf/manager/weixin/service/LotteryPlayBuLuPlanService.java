package com.sdf.manager.weixin.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.weixin.dto.LotteryPlayBuluPlanDTO;
import com.sdf.manager.weixin.entity.LotteryPlayBulufangan;

public interface LotteryPlayBuLuPlanService 
{
	public void save(LotteryPlayBulufangan entity);
	
	
	public void update(LotteryPlayBulufangan entity);
	
	
	public QueryResult<LotteryPlayBulufangan> getLotteryPlayBulufanganList(Class<LotteryPlayBulufangan> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	
	public List<LotteryPlayBuluPlanDTO> toRDTOS(List<LotteryPlayBulufangan> entities);
	
	
	public LotteryPlayBuluPlanDTO toDTO(LotteryPlayBulufangan entity);
	
	public LotteryPlayBulufangan getLotteryPlayBulufanganById(String id);
}
