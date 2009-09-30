package javacommon.base;

import java.io.Serializable;
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
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
import cn.org.rapid_framework.jdbc.support.OffsetLimitResultSetExtractor;
import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;
import cn.org.rapid_framework.util.CollectionUtils;
import cn.org.rapid_framework.util.ObjectUtils;
import cn.org.rapid_framework.util.SqlRemoveUtils;
/**
 * Spring的JDBC基类
 * @author badqiu
 *
 */
public abstract class BaseSpringJdbcDao<E,PK extends Serializable> extends JdbcDaoSupport implements EntityDao<E,PK>{

	protected final Log log = LogFactory.getLog(getClass());

	protected SimpleJdbcTemplate simpleJdbcTemplate;
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	public abstract String getIdentifierPropertyName();
	
	public abstract Class getEntityClass();
	
	//用于分页的dialect
	private Dialect dialect;
	
	public void setDialect(Dialect d) {
		this.dialect = d;
	}
	
	protected void checkDaoConfig() {
		super.checkDaoConfig();
		if(dialect == null) throw new IllegalStateException("'dialect' property must be not null");
		log.info("use jdbc dialect:"+dialect);
		simpleJdbcTemplate = new SimpleJdbcTemplate(getJdbcTemplate());
		namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
	}
	
	public SimpleJdbcTemplate getSimpleJdbcTemplate() {
		return simpleJdbcTemplate;
	}

	public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
		return namedParameterJdbcTemplate;
	}
	
	protected Object getIdentifierPropertyValue(Object entity) {
		try {
			return PropertyUtils.getProperty(entity, getIdentifierPropertyName());
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
		return pageQuery("select * from "+tableName+" /~ order by [sortColumns] ~/",pageRequest);
	}
	
	public Page pageQuery(String query,PageRequest pageRequest) {
		return pageQuery(query,pageRequest,new BeanPropertyRowMapper(getEntityClass()));
	}
	
	public Page pageQuery(String query,PageRequest pageRequest,RowMapper rowMapper) {
		return pageQuery(query,"select count(*) "+SqlRemoveUtils.removeSelect(query),pageRequest,rowMapper);
	}
	
	public Page pageQuery(String query,String countQuery,final PageRequest pageRequest,RowMapper rowMapper) {
		final int totalCount = queryTotalCount(countQuery,pageRequest.getFilters());
		
		Map otherFilters = new HashMap(1);
		otherFilters.put("sortColumns", pageRequest.getSortColumns());
		
		//混合使用otherFilters与pageRequest.getFilters()为一个filters使用
		XsqlFilterResult queryXsqlResult = getXsqlBuilder().generateHql(query,otherFilters,pageRequest.getFilters());
		String sql = queryXsqlResult.getXsql();
		Map acceptedFilters = queryXsqlResult.getAcceptedFilters();
		int pageSize = pageRequest.getPageSize();
		int pageNumber = pageRequest.getPageNumber();
		return pageQuery(sql, acceptedFilters, totalCount, pageSize, pageNumber,rowMapper);
	}

	protected XsqlBuilder getXsqlBuilder() {
		XsqlBuilder builder = new XsqlBuilder();
//		XsqlBuilder builder = new XsqlBuilder(SafeSqlProcesserFactory.getMysql());
		if(builder.getSafeSqlProcesser().getClass() == DirectReturnSafeSqlProcesser.class) {
			System.err.println("BaseSpringJdbcDao.getXsqlBuilder(): 故意报错,你未开启Sql安全过滤,单引号等转义字符在拼接sql时需要转义,不然会导致Sql注入攻击的安全问题，请修改源码使用new XsqlBuilder(SafeSqlProcesserFactory.getDataBaseName())开启安全过滤");
		}
		return builder;
	}

	private int queryTotalCount(String countQuery,Object filtersObject) {
		XsqlFilterResult countQueryXsqlResult = getXsqlBuilder().generateHql(countQuery,filtersObject);
		String removedOrderByQuery = SqlRemoveUtils.removeOrders(countQueryXsqlResult.getXsql());
		final int totalCount = getSimpleJdbcTemplate().queryForInt(removedOrderByQuery,countQueryXsqlResult.getAcceptedFilters());
		return totalCount;
	}

	private Page pageQuery(String sql, Map paramMap, final int totalCount,int pageSize, int pageNumber, RowMapper rowMapper) {
		Page page = new Page(pageNumber,pageSize,totalCount);
		List list = pageQuery(sql, paramMap,page.getFirstResult(),pageSize,rowMapper);
		page.setResult(list);
		return page;
	}

	static final String LIMIT_PLACEHOLDER = ":__limit";
	static final String OFFSET_PLACEHOLDER = ":__offset";
	public List pageQuery(String sql, final Map paramMap, int startRow,int pageSize, final RowMapper rowMapper) {
		//支持limit查询
		if(dialect.supportsLimit()) {
			paramMap.put(LIMIT_PLACEHOLDER.substring(1), pageSize);
			
			//支持limit及offset.则完全使用数据库分页
			if(dialect.supportsLimitOffset()) {
				paramMap.put(OFFSET_PLACEHOLDER.substring(1), startRow);
				sql = dialect.getLimitString(sql,startRow,OFFSET_PLACEHOLDER,pageSize,LIMIT_PLACEHOLDER);
				startRow = 0;
			}else {
				//不支持offset,则在后面查询中使用游标配合limit分页
				sql = dialect.getLimitString(sql, 0,null, pageSize,LIMIT_PLACEHOLDER);
			}
			
			pageSize = Integer.MAX_VALUE;
		}
		return (List)getNamedParameterJdbcTemplate().query(sql, paramMap, new OffsetLimitResultSetExtractor(startRow,pageSize,rowMapper));
	}
	
	private void setIdentifierProperty(Object entity, Object id) {
		try {
			BeanUtils.setProperty(entity, getIdentifierPropertyName(), id);
		} catch (Exception e) {
			ReflectionUtils.handleReflectionException(e);
		}
	}
	
	/**
	 * 适用sqlserver,mysql 自动生成主键
	 */
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
	/**
	 * 手工分配ID插入
	 * @param entity
	 * @param insertSql
	 */
	protected void insertWithAssigned(Object entity,String insertSql) {
		getNamedParameterJdbcTemplate().update(insertSql, new BeanPropertySqlParameterSource(entity));
	}
	
	public void flush() {
		//ignore
	}
	
	public boolean isUnique(E entity, String uniquePropertyNames) {
		throw new UnsupportedOperationException();
	}
	
	public abstract String getFindByIdSql();
	public E getById(PK id) {
		List list = getSimpleJdbcTemplate().query(getFindByIdSql(), ParameterizedBeanPropertyRowMapper.newInstance(getEntityClass()), id);
		return (E)CollectionUtils.findSingleObject(list);
	}

	public abstract String getDeleteByIdSql();
	public void deleteById(PK id) {
		getSimpleJdbcTemplate().update(getDeleteByIdSql(), id);
	}
	

	public void saveOrUpdate(E entity) {
		Object id = getIdentifierPropertyValue(entity);
		if(ObjectUtils.isNullOrEmptyString(id)) {
			save(entity);
		}else {
			update(entity);
		}
	}

}
