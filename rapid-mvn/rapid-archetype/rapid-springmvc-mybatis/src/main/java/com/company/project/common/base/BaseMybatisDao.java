package com.company.project.common.base;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.beanutils.PropertyUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.page.PageList;
import cn.org.rapid_framework.util.page.Paginator;

/**
 * @author badqiu
 * @version 1.0
 */
public abstract class BaseMybatisDao<E,PK extends Serializable> extends SqlSessionDaoSupport  implements EntityDao<E,PK> {
    protected final Log log = LogFactory.getLog(getClass());

    public Object getById(PK primaryKey) {
        Object object = getSqlSession().selectOne(getFindByPrimaryKeyStatement(), primaryKey);
        return object;
    }
    
	public void deleteById(PK id) {
		int affectCount = getSqlSession().delete(getDeleteStatement(), id);
	}
	
    public void save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlSession().insert(getInsertStatement(), entity);    	
    }
    
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlSession().update(getUpdateStatement(), entity);
	}
	
	/**
	 * 用于子类覆盖,在insert,update之前调用
	 * @param o
	 */
    protected void prepareObjectForSaveOrUpdate(E o) {
    }

    public String getIbatisMapperNamesapce() {
        throw new RuntimeException("not yet implement");
    }
    
    public String getFindByPrimaryKeyStatement() {
        return getIbatisMapperNamesapce()+".getById";
    }

    public String getInsertStatement() {
        return getIbatisMapperNamesapce()+".insert";
    }

    public String getUpdateStatement() {
    	return getIbatisMapperNamesapce()+".update";
    }

    public String getDeleteStatement() {
    	return getIbatisMapperNamesapce()+".delete";
    }
    
    public SqlSession getSqlSessionTemplate() {
        return getSqlSession();
    }

    public String getCountStatementForPaging(String statementName) {
		return statementName +".count";
	}
    
	protected Page pageQuery(String statementName, PageRequest pageRequest) {
		PageList pageList = pageQuery(getSqlSession(),statementName,getCountStatementForPaging(statementName),pageRequest);
        Page page = new Page(pageRequest,pageList.getTotalItems());
        page.setResult(pageList);
        return page;
	}
	
	public static PageList pageQuery(SqlSession sqlSessionTemplate,String statementName,String countStatementName, PageRequest pageRequest) {
		return MybatisPageQueryUtils.pageQuery(sqlSessionTemplate, statementName, countStatementName,pageRequest, pageRequest.getPageNumber(), pageRequest.getPageSize());
	}
	
	public List findAll() {
		throw new UnsupportedOperationException();
	}

	public boolean isUnique(E entity, String uniquePropertyNames) {
		throw new UnsupportedOperationException();
	}
	
	public void flush() {
		//ignore
	}
	
	public static class MybatisPageQueryUtils {
		
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
}
