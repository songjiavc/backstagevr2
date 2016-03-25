package com.sdf.manager.notice.service;

import java.util.List;

import com.sdf.manager.notice.entity.AppNoticeAndArea;

/**
 * 
 * @ClassName: AppAdAndAreaService
 * @Description: 应用公告与区域关联表的业务处理层
 * @author: banna
 * @date: 2016年2月15日 下午3:00:57
 */
public interface AppNoticeAndAreaService {

	public void save(AppNoticeAndArea entity);
	
	public void update(AppNoticeAndArea entity);
	
	public void delete(AppNoticeAndArea entity);
	
	public List<AppNoticeAndArea> getAppNoticeAndAreaById(String noticeId);
}
