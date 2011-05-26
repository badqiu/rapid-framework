package com.company.project.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

public class SpringMVCUtils {
	
	public static ModelMap toModelMap(Page page, PageRequest pageRequest) {
		return toModelMap("", page, pageRequest);
	}

	public static ModelMap toModelMap(String tableId, Page page,
			PageRequest pageRequest) {
		ModelMap model = new ModelMap();
		saveIntoModelMap(tableId, page, pageRequest, model);
		return model;
	}

	/**
	 * 用于一个页面有多个extremeTable是使用
	 * 
	 * @param tableId
	 *            等于extremeTable的tableId属性
	 */
	public static void saveIntoModelMap(String tableId, Page page,
			PageRequest pageRequest, ModelMap model) {
		Assert.notNull(tableId, "tableId must be not null");
		Assert.notNull(page, "page must be not null");

		model.addAttribute(tableId + "page", page);
		model.addAttribute(tableId + "totalRows", new Integer(page
				.getTotalCount()));
		model.addAttribute(tableId + "pageRequest", pageRequest);
		model.addAttribute(tableId + "query", pageRequest);
	}

	public static PageRequest bindPageRequest(HttpServletRequest request,
			PageRequest pageRequest, String defaultSortColumns) {
		return PageRequestFactory.bindPageRequest(pageRequest, request,
				defaultSortColumns);
	}

	public static <T> T getOrCreateRequestAttribute(HttpServletRequest request,
			String key, Class<T> clazz) {
		Object value = request.getAttribute(key);
		if (value == null) {
			try {
				value = clazz.newInstance();
			} catch (Exception e) {
				ReflectionUtils.handleReflectionException(e);
			}
			request.setAttribute(key, value);
		}
		return (T) value;
	}
}
