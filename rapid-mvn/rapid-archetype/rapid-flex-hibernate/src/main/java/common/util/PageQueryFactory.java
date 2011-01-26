package common.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.util.WebUtils;

import cn.org.rapid_framework.util.page.PageQuery;

/**
 * 用于分页组件覆盖的类,新的分页组件覆盖此类的bindPageQuery()方法以适合不同的分页创建
 * 
 * @author badqiu
 */
public class PageQueryFactory {
	public static final int MAX_PAGE_SIZE = 1000;

	static BeanUtilsBean beanUtils = new BeanUtilsBean();
	static {
		// 注册全局的converters
		ConvertRegisterHelper.registerConverters();

		// 注册专用的BeanUtils用于注册日期类型的转换
		String[] datePatterns = new String[]{"yyyy-MM-dd",
				"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm:ss.SSS", "HH:mm:ss"};
		ConvertRegisterHelper.registerConverters(beanUtils.getConvertUtils(),
				datePatterns);

		System.out.println("PageQueryFactory.MAX_PAGE_SIZE=" + MAX_PAGE_SIZE);
	}

	/**
	 * 从request中绑定参数至PageQuery
	 * 
	 * @return
	 */
	public static <T extends PageQuery> T bindPageQuery(T pageQuery,
			HttpServletRequest request) {
		return bindPageQuery(pageQuery, request, null);
	}
	/**
	 * 从request中绑定参数至PageQuery
	 * 
	 * @return
	 */
	public static <T extends PageQuery> T bindPageQuery(T pageQuery,
			HttpServletRequest request, String defaultSortColumns) {
		return bindPageQuery(pageQuery, request, defaultSortColumns,
				PageQuery.DEFAULT_PAGE_SIZE);
	}

	/**
	 * 从request绑定PageQuery的属性值
	 */
	public static <T extends PageQuery> T bindPageQuery(T pageQuery,
			HttpServletRequest request, String defaultSortColumns,
			int defaultPageSize) {
		try {
			Map sourceParams = WebUtils.getParametersStartingWith(request, "");
			beanUtils.copyProperties(pageQuery, sourceParams);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(
					"beanUtils.copyProperties() error", e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(
					"beanUtils.copyProperties() error", e.getTargetException());
		}

		pageQuery.setPage(ServletRequestUtils.getIntParameter(request,
				"pageNumber", 1));
		pageQuery.setPageSize(ServletRequestUtils.getIntParameter(request,
				"pageSize", defaultPageSize));

		if (pageQuery.getPageSize() > MAX_PAGE_SIZE) {
			pageQuery.setPageSize(MAX_PAGE_SIZE);
		}
		return pageQuery;
	}

}
