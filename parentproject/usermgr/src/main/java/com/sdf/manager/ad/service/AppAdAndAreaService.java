package com.sdf.manager.ad.service;

import java.util.List;

import com.sdf.manager.ad.entity.AppAdAndArea;

/**
 * 
 * @ClassName: AppAdAndAreaService
 * @Description: 应用广告与区域关联表的业务处理层
 * @author: banna
 * @date: 2016年2月15日 下午3:00:57
 */
public interface AppAdAndAreaService {

	public void save(AppAdAndArea entity);
	
	public void update(AppAdAndArea entity);
	
	public void delete(AppAdAndArea entity);
	
	public List<AppAdAndArea> getAppAdAndAreaById(String adId);
}
