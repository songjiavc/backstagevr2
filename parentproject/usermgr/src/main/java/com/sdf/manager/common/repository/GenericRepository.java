package com.sdf.manager.common.repository;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;

import javax.persistence.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.sdf.manager.common.util.QueryResult;

@NoRepositoryBean
public interface GenericRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

		/**

		 * 设置query的参数

		 * @param query 查询对象

		 * @param queryParams 参数

		 */

		public void setQueryParams(Query query, Object[] queryParams);

		

		/**

		 * 组装ORDER BY 语句

		 * @param orderby

		 * @return

		 */

		public String buildOrderby(LinkedHashMap<String, String> orderby);

		

		/**

		 * 获取实体名

		 * @param entityClass

		 * @return

		 */

		public String getEntityName(Class<T> entityClass);

		

		/**

		 * jpql语句查询

		 * @param entityClass

		 * @param whereSql

		 * @param queryParams

		 * @param orderby

		 * @param pageable

		 * @return

		 */

		public QueryResult<T> getScrollDataByJpql(Class<T> entityClass, String whereJpql, Object[] queryParams,

				LinkedHashMap<String, String> orderby, Pageable pageable);

		

		/**

		 * sql语句查询

		 * @param sql

		 * @param queryParams

		 * @param pageable

		 * @return

		 */

		public QueryResult<T> getScrollDataBySql(Class<T> entityClass,String sql, Object[] queryParams, Pageable pageable);
		
		/**
		 * sql中带group by的sql语句执行
		 * @Title: getScrollDataByGroupBySql
		 * @Description: TODO
		 * @author:banna
		 * @return: QueryResult<T>
		 */
		public QueryResult<T> getScrollDataByGroupBySql(Class<T> entityClass,String sql, Object[] queryParams, Pageable pageable) ;
		
		/**
		 * @author  songjia
		 * @param entityClass
		 * @param sql
		 * @param queryParams
		 * @return 用sql返回指定实体内容
		 */
		public T getEntityBySql(Class<T> entityClass,String sql,Object[] queryParams);
		
		/**
		 * @author  songjia
		 * @param entityClass
		 * @param sql
		 * @param queryParams
		 * @return 用sql返回指定实体内容
		 */
		public List<T> getEntityListBySql(Class<T> entityClass,String sql,Object[] queryParams);
}
