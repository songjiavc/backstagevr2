package com.sdf.manager.weixin.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.weixin.entity.WXCommonProblem;


public interface WeixinRepository extends GenericRepository<WXCommonProblem, String>{

	@Query("select u from WXCommonProblem u where u.isDeleted='1' and u.id =?1")
	public WXCommonProblem getWXCommonProblemById(String id);
	
}
