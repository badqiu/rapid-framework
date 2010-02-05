package javacommon.base;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
/**
 * @author badqiu
 */
public abstract class BaseManager <E,PK extends Serializable>{
	
	protected Log log = LogFactory.getLog(getClass());

	protected abstract EntityDao getEntityDao();

	public E getById(PK id) {
		return (E)getEntityDao().getById(id);
	}
	
	public List<E> findAll() {
		return getEntityDao().findAll();
	}
	
	public void saveOrUpdate(E entity) {
		getEntityDao().saveOrUpdate(entity);
	}
	
	public void save(E entity) {
		getEntityDao().save(entity);
	}
	
	public void removeById(PK id) {
		getEntityDao().deleteById(id);
	}
	
	public void update(E entity) {
		getEntityDao().update(entity);
	}
	
	public boolean isUnique(E entity, String uniquePropertyNames) {
		return getEntityDao().isUnique(entity, uniquePropertyNames);
	}
}
