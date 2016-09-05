package com.sdf.manager.proxyFromCweb.repository;

import org.springframework.data.jpa.repository.Query;

import com.sdf.manager.common.repository.GenericRepository;
import com.sdf.manager.proxyFromCweb.entity.ApplyProxy;
import com.sdf.manager.proxyFromCweb.entity.Article;


public interface ArticleRepository extends GenericRepository<Article, String>  {
	
	@Query("select u from Article u where u.isDeleted='1' and u.id =?1 ")
	public Article getArticleById(String id);
}
