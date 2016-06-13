package com.bs.outer.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bs.outer.entity.AnnouncementReceipt;
import com.bs.outer.entity.QiLeCai;
import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeD;
import com.bs.outer.service.AnnouncementReceiptService;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.notice.controller.NoticeController;
import com.sdf.manager.notice.entity.AppNoticeAndArea;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.service.AppNoticeAndAreaService;
import com.sdf.manager.notice.service.NoticeService;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.service.RelaBsStaAppService;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;

@Component("taskJob") 
public class TimingQuarzMethodController {
	
	private Logger logger = LoggerFactory.getLogger(TimingQuarzMethodController.class);
	
	/***
	 * 因为quartz job和spring mvc的结合存在对象注入的问题，所以这个定时任务暂时使用mysql的events进行实现，每天凌晨执行检查
	 * CREATE event IF NOT EXISTS updateStationAndApp
 
		ON SCHEDULE EVERY 1 DAY STARTS '2016-03-14 01:00:00' 
		ON COMPLETION PRESERVE
		 
		DO
		 
		UPDATE rela_bs_station_and_app rbsas SET rbsas.STATUS='0',rbsas.MODIFY_TIME=NOW(),rbsas.MODIFY='useMysql'
		WHERE 
		rbsas.IS_DELETED='1' AND rbsas.STATUS='1' AND rbsas.END_TIME<NOW();/*更新到期的通行证和站点的关联关系*/
	
	
	
	@Autowired
	private RelaBsStaAppService relaBsStaAppService;
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private AnnouncementReceiptService announcementReceiptService;
	
	@Autowired
	private OuterInterfaceService outerInterfaceService;
	
	@Autowired
	private NoticeService noticeService;
	
	@Autowired
	private AppNoticeAndAreaService appNoticeAndAreaService;
	
	
	
	/**
	 * 
	 * @Title: updateRelaBsStaAndApp
	 * @Description: 每天凌晨一点检查到前一天为止已经到期的通行证和应用的关联数据，置查询出的数据为不可用状态
	 * @author:banna
	 * @return: void
	 */
    @Scheduled(cron = "0 0 1 * * ? ")  
	public void updateRelaBsStaAndApp()
	{
    	try
    	{
    		 String status = "1";//正在使用的应用关联数据
    		 Date et = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);//注意：在将string转换为date时，转换的格式一定要与string的格式统一，否则无法转换，eg：“2016-03-21”转换为date类型，只能使用DateUtil.SIMPLE_DATE_FORMAT转换，否则抛出异常
    		 Timestamp endTime = DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT);
    		 
    		 //获取正在使用的且已经到期的关联数据
    		 List<RelaBsStationAndApp> sapps = relaBsStaAppService.getRelaBsStationAndAppByStatusAndEndTime(status, endTime);
    		 
    		 for (RelaBsStationAndApp relaBsStationAndApp : sapps) {
    			 relaBsStationAndApp.setStatus("0");//将已过期的应用关联数据更新状态为"未使用"状态
    			  relaBsStationAndApp.setModify("quarzJob");
    		     relaBsStationAndApp.setModifyTime(new Timestamp(System.currentTimeMillis()));
    		     relaBsStaAppService.update(relaBsStationAndApp);//
    		     
    		     logger.info("更新RelaBsStationAndApp的数据到期，更新为未使用状态，更新数据id为="+relaBsStationAndApp.getId());
    			   
    		}
    		 
