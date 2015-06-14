package com.ehaier.dp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import com.ehaier.dp.bean.ExecutionLog;
import com.ehaier.dp.dao.Eop_LogMapper;

/**
 * Hello world!
 *
 */
public class App {
	private static SqlSessionFactory factory;

	public static void main(String[] args) {
		initMybatis();
		getEop_List(factory);
		System.out.println("Hello World!");
	}

	private static void initMybatis() {
		String resource = "mybatis-config.xml";
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(inputStream,"development");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static List<ExecutionLog> getEop_List(SqlSessionFactory factory) {
		SqlSession session = factory.openSession();
		try {
			Eop_LogMapper eop_LogMapper = session
					.getMapper(Eop_LogMapper.class);
			Date startTime=new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(startTime);
			cal.add(Calendar.MINUTE, -10);
			List<ExecutionLog> eop_LogList = eop_LogMapper
					.selectEop_LogList(
							"com.haier.channel.web.controller.api.ApiController.syncOrderStatusOut",
							cal.getTime(), new Date());
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
