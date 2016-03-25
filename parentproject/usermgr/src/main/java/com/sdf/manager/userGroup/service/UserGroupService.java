package com.sdf.manager.userGroup.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.userGroup.dto.UserGroupDTO;
import com.sdf.manager.userGroup.entity.UserGroup;

public interface UserGroupService {

	/**
	 * 
	* @Title: save
	* @Description: 保存通行证组实体数据
	* @Author : banna
	* @param @param entity    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void save(UserGroup entity);
	
	/**
	 * 
	* @Title: update
	* @Description: 修改通行证组实体数据
	* @Author : banna
	* @param @param entity    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void update(UserGroup entity);
	
	/**
	 * 
	* @Title: getUserGroupById
	* @Description: 根据id获取通行证组
	* @Author : banna
	* @param @param id
	* @param @return    设定文件
	* @return UserGroup    返回类型
	* @throws
	 */
	public UserGroup getUserGroupById(String id);
	
	/**
	 * 
	* @Title: getAppversionList
	* @Description: 根据筛选条件获取通行证组列表数据
	* @Author : banna
	* @param @param entityClass
	* @param @param whereJpql
	* @param @param queryParams
	* @param @param orderby
	* @param @param pageable
	* @param @return    设定文件
	* @return QueryResult<UserGroup>    返回类型
	* @throws
	 */
	public QueryResult<UserGroup> getAppversionList(
			Class<UserGroup> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) ;
	
	public  List<UserGroupDTO> toRDTOS(List<UserGroup> entities);
	
	
	public  UserGroupDTO toDTO(UserGroup entity);
	
}
