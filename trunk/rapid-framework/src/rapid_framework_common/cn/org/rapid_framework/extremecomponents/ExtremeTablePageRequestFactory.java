package cn.org.rapid_framework.extremecomponents;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import org.extremecomponents.table.limit.Filter;
import org.extremecomponents.table.limit.Limit;

import cn.org.rapid_framework.page.PageRequest;

/**
 * @author badqiu
 */
public class ExtremeTablePageRequestFactory {
	/**
	 * 通过ExtremeTable的Limit对象创建PageInfo对象
	 * @param limit
	 * @param defaultSortColumn 默认的排序字段
	 * @param defaultSortDirection 默认的排序方向
	 * @return
	 */
	public static PageRequest createFromLimit(Limit limit,String defaultSortColumn,String defaultSortDirection) {
		PageRequest result = new PageRequest();
		
		result.setPageNumber(limit.getPage());
		result.setPageSize(limit.getCurrentRowsDisplayed());
		
		setSortingColumn(limit, defaultSortColumn, result);
		
		setSortingDirection(limit, defaultSortDirection, result);
		
		setFilters(limit, result);
		
		return result;
	}

	public static PageRequest createFromLimit(Limit limit) {
		return createFromLimit(limit,null,null);
	}
	
	private static void setFilters(Limit limit, PageRequest result) {
		Filter[] filters = limit.getFilterSet().getFilters();
		Map mapFilters = new HashMap();
		for(int i = 0; i < filters.length; i++) {
			Filter filter = filters[i];
			mapFilters.put(filter.getAlias(), filter.getValue());
		}
		result.setFilters(mapFilters);
	}

	public static void setSortingDirection(Limit limit, String defaultSortDirection, PageRequest result) {
		if(limit.getSort().getSortOrder() != null) {
			result.setSortingDirection(limit.getSort().getSortOrder());
		}else {
			result.setSortingDirection(defaultSortDirection);
		}
	}

	public static void setSortingColumn(Limit limit, String defaultSortColumn, PageRequest result) {
		if(limit.getSort().getProperty() != null)
			result.setSortingColumn(limit.getSort().getProperty().replace('_', '.'));
		else
			result.setSortingColumn(defaultSortColumn);
	}

}
