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
 * <br/>
 * <b>使用</b>
 * <pre>
 * (List)getJdbcTemplate().query(sql, paramMap, new OffsetLimitResultSetExtractor(startRow,pageSize,rowMapper))
 * </pre>
 * 
 * @author badqiu
 *
 */
public class OffsetLimitResultSetExtractor implements ResultSetExtractor {
	private int limit;
	private int offset;
	private RowMapper rowMapper;
	
	public OffsetLimitResultSetExtractor(int offset,int limit,RowMapper rowMapper) {
		Assert.notNull(rowMapper,"'rowMapper' must be not null");
		this.rowMapper = rowMapper;
		this.offset = offset;
		this.limit = limit;
	}

	public Object extractData(ResultSet rs) throws SQLException,DataAccessException {
		List results = new ArrayList(limit > 50 ? 50 : limit);
		
		if (offset > 0) {
			// Skip Results
//	        if (rs.getType() == ResultSet.TYPE_FORWARD_ONLY) {
//	        	for (int i = 0; i < offset; i++) {
//		            if (!rs.next()) {
//		              return new ArrayList(0);
//		            }
//		        }
//	        } else {
	        rs.absolute(offset);
//	        }
		}
		
		int rowNum = 0;
		while (rs.next()) {
			Object row = rowMapper.mapRow(rs, rowNum++);
			results.add(row);
			if((rowNum + 1) >= limit)
				break;
		}
		return results;
	}
	
}