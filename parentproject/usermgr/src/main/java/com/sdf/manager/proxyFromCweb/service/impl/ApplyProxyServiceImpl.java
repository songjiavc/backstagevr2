package com.sdf.manager.proxyFromCweb.service.impl;

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
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.proxyFromCweb.controller.CwebProxyApplyController;
import com.sdf.manager.proxyFromCweb.dto.ApplyProxyDTO;
import com.sdf.manager.proxyFromCweb.entity.ApplyProxy;
import com.sdf.manager.proxyFromCweb.repository.ApplyProxyRepository;
import com.sdf.manager.proxyFromCweb.service.ApplyProxyService;


@Service("applyProxyService")
@Transactional(propagation= Propagation.REQUIRED)
public class ApplyProxyServiceImpl implements ApplyProxyService {
	
	@Autowired
	private ApplyProxyRepository applyProxyRepository;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;

	public void save(ApplyProxy entity) {
		applyProxyRepository.save(entity);
	}
	
	public QueryResult<ApplyProxy> getApplyProxyList(Class<ApplyProxy> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable) {
		QueryResult<ApplyProxy> applyProxys = applyProxyRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,orderby, pageable);
		return applyProxys;
	}

	public ApplyProxy getApplyProxyById(String id) {
		return applyProxyRepository.getApplyProxyById(id);
	}

	public List<ApplyProxyDTO> toRDTOS(List<ApplyProxy> entities) {
		List<ApplyProxyDTO> dtos = new ArrayList<ApplyProxyDTO>();
		ApplyProxyDTO dto;
		for (ApplyProxy entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public ApplyProxyDTO toDTO(ApplyProxy entity) {
		ApplyProxyDTO dto = new ApplyProxyDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getProvince())//省级区域
			{
				Province province = new Province();
				province = provinceService.getProvinceByPcode(entity.getProvince());
				dto.setProvinceName(null != province?province.getPname():"");
			}
			if(null != entity.getCity())//市级区域
			{
				if(Constants.CITY_ALL.equals(entity.getCity()))
				{
					dto.setCityName(Constants.CITY_ALL_NAME);
				}
				else
				{
					City city = new City();
					city = cityService.getCityByCcode(entity.getCity());
					dto.setCityName(null != city?city.getCname():"");
				}
				
			}
			
			
			if(null != entity.getIsConnect())
			{
				if("1".equals(entity.getIsConnect()))
				{
					dto.setIsConnectName("是");
				}
				else if("0".equals(entity.getIsConnect()))
				{
					dto.setIsConnectName("否");
				}
			}
			
			if(null != entity.getStatus())
			{
				if(CwebProxyApplyController.NOT_REVRIW_STATUS.equals(entity.getStatus()))
				{
					dto.setStatusName("未回访");
				}
				else if(CwebProxyApplyController.REVRIW_CONFORM_STATUS.equals(entity.getStatus()))
				{
					dto.setStatusName("回访符合");
				}
				else if(CwebProxyApplyController.REVRIW_NOT_CONFORM_STATUS.equals(entity.getStatus()))
				{
					dto.setStatusName("回访不符合");
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}

	public void update(ApplyProxy entity) {
		applyProxyRepository.save(entity);
	}


}
