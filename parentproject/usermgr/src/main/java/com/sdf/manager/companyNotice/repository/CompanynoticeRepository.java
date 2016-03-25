package com.sdf.manager.companyNotice.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.companyNotice.entity.CompanyNotice;

public interface CompanynoticeRepository extends GenericRepository<CompanyNotice, String>{

	@Query("select u from CompanyNotice u where u.isDeleted='1' and u.id =?1")
	public CompanyNotice getCompanyNoticeById(String id);
}
