package com.bs.outer.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bs.outer.dto.FigureAndPuzzlesOuterDTO;
import com.bs.outer.entity.ShuangSQ;
import com.bs.outer.entity.ThreeDTiming;
import com.bs.outer.service.FigureAndPuzzleOuterInterfaceService;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.figureMysteryPuzzlesApp.entity.FigureAndPuzzles;

/**
 * 
* @Description: TODO(图谜字谜应用后台接口类) 
* @author banna
* @date 2016年10月26日 下午4:02:29
 */
@Controller
@RequestMapping("fapOuterInterface")
public class FigureAndPuzzleOuterInterfaceController 
{
	private Logger logger = LoggerFactory.getLogger(FigureAndPuzzleOuterInterfaceController.class);
	
	@Autowired
	private FigureAndPuzzleOuterInterfaceService figureAndPuzzleOuterInterfaceService;
	
	@Autowired
	private AppService appService;
	
	
	/**
	 * 
	* @Title: getKjnumOfTodayinhistory 
	* @Description: TODO(获取3d传入期号的历史数据，不包括当前期号)实现：使用like '%期号' and issuenum !=  issuenum
	* @param @param issuenum：要获取历史开奖数据的期号
	* * @param @param playName:玩法：3D，双色球等等
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月26日 下午3:56:53 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public @ResponseBody Map<String, Object> getKjnumOfTodayinhistory(String issuenum,String playName)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		
		if("3D".equals(playName))
		{//获取3D当前期号的历史上的今天的数据
			List<ThreeDTiming> threeDTimings = new ArrayList<ThreeDTiming>();
			
			threeDTimings = figureAndPuzzleOuterInterfaceService.get3DNumKaijiangTodayInhistory(issuenum);
			
			logger.info("getThreeDOfTodayinhistory3D==issuenum"+issuenum+"==历史数据量="+threeDTimings.size());
			
			result.put("todayinhistoryData", threeDTimings);
		}
		else
			if("双色球".equals(playName))
			{//获取3D当前期号的历史上的今天的数据
				List<ShuangSQ> shuangSQs = new ArrayList<ShuangSQ>();
				
				shuangSQs = figureAndPuzzleOuterInterfaceService.getShuangSQNumKaijiangTodayInhistory(issuenum);
				
				logger.info("getThreeDOfTodayinhistory双色球==issuenum"+issuenum+"==历史数据量="+shuangSQs.size());
				
				result.put("todayinhistoryData", shuangSQs);
			}
		
		
		
		return result;
	}
	
	/**
	 * 
	* @Title: getFigureAndPuzzles 
	* @Description: TODO(获取当前玩法的即将开奖期号的图谜字谜数据) 
	* @param @param appId：应用id在这个方法中用来获取区域和彩种的信息，例如获取是辽宁省的福彩的图谜字谜应用
	* @param @param issuenum
	* @param @param playName
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月26日 下午4:02:02 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public @ResponseBody Map<String, Object> getFigureAndPuzzles(String appId,String issuenum,String playName)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		
		// TODO:获取图谜字谜数据，将底板图片等等图片信息也一起返回
		
		App app = appService.getAppById(appId);
		
		String province = app.getProvince();
		String city = app.getCity();
		String lotteryType = app.getLotteryType();
		
		List<FigureAndPuzzles> figureAndPuzzles = 
				figureAndPuzzleOuterInterfaceService.getFigureAndPuzzles(issuenum, playName, province, city, lotteryType);
		
		
		List<FigureAndPuzzlesOuterDTO> dtos = new ArrayList<FigureAndPuzzlesOuterDTO>();
		
		//TODO:将返回的图谜字谜数据进行处理，处理后返回
		/**待根据需求进行sql测试
		 * SELECT fap.* FROM T_BYL_FIGURE_AND_PUZZLES fap LEFT JOIN RELA_BS_FIGURE_AND_PUZZLE_AND_AREA faparea ON fap.ID = faparea.FIGURE_AND_PUZZLE_ID
			WHERE fap.IS_DELETED = '1' AND fap.STATUS='21' AND faparea.PROVINCE='110000' AND faparea.CITY='110100' AND fap.PLAY_NAME='3D'
		 */
		
		
		result.put("figureAndPuzzles", dtos);
		 
		
		
		return result;
	}
	
	/**
	 * 
	* @Title: getKjNum 
	* @Description: TODO(获取当前彩种玩法的当前期号的开奖号码) 
	* @param @param issuenum
	* @param @param playName
	* @param @return    设定文件 
	* @author banna
	* @date 2016年10月26日 下午4:21:25 
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public @ResponseBody Map<String, Object> getKjNum(String issuenum,String playName)
	{
		Map<String, Object> result = new HashMap<String, Object>();
		
		if("3D".equals(playName))
		{
			ThreeDTiming threeD = new ThreeDTiming();
			// TODO:1.获取3D开奖号码
			
			threeD = figureAndPuzzleOuterInterfaceService.get3DNumKaijiang(issuenum);
			
			result.put("kjNum", threeD);
			
			// TODO:2.获取当前彩种当前期的销量等信息
			Map<String,Object> sellMsg = new HashMap<String,Object>();
			//TODO:待确定如何获取销量等信息的处理
			result.put("sellMsg", sellMsg);
			
		}
		else
			if("双色球".equals(playName))
			{
				ShuangSQ shuangSQ = new ShuangSQ();
				// TODO:获取双色球开奖号码
				result.put("kjNum", shuangSQ);
				
				// TODO:2.获取当前彩种当前期的销量等信息
				Map<String,Object> sellMsg = new HashMap<String,Object>();
				//TODO:待确定如何获取销量等信息的处理
				result.put("sellMsg", sellMsg);
			}
		
		
		return result;
	}
	
	
}
