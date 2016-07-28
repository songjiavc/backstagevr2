package com.bs.outer.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bs.outer.dto.StationOuterDTO;
import com.bs.outer.entity.Fast3;
import com.bs.outer.entity.Fast3Analysis;
import com.bs.outer.entity.Fast3DanMa;
import com.bs.outer.entity.Fast3Same;
import com.bs.outer.entity.Fast3SiMa;
import com.bs.outer.entity.Ln5In12Bean;
import com.bs.outer.entity.QiLeCai;
import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeD;
import com.bs.outer.repository.Fast3AnalysisRepository;
import com.bs.outer.repository.Fast3DanMaRepository;
import com.bs.outer.repository.Fast3NumberRepository;
import com.bs.outer.repository.Fast3SameRepository;
import com.bs.outer.repository.Fast3SiMaRepository;
import com.bs.outer.repository.Ln5In12Repository;
import com.bs.outer.repository.QiLeCaiRepository;
import com.bs.outer.repository.ShuangSQRepository;
import com.bs.outer.repository.ThreeDRepository;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.repository.AdvertisementRepository;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.announcement.repository.AnnouncementRepository;
import com.sdf.manager.common.service.cache.GlobalCacheService;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.companyNotice.repository.CompanynoticeRepository;
import com.sdf.manager.notice.controller.NoticeController;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.repository.NoticeRepository;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.station.entity.Station;

@Service("outerInterfaceService")
@Transactional(propagation = Propagation.REQUIRED)
public class OuterInterfaceServiceImpl implements OuterInterfaceService {
	
	@Autowired
	private AnnouncementRepository announcementRepository;//通告数据容器层
	
	@Autowired
	private CompanynoticeRepository companynoticeRepository;//公司公告数据容器层
	
	@Autowired
	private AdvertisementRepository advertisementRepository;//应用广告数据容器层
	
	@Autowired
	private NoticeRepository noticeRepository;//应用公告数据容器层
	
	@Autowired
	private Fast3NumberRepository fast3NumberRepository;
	
	@Autowired
	private Fast3AnalysisRepository fast3AnalysisRepository;

	
	@Autowired
	private Fast3DanMaRepository fast3DanMaRepository;
	
	@Autowired
	private Fast3SiMaRepository fast3SiMaRepository;
	
	@Autowired
	private Fast3SameRepository fast3SameRepository;
	
	@Autowired
	private Ln5In12Repository ln5In12Repository;
	
	@Autowired
	private QiLeCaiRepository qiLeCaiRepository;
	
	@Autowired
	private ThreeDRepository threeDRepository;

	@Autowired
	private ShuangSQRepository shuangSQRepository;
	
	@Autowired
	private ProvinceService provinceService;

	@Autowired
	private CityService cityService;
	
	@Autowired
	private GlobalCacheService globalCacheService;
	
