package com.sdf.manager.announcement.service;

import java.util.List;

import com.sdf.manager.announcement.entity.AnnouncementAndArea;

/**
 * 
 * @ClassName: AnnouncementAndAreaService
 * @Description: 通告与区域关联表的业务处理层
 * @author: banna
 * @date: 2016年2月15日 下午3:00:57
 */
public interface AnnouncementAndAreaService {

	public void save(AnnouncementAndArea entity);
	
	public void update(AnnouncementAndArea entity);
	
	public void delete(AnnouncementAndArea entity);
	
	public List<AnnouncementAndArea> getAnnouncementAndAreaById(String announcementId);
}
