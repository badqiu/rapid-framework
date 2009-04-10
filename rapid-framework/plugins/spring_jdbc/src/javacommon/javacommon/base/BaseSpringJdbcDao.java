package javacommon.base;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.hibernate.id.IdentifierGenerationException;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
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
import cn.org.rapid_framework.util.ObjectUtils;
import cn.org.rapid_framework.util.SqlRemoveUtils;
/**
 * Spring的JDBC基类
 * @author badqiu
 *
 */
public abstract class BaseSpringJdbcDao extends JdbcDaoSupport implements EntityDao{

	protected final Log log = LogFactory.getLog(getClass());

	protected SimpleJdbcTemplate simpleJdbcTemplate;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public abstract String getIdentifierPropertyName();
	
	public abstract Class getEntityClass();
	
	private Dialect dialect;
	
	public void setDialect(Dialect d) {
		this.dialect = d;
	}
	
	@Override
	protected void initTemplateConfig() {
		super.initTemplateConfig();
		if(dialect == null) throw new IllegalStateException("'dialect' property must be not null");
		log.info("use jdbc dialect:"+dialect);
		simpleJdbcTemplate = new SimpleJdbcTemplate(getDataSource());
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getDataSource());
	}
	
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}

	public Serializable convert2CorrectIdType(Object value) throws SQLException, IdentifierGenerationException {
		return convert2CorrectIdType(value,getIdentifierPropertyType());
	}
	
	public static Serializable convert2CorrectIdType(Object value,Class type) throws SQLException, IdentifierGenerationException {
		try {
			return (Serializable)type.getConstructor(String.class).newInstance(value.toString());
		}catch(Exception e) {
			throw new IllegalArgumentException(String.format("convert error,value=%s type=%s",value,type.getCanonicalName()),e);
		}
	}
	
	protected Object getIdentifierPropertyValue(Object entity) {
		try {
			return BeanUtils.getProperty(entity, getIdentifierPropertyName());
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
			return null;
		}
	}

	protected Class getIdentifierPropertyType() {
		try {
			return PropertyUtils.getPropertyType(getEntityClass().newInstance(), getIdentifierPropertyName());
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
			return null;
		}
	}
	
	/**
	 * 得到全部数据,但执行分页
	 * @param pageRequest
	 * @return
	 */
	public Page findAll(String tableName,final PageRequest pageRequest) {
		return pageQuery("select * from "+tableName+" /~ order by {sortingColumn} [sortingDirection] ~/",pageRequest);
	}
	
	public Page pageQuery(String query,PageRequest pageRequest) {
		return pageQuery(query,"select count(*) "+SqlRemoveUtils.removeSelect(query),pageRequest);
	}
	
	public Page pageQuery(String query,String countQuery,final PageRequest pageRequest) {
		Map filters = pageRequest.getFilters();
		filters.put("sortColumns", pageRequest.getSortColumns());
		
		XsqlBuilder builder = new XsqlBuilder();
//		XsqlBuilder builder = new XsqlBuilder(SafeSqlProcesserFactory.getMysql());
		if(builder.getSafeSqlProcesser().getClass() == DirectReturnSafeSqlProcesser.class) {
			System.err.println(getClass().getSimpleName()+": 你未开启Sql安全过滤,单引号等转义字符在拼接sql时需要转义,不然会导致Sql注入攻击的安全问题，请使用new XsqlBuilder(SafeSqlProcesserFactory.getDataBaseName())开启安全过滤");
		}
		
		XsqlFilterResult queryXsqlResult = builder.generateHql(query,filters);
		
		XsqlFilterResult countQueryXsqlResult = builder.generateHql(countQuery,filters);
		final int totalCount = getSimpleJdbcTemplate().queryForInt(SqlRemoveUtils.removeOrders(countQueryXsqlResult.getXsql()),countQueryXsqlResult.getAcceptedFilters());
		
		String sql = queryXsqlResult.getXsql();
		Map acceptedFilters = queryXsqlResult.getAcceptedFilters();
		int limit = pageRequest.getPageSize();
		int pageNumber = pageRequest.getPageNumber();
		return pageQuery(sql, acceptedFilters, totalCount, limit, pageNumber);
	}

	private Page pageQuery(String sql, Map paramMap,final int totalCount, int limit, int pageNumber) {
		if(dialect.supportsLimit()) {
			if(dialect.supportsLimitOffset()) {
				Page page = new Page(pageNumber,limit,totalCount);
				String limitSql = dialect.getLimitString(sql,page.getFirstResult(),limit);
				List list = getNamedParameterJdbcTemplate().query(limitSql, paramMap, new BeanPropertyRowMapper(getEntityClass()));
				page.setThisPageElements(list);
				return page;
			}else {
				String limitSql = dialect.getLimitString(sql, 0, limit);
				return getJdbcScrollPage(pageNumber,limit, limitSql,paramMap,totalCount);
			}
		}else {
			return getJdbcScrollPage(pageNumber,limit, sql,paramMap,totalCount);			
		}
	}

	public Page getJdbcScrollPage(final int pageNumber,final int pageSize,
			String sql,Map filters, final int totalCount) {
		return (Page)getNamedParameterJdbcTemplate().execute(sql, filters, new PreparedStatementCallback() {
			public Object doInPreparedStatement(PreparedStatement ps)
					throws SQLException, DataAccessException {
				ps.setMaxRows(pageSize);
				ResultSet rs = ps.executeQuery();
				return new JdbcScrollPage(rs,totalCount,new BeanPropertyRowMapper(getEntityClass()),pageNumber,pageSize);
			}
		});
	}
	
	private void setIdentifierProperty(Object entity, Object id) {
		try {
			BeanUtils.setProperty(entity, getIdentifierPropertyName(), convert2CorrectIdType(id));
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
	}

	protected void insertWithIdentity(Object entity,String insertSql) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity) , keyHolder);
		setIdentifierProperty(entity, keyHolder.getKey().longValue());
	}

	protected void insertWithAutoIncrement(Object entity,String insertSql) {
		insertWithIdentity(entity,insertSql);
	}
	
	protected void insertWithSequence(Object entity,AbstractSequenceMaxValueIncrementer sequenceIncrementer,String insertSql) {
		Long id = sequenceIncrementer.nextLongValue();
		setIdentifierProperty(entity, id);
		getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	
	protected void insertWithDB2Sequence(Object entity,String sequenceName,String insertSql) {
		insertWithSequence(entity, new DB2SequenceMaxValueIncrementer(getDataSource(),sequenceName), insertSql);
	}
	
	protected void insertWithOracleSequence(Object entity,String sequenceName,String insertSql) {
		insertWithSequence(entity, new OracleSequenceMaxValueIncrementer(getDataSource(),sequenceName), insertSql);
	}
	
	protected void insertWithUUID(Object entity,String insertSql) {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		setIdentifierProperty(entity, uuid);
		getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	
	protected void insertWithAssigned(Object entity,String insertSql) {
		getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	
	public void flush() {
		//ignore
	}
	
	public boolean isUnique(Object entity, String uniquePropertyNames) {
		throw new UnsupportedOperationException();
	}
	
	public void saveOrUpdate(Object entity) {
		Object id = getIdentifierPropertyValue(entity);
		if(ObjectUtils.isNullOrEmptyString(id)) {
			save(entity);
		}else {
			update(entity);
		}
	}

}
