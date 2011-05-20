package cn.org.rapid_framework.util.fortest;

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
public abstract class BaseHibernateDao<E,PK extends Serializable> extends HibernateDaoSupport {
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
	
	protected Page pageQuery(String sql, PageRequest pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * �õ�ȫ������,��ִ�з�ҳ
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
				return null;
			}
		});
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
	 * �ж϶���ĳЩ���Ե�ֵ�����ݿ����Ƿ�Ψһ.
	 *
	 * @param uniquePropertyNames ��POJO�ﲻ���ظ��������б�,�Զ��ŷָ� ��"name,loginid,password"
	 */
	public boolean isUnique(E entity, String uniquePropertyNames) {
		Assert.hasText(uniquePropertyNames);
		Criteria criteria = getSession().createCriteria(getEntityClass()).setProjection(Projections.rowCount());
		String[] nameList = uniquePropertyNames.split(",");
		try {
			// ѭ������Ψһ��
			for (int i = 0; i < nameList.length; i++) {
				criteria.add(Restrictions.eq(nameList[i], PropertyUtils.getProperty(entity, nameList[i])));
			}

			// ���´���Ϊ�������update�����,�ų�entity����.

			String idName = getSessionFactory().getClassMetadata(entity.getClass()).getIdentifierPropertyName();
			if(idName != null) {
				// ȡ��entity������ֵ
				Serializable id =  (Serializable)PropertyUtils.getProperty(entity, idName);
	
				// ���id!=null,˵�������Ѵ���,�ò���Ϊupdate,�����ų�������ж�
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
