package com.sdf.manager.appUnitPrice.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.appUnitPrice.entity.UserYearDiscount;
import com.sdf.manager.common.repository.GenericRepository;


/**
 * 
 * @ClassName: UserYearDiscountRepository
 * @Description: 使用年限折扣表的dao层
 * @author: banna
 * @date: 2016年2月2日 上午10:51:19
 */
public interface UserYearDiscountRepository extends GenericRepository<UserYearDiscount, String>{

	@Query("select u from UserYearDiscount u where u.id =?1")
	public UserYearDiscount getUserYearDiscountById(String id);
}
