package com.bs.outer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bs.outer.entity.Fast3Analysis;
import com.bs.outer.entity.Fast3DanMa;
import com.bs.outer.entity.Fast3Same;
import com.bs.outer.entity.Fast3SiMa;
import com.bs.outer.entity.HotCoolBean;
import com.bs.outer.entity.Ln5In12Bean;
import com.bs.outer.service.OuterInterfaceService;
import com.sdf.manager.common.bean.ResultBeanData;
import com.sdf.manager.common.util.DateUtil;


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
	
	/**
	 * @return  彩民工具获取辽宁12选5列表数据
	 */
	@RequestMapping(value="/get5In12ListByProvinceNumber",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get5In12ListByProvinceNumber(@RequestParam(value="issueNumber",required=false) String issueNumber,@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		List<Ln5In12Bean> ln5In12List = null;
		try{
			if(issueNumber == null){
				ln5In12List =outerInterfaceService.get5In12LastRecord100List(provinceNumber);
			}else{
				ln5In12List=outerInterfaceService.get5In12ListByIssueNumber(issueNumber,provinceNumber);
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
			logger.error("12选5应用获取应用数据错误！"+ex.getMessage());
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
	/**
	 * @return  彩民工具获取辽宁12选5列表数据
	 */
	@RequestMapping(value="/get5In11ListByProvinceNumber",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get5In11ListByProvinceNumber(@RequestParam(value="issueNumber",required=false) String issueNumber,@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		List<Ln5In12Bean> ln5In12List = null;
		try{
			if(issueNumber == null){
				ln5In12List =outerInterfaceService.get5In11LastRecord100List(provinceNumber);
			}else{
				ln5In12List=outerInterfaceService.get5In11ListByIssueNumber(issueNumber, provinceNumber);
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
			logger.error("11选5应用获取应用数据错误！"+ex.getMessage());
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
	/**
	 * @return  彩民工具获取辽宁12选5列表数据
	 */
	@RequestMapping(value="/get5In11MissAnalysisTop3",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get5In11MissAnalysisTop3(@RequestParam(value="issueNumber",required=false) String issueNumber,@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		List<Fast3Analysis> fast3AnalysisList = null;
		try{
			fast3AnalysisList = outerInterfaceService.get5In11MissAnalysisTop3(issueNumber, provinceNumber);
			if(fast3AnalysisList.size() == 0 ){
				rtnMap.put("message","failure");
				rtnMap.put("status", "0");
			}else{
				rtnMap.put("message","success");
				rtnMap.put("status", "1");
				rtnMap.put("analysisList", fast3AnalysisList);
			}
		}catch(Exception ex){
			logger.error("11选5遗漏统计结果错误！"+ex.getMessage()+"\nissueNumber="+issueNumber+"provinceNumber="+provinceNumber);
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
	/**
	 * @return  彩民工具获取辽宁12选5列表数据
	 */
	@RequestMapping(value="/get5In12MissAnalysisTop3",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get5In12MissAnalysisTop3(@RequestParam(value="issueNumber",required=false) String issueNumber,@RequestParam(value="provinceNumber",required=true) String provinceNumber)
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		List<Fast3Analysis> fast3AnalysisList = null;
		try{
			fast3AnalysisList = outerInterfaceService.get5In12MissAnalysisTop3(issueNumber, provinceNumber);
			if(fast3AnalysisList.size() == 0 ){
				rtnMap.put("message","failure");
				rtnMap.put("status", "0");
			}else{
				rtnMap.put("message","success");
				rtnMap.put("status", "1");
				rtnMap.put("analysisList", fast3AnalysisList);
			}
		}catch(Exception ex){
			logger.error("12选5遗漏统计结果错误！"+ex.getMessage()+"\nissueNumber="+issueNumber+"provinceNumber="+provinceNumber);
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
	/**
	 * @return  彩民工具获取辽宁12选5列表数据
	 */
	@RequestMapping(value="/get5In12MissAnalysisByTypeAndGroup",method = RequestMethod.GET)
	public @ResponseBody ResultBeanData<Fast3Analysis> get5In12MissAnalysisByTypeAndGroup(
			@RequestParam(value="provinceNumber",required=true) String provinceNumber,
			@RequestParam(value="type",required=true) String type,
			@RequestParam(value="group",required=true) String group)
	{
		ResultBeanData<Fast3Analysis> result = new ResultBeanData<Fast3Analysis>();
		try{
			Fast3Analysis fast3Analysis = outerInterfaceService.get5In12MissAnalysisByTypeAndGroup(type,group, provinceNumber);
			result.setStatus("success");
			result.setMessage("遗漏数据查询成功！");
			result.setEntity(fast3Analysis);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("12选5遗漏数据查询失败！"+ex.getMessage()+"\type="+type+"group="+group+"provinceNumber="+provinceNumber);
			result.setStatus("failure");
			result.setMessage("遗漏数据查询失败！");
		}finally{
			return result;
		}
	}
	
	/**
	 * @return  彩民工具获取辽宁11选5列表数据
	 */
	@RequestMapping(value="/get5In11MissAnalysisByTypeAndGroup",method = RequestMethod.GET)
	public @ResponseBody ResultBeanData<Fast3Analysis> get5In11MissAnalysisByTypeAndGroup(
			@RequestParam(value="provinceNumber",required=true) String provinceNumber,
			@RequestParam(value="type",required=true) String type,
			@RequestParam(value="group",required=true) String group)
	{
		ResultBeanData<Fast3Analysis> result = new ResultBeanData<Fast3Analysis>();
		try{
			Fast3Analysis fast3Analysis = outerInterfaceService.get5In11MissAnalysisByTypeAndGroup(type,group, provinceNumber);
			result.setStatus("success");
			result.setMessage("遗漏数据查询成功！");
			result.setEntity(fast3Analysis);
		}catch(Exception ex){
			ex.printStackTrace();
			logger.error("11选5遗漏数据查询失败！"+ex.getMessage()+"\type="+type+"group="+group+"provinceNumber="+provinceNumber);
			result.setStatus("failure");
			result.setMessage("遗漏数据查询失败！");
		}finally{
			return result;
		}
	}

	/**
	 *
	 * @Title: getLotteryNum
	 * @Description:  获取遗漏统计内容
	 * * 对应的返回json数据结构：
	 * @author:songjia
	 * @return: Fast3
	 */
	@RequestMapping(value="/get5In11StatisticsInfo",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get5In11StatisticsInfo(@RequestParam(value="danMaIssueNumber",required=true) String danMaIssueNumber ,
																   @RequestParam(value="siMaId",required=true) String siMaId ,
																   @RequestParam(value="sameIssueNumber",required=true) String sameIssueNumber ,
																   @RequestParam(value="followIssueNumber",required=true) String followIssueNumber,
																   @RequestParam(value="provinceNumber",required=true) String provinceNumber
																   )
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		try{
			List<Fast3DanMa> danMaList =outerInterfaceService.get5In11InitDanmaList(danMaIssueNumber, provinceNumber);
			List<Fast3SiMa> simaList = outerInterfaceService.get5In11InitSimaList(Integer.parseInt(siMaId), provinceNumber);
			List<Fast3Same> sameList = outerInterfaceService.get5In11InitSameList(sameIssueNumber, provinceNumber);
			List<HotCoolBean> hotList = outerInterfaceService.getHotCoolList(followIssueNumber,provinceNumber);
			if(danMaList.size() == 0 || simaList.size() == 0 || sameList.size() == 0 || hotList.size() == 0){
				rtnMap.put("message","failure");
				rtnMap.put("status", "0");
			}else{
				rtnMap.put("message","success");
				rtnMap.put("status", "1");
				rtnMap.put("danMaList", danMaList);
				rtnMap.put("simaList", simaList);
				rtnMap.put("sameList", sameList);
				rtnMap.put("hotList",hotList);
			}
		}catch(Exception ex){
			logger.error("获取统计附表数据接口错误！provinceNumber="+provinceNumber);
			ex.printStackTrace();
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
	
	/**
	 *
	 * @Title: getLotteryNum
	 * @Description:  获取遗漏统计内容
	 * * 对应的返回json数据结构：
	 * @author:songjia
	 * @return: Fast3
	 */
	@RequestMapping(value="/get5In12StatisticsInfo",method = RequestMethod.GET)
	public @ResponseBody Map<String,Object> get5In12StatisticsInfo(@RequestParam(value="danMaIssueNumber",required=true) String danMaIssueNumber ,
																   @RequestParam(value="siMaId",required=true) String siMaId ,
																   @RequestParam(value="sameIssueNumber",required=true) String sameIssueNumber ,
																   @RequestParam(value="followIssueNumber",required=true) String followIssueNumber,
																   @RequestParam(value="provinceNumber",required=true) String provinceNumber
																   )
	{
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		try{
			List<Fast3DanMa> danMaList =outerInterfaceService.get5In12InitDanmaList(danMaIssueNumber, provinceNumber);
			List<Fast3SiMa> simaList = outerInterfaceService.get5In12InitSimaList(Integer.parseInt(siMaId), provinceNumber);
			List<Fast3Same> sameList = outerInterfaceService.get5In12InitSameList(sameIssueNumber, provinceNumber);
			List<HotCoolBean> hotList = outerInterfaceService.get5In12HotCoolList(followIssueNumber,provinceNumber);
			if(danMaList.size() == 0 || simaList.size() == 0 || sameList.size() == 0 || hotList.size() == 0){
				rtnMap.put("message","failure");
				rtnMap.put("status", "0");
			}else{
				rtnMap.put("message","success");
				rtnMap.put("status", "1");
				rtnMap.put("danMaList", danMaList);
				rtnMap.put("simaList", simaList);
				rtnMap.put("sameList", sameList);
				rtnMap.put("hotList",hotList);
			}
		}catch(Exception ex){
			logger.error("获取统计附表数据接口错误！provinceNumber="+provinceNumber);
			rtnMap.put("message","failure");
			rtnMap.put("status", "0");
		}finally{
			return rtnMap;
		}
	}
	
	/**
	 *
	 * @Title: getLotteryNum
	 * @Description:  获取遗漏统计内容
	 * * 对应的返回json数据结构：
	 * @author:songjia
	 * @return: Fast3
	 */
	@RequestMapping(value="/getServiceNowTime",method = RequestMethod.GET)
	public @ResponseBody String getServiceNowTime()
	{
		
		return DateUtil.formatCurrentDateWithYMDHMS();
	}
}
