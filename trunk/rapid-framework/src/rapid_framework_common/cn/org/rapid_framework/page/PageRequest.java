package cn.org.rapid_framework.page;

import java.io.Serializable;
import java.util.HashMap;
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
	 * 排序的方向,ASC 或是 DESC
	 */
	private String sortingColumn;
	/**
	 * 排序的列字段
	 */
	private String sortingDirection;
	
	public PageRequest() {
	}
	
	public PageRequest(int pageNumber, int pageSize, Map filters) {
		this(pageNumber,pageSize,filters,null,null);
	}
	
	public PageRequest(int pageNumber, int pageSize, String sortingColumn, String sortingDirection) {
		this(pageNumber,pageSize,new HashMap(),sortingColumn,sortingDirection);
	}
	
	public PageRequest(int pageNumber, int pageSize, Map filters, String sortingColumn, String sortingDirection) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.filters = filters;
		this.sortingColumn = sortingColumn;
		this.sortingDirection = sortingDirection;
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

	public String getSortingColumn() {
		return sortingColumn;
	}

	public void setSortingColumn(String sortingColumn) {
		this.sortingColumn = sortingColumn;
	}

	public String getSortingDirection() {
		return sortingDirection;
	}

	public void setSortingDirection(String sortingDirection) {
		this.sortingDirection = sortingDirection;
	}
	
}
