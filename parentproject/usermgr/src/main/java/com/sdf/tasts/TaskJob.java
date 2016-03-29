package com.sdf.tasts;

 import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.sdf.manager.order.entity.Orders;
import com.sdf.manager.order.service.OrderService;  
      
    @Component("taskJob") 
    public class TaskJob { 
    	@Autowired
   	   private OrderService orderService;
        @Scheduled(cron = "0 06 22 * * ?")  
        public void job1() {  
        	Orders order = orderService.getOrdersByCode("1");
            System.out.println("任务进行中。。。"); 
        }  
    }  