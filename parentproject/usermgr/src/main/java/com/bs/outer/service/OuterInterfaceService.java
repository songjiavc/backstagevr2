package com.bs.outer.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import com.bs.outer.entity.*;
import org.springframework.data.domain.Pageable;

import com.bs.outer.dto.StationOuterDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.station.entity.Station;

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
	 * @Title: getStationAdvertisementOfStaAndApp
	 * @Description: 获取应用广告的站点类别的数据
	 * @author:banna
	 * @return: QueryResult<Advertisement>
	 */
	public QueryResult<Advertisement> getStationAdvertisementOfStaAndApp(
			Class<Advertisement> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType,String stationId);
	
	
	/**
	 * 
	 * @Title: getAdvertisementOfSta
	 * @Description: TODO根据通行证组和应用id还有通行证的区域信息获取应用公告数据
	 * @author:banna
	 * @return: QueryResult<Advertisement>
	 */
	public QueryResult<Notice> getNoticeOfStaAndApp(Class<Notice> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType);
	
	public Date getNoticeByAppNoticeName(String appNoticeName);
	
	
	public List<QiLeCai> getQiLeCaiKaijiang(Date ct);
	
	public List<ThreeDTiming> get3DNumKaijiang(Date ct);
	
	public List<ShuangSQ> getShuangSQKaijiang(Date ct);
	
	public QueryResult<Notice> getLastKjNoticeOfNoticename(String appNoticeName);
	
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
	 * @return 获取通用12选5列表
	 */
	public List<Ln5In12Bean> get5In12LastRecord100List(String provinceNumber);
	
	
	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return
	 */
	public List<Ln5In12Bean> get5In12ListByIssueNumber(String issueNumber,String provinceNumber);
	
	/**
	 * @return 获取通用11选5列表
	 */
	public List<Ln5In12Bean> get5In11LastRecord100List(String provinceNumber);
	
	
	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return
	 */
	public List<Ln5In12Bean> get5In11ListByIssueNumber(String issueNumber,String provinceNumber);
	
	/**
	 * add by songjia 获取11选5遗漏统计前三名
	 * @param issueNumber
	 * @param provinceNumber
	 * @return
	 */
	public List<Fast3Analysis> get5In11MissAnalysisTop3(String issueNumber,String provinceNumber);
	
	
	/**
	 * add by songjia 获取11选5遗漏统计类型
	 * @param type
	 * @param group
	 * @return
	 */
	public Fast3Analysis get5In11MissAnalysisByTypeAndGroup(String type,String group,String provinceNumber);


	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return 如果期号为空则认为是初始化内容，不为空则判断是否有更新的记录然后返回
	 */
	public List<Fast3DanMa> get5In11InitDanmaList(String issueNumber,String provinceNumber);

	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return 如果期号为空则认为是初始化内容，不为空则判断是否有更新的记录然后返回
	 */
	public List<Fast3DanMa> get5In12InitDanmaList(String issueNumber,String provinceNumber);

	/**
	 * @param siMaId
	 * @param provinceNumber
	 * @return 获取四码复式列表
	 */
	public List<Fast3SiMa> get5In11InitSimaList(int siMaId,String provinceNumber);
	/**
	 * @param siMaId
	 * @param provinceNumber
	 * @return 获取四码复式列表
	 */
	public List<Fast3SiMa> get5In12InitSimaList(int siMaId,String provinceNumber);

	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return 获取相同号码统计列表
	 */
	public List<Fast3Same> get5In11InitSameList(String issueNumber,String provinceNumber);


	/**
	 * 获取号码所应对的冷热号码
	 * @param issueNumber  根据期号判断是否产生新的数据
	 * @param provinceNumber
	 * @return
	 */
	public List<HotCoolBean> getHotCoolList(String issueNumber,String provinceNumber);
	/**
	 * @param issueNumber
	 * @param provinceNumber
	 * @return 获取相同号码统计列表
	 */
	public List<Fast3Same> get5In12InitSameList(String issueNumber,String provinceNumber);

	/**
	 * add by songjia 获取11选5遗漏统计类型
	 * @param type
	 * @param group
	 * @return
	 */
	public Fast3Analysis get5In12MissAnalysisByTypeAndGroup(String type,String group,String provinceNumber);
	/**
	 * add by songjia 获取11选5遗漏统计前三名
	 * @param issueNumber
	 * @param provinceNumber
	 * @return
	 */
	public List<Fast3Analysis> get5In12MissAnalysisTop3(String issueNumber,String provinceNumber);
	
	/**
     * @param issueNumber
     * @return 根据期号找到比这个期号更大的期
     */
    public List<Ln5In12Bean> getLn5In12ListByIssueNumber(String issueNumber);
    
    /**
     * 
     * @Title: toDto
     * @Description: 实体和接口dto转换方法
     * @author:banna
     * @return: StationOuterDTO
     */
    public StationOuterDTO toDto(	Station station);
    
   /**
 * @param issueNumber
 * @return
 */
  public List<ShuangSQ> getShuangSQNumByIssueNumber();
   
   /**
 * @param issueNumber
 * @return
 */ 
  public List<ThreeD> get3DNumByIssueNumber();
   
   /**
    * @param issueNumber
    * @return
    */
  public List<QiLeCai> getQiLeCaiNumByIssueNumber();
   
   
}
