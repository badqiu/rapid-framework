package javacommon.base;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javacommon.xsqlbuilder.XsqlBuilder;
import javacommon.xsqlbuilder.XsqlBuilder.XsqlFilterResult;
import javacommon.xsqlbuilder.safesql.DirectReturnSafeSqlProcesser;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.ParameterizedBeanPropertyRowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.incrementer.AbstractSequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.DB2SequenceMaxValueIncrementer;
import org.springframework.jdbc.support.incrementer.OracleSequenceMaxValueIncrementer;
import org.springframework.util.ReflectionUtils;

import cn.org.rapid_framework.jdbc.dialect.Dialect;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.page.impl.JdbcScrollPage;
import cn.org.rapid_framework.util.CollectionUtils;
import cn.org.rapid_framework.util.ObjectUtils;
import cn.org.rapid_framework.util.SqlRemoveUtils;
/**
 * 只读的Spring的JDBC基类,不支持save() update()等方法 
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
