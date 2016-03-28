package com.bs.outer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.bs.outer.entity.Fast3;
import com.bs.outer.entity.Fast3Analysis;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.notice.entity.Notice;

public interface OuterInterfaceService {
	
	/**
	 * 
	 * @Title: getAnnouncementOfSta
	 * @Description: TODO根据通行证组合通行证相关的区域信息获取通告数据
	 * @author:banna
	 * @return: QueryResult<Announcement>
	 */
	public QueryResult<Announcement> getAnnouncementOfSta(Class<Announcement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String lotteryType);
	/**
	 * 
	 * @Title: getCompanyNoticeOfSta
	 * @Description: TODO根据通行证组合通行证相关的区域信息获取公司公告数据
	 * @author:banna
	 * @return: QueryResult<CompanyNotice>
	 */
	public QueryResult<CompanyNotice> getCompanyNoticeOfSta(Class<CompanyNotice> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String lotteryType);
	/**
	 * 
	 * @Title: getAdvertisementOfSta
	 * @Description: TODO根据通行证组和应用id还有通行证的区域信息获取应用广告数据
	 * @author:banna
	 * @return: QueryResult<Advertisement>
	 */
	public QueryResult<Advertisement> getAdvertisementOfStaAndApp(Class<Advertisement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType);
	
	/**
	 * 
	 * @Title: getAdvertisementOfSta
	 * @Description: TODO根据通行证组和应用id还有通行证的区域信息获取应用公告数据
	 * @author:banna
	 * @return: QueryResult<Advertisement>
	 */
	public QueryResult<Notice> getNoticeOfStaAndApp(Class<Notice> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType);
	
	/**
	 * 
	 * @Title: getKaijiangNoticeOfStaAndApp
	 * @Description: 获取应用的开奖公告
	 * @author:banna
	 * @return: QueryResult<Notice>
	 */
	public QueryResult<Notice> getKaijiangNoticeOfStaAndApp(Class<Notice> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType);

	/**
	 * 初始化开奖结果内容
	 * @param provinceNumber
	 * @return
	 */
	public List<Fast3> getKaijiangNumberListByProvinceNumber(String provinceNumber);
	
	/**
	 * @Title: getKaiJiangNumberByIssueId
	 * @Description: 
	 * @author:songjia
	 * @return: QueryResult<Notice>
	 */
	public Fast3 getKaiJiangNumberByIssueId(String issueNumber,String provinceNumber);

	
	/**
	 * @param entityClass
	 * @param issueNumber
	 * @param provinceNumber
	 * @return  对外接口，用于返回遗漏统计列表内容
	 */
	public List<Fast3Analysis> getAnalysisListByIssueNumber(String issueNumber,String provinceNumber);
	
	
	
}
