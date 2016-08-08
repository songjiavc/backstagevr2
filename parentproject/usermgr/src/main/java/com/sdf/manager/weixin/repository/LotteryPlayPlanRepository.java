package com.sdf.manager.weixin.repository;


import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.weixin.entity.LotteryPlayBulufangan;


/**
 * 
 * 补录模块方案表实现层
 * @author Administrator
 *
 */
public interface LotteryPlayPlanRepository extends GenericRepository<LotteryPlayBulufangan, String> {

	@Query("select u from LotteryPlayBulufangan u where  u.id =?1")
	public LotteryPlayBulufangan getLotteryPlayBulufanganById(String id);
	
}
