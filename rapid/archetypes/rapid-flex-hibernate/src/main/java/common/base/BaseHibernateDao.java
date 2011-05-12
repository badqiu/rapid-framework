package common.base;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.orm.ObjectRetrievalFailureException;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.util.SqlRemoveUtils;
import cn.org.rapid_framework.util.page.PageQuery;

/**
 * @author badqiu
 * @author hunhun
 * @since 4.0
 */
@SuppressWarnings(value = "all")
public abstract class BaseHibernateDao<E, PK extends Serializable>
		extends
			HibernateDaoSupport implements EntityDao<E, PK> {
	/**
	 * Logger for subclass
	 */
	protected Log log = LogFactory.getLog(getClass());
	public abstract Class getEntityClass();

	public long queryForLong(final String queryString) {
		return queryForLong(queryString, new Object[]{});
	}

	public long queryForLong(final String queryString, Object value) {
		return queryForLong(queryString, new Object[]{value});
	}

	public long queryForLong(final String queryString, Object[] values) {
		return DataAccessUtils.longResult(getHibernateTemplate().find(
				queryString, values));
	}

	public E getById(PK id) {
		return (E) getHibernateTemplate().get(getEntityClass(), id);
	}

	public List<E> findAll() {
		return getHibernateTemplate().loadAll(getEntityClass());
	}

	public void save(E entity) {
		getHibernateTemplate().save(entity);
	}

	public void saveAll(Collection<E> entities) {
		for (Iterator<E> it = entities.iterator(); it.hasNext();) {
			save(it.next());
		}
	}

	public void saveOrUpdate(E entity) {
		getHibernateTemplate().saveOrUpdate(entity);
	}

	public void update(E entity) {
		getHibernateTemplate().update(entity);
	}

	public void deleteById(PK id) {
		Object entity = getById(id);
		if (entity == null) {
			throw new ObjectRetrievalFailureException(getEntityClass(), id);
		}
		getHibernateTemplate().delete(entity);
	}

	public void delete(Object entity) {
		getHibernateTemplate().delete(entity);
	}

	public void delete(Serializable entity) {
		getHibernateTemplate().delete(entity);
	}

	public void deleteAll(Collection entities) {
		getHibernateTemplate().deleteAll(entities);
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

	public E findByProperty(final String propertyName, final Object value) {

		return (E) getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createCriteria(getEntityClass())
						.add(Expression.eq(propertyName, value)).uniqueResult();
			}
		});
	}

	public List<E> findAllByProperty(final String propertyName,
			final Object value) {
		return getHibernateTemplate().executeFind(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				return session.createCriteria(getEntityClass())
						.add(Expression.eq(propertyName, value)).list();
			}
		});
	}

	/**
	 * 判断对象某些属性的值在数据库中是否唯一.
	 * 
	 * @param uniquePropertyNames
	 *            在POJO里不能重复的属性列表,以逗号分割 如"name,loginid,password"
	 */
	public boolean isUnique(E entity, String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = getSession().createCriteria(getEntityClass())
				.setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// 循环加入唯一列
			for (int i = 0; i < nameList.length; i++) {
				criteria.add(Restrictions.eq(nameList[i],
						PropertyUtils.getProperty(entity, nameList[i])));
			}

			// 以下代码为了如果是update的情况,排除entity自身.

			String idName = getSessionFactory().getClassMetadata(
					entity.getClass()).getIdentifierPropertyName();
			if (idName != null) {
				// 取得entity的主键值
				Serializable id = (Serializable) PropertyUtils.getProperty(
						entity, idName);

				// 如果id!=null,说明对象已存在,该操作为update,加入排除自身的判断
				if (id != null)
					criteria.add(Restrictions.not(Restrictions.eq(idName, id)));
			}
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
		return ((Number) criteria.uniqueResult()).intValue() == 0;
	}

	// 以下为方便分页使用的方法/////////////////////////////////////////////////////////

	/**
	 * 得到全部数据并执行分页
	 * 
	 * @param pageQuery
	 * @return
	 */
	public Page<E> findAllPage(final PageQuery pageQuery) {
		return (Page<E>) getHibernateTemplate().executeFind(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						StringBuilder hqlSb = new StringBuilder(" FROM ")
								.append(getEntityClass().getSimpleName());
						String countHql = "SELECT count(*) " + hqlSb.toString();
						Query query = session.createQuery(hqlSb.toString());
						Query countQuery = session.createQuery(countHql);
						return executeQueryForPage(query, countQuery,
								pageQuery.getPage(), pageQuery.getPageSize());
					}
				});
	}

	/**
	 * 根据hql和pageQuery进行有条件分页
	 * 
	 * @param hql
	 * @param pageQuery
	 * @return
	 */
	protected Page<E> pageQuery(final String hql, final PageQuery pageQuery) {

		final String countHql = "SELECT count(*) "
				+ SqlRemoveUtils.removeSelect(SqlRemoveUtils
						.removeFetchKeyword((hql)));
		return pageQuery(hql, countHql, pageQuery);
	}

	private Page<E> pageQuery(final String hql, final String countHql,
			final PageQuery pageQuery) {
		return (Page<E>) getHibernateTemplate().execute(
				new HibernateCallback() {
					public Object doInHibernate(Session session)
							throws HibernateException, SQLException {

						Query query = session.createQuery(hql);
						setQueryParameters(query, pageQuery);
						Query countQuery = session.createQuery(countHql);
						setQueryParameters(countQuery, pageQuery);
						return executeQueryForPage(query, countQuery,
								pageQuery.getPage(), pageQuery.getPageSize());
					}
				});
	}

	private Page<E> executeQueryForPage(Query query, Query countQuery,
			int pageNo, int pageSize) {

		Assert.isTrue(pageSize > 0, "pageSize > 0 must be true");
		Number totalCount = (Number) countQuery.uniqueResult();
		Page<E> page = new Page<E>(pageNo, pageSize, totalCount.intValue());

		if (totalCount != null && totalCount.intValue() > 0) {
			page.setResult(query.setFirstResult(page.getFirstResult())
					.setMaxResults(page.getPageSize()).list());
		}
		return page;

	}

	/**
	 * 为hibernate 的Query设置Object参数
	 * 
	 * @param q
	 * @param params
	 * @return
	 */
	protected void setQueryParameters(Query q, Object params) {
		q.setProperties(params);
	}

	/**
	 * 为hibernate 的Query设置Map参数
	 * 
	 * @param q
	 * @param params
	 * @return
	 */
	protected void setQueryParameters(Query q, Map params) {
		q.setProperties(params);
	}

}
