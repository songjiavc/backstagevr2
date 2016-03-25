package com.sdf.manager.userGroup.service.impl;

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
import com.sdf.manager.userGroup.dto.UserGroupDTO;
import com.sdf.manager.userGroup.entity.UserGroup;
import com.sdf.manager.userGroup.repository.UserGroupRepository;
import com.sdf.manager.userGroup.service.UserGroupService;


@Service("userGroupService")
@Transactional(propagation = Propagation.REQUIRED)
public class UserGroupServiceImpl implements UserGroupService {

	
	@Autowired
	private UserGroupRepository userGroupRepository;

	public void save(UserGroup entity) {
		userGroupRepository.save(entity);
	}

	public void update(UserGroup entity) {

		userGroupRepository.save(entity);
	}

	public UserGroup getUserGroupById(String id) {
		
		return userGroupRepository.getUserGroupById(id);
	}

	public QueryResult<UserGroup> getAppversionList(
			Class<UserGroup> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		
		QueryResult<UserGroup> uGroupList = userGroupRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return uGroupList;
	}

	public List<UserGroupDTO> toRDTOS(List<UserGroup> entities) {
		List<UserGroupDTO> dtos = new ArrayList<UserGroupDTO>();
		UserGroupDTO dto;
		for (UserGroup entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public UserGroupDTO toDTO(UserGroup entity) {
		UserGroupDTO dto = new UserGroupDTO();
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
