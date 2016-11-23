package com.sdf.manager.appversion.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.common.repository.GenericRepository;

public interface AppversionRepository extends GenericRepository<Appversion, String>{

	
	@Query("select u from Appversion u where u.isDeleted='1' and u.id =?1")
	public Appversion getAppversionById(String id);
	
	/**
	 * 
	 * @Title: findMaxVersionFlowId
	 * @Description: TODO:获取指定应用版本状态下 app下的最大的版本流水号id
	 * @author:banna
	 * @return: String
	 */
	@Query("select max(u.versionFlowId) as versionFlowId from Appversion u where  u.isDeleted='1' and u.app.id =?1 and u.appVersionStatus=?2")
	public Integer findMaxVersionFlowId(String appId,String appVersionStatus);
	
	/**
	 * 
	 * @Title: findMaxVersionFlowId
	 * @Description: TODO:获取有效的应用版本数据中 app下的最大的版本流水号id
	 * @author:banna
	 * @return: String
	 */
	@Query("select max(u.versionFlowId) as versionFlowId from Appversion u where  u.isDeleted='1' and u.app.id =?1 ")
	public Integer findMaxVersionFlowId(String appId);
	
	/**
	 * 
	 * @Title: getAppversionByAppIdAndVersionFlowId
	 * @Description: TODO:根据appid和流水号获取应用版本数据
	 * @author:banna
	 * @return: Appversion
	 */
	@Query("select u from Appversion u where  u.isDeleted='1' and u.app.id =?1 and u.versionFlowId =?2 ")
	public Appversion getAppversionByAppIdAndVersionFlowId(String appId,Integer versionFlowId);
}
