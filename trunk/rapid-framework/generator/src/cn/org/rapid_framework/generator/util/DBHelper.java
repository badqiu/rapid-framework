package cn.org.rapid_framework.generator.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;

import com.mysql.jdbc.ResultSet;

public class DBHelper {
	
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close();
			}catch(Exception e) {
				//ignore
			}
		}
	}
	
	public static void close(PreparedStatement s) {
		if(s != null) {
			try {
				s.close();
			}catch(Exception e) {
				//ignore
			}
		}
	}
	
	public static void close(Statement s) {
		if(s != null) {
			try {
				s.close();
			}catch(Exception e) {
				//ignore
			}
		}
	}
	
	public static void close(ResultSet s) {
		if(s != null) {
			try {
				s.close();
			}catch(Exception e) {
				//ignore
			}
		}
	}
	
	public static void close(Connection conn,ResultSet rs) {
		close(conn);
		close(rs);
	}
	
	public static void close(Connection conn,PreparedStatement ps,ResultSet rs) {
		close(conn);
		close(ps);
		close(rs);
	}
	
	public static void close(Connection conn,Statement s,ResultSet rs) {
		close(conn);
		close(s);
		close(rs);
	}
	
}
