package com.sdf.manager.weixin.dao;

import java.util.Map;

public interface WeixinDao {

	public Map<String,Object> findALL(int page,int rows,String tableName);
	
	/**
	 * 根据id删除补录的数据
	 * @param tableName
	 * @param id
	 * @return
	 */
	public boolean deleteById(String tableName,String id);
	
}
