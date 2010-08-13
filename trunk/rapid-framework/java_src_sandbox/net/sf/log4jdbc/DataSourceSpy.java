package net.sf.log4jdbc;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import net.sf.log4jdbc.ConnectionSpy;

public class DataSourceSpy implements DataSource{
    private DataSource realDataSource;
    private RdbmsSpecifics rdbmsSpecifics = null;
    private boolean enabled = Boolean.parseBoolean(System.getProperty("log4jdbc.enabled","false"));
    
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
    
    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Connection getConnection() throws SQLException {
        if(enabled) {
            return new ConnectionSpy(this.realDataSource.getConnection(),getRdbmsSpecifics());
        }else {
            return this.realDataSource.getConnection();
        }
    }

    public Connection getConnection(String username, String password) throws SQLException {
        if(enabled) {
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

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        return realDataSource.isWrapperFor(iface);
    }

    public void setLoginTimeout(int seconds) throws SQLException {
        realDataSource.setLoginTimeout(seconds);
    }

    public void setLogWriter(PrintWriter out) throws SQLException {
        realDataSource.setLogWriter(out);
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        return realDataSource.unwrap(iface);
    }
   

}
