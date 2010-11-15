package cn.org.rapid_framework.util;

import java.util.Iterator;
import java.util.List;


public class Pager<E> implements IPage<E>{
	
    private int               pageSize;
    private int               pageNo;
    private long              totalCount;
    private List<E> itemList;
    
	public int getPageSize() {
		return pageSize;
	}
	
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	public int getPageNo() {
		return pageNo;
	}
	
	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}
	
	public long getTotalCount() {
		return totalCount;
	}
	
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public List<E> getItemList() {
		return itemList;
	}
	
	public void setItemList(List<E> itemList) {
		this.itemList = itemList;
	}
	
	@SuppressWarnings("unchecked")
	public Iterator<E> iterator() {
		if(itemList == null) return EMPTY_ITERATOR;
		return itemList.iterator();
	}
	
	@SuppressWarnings("unchecked")
	private static Iterator EMPTY_ITERATOR = new Iterator() {
		public boolean hasNext() {
			return false;
		}
		public Object next() {
			throw new IllegalStateException();
		}
		public void remove() {
			throw new IllegalStateException();
		}
	};
	
}
