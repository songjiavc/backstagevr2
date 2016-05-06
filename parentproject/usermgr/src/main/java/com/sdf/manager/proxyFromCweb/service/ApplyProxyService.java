package com.sdf.manager.proxyFromCweb.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.proxyFromCweb.dto.ApplyProxyDTO;
import com.sdf.manager.proxyFromCweb.entity.ApplyProxy;


public interface ApplyProxyService {

	public void save(ApplyProxy entity);
	
	
	public void update(ApplyProxy entity);
	
	public QueryResult<ApplyProxy> getApplyProxyList(Class<ApplyProxy> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public ApplyProxy getApplyProxyById(String id);
	
	
	public List<ApplyProxyDTO> toRDTOS(List<ApplyProxy> entities) ;
	
	
	public ApplyProxyDTO toDTO(ApplyProxy entity) ;
}
