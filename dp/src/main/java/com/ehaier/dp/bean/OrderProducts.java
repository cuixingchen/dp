package com.ehaier.dp.bean;

import java.math.BigDecimal;

public class OrderProducts {

	private Integer orderId;
	private String cOrderSn;
	private String sku;
	private BigDecimal productAmount;
	private BigDecimal couponAmount;
	private BigDecimal price;
	private Long number;
	
	

	public Long getNumber() {
		return number;
	}

	public void setNumber(Long number) {
		this.number = number;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getcOrderSn() {
		return cOrderSn;
	}

	public void setcOrderSn(String cOrderSn) {
		this.cOrderSn = cOrderSn;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}

	public BigDecimal getCouponAmount() {
		return couponAmount;
	}

	public void setCouponAmount(BigDecimal couponAmount) {
		this.couponAmount = couponAmount;
	}

}
