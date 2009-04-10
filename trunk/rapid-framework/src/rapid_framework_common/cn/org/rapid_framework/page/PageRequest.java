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
public class PageRequest implements Serializable {

	/**
	 * 过滤参数
	 */
	private Map filters;
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
	
	private List<SortInfo> cacheSortInfos;
	
	public PageRequest() {
	}
	
	public PageRequest(int pageNumber, int pageSize, Map filters) {
		this(pageNumber,pageSize,filters,null);
	}
	
	public PageRequest(int pageNumber, int pageSize) {
		this(pageNumber,pageSize,new HashMap());
	}
	
	public PageRequest(int pageNumber, int pageSize,String sortColumns) {
		this(pageNumber,pageSize,new HashMap(),sortColumns);
	}
	
	public PageRequest(int pageNumber, int pageSize, Map filters,String sortColumns) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.filters = filters;
		setSortColumns(sortColumns);
	}

	public Map getFilters() {
		if(filters == null) {
			filters = new HashMap(0);
		}
		return filters;
	}

	public void setFilters(Map filters) {
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
		this.cacheSortInfos = Collections.unmodifiableList(SortInfo.parseSortColumns(sortColumns));
	}

	public List<SortInfo> getSortInfos() {
		return cacheSortInfos;
	}
	
}
