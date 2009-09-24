package javacommon;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import javacommon.base.EntityDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.MapAndObject;

/**
 * @author badqiu
 * @version 1.0
 */
public abstract class BaseIbatis3Dao<E,PK extends Serializable> extends DaoSupport implements EntityDao<E,PK> {
    protected final Log log = LogFactory.getLog(getClass());
    
    private SqlSessionFactory sqlSessionFactory;
    private SqlSessionTemplate sqlSessionTemplate;
	protected void checkDaoConfig() throws IllegalArgumentException {
		Assert.notNull("sqlSessionFactory must be not null");
	}

	public SqlSessionFactory getSqlSessionFactory() {
		return sqlSessionFactory;
	}

	public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
		this.sqlSessionFactory = sqlSessionFactory;
		this.sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
	}

	public abstract Class getEntityClass();
    
    public SqlSessionTemplate getSqlSessionTemplate() {
    	return sqlSessionTemplate;
    }
    
    public Object getById(PK primaryKey) {
        Object object = getSqlSessionTemplate().selectOne(getFindByPrimaryKeyQuery(), primaryKey);
        return object;
    }
    
	public void deleteById(PK id) {
		getSqlSessionTemplate().delete(getDeleteQuery(), id);
	}
	
    public void save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		Object primaryKey = getSqlSessionTemplate().insert(getInsertQuery(), entity);    	
    }
    
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		Object primaryKey = getSqlSessionTemplate().update(getUpdateQuery(), entity);
	}
	
	/**
	 * 用于子类覆盖,在insert,update之前调用
	 * @param o
	 */
    protected void prepareObjectForSaveOrUpdate(Object o) {
    }

    public String getFindByPrimaryKeyQuery() {
        return getEntityClass().getSimpleName()+".getById";
    }

    public String getInsertQuery() {
        return getEntityClass().getSimpleName()+".insert";
    }

    public String getUpdateQuery() {
    	return getEntityClass().getSimpleName()+".update";
    }

    public String getDeleteQuery() {
    	return getEntityClass().getSimpleName()+".delete";
    }

    public String getCountQuery() {
		return getEntityClass().getSimpleName() +".count";
	}
    
	protected Page pageQuery(String statementName, PageRequest pageRequest) {
		
		Number totalCount = (Number) this.getSqlSessionTemplate().selectOne(getCountQuery(),pageRequest.getFilters());
		Page page = new Page(pageRequest,totalCount.intValue());
		
		//其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用. 与getSqlMapClientTemplate().queryForList(statementName, parameterObject)配合使用
		Map otherFilters = new HashMap();
		otherFilters.put("offset", page.getFirstResult());
		otherFilters.put("pageSize", page.getPageSize());
		otherFilters.put("lastRows", page.getFirstResult() + page.getPageSize());
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		
		//混合两个filters为一个filters,MapAndObject.get()方法将在两个对象取值,Map如果取值为null,则再在Bean中取值
		Map parameterObject = BeanUtils.describe(pageRequest.getFilters());
		otherFilters.putAll(parameterObject);
		
		List list = getSqlSessionTemplate().selectList(statementName, otherFilters,page.getFirstResult(),page.getPageSize());
		page.setResult(list);
		return page;
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
	
	public static class SqlSessionTemplate {
		SqlSessionFactory sqlSessionFactory;
		
		public SqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
			this.sqlSessionFactory = sqlSessionFactory;
		}

		public Object execute(SqlSessionCallback action)  {
			SqlSession session = sqlSessionFactory.openSession();
			try {
				return action.doInSession(session);
			}finally {
				if(session != null) session.close();
			}
		}
		
		public Object selectOne(final String statement,final Object parameter) {
			return execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectOne(statement, parameter);
				}
			});
		}
		
		public List selectList(final String statement,final Object parameter,final int offset,final int limit) {
			return (List)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.selectList(statement, parameter, offset, limit);
				}
			});
		}
		
		
		public Object delete(final String statement,final Object parameter) {
			return execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.delete(statement, parameter);
				}
			});
		}
		
		public Object update(final String statement,final Object parameter) {
			return execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.update(statement, parameter);
				}
			});
		}
		
		public Object insert(final String statement,final Object parameter) {
			return execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.insert(statement, parameter);
				}
			});
		}
	} 
	
	public static interface SqlSessionCallback {
		
		public Object doInSession(SqlSession session);
		
	}
	
	
}