	//TODO:未完成
	public QueryResult<Announcement> getAnnouncementOfSta(Class<Announcement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String lotteryType){
		StringBuffer sql = new StringBuffer("SELECT u.* FROM (T_BS_ANNOUNCEMENT u LEFT JOIN RELA_BS_ANN_AND_AREA a ON u.ID=a.ANNOUNCEMENT_ID) LEFT JOIN RELA_BS_ANN_AND_UGROUP au ON u.ID=au.ANNOUNCE_ID"+
					"	WHERE u.IS_DELETED='1' AND u.ANNOUNCE_STATUS='1'   "+
					"   AND u.END_TIME>=CURDATE() AND u.START_TIME<=CURDATE()  AND u.LOTTERY_TYPE='"+lotteryType+"'");
					if(ugroups.length()>0)
					{
						sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (a.PROVINCE='"+province+"' AND a.CITY='"+city+"')) GROUP BY u.id");
					}
					else
					{
						sql.append(" AND a.PROVINCE='"+province+"' AND a.CITY='"+city+"' GROUP BY u.id");
					}
		QueryResult<Announcement> userObj = announcementRepository.
				getScrollDataBySql(Announcement.class,sql.toString(), queryParams, pageable);
			return userObj;
	}
	
	
	public QueryResult<Announcement> getAnnouncementOfStaAndNotInReceipt(Class<Announcement> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String lotteryType,String stationId){
		StringBuffer sql = new StringBuffer("SELECT u.* FROM (T_BS_ANNOUNCEMENT u LEFT JOIN RELA_BS_ANN_AND_AREA a ON u.ID=a.ANNOUNCEMENT_ID) LEFT JOIN RELA_BS_ANN_AND_UGROUP au ON u.ID=au.ANNOUNCE_ID"+
					"	WHERE u.IS_DELETED='1' AND u.ANNOUNCE_STATUS='1'   "+
					"   AND u.END_TIME>=CURDATE() AND u.START_TIME<=CURDATE()  AND u.LOTTERY_TYPE='"+lotteryType+"'");
					if(ugroups.length()>0)
					{
						sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (a.PROVINCE='"+province+"' AND a.CITY='"+city+"')) ");
					}
					else
					{
						sql.append(" AND a.PROVINCE='"+province+"' AND a.CITY='"+city+"' ");
					}
					
					sql.append(" AND u.ID NOT IN (SELECT an.ID FROM T_BS_RECEIPT_OF_ANNOUNCEMENT bra LEFT JOIN T_BS_ANNOUNCEMENT an ON an.ID=bra.ANNOUNCEMENT_ID WHERE an.IS_DELETED='1' " +
                                " AND an.ANNOUNCE_STATUS='1' AND bra.STATION_ID='"+stationId+"' )");
		QueryResult<Announcement> userObj = announcementRepository.
				getScrollDataBySql(Announcement.class,sql.toString(), queryParams, pageable);
			return userObj;
	}


	public QueryResult<CompanyNotice> getCompanyNoticeOfSta(
			Class<CompanyNotice> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable,String ugroups,String province,String city,String lotteryType) {
		StringBuffer sql = new StringBuffer("SELECT u.* FROM (T_BS_COM_NOTICE u LEFT JOIN RELA_BS_COMNOTICE_AND_AREA a ON u.ID=a.COMNOTICE_ID) LEFT JOIN RELA_BS_COMNOTICE_AND_UGROUP au ON u.ID=au.COM_NOTICE_ID  "+
				"	WHERE u.IS_DELETED='1' AND u.COMNOTICE_STATUS='1'  "+
				"   AND u.END_TIME>=CURDATE() AND u.START_TIME<=CURDATE() AND u.LOTTERY_TYPE='"+lotteryType+"'  ");
		
		if(ugroups.length()>0)
		{
			sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (a.PROVINCE='"+province+"' AND a.CITY='"+city+"')) GROUP BY u.id");
		}
		else
		{
			sql.append(" AND a.PROVINCE='"+province+"' AND a.CITY='"+city+"' GROUP BY u.id");
		}
		QueryResult<CompanyNotice> userObj = companynoticeRepository.
			getScrollDataBySql(CompanyNotice.class,sql.toString(), queryParams, pageable);
		return userObj;
	}


	public QueryResult<Advertisement> getAdvertisementOfStaAndApp(
			Class<Advertisement> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType,String stationId) {
		StringBuffer sql = new StringBuffer("SELECT u.* FROM ((T_BS_APP_AD u LEFT JOIN RELA_BS_APPAD_AND_APP app ON u.ID=app.APP_AD_ID)  "+
				"	 LEFT JOIN RELA_BS_APPAD_AND_UGROUP au ON u.ID=au.APP_AD_ID) LEFT JOIN RELA_BS_APPAD_AND_AREA aarea ON u.id = aarea.APP_AD_ID WHERE u.IS_DELETED='1'  "+
				"	AND u.AD_STATUS='1' AND app.APP_ID='"+appId+"'"+
				"   AND u.AD_END_TIME>=CURDATE() AND u.AD_START_TIME<=NOW() ");//AND u.LOTTERY_TYPE='"+lotteryType+"'"
		/*//查询应用广告类别不包括站点类型的应用广告
		if(ugroups.length()>0)
		{
			sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"')) GROUP BY u.id");
		}
		else
		{
			sql.append(" AND aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"' GROUP BY u.id");
		}*/
		//查询应用广告类别包括站点应用广告
		if(ugroups.length()>0)
		{
			sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"') "
					+ "OR (u.CREATOR_STATION='"+stationId+"' AND u.STATION_AD_STATUS='21')) ");
		}
		else
		{
			sql.append("	AND ( (aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"') "
					+ "OR (u.CREATOR_STATION='"+stationId+"' AND u.STATION_AD_STATUS='21')) ");
		}
		QueryResult<Advertisement> userObj = advertisementRepository.
			getScrollDataBySql(Advertisement.class,sql.toString(), queryParams, pageable);
		return userObj;
	}
	
	
	public QueryResult<Advertisement> getStationAdvertisementOfStaAndApp(
			Class<Advertisement> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable, String ugroups, String province, String city,
			String appId, String lotteryType, String stationId) {
		StringBuffer sql = new StringBuffer("SELECT u.* FROM ((T_BS_APP_AD u LEFT JOIN RELA_BS_APPAD_AND_APP app ON u.ID=app.APP_AD_ID)  "+
				"	 LEFT JOIN RELA_BS_APPAD_AND_UGROUP au ON u.ID=au.APP_AD_ID) LEFT JOIN RELA_BS_APPAD_AND_AREA aarea ON u.id = aarea.APP_AD_ID WHERE u.IS_DELETED='1'  "+
				"	AND u.AD_STATUS='1' AND app.APP_ID='"+appId+"'"+
				"   AND u.AD_END_TIME>=CURDATE() AND u.AD_START_TIME<=NOW() ");//AND u.LOTTERY_TYPE='"+lotteryType+"'"
		//查询应用广告类别站点应用广告
		sql.append("	AND u.CREATOR_STATION='"+stationId+"' AND u.STATION_AD_STATUS='21' ");
		QueryResult<Advertisement> userObj = advertisementRepository.
			getScrollDataBySql(Advertisement.class,sql.toString(), queryParams, pageable);
		return userObj;
	}


	public QueryResult<Notice> getNoticeOfStaAndApp(Class<Notice> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType) {
		StringBuffer sql = new StringBuffer("SELECT u.* FROM ((T_BS_APP_NOTICE u LEFT JOIN RELA_BS_NOTICE_AND_APP app ON u.ID=app.APP_NOTICE_ID) "+
				"    LEFT JOIN RELA_BS_NOTICE_AND_UGROUP au ON u.ID=au.APP_NOTICE_ID) LEFT JOIN RELA_BS_NOTICE_AND_AERA aarea ON u.id = aarea.NOTICE_ID WHERE u.IS_DELETED='1' "+
				"   AND u.NOTICE_STATUS='1' AND app.APP_ID='"+appId+"' "+
				"   AND u.NOTICE_ENDTIME>=CURDATE() AND u.NOTICE_STARTTIME<=CURDATE() AND u.LOTTERY_TYPE='"+lotteryType+"'");
		if(ugroups.length()>0)
		{
			sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"')) GROUP BY u.id");
		}
		else
		{
			sql.append(" AND aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"' GROUP BY u.id");
		}
		QueryResult<Notice> userObj = noticeRepository.
			getScrollDataBySql(Notice.class,sql.toString(), queryParams, pageable);
		return userObj;
	}
	
	public QueryResult<Notice> getKaijiangNoticeOfStaAndApp(Class<Notice> entityClass,
			String whereJpql, Object[] queryParams,
			LinkedHashMap<String, String> orderby, Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType) {
		StringBuffer sql = new StringBuffer(" select atable.* from  (SELECT u.* FROM ((T_BS_APP_NOTICE u LEFT JOIN RELA_BS_NOTICE_AND_APP app ON u.ID=app.APP_NOTICE_ID) "+
				"    LEFT JOIN RELA_BS_NOTICE_AND_UGROUP au ON u.ID=au.APP_NOTICE_ID) LEFT JOIN RELA_BS_NOTICE_AND_AERA aarea ON u.id = aarea.NOTICE_ID WHERE u.IS_DELETED='1' "+
				"   AND u.NOTICE_STATUS='1' AND  u.APP_CATEGORY='"+NoticeController.APP_CATEGORY_COMPANY_KAIJIANG+"' "+
//				"   AND u.NOTICE_ENDTIME>=CURDATE() AND u.NOTICE_STARTTIME<=CURDATE() "
				 " AND u.LOTTERY_TYPE='"+lotteryType+"'  AND aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"'  "
				 		+ "UNION "+
				 "   SELECT a.*  FROM T_BS_APP_NOTICE a LEFT JOIN RELA_BS_NOTICE_AND_AERA rarea ON a.ID=rarea.NOTICE_ID "+
				 "	WHERE rarea.NOTICE_ID IS NULL AND a.IS_DELETED='1'    AND a.NOTICE_STATUS='1' "
				 + "AND  a.APP_CATEGORY='"+NoticeController.APP_CATEGORY_COMPANY_KAIJIANG+"'  "
				 		+ "  AND a.LOTTERY_TYPE='"+lotteryType+"' ) atable  ORDER BY atable.APP_NOTICE_NAME ASC");
		//getScrollDataBySql参数不传排序参数，所以order by要写在sql里
		QueryResult<Notice> userObj = noticeRepository.
			getScrollDataBySql(Notice.class,sql.toString(), queryParams, pageable);
		return userObj;
	}
	
	/**
	 * 
	 * @Title: getNoticeByAppNoticeName
	 * @Description: TODO:获取当前彩种开奖公告的最近一次生成时间
	 * @author:banna
	 */
	public Date getNoticeByAppNoticeName(String appNoticeName)
	{
		List<Object> params = new ArrayList<Object>();
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);//查询所有的数据
		
		StringBuffer sql = new StringBuffer("SELECT MAX(n.CREATER_TIME) AS CREATER_TIME,n.ID,n.CREATER,n.IS_DELETED,n.MODIFY,"
				+ "n.MODIFY_TIME,n.APP_CATEGORY,n.APP_NOTICE_NAME,n.APP_NOTICE_WORD,n.NOTICE_ENDTIME,n.LOTTERY_TYPE,n.NOTICE_STATUS,n.NOTICE_STARTTIME,n.NOTICE_FONT_COLOR   FROM T_BS_APP_NOTICE n WHERE n.IS_DELETED='1' "+
					" AND n.APP_CATEGORY='3' AND n.NOTICE_STATUS='1' AND n.APP_NOTICE_NAME = '"+appNoticeName+"'");
		QueryResult<Notice> userObj = noticeRepository.
				getScrollDataBySql(Notice.class,sql.toString(), params.toArray(), pageable);
		if(0 == userObj.getTotalCount())
		{
		 
			Date finalDate = null;
				
			return finalDate;
		}
		else
		{
			System.out.println(userObj.getResultList().get(0).getCreaterTime());
			return userObj.getResultList().get(0).getCreaterTime();
		}
		
		
		
				
	}
	
	public QueryResult<Notice> getLastKjNoticeOfNoticename(String appNoticeName)
	{
		String ct = "";
		List<Object> params = new ArrayList<Object>();
		Pageable pageable = new PageRequest(0,Integer.MAX_VALUE);//查询所有的数据
		
		StringBuffer sql = new StringBuffer("SELECT n.CREATER_TIME,n.ID,n.CREATER,n.IS_DELETED,n.MODIFY,"
				+ "n.MODIFY_TIME,n.APP_CATEGORY,n.APP_NOTICE_NAME,n.APP_NOTICE_WORD,n.NOTICE_ENDTIME,n.LOTTERY_TYPE,n.NOTICE_STATUS,n.NOTICE_STARTTIME,n.NOTICE_FONT_COLOR   FROM T_BS_APP_NOTICE n WHERE n.IS_DELETED='1' "+
					" AND n.APP_CATEGORY='3' AND n.NOTICE_STATUS='1' AND n.APP_NOTICE_NAME = '"+appNoticeName+"'");
		QueryResult<Notice> userObj = noticeRepository.
				getScrollDataBySql(Notice.class,sql.toString(), params.toArray(), pageable);
		
		
		return userObj;
				
	}
	
	/**
	 * 
	 * @Title: getQiLeCaiKaijiang
	 * @Description: TODO:获取“七乐彩”的最新开奖数据
	 * @author:banna
	 * @return: List<QiLeCai>
	 */
	 public List<QiLeCai> getQiLeCaiKaijiang(Date ct){
		    StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7,NO8 FROM analysis.T_DATA_BASE_QILECAI ");
		    		if(null != ct)
		    		{
		    			execSql.append(" WHERE CREATE_TIME>'"+ct+"' ");
		    		}
		    		execSql.append(" ORDER BY ISSUE_NUMBER DESC LIMIT 300");
			Object[] queryParams = new Object[]{
			};
		    List<QiLeCai> qiLeCaieDList =qiLeCaiRepository.getEntityListBySql(QiLeCai.class,execSql.toString(), queryParams);
			return qiLeCaieDList;
	  }
	 
	 /**
		 * 
		 * @Title: get3DNumKaijiang
		 * @Description: TODO:获取“3D”的最新开奖数据
		 * @author:banna
		 * @return: List<QiLeCai>
		 */
	 public List<ThreeD> get3DNumKaijiang(Date ct){
		 StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,TEST_NUM FROM analysis.T_DATA_BASE_3D ");
			 if(null != ct)
	 		{
	 			execSql.append(" WHERE CREATE_TIME>'"+ct+"' ");
	 		}
	 		execSql.append(" ORDER BY ISSUE_NUMBER DESC LIMIT 300");
			Object[] queryParams = new Object[]{
			};
			List<ThreeD> threeDList =threeDRepository.getEntityListBySql(ThreeD.class,execSql.toString(), queryParams);
			return threeDList;
	  }
	 
	 /**
		 * 
		 * @Title: getShuangSQKaijiang
		 * @Description: TODO:获取“双色球”的最新开奖数据
		 * @author:banna
		 * @return: List<QiLeCai>
		 */
	 public List<ShuangSQ> getShuangSQKaijiang(Date ct){
		 StringBuffer execSql = new StringBuffer("SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7 FROM analysis.T_DATA_BASE_SHUANG ");
		 if(null != ct)
	 		{
	 			execSql.append(" WHERE CREATE_TIME>'"+ct+"' ");
	 		}
	 		execSql.append(" ORDER BY ISSUE_NUMBER DESC LIMIT 300");
			Object[] queryParams = new Object[]{
			};
			List<ShuangSQ> shuangSQList = shuangSQRepository.getEntityListBySql(ShuangSQ.class,execSql.toString(), queryParams);
			return shuangSQList;
	 }

	public List<Fast3> getKaijiangNumberListByProvinceNumber(String provinceNumber){
		String tableName = "analysis.T_ANHUI_KUAI3_NUMBER";
		String execSql = "SELECT \n    p.*\nFROM\n    (SELECT \n        u.*\n    FROM\n        "+ tableName +" u\n    ORDER BY u.issue_number DESC\n    LIMIT 200) p\nORDER BY p.issue_number ASC";
		Object[] queryParams = new Object[]{
		};
		List<Fast3> fast3List = fast3NumberRepository.getEntityListBySql(Fast3.class,execSql, queryParams);
		return fast3List;
		
	}
	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#getKaiJiangNumberByIssueId(java.lang.Class, java.lang.String, java.lang.String)
	 */
	public Fast3 getKaiJiangNumberByIssueId(String issueNumber,String provinceNumber) {
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[0];
		String execSql = "SELECT u.* FROM "+tableName +" u  WHERE ISSUE_NUMBER > ? LIMIT 1 ";
		Object[] queryParams = new Object[]{
				issueNumber
		};
		Fast3 fast3 = fast3NumberRepository.getEntityBySql(Fast3.class,execSql, queryParams);
		return fast3;
	}
	
	public List<Fast3Analysis> getAnalysisListByIssueNumber(String issueNumber,String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[1];
		String execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM "+tableName +" WHERE ISSUE_NUMBER > ? ORDER BY TYPE,CURRENT_MISS DESC; ";
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Fast3Analysis> fast3AnalysisList = fast3AnalysisRepository.getEntityListBySql(Fast3Analysis.class,execSql, queryParams);
		return fast3AnalysisList;
	}

	public List<Fast3DanMa> getInitDanmaList(String issueNumber,String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[2];
		String where = null;
		if(StringUtils.isEmpty(issueNumber)){
			where = " ORDER BY ISSUE_NUMBER DESC LIMIT 10 ";
		}else{
			where = " WHERE  "+ issueNumber +" <  (SELECT ISSUE_NUMBER FROM "+ tableName +" ORDER BY ISSUE_NUMBER DESC LIMIT 1)  ORDER BY ISSUE_NUMBER DESC LIMIT 10 ";
		}
		String execSql = "SELECT * FROM "+tableName + where;
		Object[] queryParams = new Object[]{
		};
		List<Fast3DanMa> fast3AnalysisList = fast3DanMaRepository.getEntityListBySql(Fast3DanMa.class,execSql, queryParams);
		return fast3AnalysisList;
	}


	public List<Fast3SiMa> getInitSimaList(int siMaId, String provinceNumber) {
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[3];
		String where = null;
		if(siMaId == 0){
			where = " ORDER BY CREATE_TIME DESC LIMIT 10 ";
		}else{
			where = " WHERE "+ siMaId +"  <>  (SELECT ID*DROWN_CYCLE FROM "+ tableName +" ORDER BY ID DESC LIMIT 1)  ORDER BY CREATE_TIME DESC LIMIT 10 ";
		}
		
		String execSql = "SELECT ID*DROWN_CYCLE AS ID,YUCE_ISSUE_START,YUCE_ISSUE_STOP,DROWN_PLAN,DROWN_ISSUE_NUMBER,DROWN_NUMBER,DROWN_CYCLE,STATUS,CREATE_TIME FROM "+tableName + where;
		Object[] queryParams = new Object[]{
		};
		List<Fast3SiMa> fast3SiMaList = fast3SiMaRepository.getEntityListBySql(Fast3SiMa.class,execSql, queryParams);
		return fast3SiMaList;
	}


	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#getInitSameList(java.lang.String, java.lang.String)
	 */
	public List<Fast3Same> getInitSameList(String issueNumber, String provinceNumber) {
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[4];
		String where = null;
		if(StringUtils.isEmpty(issueNumber)){
			where = " ORDER BY CURRENT_ISSUE DESC LIMIT 10 ";
		}else{
			where = " WHERE "+ issueNumber +"  <  (SELECT CURRENT_ISSUE FROM "+ tableName +" ORDER BY CURRENT_ISSUE DESC LIMIT 1)  ORDER BY CURRENT_ISSUE DESC LIMIT 10  ";
		}
		String execSql = "SELECT * FROM "+tableName + where;
		Object[] queryParams = new Object[]{
		};
		List<Fast3Same> fast3SameList = fast3SameRepository.getEntityListBySql(Fast3Same.class,execSql, queryParams);
		return fast3SameList;
	}
	
	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#getLn5In12Last3DaysList()
	 */
	public List<Ln5In12Bean> getLn5In12Last3DaysList(){
		String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM analysis.T_LN_5IN12_NUMBER WHERE CREATE_TIME >= date_add(CURDATE(),INTERVAL -3 DAY)";
		Object[] queryParams = new Object[]{
		};
		List<Ln5In12Bean> ln5In12List =ln5In12Repository.getEntityListBySql(Ln5In12Bean.class,execSql, queryParams);
		return ln5In12List;
	}
	
	
	public List<Ln5In12Bean> getLn5In12ListByIssueNumber(String issueNumber){
		String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM analysis.T_LN_5IN12_NUMBER WHERE ISSUE_NUMBER > ?  LIMIT 300" ;
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Ln5In12Bean> ln5In12 =ln5In12Repository.getEntityListBySql(Ln5In12Bean.class,execSql, queryParams);
		return ln5In12;
	}
	
	
	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#getLn5In12Last3DaysList()
	 */
	public List<Ln5In12Bean> get5In12LastRecord100List(String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[2];
		String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM "+ tableName+" ORDER BY ISSUE_NUMBER DESC LIMIT 300";
		Object[] queryParams = new Object[]{
		};
		List<Ln5In12Bean> ln5In12List =ln5In12Repository.getEntityListBySql(Ln5In12Bean.class,execSql, queryParams);
		return ln5In12List;
	}
	
	
	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#getLn5In12Last3DaysList()
	 */
	public List<Ln5In12Bean> get5In11LastRecord100List(String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[0];
		String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM "+ tableName+" ORDER BY ISSUE_NUMBER DESC LIMIT 300";
		Object[] queryParams = new Object[]{
		};
		List<Ln5In12Bean> ln5In12List =ln5In12Repository.getEntityListBySql(Ln5In12Bean.class,execSql, queryParams);
		return ln5In12List;
	}
	
	public List<Ln5In12Bean> get5In12ListByIssueNumber(String issueNumber,String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[2];
		String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM " + tableName + " WHERE ISSUE_NUMBER > ?  LIMIT 300" ;
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Ln5In12Bean> ln5In12 =ln5In12Repository.getEntityListBySql(Ln5In12Bean.class,execSql, queryParams);
		return ln5In12;
	}
	
	public List<Ln5In12Bean> get5In11ListByIssueNumber(String issueNumber,String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[0];
		String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5 FROM " + tableName + " WHERE ISSUE_NUMBER > ?  LIMIT 300" ;
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Ln5In12Bean> ln5In12 =ln5In12Repository.getEntityListBySql(Ln5In12Bean.class,execSql, queryParams);
		return ln5In12;
	}
	
	/**
	 *    获取当前遗漏值最大的n个记录，掐尖
	 */
	public List<Fast3Analysis> get5In11MissAnalysisTop3(String issueNumber,String provinceNumber){
		List<Fast3Analysis>  rtnList = new ArrayList<Fast3Analysis>();
		List<Fast3Analysis> allMissList = this.get5In11AllMissAnalysis(issueNumber, provinceNumber);
		int n = 0;
		int type = 0;
		for(Fast3Analysis fast3Analysis :allMissList ){
			if(type != fast3Analysis.getType()){
				if(n < 3){
					rtnList.add(fast3Analysis);
					n++;
				}else if(n == 3){
					n = 0;
					type = fast3Analysis.getType();
				}else{
					continue;
				}
			}
		}
		return rtnList;
	}
	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#get5In11MissAnalysisByTypeAndGroup(java.lang.String, java.lang.String)
	 */
	public Fast3Analysis get5In11MissAnalysisByTypeAndGroup(String type,String group,String provinceNumber){
		Fast3Analysis fast3Analysis = null;
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[1];
		String execSql = null;
		int groupLength = group.length();
		if("1".equals(type)){
			execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 12 AND GROUP_NUMBER =  ?  ;"  ;
		}else if("2".equals(type)){
			if(groupLength == 3){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 11 AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,THREECODE_COMPOUND AS CURRENT_MISS,THREECODE_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +" AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else if("3".equals(type)){
			//前二值
			execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 9 AND GROUP_NUMBER =  ?  ;"  ;
		}else if("4".equals(type)){
			//前二组
			if(groupLength == 2){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 10 AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,TWOCODE_COMPOUND AS CURRENT_MISS,TWOCODE_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +" AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else if("5".equals(type)){
			if(groupLength == 3){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,OPTIONAL_COMPOUND AS CURRENT_MISS,OPTIONAL_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else if("6".equals(type)){
			if(groupLength == 4){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,OPTIONAL_COMPOUND AS CURRENT_MISS,OPTIONAL_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else{
			execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
		}
		Object[] queryParams = new Object[]{
				group
		};
		fast3Analysis = fast3AnalysisRepository.getEntityBySql(Fast3Analysis.class,execSql, queryParams);
		return fast3Analysis;
	}
	
	/* (non-Javadoc)
	 * @see com.bs.outer.service.OuterInterfaceService#get5In11MissAnalysisByTypeAndGroup(java.lang.String, java.lang.String)
	 */
	public Fast3Analysis get5In12MissAnalysisByTypeAndGroup(String type,String group,String provinceNumber){
		Fast3Analysis fast3Analysis = null;
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[3];
		String execSql = null;
		int groupLength = group.length();
		if("1".equals(type)){
			execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 12 AND GROUP_NUMBER =  ?  ;"  ;
		}else if("2".equals(type)){
			if(groupLength == 3){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 10 AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,THREECODE_COMPOUND AS CURRENT_MISS,THREECODE_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +" AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else if("3".equals(type)){
			//前二值
			execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 11 AND GROUP_NUMBER =  ?  ;"  ;
		}else if("4".equals(type)){
			//前二组
			if(groupLength == 2){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = 9 AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,TWOCODE_COMPOUND AS CURRENT_MISS,TWOCODE_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +" AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else if("5".equals(type)){
			if(groupLength == 3){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,OPTIONAL_COMPOUND AS CURRENT_MISS,OPTIONAL_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else if("6".equals(type)){
			if(groupLength == 4){
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}else{
				execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,OPTIONAL_COMPOUND AS CURRENT_MISS,OPTIONAL_COMPOUND_MAXMISS AS MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
			}
		}else{
			execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE TYPE = "+ groupLength +"  AND GROUP_NUMBER =  ?  ;"  ;
		}
		Object[] queryParams = new Object[]{
				group
		};
		fast3Analysis = fast3AnalysisRepository.getEntityBySql(Fast3Analysis.class,execSql, queryParams);
		return fast3Analysis;
	}
	/**
	 *    获取所有的遗漏统计结果
	 */
	private List<Fast3Analysis> get5In11AllMissAnalysis(String issueNumber,String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[1];
		//String tableName = "analysis.T_LN_5IN11_MISSANALYSIS";
		String execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE ISSUE_NUMBER > ?  ORDER BY TYPE,CURRENT_MISS DESC;"  ;
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Fast3Analysis> rtnList = fast3AnalysisRepository.getEntityListBySql(Fast3Analysis.class,execSql, queryParams);
		return rtnList;
	}
	
	/**
	 *    获取当前遗漏值最大的n个记录，掐尖
	 */
	public List<Fast3Analysis> get5In12MissAnalysisTop3(String issueNumber,String provinceNumber){
		List<Fast3Analysis>  rtnList = new ArrayList<Fast3Analysis>();
		List<Fast3Analysis> allMissList = this.get5In12AllMissAnalysis(issueNumber, provinceNumber);
		int n = 0;
		int type = 0;
		for(Fast3Analysis fast3Analysis :allMissList ){
			if(type != fast3Analysis.getType()){
				if(n < 3){
					rtnList.add(fast3Analysis);
					n++;
				}else if(n == 3){
					n = 0;
					type = fast3Analysis.getType();
				}else{
					continue;
				}
			}
		}
		return rtnList;
	}
	
	/**
	 *    获取所有的遗漏统计结果
	 */
	private List<Fast3Analysis> get5In12AllMissAnalysis(String issueNumber,String provinceNumber){
		String tableName = "analysis."+globalCacheService.getCacheMap(provinceNumber)[3];
		//String tableName = "analysis.T_LN_5IN11_MISSANALYSIS";
		String execSql = "SELECT ID,ISSUE_NUMBER,GROUP_NUMBER,CURRENT_MISS,MAX_MISS,TYPE FROM " + tableName + " WHERE ISSUE_NUMBER > ?  ORDER BY TYPE,CURRENT_MISS DESC;"  ;
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Fast3Analysis> rtnList = fast3AnalysisRepository.getEntityListBySql(Fast3Analysis.class,execSql, queryParams);
		return rtnList;
	}
	
	public StationOuterDTO toDto(	Station station){
		StationOuterDTO stationDto = new StationOuterDTO();
		stationDto.setId(station.getId());
		stationDto.setStationCode(station.getCode());
		stationDto.setStationNumber(station.getStationNumber());
		stationDto.setName(station.getOwner());
		stationDto.setTelephone(station.getOwnerTelephone());
		stationDto.setStationStyle("1".equals(station.getStationType()) ?"体彩":"福彩");
		//处理实体中的特殊转换值
		if(null != station.getCreaterTime())//创建时间
		{
			stationDto.setCreateTime(DateUtil.formatDate(station.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
		}
		if(null != station.getProvinceCode())//省级区域
		{
			Province province = new Province();
			province = provinceService.getProvinceByPcode(station.getProvinceCode());
			stationDto.setProvince(null != province?province.getPname():"");
		}
		if(null != station.getCityCode())//市级区域
		{
			if(Constants.CITY_ALL.equals(station.getCityCode()))
			{
				stationDto.setCity(Constants.CITY_ALL_NAME);
			}
			else
			{
				City city = new City();
				city = cityService.getCityByCcode(station.getCityCode());
				stationDto.setCity(null != city?city.getCname():"");
			}
		}
		return stationDto;
	}
	
	 public List<ShuangSQ> getShuangSQNumByIssueNumber(){
		 String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7 FROM analysis.T_DATA_BASE_SHUANG ORDER BY ISSUE_NUMBER DESC  LIMIT 300" ;
			Object[] queryParams = new Object[]{
			};
			List<ShuangSQ> shuangSQList = shuangSQRepository.getEntityListBySql(ShuangSQ.class,execSql, queryParams);
			return shuangSQList;
	 }
	   
	   /**
	 * @param issueNumber
	 * @return
	 */ 
	  public List<ThreeD> get3DNumByIssueNumber(){
		  String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,TEST_NUM FROM analysis.T_DATA_BASE_3D ORDER BY ISSUE_NUMBER DESC LIMIT 300" ;
			Object[] queryParams = new Object[]{
			};
			List<ThreeD> threeDList =threeDRepository.getEntityListBySql(ThreeD.class,execSql, queryParams);
			return threeDList;
	  }
	   
	   /**
	    * @param issueNumber
	    * @return
	    */
	  public List<QiLeCai> getQiLeCaiNumByIssueNumber(){
		    String execSql = "SELECT ID,ISSUE_NUMBER,NO1,NO2,NO3,NO4,NO5,NO6,NO7,NO8 FROM analysis.T_DATA_BASE_QILECAI ORDER BY ISSUE_NUMBER DESC LIMIT 300" ;
			Object[] queryParams = new Object[]{
			};
			List<QiLeCai> qiLeCaieDList =qiLeCaiRepository.getEntityListBySql(QiLeCai.class,execSql, queryParams);
			return qiLeCaieDList;
	  }	
}
