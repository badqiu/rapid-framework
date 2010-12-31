package com.company.project.common.util;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.beanutils.PropertyUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.page.PageList;
import cn.org.rapid_framework.util.page.Paginator;

public class MybatisPageQueryUtils {

		public static Page pageQuery(SqlSession sqlSession, String statement,PageRequest parameter) {
			String countStatement = statement + ".count";

			PageList pageList = pageQuery(sqlSession, statement, countStatement, parameter, parameter.getPageNumber(),
					parameter.getPageSize());
	        Page page = new Page(parameter,pageList.getTotalItems());
	        page.setResult(pageList);
	        return page;
		}
		
		public static PageList pageQuery(SqlSession sqlSession, String statement,
				Object parameter, int pageNo, int pageSize) {
			String countStatement = statement + ".count";

			return pageQuery(sqlSession, statement, countStatement, parameter, pageNo,
					pageSize);
		}

		public static PageList pageQuery(SqlSession sqlSession, String statement,
				String countStatement, Object parameter, int pageNo,
				int pageSize) {
			Assert.isTrue(pageSize > 0,"pageSize > 0 must be true");
			
			Number totalItems = (Number) sqlSession.selectOne(countStatement,parameter);

			if (totalItems != null && totalItems.intValue() > 0) {
				Paginator paginator = new Paginator(pageNo, pageSize, totalItems.intValue());
				List list = sqlSession.selectList(statement, attachPageQueryVariable(parameter,paginator));
				PageList page = new PageList(list,paginator);
				return page;
			}

			return new PageList(0,pageSize,0);
		}

		public static Map attachPageQueryVariable(Object parameter,Paginator p) {
			Map map = toParameterMap(parameter);
			map.put("startRow", p.getStartRow());
			map.put("endRow", p.getEndRow());
			map.put("offset", p.getOffset());
			map.put("limit", p.getLimit());
			return map;
		}
		
		public static Map toParameterMap(Object parameter) {
			if(parameter instanceof Map) {
				return (Map)parameter;
			}else {
				try {
					return PropertyUtils.describe(parameter);
				} catch (Exception e) {
					ReflectionUtils.handleReflectionException(e);
					return null;
				}
			}
		}
	}