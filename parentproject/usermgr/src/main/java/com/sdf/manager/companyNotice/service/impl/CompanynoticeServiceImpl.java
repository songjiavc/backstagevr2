package com.sdf.manager.companyNotice.service.impl;

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
import com.sdf.manager.companyNotice.controller.CompanynoticeController;
import com.sdf.manager.companyNotice.dto.ComnoticeDTO;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.companyNotice.repository.CompanynoticeRepository;
import com.sdf.manager.companyNotice.service.CompanynoticeService;

@Service("companynoticeService")
@Transactional(propagation = Propagation.REQUIRED)
public class CompanynoticeServiceImpl implements CompanynoticeService {
	
	@Autowired
	private CompanynoticeRepository companynoticeRepository;

	public void save(CompanyNotice entity) {

		companynoticeRepository.save(entity);
	}

	public void update(CompanyNotice entity) {

		companynoticeRepository.save(entity);
	}

	public CompanyNotice getCompanyNoticeById(String id) {
		return companynoticeRepository.getCompanyNoticeById(id);
	}

	public List<ComnoticeDTO> toRDTOS(List<CompanyNotice> entities) {
		List<ComnoticeDTO> dtos = new ArrayList<ComnoticeDTO>();
		ComnoticeDTO dto;
		for (CompanyNotice entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public ComnoticeDTO toDTO(CompanyNotice entity) {
		ComnoticeDTO dto = new ComnoticeDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getStartTime())
			{
				dto.setStartTimestr(DateUtil.formatTimestampToString(entity.getStartTime()));
			}
			
			if(null != entity.getEndTime())
			{
				dto.setEndTimestr(DateUtil.formatTimestampToString(entity.getEndTime()));
			}
			
			if(null != entity.getComnoticeStatus())
			{
				if(CompanynoticeController.COMPANYNOTIE_FB.equals(entity.getComnoticeStatus()))
				{
					dto.setComnoticeStatusName("发布");
				}
				else
					if(CompanynoticeController.COMPANYNOTIE_BC.equals(entity.getComnoticeStatus()))
					{
						dto.setComnoticeStatusName("保存");
					}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

	public QueryResult<CompanyNotice> getCompanynoticeList(
			Class<CompanyNotice> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		QueryResult<CompanyNotice> companynoticeList = companynoticeRepository.
				getScrollDataByJpql(entityClass, whereJpql, queryParams, orderby, pageable);
		
		return companynoticeList;
	}

}
