package com.bs.outer.service.impl;

import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.bs.outer.entity.Fast3;
import com.bs.outer.entity.Fast3Analysis;
import com.bs.outer.entity.Fast3DanMa;
import com.bs.outer.entity.Fast3Same;
import com.bs.outer.entity.Fast3SiMa;
import com.bs.outer.repository.Fast3AnalysisRepository;
import com.bs.outer.repository.Fast3DanMaRepository;
import com.bs.outer.repository.Fast3NumberRepository;
import com.bs.outer.repository.Fast3SameRepository;
import com.bs.outer.repository.Fast3SiMaRepository;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.repository.AdvertisementRepository;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.announcement.repository.AnnouncementRepository;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.companyNotice.repository.CompanynoticeRepository;
import com.sdf.manager.notice.controller.NoticeController;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.repository.NoticeRepository;

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
			Pageable pageable,String ugroups,String province,String city,String appId,String lotteryType) {
		StringBuffer sql = new StringBuffer("SELECT u.* FROM ((T_BS_APP_AD u LEFT JOIN RELA_BS_APPAD_AND_APP app ON u.ID=app.APP_AD_ID)  "+
				"	 LEFT JOIN RELA_BS_APPAD_AND_UGROUP au ON u.ID=au.APP_AD_ID) LEFT JOIN RELA_BS_APPAD_AND_AREA aarea ON u.id = aarea.APP_AD_ID WHERE u.IS_DELETED='1'  "+
				"	AND u.AD_STATUS='1' AND app.APP_ID='"+appId+"'"+
				"   AND u.AD_END_TIME>=CURDATE() AND u.AD_START_TIME<=CURDATE() ");//AND u.LOTTERY_TYPE='"+lotteryType+"'"
		
		if(ugroups.length()>0)
		{
			sql.append("	AND (au.USERGROUP_ID IN ("+ugroups+") OR (aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"')) GROUP BY u.id");
		}
		else
		{
			sql.append(" AND aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"' GROUP BY u.id");
		}
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
		StringBuffer sql = new StringBuffer("SELECT u.* FROM ((T_BS_APP_NOTICE u LEFT JOIN RELA_BS_NOTICE_AND_APP app ON u.ID=app.APP_NOTICE_ID) "+
				"    LEFT JOIN RELA_BS_NOTICE_AND_UGROUP au ON u.ID=au.APP_NOTICE_ID) LEFT JOIN RELA_BS_NOTICE_AND_AERA aarea ON u.id = aarea.NOTICE_ID WHERE u.IS_DELETED='1' "+
				"   AND u.NOTICE_STATUS='1' AND  u.APP_CATEGORY='"+NoticeController.APP_CATEGORY_COMPANY_KAIJIANG+"' "+
				"   AND u.NOTICE_ENDTIME>=CURDATE() AND u.NOTICE_STARTTIME<=CURDATE() AND u.LOTTERY_TYPE='"+lotteryType+"'");
			sql.append(" AND aarea.PROVINCE='"+province+"' AND aarea.CITY='"+city+"' GROUP BY u.id");
		QueryResult<Notice> userObj = noticeRepository.
			getScrollDataBySql(Notice.class,sql.toString(), queryParams, pageable);
		return userObj;
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
		String tableName = "analysis.T_ANHUI_KUAI3_NUMBER";
		String execSql = "SELECT u.* FROM "+tableName +" u  WHERE ISSUE_NUMBER > ? LIMIT 1 ";
		Object[] queryParams = new Object[]{
				issueNumber
		};
		Fast3 fast3 = fast3NumberRepository.getEntityBySql(Fast3.class,execSql, queryParams);
		return fast3;
	}
	
	public List<Fast3Analysis> getAnalysisListByIssueNumber(String issueNumber,String provinceNumber){
		String tableName = "analysis.T_ANHUI_KUAI3_MISSANALYSIS";
		String execSql = "SELECT u.* FROM "+tableName +" u  WHERE ISSUE_NUMBER >  ?  ";
		Object[] queryParams = new Object[]{
				issueNumber
		};
		List<Fast3Analysis> fast3AnalysisList = fast3AnalysisRepository.getEntityListBySql(Fast3Analysis.class,execSql, queryParams);
		return fast3AnalysisList;
	}

	public List<Fast3DanMa> getInitDanmaList(String issueNumber,String provinceNumber){
		String tableName = "analysis.T_ANHUI_KUAI3_DANMA";
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
		String tableName = "analysis.T_ANHUI_KUAI3_SIMA";
		String where = null;
		if(siMaId == 0){
			where = " ORDER BY ID DESC LIMIT 10 ";
		}else{
			where = " WHERE "+ siMaId +"  <  (SELECT ID FROM "+ tableName +" ORDER BY ID DESC LIMIT 1)  ORDER BY ID DESC LIMIT 10 ";
		}
		String execSql = "SELECT * FROM "+tableName + where;
		Object[] queryParams = new Object[]{
		};
		List<Fast3SiMa> fast3SiMaList = fast3SiMaRepository.getEntityListBySql(Fast3SiMa.class,execSql, queryParams);
		return fast3SiMaList;
	}


	public List<Fast3Same> getInitSameList(String issueNumber, String provinceNumber) {
		String tableName = "analysis.T_ANHUI_KUAI3_SAMENUMBER";
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

}
