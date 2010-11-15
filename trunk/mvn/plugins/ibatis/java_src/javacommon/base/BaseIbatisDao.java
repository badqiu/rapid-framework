package javacommon.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.ibatis.SqlMapClientTemplate;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.MapAndObject;

/**
 * @author badqiu
 * @version 1.0
 */
public abstract class BaseIbatisDao<E,PK extends Serializable> extends SqlMapClientDaoSupport implements EntityDao<E,PK> {
    protected final Log log = LogFactory.getLog(getClass());
    
    
    public Object getById(PK primaryKey) {
        Object object = getSqlMapClientTemplate().queryForObject(getFindByPrimaryKeyStatement(), primaryKey);
        return object;
    }
    
	public void deleteById(PK id) {
		int affectCount = getSqlMapClientTemplate().delete(getDeleteStatement(), id);
	}
	
    public void save(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		getSqlMapClientTemplate().insert(getInsertStatement(), entity);    	
    }
    
	public void update(E entity) {
		prepareObjectForSaveOrUpdate(entity);
		int affectCount = getSqlMapClientTemplate().update(getUpdateStatement(), entity);
	}
	
	/**
	 * 用于子类覆盖,在insert,update之前调用
	 * @param o
	 */
    protected void prepareObjectForSaveOrUpdate(E o) {
    }

    public String getFindByPrimaryKeyStatement() {
        return getIbatisSqlMapNamespace()+".getById";
    }

    public String getInsertStatement() {
        return getIbatisSqlMapNamespace()+".insert";
    }

    public String getUpdateStatement() {
    	return getIbatisSqlMapNamespace()+".update";
    }

    public String getDeleteStatement() {
    	return getIbatisSqlMapNamespace()+".delete";
    }

    public String getCountStatementForPaging(String statementName) {
		return statementName+".count";
	}
    
    public String getIbatisSqlMapNamespace() {
    	 throw new RuntimeException("not yet implement");
    }
    
    @SuppressWarnings("unchecked")
	protected Page pageQuery(String statementName, PageRequest pageRequest) {
		return pageQuery(getSqlMapClientTemplate(),statementName,getCountStatementForPaging(statementName),pageRequest);
	}
	
	@SuppressWarnings("unchecked")
	public static Page pageQuery(SqlMapClientTemplate sqlMapClientTemplate,String statementName,String countStatementName, PageRequest pageRequest) {
		
		Number totalCount = (Number) sqlMapClientTemplate.queryForObject(countStatementName,pageRequest);
		if(totalCount == null || totalCount.longValue() <= 0) {
			return new Page(pageRequest,0);
		}
		
		Page page = new Page(pageRequest,totalCount.intValue());
		
		//其它分页参数,用于不喜欢或是因为兼容性而不使用方言(Dialect)的分页用户使用. 与getSqlMapClientTemplate().queryForList(statementName, parameterObject)配合使用
		Map otherFilters = new HashMap();
		otherFilters.put("offset", page.getFirstResult());
		otherFilters.put("pageSize", page.getPageSize());
		otherFilters.put("lastRows", page.getFirstResult() + page.getPageSize());
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		
		//混合两个filters为一个filters,MapAndObject.get()方法将在两个对象取值,Map如果取值为null,则再在Bean中取值
		Map parameterObject = new MapAndObject(otherFilters,pageRequest);
		List list = sqlMapClientTemplate.queryForList(statementName, parameterObject,page.getFirstResult(),page.getPageSize());
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
