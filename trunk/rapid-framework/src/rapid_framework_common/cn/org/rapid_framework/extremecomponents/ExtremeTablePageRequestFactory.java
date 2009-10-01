package cn.org.rapid_framework.extremecomponents;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.extremecomponents.table.limit.Filter;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.Sort;

import cn.org.rapid_framework.page.PageRequest;

/**
 * @author badqiu
 */
public class ExtremeTablePageRequestFactory {
	/**
	 * 通过ExtremeTable的Limit对象创建PageRequest对象
	 * @param limit
	 * @param defaultSortColumns 默认的排序字段s,如 username desc,age asc
	 * @return
	 */
	public static PageRequest<Map> createFromLimit(Limit limit,String defaultSortColumns) {
		PageRequest result = new PageRequest();
		
		result.setPageNumber(limit.getPage());
		result.setPageSize(limit.getCurrentRowsDisplayed());
		result.setSortColumns(getSortingColumns(limit, defaultSortColumns));
		result.setFilters(getFilters(limit));
		
		return result;
	}

	public static PageRequest createFromLimit(Limit limit) {
		return createFromLimit(limit,null);
	}
	
	private static Map getFilters(Limit limit) {
		Filter[] filters = limit.getFilterSet().getFilters();
		Map result = new HashMap();
		for(int i = 0; i < filters.length; i++) {
			Filter filter = filters[i];
			result.put(filter.getAlias(), filter.getValue());
		}
		return result;
	}

	public static String getSortingColumns(Limit limit, String defaultSortColumns) {
		Sort sort = limit.getSort();
		if(sort.getProperty() == null) {
			return defaultSortColumns;
		}
		
		String sortOrder = sort.getSortOrder() == null ? ""  : " " + sort.getSortOrder();
		String column = sort.isAliased() ? sort.getAlias() : sort.getProperty();
		String sortColumns = column + sortOrder;
		return sortColumns;
	}

}
