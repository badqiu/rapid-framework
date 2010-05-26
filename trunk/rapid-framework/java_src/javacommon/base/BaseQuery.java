package javacommon.base;

public class BaseQuery implements java.io.Serializable {
	private static final long serialVersionUID = -360860474471966681L;

	private int pageNumber;
	private int pageSize;
	private String orderBy;
	
	public BaseQuery() {
	}
	
	public BaseQuery(int pageNumber, int pageSize, String orderBy) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.orderBy = orderBy;
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

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}
	
	public String toString() {
		return String.format("pageNumber:%s pageSize:%s orderBy:%s",pageNumber,pageSize,orderBy);
	}

}
