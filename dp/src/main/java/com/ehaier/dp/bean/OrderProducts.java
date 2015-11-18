package com.ehaier.dp.bean;

import java.math.BigDecimal;

public class OrderProducts {

	private Integer orderId;
	private String cOrderSn;
	private String sku;
	private BigDecimal productAmount;
	private BigDecimal couponAmount;

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
