package com.sdf.manager.common.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 与日期相关的工具类
 * @author user
 *
 */
public class DateUtil {
    private static int year;

    public static final String SIMPLE_DATE_FORMAT = "yyyy-MM-dd";

    public static final String SHORT_DATE_FORMAT = "MM-dd HH:mm";

    public static final String FULL_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static Log logger = LogFactory.getLog(DateUtil.class);
    /**
     *  "yyyy-MM-dd hh:mm:ss"，
     * @param date
     * @param formatstr
     * @return
     */
    public static String formatDate(Date date, String formatstr) {

        SimpleDateFormat df = new SimpleDateFormat(formatstr);
        String result = "";
        if (date == null) {
            return result;
        }
        try {
            result = df.format(date);
        } catch (Exception e) {
           logger.error(e);
        }

        return result;
    }

    /**
     * 
     * Created on 2010-8-24 
     * <p>Description:[把string转换date]</p>
     * @param dateStr
     * @param formatstr
     * @return
     * @author:<a href=wang_l_neu@neusoft.com>王亮</a>
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public static Date formatStringToDate(String dateStr, String formatstr) {

        SimpleDateFormat df = new SimpleDateFormat(formatstr);
        Date date = null;
        if (dateStr.isEmpty()) {
            return date;
        }
        try {
            date = df.parse(dateStr);
        } catch (ParseException e) {
            logger.error(e);
        }
        return date;
    }
    
    /**
     * 
    * @Title: formatDateToTimestamp
    * @Description: 将date转换为时间戳
    * @Author : banna
    * @param @param date
    * @param @param formatstr
    * @param @return
    * @param @throws ParseException    设定文件
    * @return Timestamp    返回类型
    * @throws
     */
    public static Timestamp formatDateToTimestamp(Date date,String formatstr) throws ParseException {

        SimpleDateFormat df = new SimpleDateFormat(formatstr);
        Timestamp timestamp = null;
        if (null == date) {
            return timestamp;
        }
        //先转换dateTostring
		String dateString = DateUtil.formatDate(date, formatstr);
		
		timestamp = Timestamp.valueOf(dateString);
        return timestamp;
    }

    /**
     * 获得一天的最早日期时间,如传进来2008-09-09 20:15:30 返回 2008-09-09 00:00:00
     * @param date
     * @return
     */
    public static Timestamp getBeginINDay(Timestamp date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return new Timestamp(ca.getTimeInMillis());
    }

