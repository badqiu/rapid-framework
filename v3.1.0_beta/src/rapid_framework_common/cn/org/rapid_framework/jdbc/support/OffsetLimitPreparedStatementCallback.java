package cn.org.rapid_framework.jdbc.support;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;

/**
 * 用于分页查询的PreparedStatementCallback,会移动游标(cursor)至offset,并根据limit参数抽取数据
 * 执行查询前会设置PreparedStatement.setMaxRows(limit);
 * 
 * @author badqiu
 *
 */
public class OffsetLimitPreparedStatementCallback  implements PreparedStatementCallback{
	private int offset;
	private int limit;
	private RowMapper rowMapper;
	
	public OffsetLimitPreparedStatementCallback(int offset, int limit,RowMapper rowMapper) {
		Assert.notNull(rowMapper,"'rowMapper' must be not null");
		this.offset = offset;
		this.limit = limit;
		this.rowMapper = rowMapper;
	}

	public Object doInPreparedStatement(PreparedStatement ps)throws SQLException, DataAccessException {
		ps.setMaxRows(limit);
		ResultSet rs = null;
		try {
			rs = ps.executeQuery();
			OffsetLimitResultSetExtractor rse = new OffsetLimitResultSetExtractor(offset,limit,rowMapper);
			return (List)rse.extractData(rs);
		}finally {
			JdbcUtils.closeResultSet(rs);
		}
	}
}
