package javacommon.base;

import static cn.org.rapid_framework.util.SqlRemoveUtils.removeFetchKeyword;
import static cn.org.rapid_framework.util.SqlRemoveUtils.removeOrders;
import static cn.org.rapid_framework.util.SqlRemoveUtils.removeSelect;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javacommon.xsqlbuilder.SafeSqlProcesser;
import javacommon.xsqlbuilder.SafeSqlProcesserFactory;
import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;
import javacommon.xsqlbuilder.safesql.DirectReturnSafeSqlProcesser;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.Dialect;
import org.hibernate.impl.SessionFactoryImpl;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

/**
 * @author badqiu
 */
public abstract class BaseHibernateDao<E,PK extends Serializable> extends HibernateDaoSupport implements EntityDao<E,PK>{
	/**
	 * Logger for subclass
	 */
	protected Log log = LogFactory.getLog(getClass());
	
	public long queryForLong(final String queryString) {
		return queryForLong(queryString,new Object[]{});
	}
	
	public long queryForLong(final String queryString,Object value) {
		return queryForLong(queryString,new Object[]{value});
	}
	
	public long queryForLong(final String queryString,Object[] values) {
		return DataAccessUtils.longResult(getHibernateTemplate().find(queryString, values));
	}
	
	/**
	 * 得到全部数据,但执行分页
	 * @param pageRequest
	 * @return
	 */
	public Page findAll(final PageRequest pageRequest) {
		return (Page)getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				
				StringBuffer queryString = new StringBuffer(" FROM ").append(getEntityClass().getSimpleName());
				String countQueryString = "SELECT count(*) " + queryString.toString();
				if(StringUtils.hasText(pageRequest.getSortColumns())) {
					queryString.append(" ORDER BY "+pageRequest.getSortColumns());
				}
				
				Query query = session.createQuery(queryString.toString());
				Query countQuery = session.createQuery(countQueryString);
				return PageQueryUtils.executeQueryForPage(pageRequest, query, countQuery);
			}
		});
	}
	
	public Page pageQuery(final String sql,final PageRequest pageRequest) {
		final String countQuery = "select count(*) " + removeSelect(removeFetchKeyword((sql)));
		return pageQuery(sql,countQuery,pageRequest);
	}

	public Page pageQuery(final String sql,String countQuery,final PageRequest pageRequest) {
		Map otherFilters = new HashMap(1);
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		
		XsqlBuilder builder = getXsqlBuilder();
		
		//混合使用otherFilters与pageRequest为一个filters使用
		XsqlFilterResult queryXsqlResult = builder.generateHql(sql,otherFilters,pageRequest);
		XsqlFilterResult countQueryXsqlResult = builder.generateHql(countQuery,otherFilters,pageRequest);
		
		return PageQueryUtils.pageQuery(getHibernateTemplate(),pageRequest,queryXsqlResult,countQueryXsqlResult);
	}
	
	protected XsqlBuilder getXsqlBuilder() {
		SessionFactoryImpl sf = (SessionFactoryImpl)(getSessionFactory());
		Dialect dialect = sf.getDialect();
		
		//or SafeSqlProcesserFactory.getMysql();
		SafeSqlProcesser safeSqlProcesser = SafeSqlProcesserFactory.getFromCacheByHibernateDialect(dialect); 
		XsqlBuilder builder = new XsqlBuilder(safeSqlProcesser);
		
		if(builder.getSafeSqlProcesser().getClass() == DirectReturnSafeSqlProcesser.class) {
			System.err.println(BaseHibernateDao.class.getSimpleName()+".getXsqlBuilder(): 故意报错,你未开启Sql安全过滤,单引号等转义字符在拼接sql时需要转义,不然会导致Sql注入攻击的安全问题，请修改源码使用new XsqlBuilder(SafeSqlProcesserFactory.getDataBaseName())开启安全过滤");
		}
		return builder;
	}
	
	static class PageQueryUtils {
		private static Page pageQuery(HibernateTemplate template,final PageRequest pageRequest, final XsqlFilterResult queryXsqlResult, final XsqlFilterResult countQueryXsqlResult) {
			return (Page)template.execute(new HibernateCallback() {
				public Object doInHibernate(Session session) throws HibernateException, SQLException {
					
					Query query = setQueryParameters(session.createQuery(queryXsqlResult.getXsql()),pageRequest);
					Query countQuery = setQueryParameters(session.createQuery(removeOrders(countQueryXsqlResult.getXsql())),pageRequest);
					
					return executeQueryForPage(pageRequest, query, countQuery);
				}
			});
		}
		
		private static Object executeQueryForPage(final PageRequest pageRequest,Query query, Query countQuery) {
			Page page = new Page(pageRequest,((Number)countQuery.uniqueResult()).intValue());
			if(page.getTotalCount() <= 0) {
				page.setResult(new ArrayList(0));
			}else {
				page.setResult(query.setFirstResult(page.getFirstResult()).setMaxResults(page.getPageSize()).list());
			}
			return page;
		}
	
		public static Query setQueryParameters(Query q,Object params) {
			q.setProperties(params);
			return q;
		}
		
		public static Query setQueryParameters(Query q,Map params) {
			q.setProperties(params);
			return q;
		}
	}
	 
	public void save(E entity) {
		getHibernateTemplate().save(entity);
	}

	public List<E> findAll() {
		return getHibernateTemplate().loadAll(getEntityClass());
	}

	public E getById(PK id) {
		return (E)getHibernateTemplate().get(getEntityClass(),id);
	}

	public void delete(Object entity) {
		getHibernateTemplate().delete(entity);
	}
	
	public void delete(Serializable entity) {
		getHibernateTemplate().delete(entity);
	}
	
	public void deleteById(PK id) {
		Object entity = getById(id);
		if(entity == null) {
			throw new ObjectRetrievalFailureException(getEntityClass(),id);
		}
		getHibernateTemplate().delete(entity);
	}

	public void update(E entity) {
		getHibernateTemplate().update(entity);
	}

	public void saveOrUpdate(E entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public void refresh(Object entity) {
		getHibernateTemplate().refresh(entity);
	}

	public void flush() {
		getHibernateTemplate().flush();
	}

	public void evict(Object entity) {
		getHibernateTemplate().evict(entity);
	}

	public void saveAll(Collection<E> entities) {
		for(Iterator<E> it = entities.iterator(); it.hasNext();) {
			save(it.next());
		}
	}

	public void deleteAll(Collection entities) {
		getHibernateTemplate().deleteAll(entities);
	}

    public E findByProperty(final String propertyName, final Object value){
    	
        return (E)getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(getEntityClass())
					.add(Expression.eq(propertyName,value))
					.uniqueResult();
			}
        });
    }

    public List<E> findAllBy(final String propertyName, final Object value) {
        return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				return session.createCriteria(getEntityClass())
					.add(Expression.eq(propertyName,value))
					.list();
			}
        });
    }

    /**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 *
	 * @param uniquePropertyNames 在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(E entity, String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = getSession().createCriteria(getEntityClass()).setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (int i = 0; i < nameList.length; i++) {
				criteria.add(Restrictions.eq(nameList[i], PropertyUtils.getProperty(entity, nameList[i])));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getSessionFactory().getClassMetadata(entity.getClass()).getIdentifierPropertyName();
			if(idName != null) {
				// 取得entity的主键值
				Serializable id =  (Serializable)PropertyUtils.getProperty(entity, idName);
	
				// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
				if (id != null)
					criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
			}
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return ((Number) criteria.uniqueResult()).intValue() == 0;
	}
	
    public abstract Class getEntityClass();

}
