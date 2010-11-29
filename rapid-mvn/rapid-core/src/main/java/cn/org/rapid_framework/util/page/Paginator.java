package cn.org.rapid_framework.util.page;

import java.util.List;
/**
 * 分页器，根据page,pageSize,totalItem用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 * 
 * @author badqiu
 * @version $Id: Paginator.java,v 0.1 2010-11-29 下午05:35:58 zhongxuan Exp $
 */
public class Paginator implements java.io.Serializable{
	private static final long serialVersionUID = 6089482156906595931L;
	
	private static final int DEFAULT_SLIDERS_COUNT = 7;
	private static final int DEFAULT_PAGE_SIZE = 10;
	
	private int page;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private long totalItems;
	
	public Paginator() {
	    this(0,DEFAULT_PAGE_SIZE,0);
	}

	public Paginator(int page, int pageSize, long totalItems) {
		super();
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.page = computePageNo(page);
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
		computePageNo(page);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		computePageNo(page);
	}

	public long getTotalItems() {
		return totalItems;
	}

	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems;
		computePageNo(page);
	}
	
    /**
     * 是否是首页（第一页），第一页页码为1
     *
     * @return 首页标识
     */
	public boolean isFirstPage() {
		return page <= 1;
	}

    /**
     * 是否是最后一页
     *
     * @return 末页标识
     */
	public boolean isLastPage() {
		return page >= getTotalPages();
	}
	
	public int getPrePage() {
		if (isHasPrePage()) {
			return page - 1;
		} else {
			return page;
		}
	}
	
	public int getNextPage() {
		if (isHasNextPage()) {
			return page + 1;
		} else {
			return page;
		}
	}
    /**
     * 是否有上一页
     *
     * @return 上一页标识
     */
	public boolean isHasPrePage() {
		return (page - 1 >= 1);
	}	
    /**
     * 是否有下一页
     *
     * @return 下一页标识
     */
	public boolean isHasNextPage() {
		return (page + 1 <= getTotalPages());
	}
	
	/**
	 * 开始行，可以用于oracle分页使用
	 **/
	public long getStartRow() {
		return page > 0 ? (page - 1) * getPageSize() + 1 : 0;
	}
	/**
     * 结束行，可以用于oracle分页使用
     **/
	public long getEndRow() {
	    return page > 0 ? Math.min(pageSize * page, getTotalItems()) : 0; 
	}
    /**
     * offset，可以用于mysql分页使用
     **/	
	public long getOffset() {
		return page > 0 ? (page - 1) * getPageSize() : 0;
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
    	return generateLinkPageNumbers(getPage(),(int)getTotalPages(), DEFAULT_SLIDERS_COUNT);
    }
    
    public List<Integer> getSliders(int slidersCount) {
    	return generateLinkPageNumbers(getPage(),(int)getTotalPages(), slidersCount);
    }
    
    private static int computeLastPageNumber(long totalItems,int pageSize) {
        int result = (int)(totalItems % pageSize == 0 ? 
                totalItems / pageSize 
                : totalItems / pageSize + 1);
        if(result <= 1)
            result = 1;
        return result;
    }
    
    private static int computePageNumber(int page, int pageSize,long totalItems) {
        if(page <= 1) {
            return 1;
        }
        if (Integer.MAX_VALUE == page
                || page > computeLastPageNumber(totalItems,pageSize)) { //last page
            return computeLastPageNumber(totalItems,pageSize);
        }
        return page;
    }
    
    public static List<Integer> generateLinkPageNumbers(int currentPageNumber,int lastPageNumber,int count) {
        int avg = count / 2;
        
        int startPageNumber = currentPageNumber - avg;
        if(startPageNumber <= 0) {
            startPageNumber = 1;
        }
        
        int endPageNumber = startPageNumber + count - 1;
        if(endPageNumber > lastPageNumber) {
            endPageNumber = lastPageNumber;
        }
        
        if(endPageNumber - startPageNumber < count) {
            startPageNumber = endPageNumber - count;
            if(startPageNumber <= 0 ) {
                startPageNumber = 1;
            }
        }
        
        java.util.List<Integer> result = new java.util.ArrayList();
        for(int i = startPageNumber; i <= endPageNumber; i++) {
            result.add(new Integer(i));
        }
        return result;
    }
}
