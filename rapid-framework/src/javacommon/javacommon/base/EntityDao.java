package javacommon.base;

import java.io.Serializable;
import java.util.List;
/**
 * @author badqiu
 */
public interface EntityDao <E,PK extends Serializable>{

	public Object getById(PK id);
	
	public void deleteById(PK id);
	
	public void save(E entity);
	
	public void update(E entity);

	public void saveOrUpdate(E entity);

	public boolean isUnique(E entity, String uniquePropertyNames);
	
	public void flush();
	
	public List<E> findAll();
	
}
