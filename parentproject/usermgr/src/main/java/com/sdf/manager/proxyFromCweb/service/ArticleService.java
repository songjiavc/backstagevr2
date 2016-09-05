package com.sdf.manager.proxyFromCweb.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.proxyFromCweb.dto.ArticleDTO;
import com.sdf.manager.proxyFromCweb.entity.Article;


public interface ArticleService {

	public QueryResult<Article> getArticleList(Class<Article> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable);
	
	public Article getArticleById(String id);
	
	public void save(Article article);
	
	
	public void update(Article article);
	
	public List<ArticleDTO> toRDTOS(List<Article> entities);
	
	public ArticleDTO toDTO(Article entity);
}
