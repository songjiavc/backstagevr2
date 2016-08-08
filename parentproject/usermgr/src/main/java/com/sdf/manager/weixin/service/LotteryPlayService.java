package com.sdf.manager.weixin.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.weixin.dto.LotteryPlayDTO;
import com.sdf.manager.weixin.entity.LotteryPlay;

public interface LotteryPlayService 
{
	public void save(LotteryPlay entity);
	
	
	public void update(LotteryPlay entity);
	
	
	public QueryResult<LotteryPlay> getLotteryPlayList(Class<LotteryPlay> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	
	public List<LotteryPlayDTO> toRDTOS(List<LotteryPlay> entities);
	
	
	public LotteryPlayDTO toDTO(LotteryPlay entity);
	
	public LotteryPlay getLotteryPlayById(String id);
}
