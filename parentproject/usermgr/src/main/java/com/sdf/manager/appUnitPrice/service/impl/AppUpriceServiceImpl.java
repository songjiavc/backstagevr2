package com.sdf.manager.appUnitPrice.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.app.dto.AppDTO;
import com.sdf.manager.app.entity.App;
import com.sdf.manager.app.service.AppService;
import com.sdf.manager.appUnitPrice.dto.AppUnitPriceDTO;
import com.sdf.manager.appUnitPrice.entity.AppUnitPrice;
import com.sdf.manager.appUnitPrice.entity.UserYearDiscount;
import com.sdf.manager.appUnitPrice.repository.AppUnitPriceRepository;
import com.sdf.manager.appUnitPrice.repository.UserYearDiscountRepository;
import com.sdf.manager.appUnitPrice.service.AppUPriceService;
import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Province;
import com.sdf.manager.product.service.CityService;
import com.sdf.manager.product.service.ProvinceService;

@Service("appUPriceService")
@Transactional(propagation = Propagation.REQUIRED)
public class AppUpriceServiceImpl implements AppUPriceService {

	@Autowired
	private AppUnitPriceRepository appUnitPriceRepository;
	
	@Autowired
	private ProvinceService provinceService;
	
	@Autowired
	private CityService cityService;
	
	@Autowired
	private UserYearDiscountRepository userYearDiscountRepository;
	
	
	public void save(AppUnitPrice entity) {
		appUnitPriceRepository.save(entity);
	}

	public void update(AppUnitPrice entity) {
		
		appUnitPriceRepository.save(entity);

	}

	public AppUnitPrice getAppUnitPriceById(String id) {
		return appUnitPriceRepository.getAppUnitPriceById(id);
	}

	public QueryResult<AppUnitPrice> getAppUnitPriceList(
			Class<AppUnitPrice> entityClass, String whereJpql,
			Object[] queryParams, LinkedHashMap<String, String> orderby,
			Pageable pageable) {
		
		QueryResult<AppUnitPrice> appUpriceList = appUnitPriceRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		
		return appUpriceList;
	}

	public List<AppUnitPriceDTO> toRDTOS(List<AppUnitPrice> entities) {
		List<AppUnitPriceDTO> dtos = new ArrayList<AppUnitPriceDTO>();
		AppUnitPriceDTO dto;
		for (AppUnitPrice entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	public AppUnitPriceDTO toDTO(AppUnitPrice entity) {
		
		AppUnitPriceDTO dto = new AppUnitPriceDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreaterTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			if(null != entity.getProvince())//省级区域
			{
				Province province = new Province();
				province = provinceService.getProvinceByPcode(entity.getProvince());
				dto.setProvinceName(null != province?province.getPname():"");
			}
			if(null != entity.getCity())//市级区域
			{
				if(Constants.CITY_ALL.equals(entity.getCity()))
				{
					dto.setCityName(Constants.CITY_ALL_NAME);
				}
				else
				{
					City city = new City();
					city = cityService.getCityByCcode(entity.getCity());
					dto.setCityName(null != city?city.getCname():"");
				}
				
			}
			
			if(null != entity.getApp())
			{
				dto.setAppId(entity.getApp().getId());
				dto.setAppName(entity.getApp().getAppName());
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		return dto;
	}
	
	public AppUnitPrice getAppUnitPriceByAppIdAndProvinceAndCity(String appId,
			String province, String city) {
		return appUnitPriceRepository.getAppUnitPriceByAppIdAndProvinceAndCity(appId, province, city);
	}

	public AppUnitPrice getAppUnitPriceByAppIdAndProvinceAndCityNotType(
			String appId, String province, String city) {
		return appUnitPriceRepository.getAppUnitPriceByAppIdAndProvinceAndCityNotType(appId, province, city);
	}
	
	/**********调用使用年限折扣dao层获取使用年限数据的分割线***********/
	
	/**
	 * 
	* @Title: findAll
	* @Description: 获取所有的年限折扣数据
	* @param @return    设定文件
	* @return List<UserYearDiscount>    返回类型
	* @author banna
	* @throws
	 */
	public List<UserYearDiscount> findAll()
	{
		return userYearDiscountRepository.findAll();
	}

	public UserYearDiscount getUserYearDiscountById(String id) {
		return userYearDiscountRepository.getUserYearDiscountById(id);
	}

	

	
}
