package com.sdf.manager.appUnitPrice.entity;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


import com.sdf.manager.user.entity.BaseEntiry;

/**
 * 
 * @ClassName: UserYearDiscount
 * @Description: 使用年限折扣表(不做模块维护，在数据库中进行维护，用来在形成订单时按照购买的使用期限进行成交价格的计算)
 * @author: banna
 * @date: 2016年2月2日 上午10:47:01
 */
@Entity
@Table(name="T_BS_USE_YEAR_DISCOUNT")
public class UserYearDiscount extends BaseEntiry{

	
	@Id
	@Column(name="ID", nullable=false, length=45)
	private String id;
	
	@Column(name="USER_YEAR", length=45)
	private String useYear;//使用年限
	
	@Column(name="USER_YEAR_NAME",length=45)
	private String userYearName;//使用年限名称
	
	@Column(name="DAY_OF_YEAR", length=45)
	private String dayOfyear;//使用年限对应的天数,1年是365天，2年是365*2天
	
	@Column(name="DISCOUNT")
	private int discount;//折扣
	
	

	public String getDayOfyear() {
		return dayOfyear;
	}

	public void setDayOfyear(String dayOfyear) {
		this.dayOfyear = dayOfyear;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUseYear() {
		return useYear;
	}

	public void setUseYear(String useYear) {
		this.useYear = useYear;
	}

	public String getUserYearName() {
		return userYearName;
	}

	public void setUserYearName(String userYearName) {
		this.userYearName = userYearName;
	}

	public int getDiscount() {
		return discount;
	}

	public void setDiscount(int discount) {
		this.discount = discount;
	}
	
	
	
	
}
