package com.sdf.manager.common.bean;

/**
 * 
  * @ClassName: resultBean 
  * @Description: TODO(返回数据bean) 
  * @author bann@sdfcp.com
  * @date 2015年10月9日 下午4:46:57 
  *
 */
public class ResultBean
{
	private String message;//返回提示信息
	
	private String status;//返回状态（success of fail）
	
	private boolean isExist;//当前值是否存在
	
	private boolean isProxy;//是否拥有代理角色
	
	private boolean isFinancialManager;//是否拥有财政管理员角色
	
	private boolean useFlag;//是否可以使用
	
	private boolean isCityCenterManager;//是否拥市中心角色
	
	
	

	public boolean isCityCenterManager() {
		return isCityCenterManager;
	}

	public void setCityCenterManager(boolean isCityCenterManager) {
		this.isCityCenterManager = isCityCenterManager;
	}

	public boolean isUseFlag() {
		return useFlag;
	}

	public void setUseFlag(boolean useFlag) {
		this.useFlag = useFlag;
	}

	public boolean isProxy() {
		return isProxy;
	}

	public void setProxy(boolean isProxy) {
		this.isProxy = isProxy;
	}

	public boolean isFinancialManager() {
		return isFinancialManager;
	}

	public void setFinancialManager(boolean isFinancialManager) {
		this.isFinancialManager = isFinancialManager;
	}

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

	public boolean isExist() {
		return isExist;
	}

	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
	
}
