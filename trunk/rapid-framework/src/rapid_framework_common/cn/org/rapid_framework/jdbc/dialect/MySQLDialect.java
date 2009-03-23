package cn.org.rapid_framework.jdbc.dialect;
/**
 * @author badqiu
 */
public class MySQLDialect implements Dialect{

	public boolean supportsLimitOffset(){
		return true;
	}
	
    public boolean supportsLimit() {   
        return true;   
    }  
    
    public String getLimitString(String sql, int offset, int limit) {   
        if (offset > 0) {   
        	return sql + " limit "+offset+","+limit; 
        } else {   
            return sql + " limit "+limit;
        }   
    }   
  
  
}
