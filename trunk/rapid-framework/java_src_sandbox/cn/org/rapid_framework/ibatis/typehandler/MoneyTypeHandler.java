package cn.org.rapid_framework.ibatis.typehandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import cn.org.rapid_framework.util.Money;

import com.ibatis.sqlmap.engine.type.BaseTypeHandler;
import com.ibatis.sqlmap.engine.type.TypeHandler;
/**
 * 
 * <typeHandlers>
 * 		<typeHandler jdbcType="NUMERIC" javaType="cn.org.rapid_framework.util.Money" callback="cn.org.rapid_framework.ibatis.typehandler.MoneyTypeHandler"/>
 * </typeHandlers>
 * 
 * @author badqiu
 *
 */
public class MoneyTypeHandler extends BaseTypeHandler implements TypeHandler {

	public void setParameter(PreparedStatement ps, int i, Object parameter,
			String jdbcType) throws SQLException {
		ps.setLong(i, ((Money) parameter).getCent());
	}

	public Object getResult(ResultSet rs, String columnName)
			throws SQLException {
		long d = rs.getLong(columnName);
		if (rs.wasNull()) {
			return new Money(0, 0);
		} else {
			return Money.newMoneyWithCent(d);
		}
	}

	public Object getResult(ResultSet rs, int columnIndex) throws SQLException {
		long d = rs.getLong(columnIndex);
		if (rs.wasNull()) {
		    return new Money(0, 0);
		} else {
			return Money.newMoneyWithCent(d);
		}
	}

	public Object getResult(CallableStatement cs, int columnIndex)
			throws SQLException {
		long d = cs.getLong(columnIndex);
		if (cs.wasNull()) {
		    return new Money(0, 0);
		} else {
			return Money.newMoneyWithCent(d);
		}
	}

	public Object valueOf(String s) {
		return new Money(s);
	}
}
