package cn.org.rapid_framework.page.impl;

import java.util.ArrayList;

import org.hibernate.Query;
import org.hibernate.ScrollableResults;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

/**
 * Hibernate分页信息
 * 
 * @author badqiu
 * @see BasePage
 */

@Deprecated
public class Hibernate3Page extends Page  {

	/**
	 * 构建HibernatePage对象，完成Hibernate的Query数据的分页处理
	 * 
	 * @param query
	 *            Hibernate的Query对象
	 * @param pageNumber
	 *            当前页编码，从1开始，如果传的值为Integer.MAX_VALUE表示获取最后一页。
	 *            如果你不知道最后一页编码，传Integer.MAX_VALUE即可。如果当前页超过总页数，也表示最后一页。
	 *            这两种情况将重新更改当前页的页码，为最后一页编码。
	 * @param pageSize
	 *            每一页显示的条目数
	 */
	public Hibernate3Page(Query query, int pageNumber, int pageSize) {
		super(pageNumber, pageSize,queryTatalCountByScrollableResults(query));
		result = query.setFirstResult(
				(this.pageNumber - 1) * this.pageSize).setMaxResults(
				this.pageSize).list();
	}

	public Hibernate3Page(Query query, PageRequest p) {
		this(query,p.getPageNumber(),p.getPageSize());
	}
	/**
	 * 构建HibernatePage对象，完成Hibernate的Query数据的分页处理
	 * 
	 * @param selectQuery
	 *            Hibernate的查询Query对象
	 * @param countQuery
	 *            Hibernate的查询总数据行数的Query对象
	 * @param pageNumber
	 *            当前页编码，从1开始，如果传的值为Integer.MAX_VALUE表示获取最后一页。
	 *            如果你不知道最后一页编码，传Integer.MAX_VALUE即可。如果当前页超过总页数，也表示最后一页。
	 *            这两种情况将重新更改当前页的页码，为最后一页编码。
	 * @param pageSize
	 *            每一页显示的条目数
	 */
	public Hibernate3Page(Query selectQuery, Query countQuery, int pageNumber,int pageSize) {
		super(pageNumber, pageSize, ((Number)countQuery.uniqueResult()).intValue());
		if(getTotalCount() == 0) {
			result = new ArrayList(0);
		}else {
			result = selectQuery.setFirstResult(getFirstResult()).setMaxResults(this.pageSize).list();
		}

	}
	
	public Hibernate3Page(Query selectQuery, Query countQuery,PageRequest p) {
		this(selectQuery,countQuery,p.getPageNumber(),p.getPageSize());
	}
	
	private static int queryTatalCountByScrollableResults(Query query) {
		ScrollableResults scrollableResults = query.scroll();
		scrollableResults.last();
		return scrollableResults.getRowNumber() + 1;
	}

}
