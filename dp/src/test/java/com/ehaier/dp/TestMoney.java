package com.ehaier.dp;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class TestMoney extends TestCase {

	public void testM(){
		BigDecimal productFee = new BigDecimal("1");
		BigDecimal orderFee = new BigDecimal("1999999");
		BigDecimal couponAmount = new BigDecimal("1999999");
		BigDecimal couponAmt = productFee.divide(orderFee,8, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal pCouponAmount = couponAmt.multiply(couponAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
		System.out.println(pCouponAmount.doubleValue());
	}
}
