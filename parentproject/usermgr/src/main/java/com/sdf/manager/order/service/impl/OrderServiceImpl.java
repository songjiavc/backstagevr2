package com.sdf.manager.order.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sdf.manager.common.util.BeanUtil;
import com.sdf.manager.common.util.Constants;
import com.sdf.manager.common.util.DateUtil;
import com.sdf.manager.common.util.QueryResult;
import com.sdf.manager.order.dto.OrdersDTO;
import com.sdf.manager.order.entity.OrderStatus;
import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.repository.OrderRepository;
import com.sdf.manager.order.service.OrderService;
import com.sdf.manager.order.service.OrderStatusService;
import com.sdf.manager.product.application.dto.ProductDto;
import com.sdf.manager.product.entity.City;
import com.sdf.manager.product.entity.Product;
import com.sdf.manager.product.entity.ProductDL;
import com.sdf.manager.product.entity.ProductXL;
import com.sdf.manager.product.entity.ProductZL;
import com.sdf.manager.product.entity.Province;


@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderServiceImpl implements OrderService {
	
	
	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderStatusService orderStatusService;
	
	
	/**
	 * 根据id获取订单详情
	 */
	public Orders getOrdersById(String id)
	{
		return orderRepository.getOrdersById(id);
	}
	
	public Orders getOrdersByCode(String code)
	{
		return orderRepository.getOrdersByCode(code);
	}
	
	/**
	 * 
	* @Description: 保存订单实体数据
	* @author bann@sdfcp.com
	* @date 2015年11月16日 下午3:38:41
	 */
	public void save(Orders entity)
	{
		orderRepository.save(entity);
	}
	
	/**
	 * 
	* @Description: 修改订单实体数据
	* @author bann@sdfcp.com
	* @date 2015年11月16日 下午3:38:52
	 */
	public void update(Orders entity)
	{
		orderRepository.save(entity);
	}
	
	/**
	 * 
	* @Description: 根据参数查询订单列表数据
	* @author bann@sdfcp.com
	* @date 2015年11月16日 下午3:46:38
	 */
	public QueryResult<Orders> getOrdersList(Class<Orders> entityClass, String whereJpql, Object[] queryParams, 
			LinkedHashMap<String, String> orderby, Pageable pageable)
	{
		
		QueryResult<Orders> orderslist = orderRepository.getScrollDataByJpql(entityClass, whereJpql, queryParams,
				orderby, pageable);
		return orderslist;
	}
	
	/**
	 * 
	* @Description: 将订单实体转换为dto
	* @author bann@sdfcp.com
	* @date 2015年11月17日 上午10:38:31
	 */
	public  OrdersDTO toDTO(Orders entity) {
		OrdersDTO dto = new OrdersDTO();
		try {
			BeanUtil.copyBeanProperties(dto, entity);
			
			//处理实体中的特殊转换值
			if(null != entity.getCreaterTime())//创建时间
			{
				dto.setCreateTime(DateUtil.formatDate(entity.getCreaterTime(), DateUtil.FULL_DATE_FORMAT));
			}
			
			dto.setOperator("admin");
			
			
			if(null != entity.getStatus())
			{
				String statusName = "";
				OrderStatus orderStatus = orderStatusService.getOrdersByStatusId(entity.getStatus());
				statusName = orderStatus.getStatusName();
				dto.setStatusName(statusName);
			}
			
			if(null != entity.getApp())
			{
				dto.setAppId(entity.getApp().getId());
				dto.setAppName(entity.getApp().getAppName());
				dto.setAppprovince(entity.getApp().getProvince());
				dto.setAppCity(entity.getApp().getCity());
			}
			
			if(null != entity.getUserYearDiscount())
			{
				dto.setUserYear(entity.getUserYearDiscount().getId());
				dto.setUserYearName(entity.getUserYearDiscount().getUserYearName());
			}
			
			if(null != entity.getStation())
			{
				dto.setStationId(entity.getStation().getId());
				dto.setStationCode(entity.getStation().getCode());
			}
			
			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
					
		return dto;
	}
	
	/**
	 * 
	* @Description: 批量将订单实体转换为dto
	* @author bann@sdfcp.com
	* @date 2015年11月17日 上午10:37:01
	 */
	public  List<OrdersDTO> toDTOS(List<Orders> entities) {
		List<OrdersDTO> dtos = new ArrayList<OrdersDTO>();
		OrdersDTO dto;
		for (Orders entity : entities) 
		{
			dto = toDTO(entity);
			dtos.add(dto);
		}
		return dtos;
	}

	
}
