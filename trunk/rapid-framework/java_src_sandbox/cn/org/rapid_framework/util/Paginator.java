package cn.org.rapid_framework.util;


public class Paginator implements java.io.Serializable{
	private static final long serialVersionUID = 6089482156906595931L;
	
	private int pageNo;
	private int pageSize;
	private long totalItems;
	
	public Paginator() {}

	public Paginator(int pageNo, int pageSize, long totalItems) {
		super();
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.pageNo = computePageNo(pageNo);
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		this.pageNo = computePageNo(pageNo);
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
		computePageNo(pageNo);
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
		return pageNo > 0 ? (pageNo - 1) * getPageSize() + 1 : 0;
	}

	public long getEndIndex() {
	    return pageNo > 0 ? Math.min(pageSize * pageNo, getTotalItems()) : 0; 
	}
	
	public long getOffset() {
		return pageNo > 0 ? (pageNo - 1) * getPageSize() : 0;
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

    protected int computePageNo(int page) {
        return computePageNumber(page,pageSize,totalItems);
    }
    
    public static int computeLastPageNumber(long totalItems,int pageSize) {
        int result = (int)(totalItems % pageSize == 0 ? 
                totalItems / pageSize 
                : totalItems / pageSize + 1);
        if(result <= 1)
            result = 1;
        return result;
    }
    
    public static int computePageNumber(int pageNo, int pageSize,long totalItems) {
        if(pageNo <= 1) {
            return 1;
        }
        if (Integer.MAX_VALUE == pageNo
                || pageNo > computeLastPageNumber(totalItems,pageSize)) { //last page
            return computeLastPageNumber(totalItems,pageSize);
        }
        return pageNo;
    }
}
