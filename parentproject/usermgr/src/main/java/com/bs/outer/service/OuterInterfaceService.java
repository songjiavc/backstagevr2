package com.bs.outer.service;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.data.domain.Pageable;

import com.bs.outer.entity.Fast3;
import com.bs.outer.entity.Fast3Analysis;
import com.bs.outer.entity.Fast3DanMa;
import com.bs.outer.entity.Fast3Same;
import com.bs.outer.entity.Fast3SiMa;
import com.bs.outer.entity.Ln5In12Bean;
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
	 * @Title: getAnnouncementOfStaAndNotInReceipt
	 * @Description: TODO:查询符合当前通行证要求且在有效期内且不存在于通告回执表中的数据
	 * @author:banna
	 * @return: QueryResult<Announcement>
	 */
	public QueryResult<Announcement> getAnnouncementOfStaAndNotInReceipt(Class<Announcement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String lotteryType,String stationId);
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
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType,String stationId);
	
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
	
	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return 如果期号为空则认为是初始化内容，不为空则判断是否有更新的记录然后返回
	 */
	public List<Fast3DanMa> getInitDanmaList(String issueNumber,String provinceNumber);
	
	/**
	 * @param siMaId
	 * @param provinceNumber
	 * @return 获取四码复式列表
	 */
	public List<Fast3SiMa> getInitSimaList(int siMaId,String provinceNumber);
	
	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return 获取相同号码统计列表
	 */
	public List<Fast3Same> getInitSameList(String issueNumber,String provinceNumber);
	
	/**
	 * @return 获取辽宁12选5列表
	 */
	public List<Ln5In12Bean> getLn5In12Last3DaysList();
	
    /**
     * @param issueNumber
     * @return 根据期号找到比这个期号更大的期
     */
    public List<Ln5In12Bean> getLn5In12ListByIssueNumber(String issueNumber);
}
