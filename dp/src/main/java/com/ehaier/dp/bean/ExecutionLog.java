/**
 * Copyright (c) mbaobao.com 2011 All Rights Reserved.
 */
package com.ehaier.dp.bean;

import java.util.Date;

/**
 * 
 * @Filename: ExecutionLog.java
 * @Version: 1.0
 * @Author: 丛峰
 * @Email: 3024992@qq.com
 *
 */
public class ExecutionLog {

	public static enum Type {
		dao, model, service, web, webservice, job
	}

	private Integer id;
	private Type type;
	private String code;
	private Date request_time;
	private String request_from;
	private Date response_time;
	private Long elapsed_time;
	private String request_msg;
	private String response_msg;
	private String exception_msg;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Date getRequest_time() {
		return request_time;
	}

	public void setRequest_time(Date request_time) {
		this.request_time = request_time;
	}

	public String getRequest_from() {
		return request_from;
	}

	public void setRequest_from(String request_from) {
		this.request_from = request_from;
	}

	public Date getResponse_time() {
		return response_time;
	}

	public void setResponse_time(Date response_time) {
		this.response_time = response_time;
	}

	public Long getElapsed_time() {
		return elapsed_time;
	}

	public void setElapsed_time(Long elapsed_time) {
		this.elapsed_time = elapsed_time;
	}

	public String getRequest_msg() {
		return request_msg;
	}

	public void setRequest_msg(String request_msg) {
		this.request_msg = request_msg;
	}

	public String getResponse_msg() {
		return response_msg;
	}

	public void setResponse_msg(String response_msg) {
		this.response_msg = response_msg;
	}

	public String getException_msg() {
		return exception_msg;
	}

	public void setException_msg(String exception_msg) {
		this.exception_msg = exception_msg;
	}

}
