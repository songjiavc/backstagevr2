package com.sdf.manager.common.exception;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ErrorLogFilter implements Filter {
	
	private static Logger log = LoggerFactory.getLogger(ErrorLogFilter.class);
	private String errorPage="error";

	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		try{
			chain.doFilter(request,response);
		}
		catch(Exception e){
			//logger.warn("",e);//张鹏飞修改，原因：浙江监控发现log4j会造成死锁，而且大部分线程都是由于ErrorLog阻塞的，所以把这里的日志输出关闭了，遇到问题再想办法
			if(e.getCause()!=null){
				if(e.getCause() instanceof NullPointerException){
					request.setAttribute("msg","空数据异常，您正在操作的数据可能已经被删除！");
					request.getRequestDispatcher(errorPage).forward(request,response);
					return ;
				}
			}
			
			SQLException sqlE=getSqlException(e);
			if(sqlE!=null){
				int errorCode=sqlE.getErrorCode();
				if(errorCode==1){
					request.setAttribute("msg","数据库唯一索引重复");
					request.getRequestDispatcher(errorPage).forward(request,response);
					return ;
				}
				else if(errorCode==1013){
					request.setAttribute("msg","数据库操作超时，请您细化查询条件");
					request.getRequestDispatcher(errorPage).forward(request,response);
					return ;
				}
				else if(errorCode==20){
					request.setAttribute("msg","数据库连接数过多，系统申请不到连接，请您与DBA联系");
					request.getRequestDispatcher(errorPage).forward(request,response);
					return ;
				}
			}
			if(e.getCause()!=null){
				request.setAttribute("msg",e.getCause().getMessage());
			}
			else{
				request.setAttribute("msg",e.getMessage());
			}
			request.getRequestDispatcher(errorPage).forward(request,response);
			return ;
		}

	}
	
	private SQLException getSqlException(Throwable e){
		while(!(e instanceof SQLException)&&e!=null){
			e=e.getCause();
			if(e instanceof SQLException)
				return (SQLException)e;
		}
		return null;
	}

	public void init(FilterConfig filterConfig) throws ServletException {
		this.errorPage=filterConfig.getInitParameter("errorPage");

	}

}