    		 logger.info("更新RelaBsStationAndApp到期数据操作完成，更新数据量为="+sapps.size());
    	}
    	catch(Exception e)
    	{
    		e.printStackTrace();
    		logger.error("更新RelaBsStationAndApp的数据到期定时任务执行错误，执行时间="+System.currentTimeMillis());
    	}
		
		 
	}
    
    /**
     * 
     * @Title: addReceiptOfAnnouncement
     * @Description: 每天凌晨3点筛选当天符合条件的要展示的通告数据
     * @author:banna
     * @return: void
     */
    @Scheduled(cron = "0 0 3 * * ? ")  //cron = "0 0 3 * * ? ""0 0/2 0/1 * * ? "
	public void addReceiptOfAnnouncement()
	{
    	Pageable pageable = new PageRequest(0, Integer.MAX_VALUE);
		//参数
		StringBuffer buffer = new StringBuffer();
		
		LinkedHashMap<String, String> orderBy = new LinkedHashMap<String, String>();
		orderBy.put("id", "desc");
		List<Object> params = new ArrayList<Object>();
		//只查询未删除数据
		params.add(Constants.IS_NOT_DELETED);//只查询有效的数据
		buffer.append(" isDeleted = ?").append(params.size());
		
		QueryResult<Station> stationList = stationService.getStationList(Station.class, buffer.toString(), params.toArray(),
				orderBy, pageable);
		List<Station> stations = stationList.getResultList();
		
		List<AnnouncementReceipt> announcementReceipts = new ArrayList<AnnouncementReceipt>();
		
		//循环通行证列表数据查询有效通行证对应的有效的通告数据
		String province;
		String city;
		String lotteryType;
		StringBuffer ugroupArr = new StringBuffer();
		for (Station station : stations) 
		{
			province = station.getProvinceCode();
			city = station.getCityCode();
			lotteryType = station.getStationType();//1；体彩2：福彩
			
			logger.info("1:获取通行证id为："+station.getId()+"的可以展示的通告数据");
			
			//放置分页参数
			Pageable pageableStation = new PageRequest(0,Integer.MAX_VALUE);//查询所有的数据
			
			ugroupArr = new StringBuffer();
			if(null!=station.getUserGroups()&&station.getUserGroups().size()>0)
			{
				
				for(int i=0;i<station.getUserGroups().size();i++)
				{
					if(i==0)
					{
						ugroupArr.append("'"+station.getUserGroups().get(i).getId()+"'");
					}
					else
					{
						ugroupArr.append(",");
						ugroupArr.append("'"+station.getUserGroups().get(i).getId()+"'");
					}
				}
			}
			
			//参数
			StringBuffer bufferStation = new StringBuffer();
			List<Object> paramsStation = new ArrayList<Object>();
			LinkedHashMap<String, String> orderByStation = new LinkedHashMap<String, String>();
			QueryResult<Announcement> announceResult = outerInterfaceService.
					getAnnouncementOfStaAndNotInReceipt(Announcement.class, bufferStation.toString(), paramsStation.toArray(),
							orderByStation, pageableStation, ugroupArr.toString(), province, city,lotteryType,station.getId());
			
			List<Announcement> announcements = announceResult.getResultList();
			
			//转换为通告回执表数据
			for (Announcement announcement : announcements) 
			{
				AnnouncementReceipt announcementReceipt = new AnnouncementReceipt();
				
				announcementReceipt.setAnnouncementId(announcement.getId());
				announcementReceipt.setEndTime(announcement.getEndTime());
				announcementReceipt.setStationId(station.getId());
				announcementReceipt.setStatus(OuterInterfaceController.ANNOUNCEMENT_NOT_READ);
				announcementReceipt.setStatusTime(new Timestamp(System.currentTimeMillis()));
				
				announcementReceipts.add(announcementReceipt);
			}
			
			logger.info("2：通行证id为："+station.getId()+"的可以展示的通告数据数量为="+announcements.size());
			
		}
		
		
		
		//保存通告回执表数据
		for (AnnouncementReceipt announcementReceipt : announcementReceipts) {
			
			announcementReceiptService.save(announcementReceipt);
		}
		
		
	}
    
    /**
     * 
     * @Title: addKjNotices
     * @Description: TODO:采集开奖数据作为开奖公告数据
     * @author:banna
     * @return: void
     */
    @Scheduled(cron = "0 0 7 * * ? ")  //每天上午7点执行0 0 7 * * ? ;0 0/2 0/1 * * ? 
  	public void addKjNotices()
  	{
    	
    	logger.info("addKjNotices：开始采集开奖数据作为开奖公告数据！");
    	
    	//1.analysis.T_DATA_BASE_QILECAI(七乐彩)
    	//①获取上一次的生成时间，根据“开奖公告名称”模糊查询
    	String qilecai = "七乐彩";
    	List<AppNoticeAndArea> appnoticeAndAreas = new ArrayList<AppNoticeAndArea>();
    	Date ct =  outerInterfaceService.getNoticeByAppNoticeName(qilecai);
    	
    	//②从analysis.T_DATA_BASE_QILECAI(七乐彩)获取上一次生成时间后的更新数据
    	List<QiLeCai> list = outerInterfaceService.getQiLeCaiKaijiang(ct);
    	
    	if(null!=list && list.size()>0)
    	{
    		logger.info("addKjNotices：开始生成七乐彩开奖公告！");
    		//③替换之前此彩种的开奖公告
        	List<Notice> lastlist = outerInterfaceService.getLastKjNoticeOfNoticename(qilecai).getResultList();
        	for (Notice notice : lastlist) {
        		notice.setIsDeleted("0");
    	 		notice.setModify("sysauto");
    	 		notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
    	 		
		 		
		 		notice.setAppNoticeAndAreas(appnoticeAndAreas);
    	 		noticeService.update(notice);
    	 		logger.info("删除开奖公告数据--id="+notice.getId()+"--操作人=sysauto");
    		}
        	
        	//④生成新的开奖公告数据
        	
        	   Notice  notice = new Notice();
    		   notice.setId(UUID.randomUUID().toString());
    		   notice.setAppNoticeName(qilecai);
    		   
    		   QiLeCai newQiLeCai = list.get(0);
    		   StringBuffer appNoticeWord =  new StringBuffer(qilecai+"开奖期号："+newQiLeCai.getIssueNumber() +"   开奖号码："
    		   		+ newQiLeCai.getNo1()+","+newQiLeCai.getNo2()+","+newQiLeCai.getNo3()+","+newQiLeCai.getNo4()+","+
    		   		newQiLeCai.getNo5()+","+newQiLeCai.getNo6()+","+newQiLeCai.getNo7()+","+newQiLeCai.getNo8());
    		   notice.setAppNoticeWord(appNoticeWord.toString());//开奖公告内容
    		   
    		   notice.setLotteryType(Constants.LOTTERY_TYPE_FC);
    		   notice.setNoticeStatus(NoticeController.NOTICE_STATUS_FB);//发布状态
    		   
    		   notice.setAppCategory(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG);
    		   Date st = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);
    		   Date et = DateUtil.getNextDayOfCurrentTime(new Timestamp(System.currentTimeMillis()), 365);
    		   
    		   try {
    			notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
    			notice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				System.out.println("时间转换错误");
    				logger.error("addKjNotices方法内转换七乐彩开奖公告时间转换错误!");
    			}
    		 
    		   
    		   notice.setModify("sysauto");
    		   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
    		   notice.setCreater("sysauto");
    		   notice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
    		   notice.setIsDeleted("1");
    		   
    		   //设置开奖类公告字体颜色
    		   notice.setNoticeFontColor("#1E90FF");
    		   
    		   noticeService.save(notice);//保存应用公告数据
    		   logger.info("生成七乐彩开奖公告：公告id="+notice.getId());
    	}
    	
    	/****/////
    	
    	
    	//2analysis.T_DATA_BASE_3D(3d)
    	//①获取上一次的生成时间，根据“开奖公告名称”模糊查询
    	String threeD = "3D";
    	Date threeDct =  outerInterfaceService.getNoticeByAppNoticeName(threeD);
    	
    	//②从analysis.T_DATA_BASE_3D(3d)获取上一次生成时间后的更新数据
    	List<ThreeD> threelist = outerInterfaceService.get3DNumKaijiang(threeDct);
    	
    	if(null!=threelist && threelist.size()>0)
    	{
    		logger.info("addKjNotices：开始生成3D开奖公告！");
    		//③替换之前此彩种的开奖公告
        	List<Notice> lastlist = outerInterfaceService.getLastKjNoticeOfNoticename(threeD).getResultList();
        	for (Notice notice : lastlist) {
        		notice.setIsDeleted("0");
    	 		notice.setModify("sysauto");
    	 		notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
    	 		
		 		
		 		notice.setAppNoticeAndAreas(appnoticeAndAreas);
    	 		noticeService.update(notice);
    	 		logger.info("删除开奖公告数据--id="+notice.getId()+"--操作人=sysauto");
    		}
        	
        	//④生成新的开奖公告数据
        	
        	   Notice  notice = new Notice();
    		   notice.setId(UUID.randomUUID().toString());
    		   notice.setAppNoticeName(threeD);
    		   
    		   ThreeD newThreeD = threelist.get(0);
    		   StringBuffer appNoticeWord =  new StringBuffer(threeD+"开奖期号："+newThreeD.getIssueNumber() +"   开奖号码："
    		   		+ newThreeD.getNo1()+","+newThreeD.getNo2()+","+newThreeD.getNo3());
    		   notice.setAppNoticeWord(appNoticeWord.toString());//开奖公告内容
    		   
    		   notice.setLotteryType(Constants.LOTTERY_TYPE_FC);
    		   notice.setNoticeStatus(NoticeController.NOTICE_STATUS_FB);//发布状态
    		   
    		   notice.setAppCategory(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG);
    		   Date st = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);
    		   Date et = DateUtil.getNextDayOfCurrentTime(new Timestamp(System.currentTimeMillis()), 365);
    		   
    		   try {
    			notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
    			notice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				System.out.println("时间转换错误");
    				logger.error("addKjNotices方法内3D开奖公告时间转换错误!");
    			}
    		 
    		   
    		   notice.setModify("sysauto");
    		   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
    		   notice.setCreater("sysauto");
    		   notice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
    		   notice.setIsDeleted("1");
    		   
    		   //设置开奖类公告字体颜色
    		   notice.setNoticeFontColor("#1E90FF");
    		   
    		   noticeService.save(notice);//保存应用公告数据
    		   logger.info("生成3D开奖公告：公告id="+notice.getId());
    	}
    	
    	/****/////
    	
    	//3.analysis.T_DATA_BASE_SHUANG(双色球)
    	//①获取上一次的生成时间，根据“开奖公告名称”模糊查询
    	String shuangSQ = "双色球";
    	Date shuangSQct =  outerInterfaceService.getNoticeByAppNoticeName(shuangSQ);
    	
    	//②从analysis.T_DATA_BASE_SHUANG(双色球)获取上一次生成时间后的更新数据
    	List<ShuangSQ> shuangSQlist = outerInterfaceService.getShuangSQKaijiang(shuangSQct);
    	
    	if(null!=shuangSQlist && shuangSQlist.size()>0)
    	{
    		logger.info("addKjNotices：开始生成双色球开奖公告！");
    		//③替换之前此彩种的开奖公告
        	List<Notice> lastlist = outerInterfaceService.getLastKjNoticeOfNoticename(shuangSQ).getResultList();
        	for (Notice notice : lastlist) {
        		notice.setIsDeleted("0");
    	 		notice.setModify("sysauto");
    	 		notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
    	 		
		 		
		 		notice.setAppNoticeAndAreas(appnoticeAndAreas);
    	 		noticeService.update(notice);
    	 		logger.info("删除开奖公告数据--id="+notice.getId()+"--操作人=sysauto");
    		}
        	
        	//④生成新的开奖公告数据
        	
        	   Notice  notice = new Notice();
    		   notice.setId(UUID.randomUUID().toString());
    		   notice.setAppNoticeName(shuangSQ);
    		   
    		   ShuangSQ newShuangSQ = shuangSQlist.get(0);
    		   StringBuffer appNoticeWord =  new StringBuffer(shuangSQ+"开奖期号："+newShuangSQ.getIssueNumber() +"   开奖号码："
    		   		+ newShuangSQ.getNo1()+","+newShuangSQ.getNo2()+","+newShuangSQ.getNo3()+","+newShuangSQ.getNo4()+","+
    		   		newShuangSQ.getNo5()+","+newShuangSQ.getNo6()+","+newShuangSQ.getNo7());
    		   notice.setAppNoticeWord(appNoticeWord.toString());//开奖公告内容
    		   
    		   notice.setLotteryType(Constants.LOTTERY_TYPE_FC);
    		   notice.setNoticeStatus(NoticeController.NOTICE_STATUS_FB);//发布状态
    		   
    		   notice.setAppCategory(NoticeController.APP_CATEGORY_COMPANY_KAIJIANG);
    		   Date st = DateUtil.formatStringToDate(DateUtil.formatCurrentDateWithYMD(), DateUtil.SIMPLE_DATE_FORMAT);
    		   Date et = DateUtil.getNextDayOfCurrentTime(new Timestamp(System.currentTimeMillis()), 365);
    		   
    		   try {
    			notice.setStartTime(DateUtil.formatDateToTimestamp(st, DateUtil.FULL_DATE_FORMAT));
    			notice.setEndTime(DateUtil.formatDateToTimestamp(et, DateUtil.FULL_DATE_FORMAT));
    			} catch (ParseException e) {
    				// TODO Auto-generated catch block
    				e.printStackTrace();
    				System.out.println("时间转换错误");
    				logger.error("addKjNotices方法内双色球开奖公告时间转换错误!");
    			}
    		 
    		   
    		   notice.setModify("sysauto");
    		   notice.setModifyTime(new Timestamp(System.currentTimeMillis()));
    		   notice.setCreater("sysauto");
    		   notice.setCreaterTime(new Timestamp(System.currentTimeMillis()));
    		   notice.setIsDeleted("1");
    		   
    		   //设置开奖类公告字体颜色
    		   notice.setNoticeFontColor("#1E90FF");
    		   
    		   noticeService.save(notice);//保存应用公告数据
    		   
    		   logger.info("生成双色球开奖公告：公告id="+notice.getId());
    	}
  	}
    
    
    
    
    
    
    
    
    
    
    
    
    

}
