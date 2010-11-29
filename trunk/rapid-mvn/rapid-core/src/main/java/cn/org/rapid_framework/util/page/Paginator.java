package cn.org.rapid_framework.util.page;

import java.util.List;
/**
 * 分页器，根据page,pageSize,totalItem用于页面上分页显示多项内容，计算页码和当前页的偏移量，方便页面分页使用.
 * 
 * @author badqiu
 * @version $Id: Paginator.java,v 0.1 2010-11-29 下午05:35:58 zhongxuan Exp $
 */
public class Paginator implements java.io.Serializable, Cloneable {
	private static final long serialVersionUID = 6089482156906595931L;
	
	private static final int DEFAULT_SLIDERS_COUNT = 7;
	private static final int DEFAULT_PAGE_SIZE = 10;
	
	private int page;
	private int pageSize = DEFAULT_PAGE_SIZE;
	private long totalItems;
	
	public Paginator() {
	    this(0,DEFAULT_PAGE_SIZE,0);
	}

	public Paginator(int pageSize) {
        super();
        this.pageSize = pageSize;
    }
	
	public Paginator(int page, int pageSize, long totalItems) {
		super();
		this.pageSize = pageSize;
		this.totalItems = totalItems;
		this.page = computePageNo(page);
	}
    /**
     * 取得当前页。
     */
	public int getPage() {
		return page;
	}
    /**
     * 设置并取得当前页。实际的当前页值被确保在正确的范围内。
     *
     * @param page 当前页
     *
     * @return 设置后的当前页
     */
	public void setPage(int page) {
		this.page = computePageNo(page);
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		this.page = computePageNo(page);
	}
    /**
     * 取得总项数。
     *
     * @return 总项数
     */
	public long getTotalItems() {
		return totalItems;
	}
    /**
     * 设置并取得总项数。如果指定的总项数小于0，则被看作0。自动调整当前页，确保当前页值在正确的范围内。
     *
     * @param totalItems 总项数
     *
     */
	public void setTotalItems(long totalItems) {
		this.totalItems = totalItems >= 0 ? totalItems : 0;
		this.page = computePageNo(page);
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
	
    /**
     * 取得首页页码。
     *
     * @return 首页页码
     */
    public int getFirstPage() {
        return computePageNo(1);
    }

    /**
     * 取得末页页码。
     *
     * @return 末页页码
     */
    public int getLastPage() {
        return computePageNo((int)getTotalPages());
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
     * 判断指定页码是否被禁止，也就是说指定页码超出了范围或等于当前页码。
     *
     * @param page 页码
     *
     * @return boolean  是否为禁止的页码
     */
    public boolean isDisabledPage(int page) {
        return ((page < 1) || (page > getTotalPages()) || (page == this.page));
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
	 * 开始行，可以用于oracle分页使用 (1-based)。
	 **/
	public long getStartRow() {
		return page > 0 ? (page - 1) * getPageSize() + 1 : 0;
	}
	/**
     * 结束行，可以用于oracle分页使用 (1-based)。
     **/
	public long getEndRow() {
	    return page > 0 ? Math.min(pageSize * page, getTotalItems()) : 0; 
	}
    /**
     * offset，计数从0开始，可以用于mysql分页使用
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
    
    public Object clone() {
        try {
            return super.clone();
        } catch (java.lang.CloneNotSupportedException e) {
            return null; 
        }
    }
    
    public String toString() {
        return "page:"+page+" pageSize:"+pageSize+" totalItems:"+totalItems;
    }
    
}
