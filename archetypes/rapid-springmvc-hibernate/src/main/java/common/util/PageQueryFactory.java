package common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.util.page.PageQuery;

/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的newPageQuery()方法以适合不同的分页创建
 * 
 * @author badqiu
 * @author hunhun
 */
public class PageQueryFactory {
	public static final int MAX_PAGE_SIZE = 1000;

	static BeanUtilsBean beanUtils = new BeanUtilsBean();
	static {
		// 用于注册日期类型的转换
		String[] datePatterns = new String[] { "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS",
				"HH:mm:ss" };
		ConvertRegisterHelper.registerConverters(beanUtils.getConvertUtils(), datePatterns);

		System.out.println("PageQueryFactory.MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
	}

	public static PageQuery bindPageQuery(PageQuery pageQuery, HttpServletRequest request, String defaultSortColumns) {
		return bindPageQuery(pageQuery, request, defaultSortColumns, PageQuery.DEFAULT_PAGE_SIZE);
	}

	public static PageQuery bindPageQuery(PageQuery pageQuery, HttpServletRequest request, String defaultSortColumns,
			int defaultPageSize) {
		Map params = WebUtils.getParametersStartingWith(request, "");
		try {
			beanUtils.copyProperties(pageQuery, params);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("beanUtils.copyProperties() error", e);
		} catch (InvocationTargetException e) {	
			throw new IllegalArgumentException("beanUtils.copyProperties() error", e.getTargetException());
		}

		pageQuery.setPage(getIntParameter(request, "pageNumber", 1));
		pageQuery.setPageSize(getIntParameter(request, "pageSize", defaultPageSize));
		if (pageQuery.getPageSize() > MAX_PAGE_SIZE) {
			pageQuery.setPageSize(MAX_PAGE_SIZE);
		}
		return pageQuery;
	}

	static String getStringParameter(HttpServletRequest request, String paramName, String defaultValue) {
		String value = request.getParameter(paramName);
		return StringUtils.isEmpty(value) ? defaultValue : value;
	}

	static int getIntParameter(HttpServletRequest request, String paramName, int defaultValue) {
		String value = request.getParameter(paramName);
		return StringUtils.isEmpty(value) ? defaultValue : Integer.parseInt(value);
	}

}
