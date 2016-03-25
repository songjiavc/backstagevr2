package com.sdf.manager.companyNotice.service;

import java.util.List;

import com.sdf.manager.companyNotice.entity.ComnoticeAndArea;

/**
 * 
 * @ClassName: ComnoticeAndAreaService
 * @Description: 公司公告与区域关联表的业务处理层
 * @author: banna
 * @date: 2016年2月15日 下午3:00:57
 */
public interface ComnoticeAndAreaService {

	public void save(ComnoticeAndArea entity);
	
	public void update(ComnoticeAndArea entity);
	
	public void delete(ComnoticeAndArea entity);
	
	public List<ComnoticeAndArea> getComnoticeAndAreaById(String comnoticeId);
}
