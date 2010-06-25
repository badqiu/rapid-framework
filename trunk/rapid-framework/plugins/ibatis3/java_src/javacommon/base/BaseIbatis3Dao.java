package javacommon.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.dao.support.DaoSupport;
import org.springframework.util.Assert;

import cn.org.rapid_framework.beanutils.BeanUtils;
import cn.org.rapid_framework.beanutils.PropertyUtils;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

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

    public SqlSessionTemplate getSqlSessionTemplate() {
    	return sqlSessionTemplate;
    }
    
    public Object getById(PK primaryKey) {
        Object object = getSqlSessionTemplate().selectOne(getFindByPrimaryKeyQuery(), primaryKey);
        return object;
    }
    
	public void deleteById(PK id) {
		int affectCount = getSqlSessionTemplate().delete(getDeleteQuery(), id);
	}
	
    public void save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlSessionTemplate().insert(getInsertQuery(), entity);    	
    }
    
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlSessionTemplate().update(getUpdateQuery(), entity);
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
    
    public String getFindByPrimaryKeyQuery() {
        return getIbatisMapperNamesapce()+".getById";
    }

    public String getInsertQuery() {
        return getIbatisMapperNamesapce()+".insert";
    }

    public String getUpdateQuery() {
    	return getIbatisMapperNamesapce()+".update";
    }

    public String getDeleteQuery() {
    	return getIbatisMapperNamesapce()+".delete";
    }

    public String getCountQuery(String statementName) {
		return statementName +"-count";
	}
    
	protected Page pageQuery(String statementName, PageRequest pageRequest) {
		
		Number totalCount = (Number) this.getSqlSessionTemplate().selectOne(getCountQuery(statementName),pageRequest);
		if(totalCount == null || totalCount.intValue() <= 0) {
			return new Page(pageRequest,0);
		}
		
		Page page = new Page(pageRequest,totalCount.intValue());
		
		//其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用. 与getSqlMapClientTemplate().queryForList(statementName, parameterObject)配合使用
		Map filters = new HashMap();
		filters.put("offset", page.getFirstResult());
		filters.put("pageSize", page.getPageSize());
		filters.put("lastRows", page.getFirstResult() + page.getPageSize());
		filters.put("sortColumns", pageRequest.getSortColumns());
		
		Map parameterObject = PropertyUtils.describe(pageRequest);
		filters.putAll(parameterObject);
		
		List list = getSqlSessionTemplate().selectList(statementName, filters,page.getFirstResult(),page.getPageSize());
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
			SqlSession session = null;
			try {
				session = sqlSessionFactory.openSession();
				Object result = action.doInSession(session);
				return result;
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
					return session.selectList(statement, parameter, new RowBounds(offset,limit));
				}
			});
		}
		
		
		public int delete(final String statement,final Object parameter) {
			return (Integer)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.delete(statement, parameter);
				}
			});
		}
		
		public int update(final String statement,final Object parameter) {
			return (Integer)execute(new SqlSessionCallback() {
				public Object doInSession(SqlSession session) {
					return session.update(statement, parameter);
				}
			});
		}
		
		public int insert(final String statement,final Object parameter) {
			return (Integer)execute(new SqlSessionCallback() {
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
