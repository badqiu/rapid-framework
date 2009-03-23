package javacommon.base;

import java.io.Serializable;
import java.util.List;
/**
 * @author badqiu
 */
public interface EntityDao <E>{

	public Object getById(Serializable id);
	
	public void deleteById(Serializable id);
	
	public void save(E entity);
	
	public void update(E entity);

	public void saveOrUpdate(E entity);

	public boolean isUnique(E entity, String uniquePropertyNames);
	
	public void flush();
	
	public List<E> findAll();
	
}
