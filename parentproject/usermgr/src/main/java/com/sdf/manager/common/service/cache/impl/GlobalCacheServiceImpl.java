package com.sdf.manager.common.service.cache.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sdf.manager.common.service.cache.GlobalCacheService;

@Service("globalCacheService")
public class GlobalCacheServiceImpl implements GlobalCacheService{
    
    private Map<String,Map<String,String>> cacheMap = new HashMap<String,Map<String,String>>(){
        /** 
		  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
		  */ 
		private static final long serialVersionUID = -8043475876888543076L;

		/** 
         * @Fields serialVersionUID : 唯一标示
         */ 
       {
		   /**
			* T_LN_5IN11_NUMBER
			danMaTbName=T_LN_5IN11_DANMA
			simaTbName=T_LN_5IN11_SIMA
			sameNumTbName=T_LN_5IN11_SAMENUMBER
			followNumTbName=T_LN_5IN11_FOLLOWNUMBER
			followIssue=T_LN_5IN11_FOLLOW_LAST_ISSUE
			followIssueNumber=T_LN_5IN11_FOLLOW_ISSUE_NUMBER
			*/

		   put("110000",new HashMap<String,String>(){
		   {
			   put("kuai3number","T_BEIJING_KUAI3_NUMBER");
			   put("kuai3missanalysis","T_BEIJING_KUAI3_MISSANALYSIS");
			   put("kuai3danma","T_BEIJING_KUAI3_DANMA");
			   put("kuai3sima","T_BEIJING_KUAI3_SIMA");
			   put("kuai3samenumber","T_BEIJING_KUAI3_SAMENUMBER");
			   
			   put("5in11number","T_BEIJING_5IN11_NUMBER");
			   put("5in11missanalysis","T_BEIJING_5IN11_MISSANALYSIS");
			   put("5in11danma","T_BEIJING_5IN11_DANMA");
			   put("5in11sima","T_BEIJING_5IN11_SIMA");
			   put("5in11samenumber","T_BEIJING_5IN11_SAMENUMBER");
			   put("5in11follownumber","T_BEIJING_5IN11_FOLLOWNUMBER");
			   put("5in11follownumberlastissue","T_BEIJING_5IN11_FOLLOW_LAST_ISSUE");
			   
		   }});
		   put("130000",new HashMap<String,String>(){
			   {
				   put("kuai3number","T_HEBEI_KUAI3_NUMBER");
				   put("kuai3missanalysis","T_HEBEI_KUAI3_MISSANALYSIS");
				   put("kuai3sima","T_HEBEI_KUAI3_SIMA");
				   put("kuai3samenumber","T_HEBEI_KUAI3_SAMENUMBER");
				   put("kuai3danma","T_HEBEI_KUAI3_DANMA");
				   
				   put("5in11number","T_HEBEI_5IN11_NUMBER");
				   put("5in11missanalysis","T_HEBEI_5IN11_MISSANALYSIS");
				   put("5in11danma","T_HEBEI_5IN11_DANMA");
				   put("5in11sima","T_HEBEI_5IN11_SIMA");
				   put("5in11samenumber","T_HEBEI_5IN11_SAMENUMBER");
				   put("5in11follownumber","T_HEBEI_5IN11_FOLLOWNUMBER");
				   put("5in11follownumberlastissue","T_HEBEI_5IN11_FOLLOW_LAST_ISSUE");
			   }
		   });
		   put("150000",new HashMap<String,String>(){
			   {
				   put("kuai3number","T_NMG_KUAI3_NUMBER");
				   put("kuai3missanalysis","T_NMG_KUAI3_MISSANALYSIS");
				   put("kuai3sima","T_NMG_KUAI3_SIMA");
				   put("kuai3samenumber","T_NMG_KUAI3_SAMENUMBER");
				   put("kuai3danma","T_NMG_KUAI3_DANMA");
				   
				   put("5in11number","T_NMG_5IN11_NUMBER");
				   put("5in11missanalysis","T_NMG_5IN11_MISSANALYSIS");
				   put("5in11danma","T_NMG_5IN11_DANMA");
				   put("5in11sima","T_NMG_5IN11_SIMA");
				   put("5in11samenumber","T_NMG_5IN11_SAMENUMBER");
				   put("5in11follownumber","T_NMG_5IN11_FOLLOWNUMBER");
				   put("5in11follownumberlastissue","T_NMG_5IN11_FOLLOW_LAST_ISSUE");
			   }
		   });
		   put("210000",new HashMap<String,String>(){
			   {
				   put("5in11number","T_LN_5IN11_NUMBER");
				   put("5in11missanalysis","T_LN_5IN11_MISSANALYSIS");
				   put("5in11danma","T_LN_5IN11_DANMA");
				   put("5in11sima","T_LN_5IN11_SIMA");
				   put("5in11samenumber","T_LN_5IN11_SAMENUMBER");
				   put("5in11follownumber","T_LN_5IN11_FOLLOWNUMBER");
				   put("5in11follownumberlastissue","T_LN_5IN11_FOLLOW_LAST_ISSUE");
				   
				   put("5in12number","T_LN_5IN12_NUMBER");
				   put("5in12missanalysis","T_LN_5IN12_MISSANALYSIS");
				   put("5in12danma","T_LN_5IN12_DANMA");
				   put("5in12sima","T_LN_5IN12_SIMA");
				   put("5in12samenumber","T_LN_5IN12_SAMENUMBER");
				   put("5in12follownumber","T_LN_5IN12_FOLLOWNUMBER");
				   put("5in12follownumberlastissue","T_LN_5IN12_FOLLOW_LAST_ISSUE");
			   }
		   });
		   put("220000",new HashMap<String,String>(){
			   {
				   put("kuai3number","T_JILIN_KUAI3_NUMBER");
				   put("kuai3missanalysis","T_JILIN_KUAI3_MISSANALYSIS");
				   put("kuai3sima","T_JILIN_KUAI3_SIMA");
				   put("kuai3samenumber","T_JILIN_KUAI3_SAMENUMBER");
				   put("kuai3danma","T_JILIN_KUAI3_DANMA");
				   
				   put("5in11number","T_JILIN_5IN11_NUMBER");
				   put("5in11missanalysis","T_JILIN_5IN11_MISSANALYSIS");
				   put("5in11danma","T_JILIN_5IN11_DANMA");
				   put("5in11sima","T_JILIN_5IN11_SIMA");
				   put("5in11samenumber","T_JILIN_5IN11_SAMENUMBER");
				   put("5in11follownumber","T_JILIN_5IN11_FOLLOWNUMBER");
				   put("5in11follownumberlastissue","T_JILIN_5IN11_FOLLOW_LAST_ISSUE");
			   }
		   });
		   put("230000",new HashMap<String,String>(){
			   {
				   put("5in11number","T_HLJ_5IN11_NUMBER");
				   put("5in11missanalysis","T_HLJ_5IN11_MISSANALYSIS");
				   put("5in11danma","T_HLJ_5IN11_DANMA");
				   put("5in11sima","T_HLJ_5IN11_SIMA");
				   put("5in11samenumber","T_HLJ_5IN11_SAMENUMBER");
				   put("5in11follownumber","T_HLJ_5IN11_FOLLOWNUMBER");
				   put("5in11follownumberlastissue","T_HLJ_5IN11_FOLLOW_LAST_ISSUE");
			   }
		   });
		   put("340000",new HashMap<String,String>(){
			   {
				   put("kuai3number","T_ANHUI_KUAI3_NUMBER");
				   put("kuai3missanalysis","T_ANHUI_KUAI3_MISSANALYSIS");
				   put("kuai3sima","T_ANHUI_KUAI3_SIMA");
				   put("kuai3danma","T_ANHUI_KUAI3_DANMA");
				   put("kuai3samenumber","T_ANHUI_KUAI3_SAMENUMBER");
				   
				   put("5in11number","T_ANHUI_5IN11_NUMBER");
				   put("5in11missanalysis","T_ANHUI_5IN11_MISSANALYSIS");
				   put("5in11danma","T_ANHUI_5IN11_DANMA");
				   put("5in11sima","T_ANHUI_5IN11_SIMA");
				   put("5in11samenumber","T_ANHUI_5IN11_SAMENUMBER");
				   put("5in11follownumber","T_ANHUI_5IN11_FOLLOWNUMBER");
				   put("5in11follownumberlastissue","T_ANHUI_5IN11_FOLLOW_LAST_ISSUE");
			   }});
       }
    };
    
    public Map<String,String> getCacheMap(String key) {
        return this.cacheMap.get(key);
    }

}
