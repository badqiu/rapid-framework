package cn.org.rapid_framework.util;

import java.util.List;

import cn.org.rapid_framework.page.PageUtils;


public class Paginator implements java.io.Serializable{
	private static final long serialVersionUID = 6089482156906595931L;
	
	private static final int DEFAULT_SLIDERS_COUNT = 7;
	private static final int DEFAULT_PAGE_SIZE = 10;
	
	private int pageNo;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private long totalItems;
	
	public Paginator() {
	    this(0,DEFAULT_PAGE_SIZE,0);
	}

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
		this.pageNo = pageNo;
		computePageNo(pageNo);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		computePageNo(pageNo);
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
	
	/**
	 * 开始行，可以用于oracle分页使用
	 **/
	public long getStartRow() {
		return pageNo > 0 ? (pageNo - 1) * getPageSize() + 1 : 0;
	}
	/**
     * 结束行，可以用于oracle分页使用
     **/
	public long getEndRow() {
	    return pageNo > 0 ? Math.min(pageSize * pageNo, getTotalItems()) : 0; 
	}
    /**
     * offset，可以用于mysql分页使用
     **/	
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
    
    public List<Integer> getSliders() {
    	return PageUtils.generateLinkPageNumbers(getPageNo(),(int)getTotalPages(), DEFAULT_SLIDERS_COUNT);
    }
    
    public List<Integer> getSliders(int slidersCount) {
    	return PageUtils.generateLinkPageNumbers(getPageNo(),(int)getTotalPages(), slidersCount);
    }
    
    private static int computeLastPageNumber(long totalItems,int pageSize) {
        int result = (int)(totalItems % pageSize == 0 ? 
                totalItems / pageSize 
                : totalItems / pageSize + 1);
        if(result <= 1)
            result = 1;
        return result;
    }
    
    private static int computePageNumber(int pageNo, int pageSize,long totalItems) {
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
