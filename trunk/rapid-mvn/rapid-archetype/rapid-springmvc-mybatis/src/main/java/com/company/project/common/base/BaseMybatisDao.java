package com.company.project.common.base;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.page.PageList;

import com.company.project.common.util.MybatisPageQueryUtils;

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

    public String getMybatisMapperNamesapce() {
        throw new RuntimeException("not yet implement");
    }
    
    public String getFindByPrimaryKeyStatement() {
        return getMybatisMapperNamesapce()+".getById";
    }

    public String getInsertStatement() {
        return getMybatisMapperNamesapce()+".insert";
    }

    public String getUpdateStatement() {
    	return getMybatisMapperNamesapce()+".update";
    }

    public String getDeleteStatement() {
    	return getMybatisMapperNamesapce()+".delete";
    }
    
    public SqlSession getSqlSessionTemplate() {
        return getSqlSession();
    }

    public String getCountStatementForPaging(String statementName) {
		return statementName +".count";
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
