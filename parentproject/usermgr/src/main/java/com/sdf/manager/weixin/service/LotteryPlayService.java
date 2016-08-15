package com.sdf.manager.weixin.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.weixin.dto.LotteryPlayDTO;
import com.sdf.manager.weixin.entity.LotteryPlay;

public interface LotteryPlayService 
{
	public void save(LotteryPlay entity);
	
	
	public void update(LotteryPlay entity);
	
	
	public QueryResult<LotteryPlay> getLotteryPlayList(Class<LotteryPlay> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	
	public List<LotteryPlayDTO> toRDTOS(List<LotteryPlay> entities);
	
	
	public LotteryPlayDTO toDTO(LotteryPlay entity);
	
	public LotteryPlay getLotteryPlayById(String id);
	
	/**
	 * 获取当前可以进行补录的省份数据
	 * @param entityClass
	 * @param whereJpql
	 * @param queryParams
	 * @param orderby
	 * @param pageable
	 * @return
	 */
	public QueryResult<LotteryPlay> getProvinceOfLotteryPlayList(Class<LotteryPlay> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	/**
	 * 根据省份和彩种类型信息获取补录信息数据列表
	 * @param city
	 * @param lotteryType
	 * @return
	 */
	public List<LotteryPlay> getLotteryPlayByProvinceAndLotteryType(String city,String lotteryType);
	
	
	public Map<String,Object> findALL(int page,int rows,String tableName);
	
	/**
	 * 根据id删除指定表的数据
	 * @param tableName
	 * @param id
	 * @return
	 */
	public boolean deleteById(String tableName,String id);
	
}
