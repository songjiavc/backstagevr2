package com.bs.outer.controller;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bs.outer.entity.AnnouncementReceipt;
import com.bs.outer.entity.Fast3;
import com.bs.outer.entity.Fast3Analysis;
import com.bs.outer.entity.Fast3DanMa;
import com.bs.outer.entity.Fast3Same;
import com.bs.outer.entity.Fast3SiMa;
import com.bs.outer.entity.Ln5In12Bean;
import com.bs.outer.service.AnnouncementReceiptService;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.ad.controller.AdvertisementController;
import com.sdf.manager.ad.dto.AdvertisementDTO;
import com.sdf.manager.ad.entity.Advertisement;
import com.sdf.manager.ad.entity.Uploadfile;
import com.sdf.manager.ad.service.AdvertisementService;
import com.sdf.manager.ad.service.UploadfileService;
import com.sdf.manager.announcement.dto.AnnouncementDTO;
import com.sdf.manager.announcement.entity.Announcement;
import com.sdf.manager.announcement.service.AnnouncementService;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.service.AppUPriceService;
import com.sdf.manager.appversion.controller.AppversionController;
import com.sdf.manager.appversion.dto.AppversionDTO;
import com.sdf.manager.appversion.entity.Appversion;
import com.sdf.manager.appversion.service.AppversionService;
import com.sdf.manager.common.bean.ResultBean;
import com.sdf.manager.common.bean.ResultBeanData;
import com.sdf.manager.common.bean.ResultBeanDataList;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.companyNotice.dto.ComnoticeDTO;
import com.sdf.manager.companyNotice.entity.CompanyNotice;
import com.sdf.manager.companyNotice.service.CompanynoticeService;
import com.sdf.manager.notice.controller.NoticeController;
import com.sdf.manager.notice.dto.NoticeDTO;
import com.sdf.manager.notice.entity.ForecastMessage;
import com.sdf.manager.notice.entity.Notice;
import com.sdf.manager.notice.service.NoticeService;
import com.sdf.manager.order.dto.RenewAppDTO;
import com.sdf.manager.order.entity.RelaBsStationAndApp;
import com.sdf.manager.order.entity.RelaBsStationAndAppHis;
import com.sdf.manager.order.service.RelaBsStaAppHisService;
import com.sdf.manager.order.service.RelaBsStaAppService;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;
import com.sdf.manager.station.application.dto.StationDto;
import com.sdf.manager.station.controller.StationController;
import com.sdf.manager.station.entity.Station;
import com.sdf.manager.station.service.StationService;
import com.sdf.manager.user.bean.AccountBean;
import com.sdf.manager.user.entity.User;
import com.sdf.manager.user.service.UserService;
import com.sdf.manager.userGroup.entity.UserGroup;


/**
 * 
 * @ClassName: OuterInterfaceController
 * @Description: TODO二代后台外部接口控制层
 * @author: banna
 * @date: 2016年3月2日 上午10:46:50
 */
@Controller
@RequestMapping("lottoryOuter")
public class LottoryToolOuterController
{

	private Logger logger = LoggerFactory.getLogger(LottoryToolOuterController.class);
	
	@Autowired
	private OuterInterfaceService outerInterfaceService;//接口业务层
	
	/**
	 * @return  彩民工具获取辽宁12选5列表数据
	 */
	@RequestMapping(value="/getLn5In12List",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> getLn5In12List(@RequestParam(value="issueNumber",required=false) String issueNumber)
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		List<Ln5In12Bean> ln5In12List = null;
		try{
			if(issueNumber == null){
				ln5In12List =outerInterfaceService.getLn5In12Last3DaysList();
			}else{
				ln5In12List=outerInterfaceService.getLn5In12ListByIssueNumber(issueNumber);
			}
			
			if(ln5In12List.size() == 0 ){
				rtnMap.put("message","failure");
				rtnMap.put("status", "0");
			}else{
				rtnMap.put("message","success");
				rtnMap.put("status", "1");
				rtnMap.put("ln5In12List", ln5In12List);
			}
		}catch(Exception ex){
			logger.error("彩民工具获取辽宁12选5数据错误！");
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
}
