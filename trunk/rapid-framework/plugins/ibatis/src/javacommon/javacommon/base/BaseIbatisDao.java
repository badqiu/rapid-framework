package javacommon.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

/**
 * @author badqiu
 * @version 1.0
 */
public abstract class BaseIbatisDao<E> extends SqlMapClientDaoSupport implements EntityDao<E> {
    protected final Log log = LogFactory.getLog(getClass());
    
    public abstract Class getEntityClass();
    
    public Object getById(Serializable primaryKey) {
        Object object = getSqlMapClientTemplate().queryForObject(getFindByPrimaryKeyQuery(), primaryKey);
        return object;
    }
    
	public void deleteById(Serializable id) {
		getSqlMapClientTemplate().delete(getDeleteQuery(), id);
	}
	
    public void save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		Object primaryKey = getSqlMapClientTemplate().insert(getInsertQuery(), entity);    	
    }
    
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		Object primaryKey = getSqlMapClientTemplate().update(getUpdateQuery(), entity);
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
		
		Number totalCount = (Number) this.getSqlMapClientTemplate().queryForObject(getCountQuery(),pageRequest.getFilters());
		Page page = new Page(pageRequest,totalCount.intValue());
		
		Map parameterObject = new HashMap();
		parameterObject.putAll(pageRequest.getFilters());
		parameterObject.put("offset", page.getFirstResult());
		parameterObject.put("pageSize", page.getPageSize());
		parameterObject.put("lastRows", page.getFirstResult() + page.getPageSize());
		parameterObject.put("sortColumns", pageRequest.getSortColumns());
		
		List list = getSqlMapClientTemplate().queryForList(statementName, parameterObject);
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
}
