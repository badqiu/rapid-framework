package cn.org.rapid_framework.page.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import cn.org.rapid_framework.page.Page;
import cn.org.rapid_framework.page.PageRequest;

/**
 * Jdbc分页信息，需要Jdk 1.5的支持
 * @author badqiu
 * @see Page
 */
@Deprecated
public class JdbcScrollPage extends Page //implements Page
{

    /**
     * 构建JdbcPage对象，完成JDBC的ResultSet分页处理
     *
     * @param rs         ResultSet对象，ResultSet必须是可滚动的数据集，可以通过以下的Statement执行查询。
     *                   Statement stmt = conn.createStatement( ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
     * @param pageNumber 当前页编码，从1开始，如果传的值为Integer.MAX_VALUE表示获取最后一页。
     *                   如果你不知道最后一页编码，传Integer.MAX_VALUE即可。如果当前页超过总页数，也表示最后一页。
     *                   这两种情况将重新更改当前页的页码，为最后一页编码。
     * @param pageSize   每一页显示的条目数
     * @throws SQLException 
     */
    public JdbcScrollPage(ResultSet rs,RowMapper rowMapper,int pageNumber, int pageSize) throws SQLException
    {
    	super(pageNumber,pageSize,moveToLast(rs));
        this.result = extractDataList(rs,rowMapper,pageNumber,pageSize);
    }

    public JdbcScrollPage(ResultSet rs,int totalCount,RowMapper rowMapper,int pageNumber, int pageSize) throws SQLException
    {
    	super(pageNumber,pageSize,totalCount);
        this.result = extractDataList(rs,rowMapper,pageNumber,pageSize);
    }
    
    public JdbcScrollPage(ResultSet rs,int totalCount,RowMapper rowMapper,PageRequest p) throws SQLException{
    	this(rs,totalCount,rowMapper,p.getPageNumber(),p.getPageSize());
    }
    
    public JdbcScrollPage(ResultSet rs,RowMapper rowMapper,PageRequest p) throws SQLException{
    	this(rs,rowMapper,p.getPageNumber(),p.getPageSize());
    }

	private static int moveToLast(ResultSet rs) throws SQLException {
		rs.last();
		return rs.getRow();
	}
    
	private List extractDataList(ResultSet rs, RowMapper rowMapper,int pageNumber, int pageSize) throws SQLException {
		List result = new ArrayList(pageSize);
		if (pageNumber > 1) {
			rs.absolute(((pageNumber - 1) * pageSize));
		}
		int rowNum = 0;
		while (rs.next()) {
			Object row = rowMapper.mapRow(rs, rowNum++);
			result.add(row);
			if(pageSize == (rowNum + 1))
				break;
		}
		return result;
	}
	
}

