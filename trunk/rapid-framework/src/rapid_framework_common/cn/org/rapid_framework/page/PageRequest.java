package cn.org.rapid_framework.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 分页请求信息
 * @author badqiu
 */
public class PageRequest<T> implements Serializable {

	/**
	 * 过滤参数
	 */
	private T filters;
	/**
	 * 页号码,页码从1开始
	 */
	private int pageNumber;
	/**
	 * 分页大小
	 */
	private int pageSize;
	/**
	 * 排序的多个列,如: username desc
	 */
	private String sortColumns;
	
	public PageRequest() {
		this(0,0);
	}
	
	public PageRequest(int pageNumber, int pageSize) {
		this(pageNumber,pageSize,(T)new HashMap());
	}
	
	public PageRequest(int pageNumber, int pageSize, T filters) {
		this(pageNumber,pageSize,filters,null);
	}
	
	public PageRequest(int pageNumber, int pageSize,String sortColumns) {
		this(pageNumber,pageSize,(T)new HashMap(),sortColumns);
	}
	
	public PageRequest(int pageNumber, int pageSize, T filters,String sortColumns) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		setFilters(filters);
		setSortColumns(sortColumns);
	}

	public T getFilters() {
		return filters;
	}

	public void setFilters(T filters) {
		if(filters == null) throw new IllegalArgumentException("'filters' must be not null");
		this.filters = filters;
	}

	public int getPageNumber() {
		return pageNumber;
	}

	public void setPageNumber(int pageNumber) {
		this.pageNumber = pageNumber;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getSortColumns() {
		return sortColumns;
	}

	public void setSortColumns(String sortColumns) {
		this.sortColumns = sortColumns;
	}

	public List<SortInfo> getSortInfos() {
		return Collections.unmodifiableList(SortInfo.parseSortColumns(sortColumns));
	}
	
}
