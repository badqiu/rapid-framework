
package com.alibaba.tctools.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Tsv原始信息模型
 * @author  lai.zhoul
 * @date    2011-7-8
 */
public class TsvModel {
	/*
	 * tsv文件名=>java类名
	 */
	/**
     * @uml.property  name="className"
     */
	private String className;
	
	/**
     * @uml.property  name="header"
     */
	private String[] header;
	
	/**
     * @return
     * @uml.property  name="header"
     */
	public String[] getHeader() {
		return header;
	}

	/**
     * @param header
     * @uml.property  name="header"
     */
	public void setHeader(String[] header) {
		this.header = header;
	}

	/*
	 * 除了header以外所有的行的集合
	 */
	/**
     * @uml.property  name="dataList"
     */
	private List<Map<String, String>>dataList = new ArrayList<Map<String, String>>();

	/**
     * @return
     * @uml.property  name="className"
     */
	public String getClassName() {
		return className;
	}

	/**
     * @param className
     * @uml.property  name="className"
     */
	public void setClassName(String className) {
		this.className = className;
	}

    /**
     * @return
     * @uml.property  name="dataList"
     */
    public List<Map<String, String>> getDataList() {
        return dataList;
    }

    /**
     * @param dataList
     * @uml.property  name="dataList"
     */
    public void setDataList(List<Map<String, String>> dataList) {
        this.dataList = dataList;
    }

	
}
