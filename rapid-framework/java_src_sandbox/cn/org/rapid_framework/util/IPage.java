package cn.org.rapid_framework.util;

import java.util.List;

public interface IPage<E> extends Iterable<E> {
	
	public List<E> getItemList();
	
	public void setItemList(List<E> list);
	
	public long getTotalCount();
	
	public void setTotalCount(long totalCount);
	
	public int getPageSize();

	public void setPageSize(int pageSize);
	
	public int getPageNo();
	
	public void setPageNo(int pageNo);
	
}
