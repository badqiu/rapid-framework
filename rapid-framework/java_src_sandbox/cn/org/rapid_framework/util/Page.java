package cn.org.rapid_framework.util;

import java.util.List;

public interface Page<T> extends Iterable<T> {
	
	public List<T> getItemList();
	
	public void setItemList(List<T> list);
	
	public long getTotalCount();
	
	public void setTotalCount(long totalCount);
	
	public int getPageSize();

	public void setPageSize(int pageSize);
	
	public int getPageNo();
	
	public void setPageNo(int pageNo);
	
}
