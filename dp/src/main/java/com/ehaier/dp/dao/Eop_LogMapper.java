package com.ehaier.dp.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.ehaier.dp.bean.ExecutionLog;

/**
 * Eop日志mapper
 * 
 * @author cuipengfei
 * @date 2015年6月14日 下午2:16:53
 *
 */
public interface Eop_LogMapper {

	/**
	 * 根据方法名查询指定时间段日志
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	List<ExecutionLog> selectEop_LogList(@Param("code") String code,
			@Param("startTime") Date startTime, @Param("endTime") Date endTime);
	
	List<ExecutionLog> selectEop_LogListByResponse(@Param("response_msg") String response_msg);

}
