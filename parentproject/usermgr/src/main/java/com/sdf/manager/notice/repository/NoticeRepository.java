package com.sdf.manager.notice.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.notice.entity.Notice;

public interface NoticeRepository extends GenericRepository<Notice, String>{

	@Query("select u from Notice u where u.isDeleted='1' and u.id =?1")
	public Notice getNoticeById(String id);
}
