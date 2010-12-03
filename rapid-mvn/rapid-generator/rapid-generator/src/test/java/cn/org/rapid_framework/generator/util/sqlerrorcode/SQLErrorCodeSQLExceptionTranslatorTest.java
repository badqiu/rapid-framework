package cn.org.rapid_framework.generator.util.sqlerrorcode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import cn.org.rapid_framework.generator.GeneratorTestCase;
import cn.org.rapid_framework.generator.provider.db.DataSourceProvider;
import cn.org.rapid_framework.generator.provider.db.sql.SqlFactory;

public class SQLErrorCodeSQLExceptionTranslatorTest extends GeneratorTestCase {
    
    public void testTranslator() {
        DataSource ds = DataSourceProvider.getDataSource();
        
        SQLErrorCodeSQLExceptionTranslator translator = SQLErrorCodeSQLExceptionTranslator.getSQLErrorCodeSQLExceptionTranslator(ds);
        try {
            execute("insert into role_permission(permissoin_id,role_id) values (123,123)");
            fail();
        }catch(SQLException e) {
            e.printStackTrace();
            System.err.println("ErrorCode:"+e.getErrorCode()+" SQLState:"+e.getSQLState());
            assertTrue(translator.isDataIntegrityViolation(e));
        }
        new SqlFactory().parseSql("insert into role_permission(permissoin_id,role_id) values (123,123)");
        new SqlFactory().parseSql("insert into user_info(user_id,username,password) values (123,'123',123)");
    }
    
    public void execute(String sql) throws SQLException {
        DataSource ds = DataSourceProvider.getDataSource();
        Connection conn = ds.getConnection();
        PreparedStatement ps = conn.prepareStatement(sql);
        if(ps.execute()) {
        }
        ps.close();
        conn.close();
    }
    
    public void test() {
        SQLErrorCodeSQLExceptionTranslator translator = newTranslator("Oracle");
        
        assertTrue(translator.isDataIntegrityViolation(new SQLException("","",12899)));
    }

    private SQLErrorCodeSQLExceptionTranslator newTranslator(String dbName) {
        SQLErrorCodeSQLExceptionTranslator translator = new SQLErrorCodeSQLExceptionTranslator();
        SQLErrorCodes codes = SQLErrorCodesFactory.getInstance().getErrorCodes(dbName);
        assertNotNull(codes);
        translator.setSqlErrorCodes(codes);
        return translator;
    }
}
