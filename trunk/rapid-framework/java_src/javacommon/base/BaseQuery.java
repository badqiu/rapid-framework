package javacommon.base;

public class BaseQuery implements java.io.Serializable {
	private static final long serialVersionUID = -360860474471966681L;

	private int pageNumber;
	private int pageSize;
	private String sortColumns;
	
	public BaseQuery() {
	}
	
	public BaseQuery(int pageNumber, int pageSize, String sortColumns) {
		this.pageNumber = pageNumber;
		this.pageSize = pageSize;
		this.sortColumns = sortColumns;
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

	public String toString() {
		return String.format("pageNumber:%s pageSize:%s sortColumns:%s",pageNumber,pageSize,sortColumns);
	}

}
