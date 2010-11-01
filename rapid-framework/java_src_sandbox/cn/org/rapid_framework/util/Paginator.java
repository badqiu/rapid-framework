package cn.org.rapid_framework.util;

import java.util.Iterator;
import java.util.List;

public class Paginator<T> implements java.io.Serializable,Iterable<T>{
	private static final long serialVersionUID = 6089482156906595931L;
	
	private List<T> itemList;
	private int pageNo;
	private int pageSize;
	private long totalItems;
	
	public List<T> getItemList() {
		return itemList;
	}

	public void setItemList(List<T> itemList) {
		this.itemList = itemList;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = pageNo;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
	}
	
    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
	public boolean isFirstPage() {
		return pageNo <= 1;
	}

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
	public boolean isLastPage() {
		return pageNo >= getTotalPages();
	}
	
	public int getPrePage() {
		if (isHasPrePage()) {
			return pageNo - 1;
		} else {
			return pageNo;
		}
	}
	
	public int getNextPage() {
		if (isHasNextPage()) {
			return pageNo + 1;
		} else {
			return pageNo;
		}
	}
    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
	public boolean isHasPrePage() {
		return (pageNo - 1 >= 1);
	}	
    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
	public boolean isHasNextPage() {
		return (pageNo + 1 <= getTotalPages());
	}
	
	public long getBeginIndex() {
		return (pageNo - 1) * getPageSize() + 1;
	}

	public long getEndIndex() {
		long fullPage = getBeginIndex() + getPageSize() - 1;
		return getTotalItems() < fullPage ? getTotalItems() : fullPage;
	}
	
	public long getOffset() {
		return (pageNo - 1) * getPageSize();
	}
	
	public long getTotalPages() {
		if (totalItems < 0) {
			return -1;
		}

		long count = totalItems / pageSize;
		if (totalItems % pageSize > 0) {
			count++;
		}
		return count;
	}

	@SuppressWarnings("unchecked")
	public Iterator<T> iterator() {
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
