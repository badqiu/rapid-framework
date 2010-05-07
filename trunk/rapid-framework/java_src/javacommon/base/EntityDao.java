package javacommon.base;

import java.io.Serializable;
import java.util.List;

import org.springframework.dao.DataAccessException;
/**
 * @author badqiu
 */
public interface EntityDao <E,PK extends Serializable>{

	public Object getById(PK id) throws DataAccessException;
	
	public void deleteById(PK id) throws DataAccessException;
	
	public void save(E entity) throws DataAccessException;
	
	public void update(E entity) throws DataAccessException;

	public void saveOrUpdate(E entity) throws DataAccessException;

	public boolean isUnique(E entity, String uniquePropertyNames) throws DataAccessException;
	
	public void flush() throws DataAccessException;
	
	public List<E> findAll() throws DataAccessException;
	
}
