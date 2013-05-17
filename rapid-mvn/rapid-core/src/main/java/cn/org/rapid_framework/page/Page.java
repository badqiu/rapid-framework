package cn.org.rapid_framework.page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import cn.org.rapid_framework.util.page.PageQuery;

/**
 * <pre>
 * 分页信息
 * 第一页从1开始
 * 
 * </pre>
 * 
 * @see cn.org.rapid_framework.util.page.PageList
 * @see cn.org.rapid_framework.util.page.Paginator
 * @author badqiu
 */
public class Page<T> implements Serializable, Iterable<T> {

	protected List<T> result;

	private Paginator paginator;

	public Page(PageRequest p, int totalCount) {
		this(p.getPageNumber(), p.getPageSize(), totalCount);
	}

	public Page(PageQuery p, int totalCount) {
		this(p.getPage(), p.getPageSize(), totalCount);
	}

	@Deprecated
	public Page(int pageNumber, int pageSize, int totalCount) {
		this(pageNumber, pageSize, totalCount, new ArrayList(0));
	}

	@Deprecated
	public Page(int pageNumber, int pageSize, int totalCount, List<T> result) {
		if (pageSize <= 0)
			throw new IllegalArgumentException("[pageSize] must great than zero");
		this.paginator = new Paginator(pageNumber,pageSize,totalCount);
		setResult(result);
	}

	public Page(Paginator paginator) {
		setPaginator(paginator);
	}
	
	public Page(List<T> itemList, Paginator paginator) {
		setItemList(itemList);
		setPaginator(paginator);
	}
	
	/**
	 * 用setItemList() 替换
	 */
	@Deprecated
	public void setResult(List<T> elements) {
		setItemList(elements);
	}

	/**
	 * 当前页包含的数据
	 * 用 getItemList() 替换
	 * 
	 * @return 当前页数据源
	 */
	@Deprecated
	public List<T> getResult() {
		return getItemList();
	}

	/**
	 * 是否是首页（第一页），第一页页码为1
	 *
	 * @return 首页标识
	 */
	public boolean isFirstPage() {
		return getThisPageNumber() == 1;
	}

	/**
	 * 是否是最后一页
	 *
	 * @return 末页标识
	 */
	public boolean isLastPage() {
		return getThisPageNumber() >= getLastPageNumber();
	}

	/**
	 * 是否有下一页
	 *
	 * @return 下一页标识
	 */
	public boolean isHasNextPage() {
		return getLastPageNumber() > getThisPageNumber();
	}

	/**
	 * 是否有上一页
	 *
	 * @return 上一页标识
	 */
	public boolean isHasPreviousPage() {
		return getThisPageNumber() > 1;
	}

	/**
	 * 获取最后一页页码，也就是总页数
	 *
	 * @return 最后一页页码
	 */
	public int getLastPageNumber() {
		return PageUtils.computeLastPageNumber(getTotalCount(), getPageSize());
	}

	/**
	 * 总的数据条目数量，0表示没有数据
	 *
	 * @return 总数量
	 */
	public int getTotalCount() {
		return paginator.getTotalItems();
	}

	/**
	 * 获取当前页的首条数据的行编码
	 *
	 * @return 当前页的首条数据的行编码
	 */
	public int getThisPageFirstElementNumber() {
		return (getThisPageNumber() - 1) * getPageSize() + 1;
	}

	/**
	 * 获取当前页的末条数据的行编码
	 *
	 * @return 当前页的末条数据的行编码
	 */
	public int getThisPageLastElementNumber() {
		int fullPage = getThisPageFirstElementNumber() + getPageSize() - 1;
		return getTotalCount() < fullPage ? getTotalCount() : fullPage;
	}

	/**
	 * 获取下一页编码
	 *
	 * @return 下一页编码
	 */
	public int getNextPageNumber() {
		return getThisPageNumber() + 1;
	}

	/**
	 * 获取上一页编码
	 *
	 * @return 上一页编码
	 */
	public int getPreviousPageNumber() {
		return getThisPageNumber() - 1;
	}

	/**
	 * 每一页显示的条目数
	 *
	 * @return 每一页显示的条目数
	 */
	public int getPageSize() {
		return paginator.getPageSize();
	}

	/**
	 * 当前页的页码
	 *
	 * @return 当前页的页码
	 */
	public int getThisPageNumber() {
		return paginator.getPage();
	}

	/**
	 * 得到用于多页跳转的页码
	 * @return
	 */
	public Integer[] getLinkPageNumbers() {
		return linkPageNumbers(7);
	}

	/**
	 * 得到用于多页跳转的页码
	 * 注意:不可以使用 getLinkPageNumbers(10)方法名称，因为在JSP中会与 getLinkPageNumbers()方法冲突，报exception
	 * @return
	 */
	public Integer[] linkPageNumbers(int count) {
		return PageUtils.generateLinkPageNumbers(getThisPageNumber(), getLastPageNumber(), count);
	}

	/**
	 * 得到数据库的第一条记录号
	 * @return
	 */
	public int getFirstResult() {
		return PageUtils.getFirstResult(getThisPageNumber(), getPageSize());
	}
	
	public List<T> getItemList() {
		return result;
	}

	public void setItemList(List<T> itemList) {
		if(itemList == null) 
			throw new IllegalArgumentException("'itemList' must be not null");
		this.result = itemList;
	}

	public Paginator getPaginator() {
		return paginator;
	}

	public void setPaginator(Paginator paginator) {
		if(paginator == null) 
			throw new IllegalArgumentException("'paginator' must be not null");
		this.paginator = paginator;
	}

	public Iterator<T> iterator() {
		return result == null ? (Iterator<T>)Collections.emptyList().iterator() : result.iterator();
	}
}
