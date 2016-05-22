package com.sdf.manager.common.service.cache.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.sdf.manager.common.service.cache.GlobalCacheService;

@Service("globalCacheService")
public class GlobalCacheServiceImpl implements GlobalCacheService{
    
    private Map<String,String[]> cacheMap = new HashMap<String,String[]>(){
        /** 
		  * @Fields serialVersionUID : TODO(用一句话描述这个变量表示什么) 
		  */ 
		private static final long serialVersionUID = -8043475876888543076L;

		/** 
         * @Fields serialVersionUID : 唯一标示
         */ 
       {
    	   put("110000",new String[]{"T_BEIJING_KUAI3_NUMBER","T_BEIJING_KUAI3_MISSANALYSIS","T_BEIJING_KUAI3_DANMA","T_BEIJING_KUAI3_SIMA","T_BEIJING_KUAI3_SAMENUMBER"});
    	   put("120000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("130000",new String[]{"T_HEBEI_KUAI3_NUMBER","T_HEBEI_KUAI3_MISSANALYSIS","T_HEBEI_KUAI3_DANMA","T_HEBEI_KUAI3_SIMA","T_HEBEI_KUAI3_SAMENUMBER"});
    	   put("140000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("150000",new String[]{"T_NMG_KUAI3_NUMBER","T_NMG_KUAI3_MISSANALYSIS","T_NMG_KUAI3_DANMA","T_NMG_KUAI3_SIMA","T_NMG_KUAI3_SAMENUMBER"});
    	   put("210000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("220000",new String[]{"T_JILIN_KUAI3_NUMBER","T_JILIN_KUAI3_MISSANALYSIS","T_JILIN_KUAI3_DANMA","T_JILIN_KUAI3_SIMA","T_JILIN_KUAI3_SAMENUMBER"});
    	   put("230000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("310000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("320000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("330000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("340000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS","T_ANHUI_KUAI3_DANMA","T_ANHUI_KUAI3_SIMA","T_ANHUI_KUAI3_SAMENUMBER"});
    	   put("350000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("360000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("370000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("410000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("420000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("430000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("440000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("450000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("460000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("500000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("510000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("520000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("530000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("540000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("610000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("620000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("630000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("640000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("650000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("710000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("810000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
    	   put("820000",new String[]{"T_ANHUI_KUAI3_NUMBER","T_ANHUI_KUAI3_MISSANALYSIS"});
       }
    };
    
    public String[] getCacheMap(String key) {
        return this.cacheMap.get(key);
    }

    public void setCacheMap(String key,String[] value) {
        cacheMap.put(key, value);
    }
}
