package com.sdf.manager.common.service.cache;

import java.util.Map;

/** 
  * @ClassName: GlobalCacheService 
  * @Description: 全局缓存内容,目前缓存map 
  * @author songj@sdfcp.com
  * @date 2014年12月9日 下午3:26:10 
  *  
  */
public interface GlobalCacheService{
    
    /** 
      * @Description: TODO(这里用一句话描述这个类的作用) 
      * @author songj@sdfcp.com
      * @date 2014年12月9日 下午3:19:11 
      * 
      * @return 
      */
    public Map<String,String> getCacheMap(String key);
    
   
   
} 

