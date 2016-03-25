package com.sdf.manager.notice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.notice.entity.AppNoticeAndArea;

public interface AppNoticeAndAreaRepository extends GenericRepository<AppNoticeAndArea, String>{

	@Query("select u from AppNoticeAndArea u where  u.notice.id =?1")
	public List<AppNoticeAndArea> getAppNoticeAndAreaById(String noticeId);
}
