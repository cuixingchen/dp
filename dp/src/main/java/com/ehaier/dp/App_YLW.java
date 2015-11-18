package com.ehaier.dp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ehaier.dp.bean.ExecutionLog;
import com.ehaier.dp.bean.OrderProducts;
import com.ehaier.dp.dao.Eop_LogMapper;
import com.ehaier.dp.dao.ShopMapper;
import com.ehaier.util.ExcelBean;
import com.ehaier.util.PoiUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Hello world!
 *
 */
public class App_YLW {
	private static SqlSessionFactory factory;
	private static SqlSessionFactory factoryshop;
	private static List<String> sqlList = new ArrayList<String>();
	private static ArrayList<HashMap<String, String>> contentList = new ArrayList<HashMap<String,String>>();
	// SELECT * FROM `execution_log` where
	// `code`='com.haier.channel.web.controller.api.ApiController.syncOrderStatusOut'
	// and `request_time`>'2015-06-01 00:00:00' and `request_time`<='2015-06-15
	// 00:00:00'

	public static void main(String[] args) {
		initMybatis();
		// Date startTime = DateUtil.currentDate();
		// Date endTime = DateUtil.add(startTime, Calendar.DATE, -1);
		// getEop_List(factory, startTime, endTime);
		String response_msg = "{success:true,result:\"\",msg:\"接入成功\"}";
		List<ExecutionLog> list = getEop_ListByResponse(factory, response_msg);
		int count = 0;
		BigDecimal allResult = new BigDecimal(0);
		for (ExecutionLog executionLog : list) {
			String msg = executionLog.getRequest_msg();
			// BigDecimal result = dealJson(msg);
			// if (result != null) {
			// allResult = allResult.add(result);
			dealJson(msg);
			count++;
			// }
		}
		// writeFileSql();
		// System.out.println("优惠金额:"+allResult.divide(new
		// BigDecimal("100")).doubleValue());
		List<ExcelBean> titleList = new ArrayList<ExcelBean>();
		titleList.add(new ExcelBean("网单号","wd"));
		titleList.add(new ExcelBean("订单号","orderId"));
		titleList.add(new ExcelBean("正确优惠","yh"));
		titleList.add(new ExcelBean("当前优惠","nyh"));
		titleList.add(new ExcelBean("正确付款金额","fk"));
		titleList.add(new ExcelBean("当前付款金额","nfk"));
		HSSFWorkbook file = PoiUtil.excelService("邮乐问题数据", titleList, contentList);
		try {
			FileOutputStream os = new FileOutputStream("d:\\邮乐问题数据.xls");
			file.write(os);
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("完成:" + count);
	}

	private static void initMybatis() {
		String resource = "mybatis-config.xml";
		InputStream inputStream;
		InputStream inputStream1;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			inputStream1 = Resources.getResourceAsStream(resource);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(inputStream, "development0");
			factoryshop = builder.build(inputStream1, "development_shop");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeFileSql() {
		File file = new File("D:/orderqueueextend-update.sql");
		FileWriter fw = null;
		BufferedWriter writer = null;
		try {
			fw = new FileWriter(file);
			writer = new BufferedWriter(fw);
			for (String sql : sqlList) {
				writer.write(sql);
				writer.newLine();// 换行
			}
			writer.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				writer.close();
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void dealJson(String msg) {
		// String
		// msg="[java.lang.String|haipeng,java.lang.String|39cfcd53fd56ed78aaf1c0c9c8ff5956,java.lang.String|WD150530704157,java.lang.String|[{"expressNumber":"5137539403503","expressCompany":"EMS经济单","deliveryTime":"20150601091917","sellNo":"HPSO2015053001209","price":"71.08","quantity":"1"}],HttpServletRequest:[Host:10.9.10.19,ContentType:application/x-www-form-urlencoded,Encoding:utf-8,Param:null],HttpServletResponse,]";
		String key = msg.substring(18, 23);
		if ("youle".equals(key)) {
			String[] str1 = msg.split("\\|");
			String[] str4 = str1[3].split(",HttpServletRequest");
			// System.out.println(str4[0]);
			JsonElement js = new JsonParser().parse(str4[0]);
			JsonObject obj = js.getAsJsonObject();
			JsonObject trade = obj.getAsJsonObject("trade");
			BigDecimal orderFee = trade.get("orderFee").getAsBigDecimal().divide(new BigDecimal("100"));
			BigDecimal paidFee = trade.get("paidFee").getAsBigDecimal().divide(new BigDecimal("100"));
			BigDecimal couponAmount = trade.get("couponAmount").getAsBigDecimal().divide(new BigDecimal("100"));
			String sourceOrderSn = trade.get("sourceOrderSn").getAsString();
			JsonArray itemJsonArray = obj.getAsJsonArray("items");
			int count = itemJsonArray.size();
			// 优惠累积中间结果
			BigDecimal lasttotalAmount = new BigDecimal(0);
			// while(itemJsonIter.hasNext()){
			// JsonElement itemJe = itemJsonIter.next();
			for (int i = 0; i < count; i++) {
				JsonElement itemJe = itemJsonArray.get(i);
				JsonObject itemJson = itemJe.getAsJsonObject();
				Long num = itemJson.get("num").getAsLong();
				String sku = itemJson.get("sku").getAsString();
				BigDecimal productFee = itemJson.get("productFee").getAsBigDecimal().divide(new BigDecimal("100"));
				BigDecimal price = itemJson.get("price").getAsBigDecimal().divide(new BigDecimal("100"));
				BigDecimal pCouponAmount = new BigDecimal(0);
				if (i == (count - 1)) { // 最后的优惠金额=总优惠金额-减去前优惠总金额
					pCouponAmount = couponAmount.subtract(lasttotalAmount);
				} else {
					BigDecimal couponAmt = productFee.divide(orderFee, BigDecimal.ROUND_HALF_EVEN);
					pCouponAmount = couponAmt.multiply(couponAmount).setScale(2, BigDecimal.ROUND_HALF_EVEN);
					lasttotalAmount = lasttotalAmount.add(pCouponAmount);
				}
				BigDecimal productAmount = productFee.subtract(pCouponAmount);
				List<OrderProducts> list = getOrderProduct(factoryshop, sourceOrderSn);
				if(list==null){
					System.out.println("订单"+sourceOrderSn+"不存在");
					return;
				}
				for (int m = 0; m < list.size(); m++) {
					OrderProducts orderpro = list.get(m);
					double youhui = pCouponAmount.subtract(orderpro.getCouponAmount()).doubleValue();//优惠差额
					double shiji = productAmount.subtract(orderpro.getProductAmount()).doubleValue();//实际付款差额
					if (orderpro.getSku().equals(sku)) {
						if (youhui != 0 || shiji != 0) {
							String out = "网单号：" + orderpro.getcOrderSn() + "优惠为：" + pCouponAmount.doubleValue() + "|"
									+ orderpro.getCouponAmount().doubleValue() + "实际付款为：" + productAmount.doubleValue()
									+ "|" + orderpro.getProductAmount().doubleValue() + "数量：" + num;
							HashMap<String,String> map=new HashMap<String, String>();
							map.put("wd", orderpro.getcOrderSn());
							map.put("orderId", "YLW_"+sourceOrderSn);
							map.put("yh", pCouponAmount.toString());
							map.put("nyh", orderpro.getCouponAmount().toString());
							map.put("fk",  productAmount.toString());
							map.put("nfk", orderpro.getProductAmount().toString());
							
							if(youhui==0&&shiji==-0.01){
								
							}else{
								System.out.println(out);
								contentList.add(map);
							}
						}

					}
				}
			}
		}
	}

	private static BigDecimal dealString(String msg) {
		// String
		// msg="[java.lang.String|haipeng,java.lang.String|39cfcd53fd56ed78aaf1c0c9c8ff5956,java.lang.String|WD150530704157,java.lang.String|[{"expressNumber":"5137539403503","expressCompany":"EMS经济单","deliveryTime":"20150601091917","sellNo":"HPSO2015053001209","price":"71.08","quantity":"1"}],HttpServletRequest:[Host:10.9.10.19,ContentType:application/x-www-form-urlencoded,Encoding:utf-8,Param:null],HttpServletResponse,]";
		String key = msg.substring(18, 23);
		if ("youle".equals(key)) {
			String[] str1 = msg.split("\\|");
			String[] str4 = str1[3].split(",HttpServletRequest");
			// System.out.println(str4[0]);
			JsonElement js = new JsonParser().parse(str4[0]);
			JsonObject obj = js.getAsJsonObject();
			JsonObject trade = obj.getAsJsonObject("trade");
			BigDecimal orderFee = trade.get("orderFee").getAsBigDecimal();
			BigDecimal paidFee = trade.get("paidFee").getAsBigDecimal();
			BigDecimal couponAmount = trade.get("couponAmount").getAsBigDecimal();
			if (orderFee.subtract(paidFee).doubleValue() != couponAmount.doubleValue()) {
				System.out.println(
						orderFee.doubleValue() + ":" + paidFee.doubleValue() + ":" + couponAmount.doubleValue());
			}
			return orderFee.subtract(paidFee);
		}
		return null;

	}

	private static List<ExecutionLog> getEop_ListByResponse(SqlSessionFactory factory, String response_msg) {
		SqlSession session = factory.openSession();
		try {
			Eop_LogMapper eop_LogMapper = session.getMapper(Eop_LogMapper.class);
			List<ExecutionLog> eop_LogList = eop_LogMapper.selectEop_LogListByResponse(response_msg);
			return eop_LogList;
		} catch (Exception e) {
			System.out.println("异常：" + e.toString());
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	private static List<OrderProducts> getOrderProduct(SqlSessionFactory factory, String sourceOrderSn) {
		SqlSession session = factory.openSession();
		try {
			ShopMapper shopMapper = session.getMapper(ShopMapper.class);
			Integer id = shopMapper.getShopOrderBySourceOrderSn(sourceOrderSn);
			List<OrderProducts> list = shopMapper.getOrderProducts(id);
			return list;
		} catch (Exception e) {
			System.out.println("异常：" + e.toString());
			return null;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
