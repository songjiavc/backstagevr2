package com.sdf.manager.common.bean;

/**
 * 
  * @ClassName: resultBean 
  * @Description: TODO(返回数据bean) 
  * @author songjia
  * @date 2015年10月9日 下午4:46:57 
  *
 */
public class ResultBeanData<T>
{
	private String message;//返回提示信息
	
	private String status;//返回状态（success of fail）
	
	private T entity;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getEntity() {
		return entity;
	}

	public void setEntity(T entity) {
		this.entity = entity;
	}

	
}
