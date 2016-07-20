package com.sdf.manager.weixin.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.weixin.dto.WXCommonProblemDTO;
import com.sdf.manager.weixin.entity.WXCommonProblem;

public interface WeixinService {

	public WXCommonProblem getWXCommentsProblemById(String id);
	
	
	
	public List<WXCommonProblemDTO> toRDTOS(List<WXCommonProblem> entities);
	
	
	public WXCommonProblemDTO toDTO(WXCommonProblem entity);
	
	
	
	
	public QueryResult<WXCommonProblem> getWXCommonProblemList(Class<WXCommonProblem> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public void save(WXCommonProblem entity);
	
	
	public void update(WXCommonProblem entity);
	
}
