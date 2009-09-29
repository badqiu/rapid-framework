package cn.org.rapid_framework.jdbc.support;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

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
	
	public PageQueryResultSetExtractor(RowMapper rowMapper, int offset,int limit) {
		super();
		this.rowMapper = rowMapper;
		this.offset = offset;
		this.limit = limit;
	}

	public Object extractData(ResultSet rs) throws SQLException,DataAccessException {
		List result = null;
		if(limit == PageQueryResultSetExtractor.NO_ROW_LIMIT) {
			result = new ArrayList();
		}else {
			result = new ArrayList(limit);
		}
		
		if (offset > 0) {
			rs.absolute(offset);
		}
		int rowNum = 0;
		while (rs.next()) {
			Object row = rowMapper.mapRow(rs, rowNum++);
			result.add(row);
			if(limit == (rowNum + 1))
				break;
		}
		return result;
	}
	
}