package com.sdf.manager.weixin.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.appversion.controller.AppversionController;
import com.sdf.manager.appversion.dto.AppversionDTO;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.weixin.dto.WXCommonProblemDTO;
import com.sdf.manager.weixin.entity.WXCommonProblem;
import com.sdf.manager.weixin.repository.WeixinRepository;
import com.sdf.manager.weixin.service.WeixinService;

/**
 * 
* @ClassName: WeixinServiceImpl 
* @Description: 微信业务层
* @author banna
* @date 2016年7月19日 上午10:05:41 
*
 */
@Service("weixinService")
@Transactional(propagation=Propagation.REQUIRED)
public class WeixinServiceImpl implements WeixinService {

	@Autowired
	private WeixinRepository weixinRepository;
	
	public WXCommonProblem getWXCommentsProblemById(String id) {
		return weixinRepository.getWXCommonProblemById(id);
	}
	
	public QueryResult<WXCommonProblem> getWXCommonProblemList(Class<WXCommonProblem> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable)
	{
		
		QueryResult<WXCommonProblem> wxResult = weixinRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return wxResult;
	}
	
	public void save(WXCommonProblem entity)
	{
		weixinRepository.save(entity);
	}
	
	public void update(WXCommonProblem entity)
	{
		weixinRepository.save(entity);
	}

	
	public List<WXCommonProblemDTO> toRDTOS(List<WXCommonProblem> entities) {
		List<WXCommonProblemDTO> dtos = new ArrayList<WXCommonProblemDTO>();
		WXCommonProblemDTO dto;
		for (WXCommonProblem entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public WXCommonProblemDTO toDTO(WXCommonProblem entity) {
		WXCommonProblemDTO dto = new WXCommonProblemDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dto;
	}
	
}
