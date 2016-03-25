package com.sdf.manager.companyNotice.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.dto.ComnoticeDTO;
import com.sdf.manager.companyNotice.entity.CompanyNotice;

public interface CompanynoticeService {

	public void save(CompanyNotice entity);
	
	public void update(CompanyNotice entity);
	
	public CompanyNotice getCompanyNoticeById(String id);
	
	public  List<ComnoticeDTO> toRDTOS(List<CompanyNotice> entities);
	
	
	public  ComnoticeDTO toDTO(CompanyNotice entity);
	
	public QueryResult<CompanyNotice> getCompanynoticeList(Class<CompanyNotice> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
}
