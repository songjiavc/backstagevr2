package com.sdf.manager.proxyFromCweb.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.proxyFromCweb.entity.ApplyProxy;



/** 
  * @ClassName: AuthRepository 
  * @Description: 权限 
  * @author songj@sdfcp.com
  * @date 2015年9月25日 上午8:24:21 
  *  
  */
public interface ApplyProxyRepository extends GenericRepository<ApplyProxy, String>  {
	
	
	@Query("select u from ApplyProxy u where u.isDeleted='1' and u.id =?1 ")
	public ApplyProxy getApplyProxyById(String id);
}
