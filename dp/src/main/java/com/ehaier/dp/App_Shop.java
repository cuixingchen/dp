package com.ehaier.dp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ehaier.dp.bean.ExecutionLog;
import com.ehaier.dp.bean.OrderProducts;
import com.ehaier.dp.dao.Eop_LogMapper;
import com.ehaier.dp.dao.ShopMapper;

/**
 * Hello world!
 *
 */
public class App_Shop {
	private static SqlSessionFactory factory;
	private static List<String> sqlList = new ArrayList<String>();

	// SELECT * FROM `execution_log` where
	// `code`='com.haier.channel.web.controller.api.ApiController.syncOrderStatusOut'
	// and `request_time`>'2015-06-01 00:00:00' and `request_time`<='2015-06-15
	// 00:00:00'

	public static void main(String[] args) {
		initMybatis();
		// Date startTime = DateUtil.currentDate();
		// Date endTime = DateUtil.add(startTime, Calendar.DATE, -1);
		// getEop_List(factory, startTime, endTime);
		List<Integer> list = getOrder_List(factory, "YLW", new BigDecimal(0));
		int count = 0;
		for (Integer id : list) {
			List<OrderProducts> msg = getOrderProducts_List(factory, id);
			for (int i = 0; i < msg.size(); i++) {
//				String sql = "update orderproducts set productAmount=0.01 where cOrderSn='" + msg.get(i).getcOrderSn()
//						+ "';";
//				String sql = "update Invoices set price=0.01,amount=0.01,nonTaxPrice=0.01,nonTaxAmount=0.01,taxAmount=0 where cOrderSn='" + msg.get(i).getcOrderSn()
//						+ "';";
				String sql =msg.get(i).getcOrderSn();
				sqlList.add(sql);
			}
			System.out.println("订单号:" + id);
			count++;
		}
		writeFileSql();
		System.out.println("完成:" + count);
	}

	private static void initMybatis() {
		String resource = "mybatis-config.xml";
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(inputStream, "development_shop");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeFileSql() {
		File file = new File("D:/Invoices-wd.txt");
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

	private static List<Integer> getOrder_List(SqlSessionFactory factory, String source, BigDecimal paidAmount) {
		SqlSession session = factory.openSession();
		try {
			ShopMapper shopMapper = session.getMapper(ShopMapper.class);
			List<Integer> eop_LogList = shopMapper.getShopOrders(source, paidAmount);
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

	private static List<OrderProducts> getOrderProducts_List(SqlSessionFactory factory, Integer orderId) {
		SqlSession session = factory.openSession();
		try {
			ShopMapper shopMapper = session.getMapper(ShopMapper.class);
			List<OrderProducts> eop_LogList = shopMapper.getOrderProducts(orderId);
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
}
