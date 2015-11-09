package com.ehaier.dp.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ehaier.dp.bean.OrderProducts;

public interface ShopMapper {

	List<Integer> getShopOrders(@Param("source")String source,@Param("paidAmount")BigDecimal paidAmount);
	
	List<OrderProducts> getOrderProducts(@Param("orderId")Integer orderId);
}
