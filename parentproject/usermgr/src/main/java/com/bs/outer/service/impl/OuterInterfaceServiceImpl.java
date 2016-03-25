package com.bs.outer.service.impl;

import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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


}
