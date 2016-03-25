package com.sdf.manager.appversion.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.appversion.dto.AppversionDTO;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.common.util.QueryResult;

public interface AppversionService {

	/**
	 * 
	* @Title: save
	* @Description: 保存应用版本实体数据
	* @Author : banna
	* @param @param entity    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void save(Appversion entity);
	
	/**
	 * 
	* @Title: update
	* @Description: 更新应用版本实体数据
	* @Author : banna
	* @param @param entity    设定文件
	* @return void    返回类型
	* @throws
	 */
	public void update(Appversion entity);
	
	
	/**
	 * 
	* @Title: getAppversionById
	* @Description: 根据id查找应用版本实体
	* @Author : banna
	* @param @param id
	* @param @return    设定文件
	* @return Appversion    返回类型
	* @throws
	 */
	public Appversion getAppversionById(String id);
	
	
	/**
	 * 
	* @Title: getAppversionList
	* @Description: 根据条件筛选应用版本数据
	* @Author : banna
	* @param @param entityClass
	* @param @param whereJpql
	* @param @param queryParams
	* @param @param orderby
	* @param @param pageable
	* @param @return    设定文件
	* @return QueryResult<Appversion>    返回类型
	* @throws
	 */
	public QueryResult<Appversion> getAppversionList(Class<Appversion> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	
	public  List<AppversionDTO> toRDTOS(List<Appversion> entities);
	
	
	public  AppversionDTO toDTO(Appversion entity);
	
	
	public String findMaxVersionFlowId(String appId,String appVersionStatus);
	
	/**
	 * 
	 * @Title: findMaxVersionFlowId
	 * @Description: TODO:获取有效的应用版本数据中 app下的最大的版本流水号id
	 * @author:banna
	 * @return: String
	 */
	public String findMaxVersionFlowId(String appId);
	
	/**
	 * 
	 * @Title: getAppversionByAppIdAndVersionFlowId
	 * @Description: TODO:根据appid和流水号获取应用版本数据
	 * @author:banna
	 * @return: Appversion
	 */
	public Appversion getAppversionByAppIdAndVersionFlowId(String appId,String versionFlowId);
}
