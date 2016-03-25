package com.sdf.manager.userGroup.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.userGroup.entity.UserGroup;

public interface UserGroupRepository extends GenericRepository<UserGroup, String>{

	
	@Query("select u from UserGroup u where u.isDeleted='1' and u.id =?1")
	public UserGroup getUserGroupById(String id);
}
