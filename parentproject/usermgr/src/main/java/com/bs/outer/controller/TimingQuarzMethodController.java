package com.bs.outer.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.bs.outer.entity.AnnouncementReceipt;
import com.bs.outer.service.AnnouncementReceiptService;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.service.RelaBsStaAppService;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.userGroup.entity.UserGroup;

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
    @Scheduled(cron = "0 0 3 * * ? ")  //cron = "0 0 3 * * ? "
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
			
			
			//放置分页参数
			Pageable pageableStation = new PageRequest(0,Integer.MAX_VALUE);//查询所有的数据
			
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
			
		}
		
		
		
		//保存通告回执表数据
		for (AnnouncementReceipt announcementReceipt : announcementReceipts) {
			
			announcementReceiptService.save(announcementReceipt);
		}
		
		
	}

}
