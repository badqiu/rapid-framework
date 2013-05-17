package cn.org.rapid_framework.page.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Projections;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
/**
 * @author badqiu
 */

@Deprecated
public class CriteriaPage extends Page{
	
	public CriteriaPage(Criteria criteria,int pageNumber, int pageSize) {
		super(pageNumber,pageSize,queryTotalCount(criteria));
		result = criteria
			.setFirstResult(getFirstResult())
			.setMaxResults(getPageSize()).list();
	}
	
	public CriteriaPage(Criteria criteria,PageRequest p) {
		this(criteria,p.getPageNumber(),p.getPageSize());
	}

	private static int queryTotalCount(Criteria criteria) {
		return ((Integer)(criteria.setProjection(Projections.rowCount()).uniqueResult())).intValue();
	}

}
