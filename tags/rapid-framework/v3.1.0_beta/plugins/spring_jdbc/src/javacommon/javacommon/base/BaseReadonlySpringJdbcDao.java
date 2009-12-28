package javacommon.base;

import java.io.Serializable;
/**
 * 只读的Spring的JDBC基类,便于子类可以删除save() update() delete()等方法 
 * @author badqiu
 *
 */
public abstract class BaseReadonlySpringJdbcDao<E,PK extends Serializable> extends BaseSpringJdbcDao<E, Serializable>{

	public String getDeleteByIdSql() {
		throw new UnsupportedOperationException("readonly, current operation is not support");
	}

	public void save(E entity) {
		throw new UnsupportedOperationException("readonly, current operation is not support");
	}

	public void update(E entity) {
		throw new UnsupportedOperationException("readonly, current operation is not support");
	}

}
