package com.ehaier.util;

import java.io.Serializable;

/**
 * Excel导出实体
 * 
 * @author cuipengfei
 *
 */
public class ExcelBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4135645614158653490L;
	private String title;
	private String field;

	public ExcelBean(String title, String field) {
		super();
		this.title = title;
		this.field = field;
	}

	public String getField() {
		return field;
	}

	public void setField(String field) {
		this.field = field;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
