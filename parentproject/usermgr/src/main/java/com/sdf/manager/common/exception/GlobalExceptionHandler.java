package com.sdf.manager.common.exception;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * 
  * @ClassName: GlobalExceptionHandler 
  * @Description: 系统全局异常处理类 
  * @author bann@sdfcp.com
  * @date 2015年12月3日 下午4:59:45 
  *
 */
@Controller
public abstract class GlobalExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	public static final String DEFAULT_ERROR_VIEW = "error";

	    @ExceptionHandler
	    public String exception(HttpServletRequest request, Exception e) {  
	          
	        // 根据不同的异常类型进行不同处理
	        String returnMsg ;
	        if(e instanceof SQLException) 
	        {
	        	returnMsg = "数据库异常!";
	        }
	        else 
	        	if(e instanceof NullPointerException)
	        	{
	        		returnMsg = "空指针异常!";
	        	}
	        	 else 
	 	        	if(e instanceof ClassNotFoundException)
	 	        	{
	 	        		returnMsg = "指定的类不存在异常!";
	 	        	}
	 	        	 else 
	 	 	        	if(e instanceof ArithmeticException)
	 	 	        	{
	 	 	        		returnMsg = "数学运算异常!";
	 	 	        	}
	 	 	        	else 
		 	 	        	if(e instanceof ArrayIndexOutOfBoundsException)
		 	 	        	{
		 	 	        		returnMsg = "数组下标越界异常!";
		 	 	        	}
		 	 	        	else 
			 	 	        	if(e instanceof IllegalArgumentException)
			 	 	        	{
			 	 	        		returnMsg = "方法的参数错误异常!";
			 	 	        	}
			 	 	        	else 
				 	 	        	if(e instanceof IllegalAccessException)
				 	 	        	{
				 	 	        		returnMsg = "没有访问权限异常!";
				 	 	        	}
	        else
	        {
//	        	returnMsg = e.getMessage();
	        	returnMsg = "错误信息";
	        }
	        
	        //添加自己的异常处理逻辑，如日志记录　　　
	        logger.error("全局异常捕捉 ，异常返回提示："+ returnMsg+"异常信息："+e.getMessage());
	        e.printStackTrace();
	        request.setAttribute("exceptionMessage", returnMsg);  
	        
//	        ModelAndView mav = new ModelAndView();
//	        mav.addObject("error", returnMsg);
//	        mav.setViewName("forward:error.jsp");
	        return "error";
	    }  
	    
}
