package net.sf.log4jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.log4jdbc.ConnectionSpy;

/**
 * 
 * spring dataSource config:
 * <pre>
 * &lt;bean id="dataSource" class="net.sf.log4jdbc.DataSourceSpy">
 *     &lt;property name="realDataSource" ref="realDataSource"/>
 *     &lt;property name="enabled" value="true"/>
 * &lt;/bean>
 * </pre>
 * 
 * log4j.properties
 * <pre>
 * log4j.logger.jdbc.sqlonly=WARN
 * log4j.logger.jdbc.sqltiming=DEBUG
 * log4j.logger.jdbc.audit=WARN
 * log4j.logger.jdbc.resultset=WARN
 * log4j.logger.jdbc.connection=WARN
 * </pre>
 * 
 * 
 * @author badqiu
 *
 */
public class DataSourceSpy implements DataSource{
    private DataSource realDataSource;
    private RdbmsSpecifics rdbmsSpecifics = null;
    
    public DataSourceSpy() {
    }
    
    public DataSourceSpy(DataSource realDataSource) {
        setRealDataSource(realDataSource);
    }


    public void setRealDataSource(DataSource realDataSource) {
        this.realDataSource = realDataSource;
    }    
    
    private RdbmsSpecifics getRdbmsSpecifics() {
        if(rdbmsSpecifics == null) {
            try {
                Connection conn = realDataSource.getConnection();
                rdbmsSpecifics = DriverSpy.getRdbmsSpecifics(conn);
                conn.close();
            }catch(Exception e) {
                System.err.println("error on getRdbmsSpecifics(Connection conn),caused:"+e.toString());
            }
        }
        return rdbmsSpecifics;
    }

    public Connection getConnection() throws SQLException {
        if(SpyLogFactory.getSpyLogDelegator().isJdbcLoggingEnabled()) {
            return new ConnectionSpy(this.realDataSource.getConnection(),getRdbmsSpecifics());
        }else {
            return this.realDataSource.getConnection();
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        if(SpyLogFactory.getSpyLogDelegator().isJdbcLoggingEnabled()) {
            return new ConnectionSpy(realDataSource.getConnection(username, password),getRdbmsSpecifics());
        }else {
            return realDataSource.getConnection(username, password);
        }
    }

    public int getLoginTimeout() throws SQLException {
        return realDataSource.getLoginTimeout();
    }

    public PrintWriter getLogWriter() throws SQLException {
        return realDataSource.getLogWriter();
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        realDataSource.setLoginTimeout(seconds);
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        realDataSource.setLogWriter(out);
    }

}
