package com.ehaier.dp;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
	private static List<String> sqlList=new ArrayList<String>();

//	SELECT * FROM `execution_log` where `code`='com.haier.channel.web.controller.api.ApiController.syncOrderStatusOut' and `request_time`>'2015-06-01 00:00:00' and `request_time`<='2015-06-15 00:00:00'
	
	
	public static void main(String[] args) {
		initMybatis();
		// Date startTime = DateUtil.currentDate();
		// Date endTime = DateUtil.add(startTime, Calendar.DATE, -1);
		// getEop_List(factory, startTime, endTime);
		String response_msg = "{success:true,result:\"更新同步成功\",message:\"更新同步成功\"}";
		List<ExecutionLog> list = getEop_ListByResponse(factory, response_msg);
		int count = 0;
		for (ExecutionLog executionLog : list) {
			String msg = executionLog.getRequest_msg();
			if (dealString(msg)) {
				count++;
			}
		}
		writeFileSql();
		System.out.println("完成:"+count);
	}
	
	private static void initMybatis() {
		String resource = "mybatis-config.xml";
		InputStream inputStream;
		try {
			inputStream = Resources.getResourceAsStream(resource);
			SqlSessionFactoryBuilder builder = new SqlSessionFactoryBuilder();
			factory = builder.build(inputStream, "development0");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void writeFileSql(){
		File file = new File("D:/orderqueueextend-update.sql");
        FileWriter fw = null;
        BufferedWriter writer = null;
        try {
            fw = new FileWriter(file);
            writer = new BufferedWriter(fw);
            for(String sql :sqlList){
            	writer.write(sql);
                writer.newLine();//换行
    		}
            writer.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                writer.close();
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
	}
	
	private static boolean dealString(String msg) {
		// String
		// msg="[java.lang.String|haipeng,java.lang.String|39cfcd53fd56ed78aaf1c0c9c8ff5956,java.lang.String|WD150530704157,java.lang.String|[{"expressNumber":"5137539403503","expressCompany":"EMS经济单","deliveryTime":"20150601091917","sellNo":"HPSO2015053001209","price":"71.08","quantity":"1"}],HttpServletRequest:[Host:10.9.10.19,ContentType:application/x-www-form-urlencoded,Encoding:utf-8,Param:null],HttpServletResponse,]";
		String key = msg.substring(18, 25);
		if ("haipeng".equals(key)) {
			String[] str1 = msg.split("\\|");
			String[] wdhstr=str1[3].split(",");
			String[] outjson=str1[4].split("]");
			String wdh=wdhstr[0];
			String outj=outjson[0]+"]";
			String sql="update orderqueueextend set out_json='"+outj+"' where cordersn='"+wdh+"';";
//			System.out.println(sql);
			sqlList.add(sql);
			
			return true;
		}
		return false;

	}

	private static List<ExecutionLog> getEop_ListByResponse(
			SqlSessionFactory factory, String response_msg) {
		SqlSession session = factory.openSession();
		try {
			Eop_LogMapper eop_LogMapper = session
					.getMapper(Eop_LogMapper.class);
			List<ExecutionLog> eop_LogList = eop_LogMapper
					.selectEop_LogListByResponse(response_msg);
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

	private static List<ExecutionLog> getEop_List(SqlSessionFactory factory,
			Date startTime, Date endTime) {
		SqlSession session = factory.openSession();
		try {
			Eop_LogMapper eop_LogMapper = session
					.getMapper(Eop_LogMapper.class);
			List<ExecutionLog> eop_LogList = eop_LogMapper
					.selectEop_LogList(
							"com.haier.channel.web.controller.api.ApiController.syncOrderStatusOut",
							startTime, endTime);
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
