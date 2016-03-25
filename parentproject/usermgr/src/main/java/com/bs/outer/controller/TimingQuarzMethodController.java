package com.bs.outer.controller;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.service.RelaBsStaAppService;


@RequestMapping("timingQuarzMethod")
@Controller
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
//	= new RelaBsStaAppServiceImpl();//通行证与应用关联表的管理层
	
	/**
	 * 
	 * @Title: updateRelaBsStaAndApp
	 * @Description: TODO
	 * @author:banna
	 * @return: void
	 */
	 @RequestMapping(value = "/updateRelaBsStaAndApp", method = RequestMethod.GET)
	public void updateRelaBsStaAndApp()
	{
		 String status = "1";//正在使用的应用关联数据
		 Timestamp endTime = new Timestamp(System.currentTimeMillis());
		 System.out.println(status);
		 
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

}
