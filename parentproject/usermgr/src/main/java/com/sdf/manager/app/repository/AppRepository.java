package com.sdf.manager.app.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.app.entity.App;
import com.sdf.manager.common.repository.GenericRepository;

public interface AppRepository extends GenericRepository<App, String>{

	/**
	 * 
	* @Title: getAppById
	* @Description: 根据app的id查找app实体
	* @Author : banna
	* @param @param id
	* @param @return    设定文件
	* @return App    返回类型
	* @throws
	 */
	@Query("select u from App u where u.isDeleted='1' and u.id =?1")
	public App getAppById(String id);
}