    /**
     * 获得一天的最早日期时间,如传进来2008-09-09 20:15:30 返回 2008-09-09 00:00:00
     * @param date
     * @return
     */
    public static Date getBeginINDay(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 0);
        ca.set(Calendar.MINUTE, 0);
        ca.set(Calendar.SECOND, 0);
        return new Date(ca.getTimeInMillis());
    }

    /**
     * 获得一天的最晚日期时间,如传进来2008-09-09 10:15:30 返回 2008-09-09 23:59:59
     * @param date
     * @return
     */
    public static Timestamp getEndINDay(Timestamp date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return new Timestamp(ca.getTimeInMillis());
    }

    /**
     * 获得一天的最晚日期时间,如传进来2008-09-09 10:15:30 返回 2008-09-09 23:59:59
     * @param date
     * @return
     */
    public static Date getEndINDay(Date date) {
        Calendar ca = Calendar.getInstance();
        ca.setTime(date);
        ca.set(Calendar.HOUR_OF_DAY, 23);
        ca.set(Calendar.MINUTE, 59);
        ca.set(Calendar.SECOND, 59);
        return new Date(ca.getTimeInMillis());
    }

    /**
     * 
     * Created on 2010-10-25 
     * <p>Description:[获得n天后的日期]</p>
     * @param n
     * @return
     */
    public static Date getNextDayOfCurrentTime(Timestamp currentTime,int n) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(currentTime.getTime());
        calendar.add(Calendar.DAY_OF_MONTH, n);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
    
    public static Date getNextDay(int n) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.DAY_OF_MONTH, n);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * Created on 2010-10-26 
     * <p>Description:[获得n月之后日期]</p>
     * @param i
     * @return
     * @author:<a href=wang_l_neu@neusoft.com>王亮</a>
     * @update:[日期YYYY-MM-DD] [更改人姓名]
     */
    public static Date getNextMonth(int n) {
        
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MONTH, n);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    /**
     * 根据出生日期计算年龄
     * @param birthday
     *      出生日期
     * @return 
     *      虚岁
     */
    public static int convertBirthdayToAge(Date birthday) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(birthday);
        Calendar today = new GregorianCalendar();
        int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
        today.add(Calendar.YEAR, age);
        if (cal.before(today)) {
            age++;
        }
        return age;
    }
    
    public static String toFriendlyString(Date date, String format) {
    	// 如果format为空，则直接使用yyyy-MM-dd HH:mm:ss的全格式形式
    	format = format == null ? FULL_DATE_FORMAT : format;
    	
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		Calendar today = new GregorianCalendar();
		
		//超过12个月
		//返回几年前
		int source_year = cal.get(Calendar.YEAR);
		int today_year = today.get(Calendar.YEAR);
		Format f = new SimpleDateFormat(format);
		if (source_year<=(today_year-1)){
			//return f.format(date);
//			return (today_year-source_year)+"年前";
		}
		
		//超过5周
		//返回多少月前
		today.add(Calendar.DAY_OF_YEAR, -35);
		boolean bool = cal.before(today);
		today.add(Calendar.DAY_OF_YEAR, 35);
		if (bool) {
			int month = cal.get(Calendar.MONTH);
			int today_month = today.get(Calendar.MONTH);	
			int day = cal.get(Calendar.DAY_OF_MONTH);
			int today_day = today.get(Calendar.DAY_OF_MONTH);			
			int monthsub = (today_year-source_year)*12+(today_month-month) - (day>today_day?1:0);
			return monthsub + "月前";
		}
		//1~5周
		//返回几周前
		today.add(Calendar.DATE, -7);	
		bool = cal.before(today);
		today.add(Calendar.DATE, 7);
		if (bool) {
			long ld1 = today.getTimeInMillis();
		    long ld2 = cal.getTimeInMillis();
		    long days = (long) ( (ld1 - ld2) / 86400000); 				
			return days/7 + "周前";
		}
		//1天~1周(7天)
		//返回几天前
		today.add(Calendar.DATE, -1);	
		bool = cal.before(today);
		today.add(Calendar.DATE, 1);
		if (bool) {
			long ld1 = today.getTimeInMillis();
		    long ld2 = cal.getTimeInMillis();
		    long days = (long) ( (ld1 - ld2) / 86400000); 				
			return days + "天前";
		}
		//1小时~24小时		
		//返回几小时前
		today.add(Calendar.HOUR, -1);
		bool = cal.before(today);
		today.add(Calendar.HOUR, 1);
		if (bool) {
			long ld1 = today.getTimeInMillis();
		    long ld2 = cal.getTimeInMillis();
		    long hour = (long) ( (ld1 - ld2) / 3600000);
			return hour + "小时前";
		}
		//30~60分钟
		//返回半小时前
		today.add(Calendar.MINUTE, -29);
		bool = cal.before(today);
		today.add(Calendar.MINUTE, 29);
		if (bool) {
			return "半小时前";
		}	
		
		//5~30分钟
		//返回几分钟前
		today.add(Calendar.MINUTE, -4);
		bool = cal.before(today);
		today.add(Calendar.MINUTE, 4);
		if (bool) {
			long ld1 = today.getTimeInMillis();
		    long ld2 = cal.getTimeInMillis();
		    long minute = (long) ( (ld1 - ld2) / 60000);
			return minute+"分钟前";
		}	
		
		//0~5分钟
		//返回刚刚
		bool = cal.before(today);
		if (bool) {
			return "刚刚";
		}
		//当前时间之后，按实际format显示
		return f.format(date);
	
    }
    
   //把日期转为字符串  
    public static String ConverToString(Date date)  
    {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");  
          
        return df.format(date);  
    }
    
    /**
	 * @Title:changeStringToTimestamp
	 * @Description:将string 装换为Timestamp
	 * @author:sunbo
	 * @date:2014-1-15 16:11:33
	 * @param:time 要转换的参数 fromat 格式化的格式
	 * @return:返回时间
	 */
    public static Timestamp  changeStringToTimestamp (String time ,  String format){
    	DateFormat format2 = new SimpleDateFormat(format);
    	 try {
		   Timestamp ts = new Timestamp(format2.parse(time).getTime());
		   return ts;
		   //System.out.println(ts.toString());
		  } catch (ParseException e) {
		   // TODO Auto-generated catch block
		   e.printStackTrace();
		  }
    	 return null;
    }
    
    /**
     * 
    * @Title: daysBetween
    * @Description: 计算开始时间为smdate到结束时间bdate之间相差的天数
    * @param @param smdate
    * @param @param bdate
    * @param @return
    * @param @throws ParseException    设定文件
    * @return int    返回类型
    * @author banna
    * @throws
     */
    public static int daysBetween(long smdate,long bdate) throws ParseException    
    {    
        long time1 = smdate;                 
        long time2 = bdate;         
        long between_days=(time2-time1)/(1000*3600*24);  
            
       return Integer.parseInt(String.valueOf(between_days));           
    }
    
    /**
     * 
    * @Title: getStrTime
    * @Description: 将时间戳转换为字符串
    * @param @param time
    * @param @return    设定文件
    * @return String    返回类型
    * @author banna
    * @throws
     */
    public static String formatTimestampToString(Timestamp time) {
    	  
    	  String returnTime;
    	
    	  SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FULL_DATE_FORMAT);
    	  long lcc_time = time.getTime();
    	  returnTime = sdf.format(new Date(lcc_time));
    	  return returnTime;
    	}
    
    /**
     * 
     * @Title: formatCurrentDateWithYMD
     * @Description: 获取当前时间不带时分秒的字符串,返回的格式为“年月日”
     * @author:banna
     * @return: String
     */
    public static String formatCurrentDateWithYMD() {
  	  
  	  String returnTime;
  	
  	  SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.SIMPLE_DATE_FORMAT);
  	  returnTime = sdf.format(new Date());
  	  return returnTime;
  	}
    
    /**
     * 
     * @Title: formatCurrentDateWithYMD
     * @Description: 获取当前时间不带时分秒的字符串,返回的格式为“年月日”
     * @author:banna
     * @return: String
     */
    public static String formatCurrentDateWithYMDHMS() {
  	  
  	  String returnTime;
  	
  	  SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.FULL_DATE_FORMAT);
  	  returnTime = sdf.format(new Date());
  	  return returnTime;
  	}
}
