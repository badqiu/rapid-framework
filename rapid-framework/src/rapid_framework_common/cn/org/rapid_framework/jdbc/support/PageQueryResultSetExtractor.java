package cn.org.rapid_framework.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.util.Assert;

/**
 * 用于分页查询的ResultSetExtractor,会移动游标(cursor)至offset,并根据limit参数抽取数据
 * 
 * @author badqiu
 *
 */
public class PageQueryResultSetExtractor implements ResultSetExtractor {
	public static final int NO_ROW_OFFSET = 0;
	public static final int NO_ROW_LIMIT = Integer.MAX_VALUE;

	private int limit;
	private int offset;
	private RowMapper rowMapper;
	
	public PageQueryResultSetExtractor(int offset,int limit,RowMapper rowMapper) {
		Assert.notNull(rowMapper,"'rowMapper' must be not null");
		this.rowMapper = rowMapper;
		this.offset = offset;
		this.limit = limit;
	}

	public Object extractData(ResultSet rs) throws SQLException,DataAccessException {
		List results = new ArrayList(Math.min(200, limit));
		
		if (offset > 0) {
			rs.absolute(offset);
		}
		int rowNum = 0;
		while (rs.next()) {
			Object row = rowMapper.mapRow(rs, rowNum++);
			results.add(row);
			if(limit == (rowNum + 1))
				break;
		}
		return results;
	}
	
}