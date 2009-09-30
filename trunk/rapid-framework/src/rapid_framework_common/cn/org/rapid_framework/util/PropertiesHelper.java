package cn.org.rapid_framework.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Properties的装饰器工具类,为Properties提供一个代理增加相关工具方法如 getRequiredProperty(),getInt(),getBoolean()等方法
 * 
 * <pre>
 * 使用:
 * public class ConnectionUtils {
 *     static Properties properties = new Properties(); 
 *     // ... do load properties 
 *     
 *     // delegate to properties
 * 	   static PropertiesHelper props = new PropertiesHelper(properties);
 *     public static Connection getConnection() {
 *     		// use getRequiredProperty() 
 *     		DriverManager.getConnection(props.getRequiredProperty("jdbc.url"));
 *     }
 * }
 * </pre>
 * @author badqiu
 */
public class PropertiesHelper {
	Properties p;

	public PropertiesHelper(Properties p) {
		if(p == null) throw new IllegalArgumentException("properties must be not null");
		this.p = p;
	}
	
	public Properties getProperties() {
		return p;
	}
	
	public String getRequiredString(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			throw new IllegalStateException("required property is blank by key="+key);
		}
		return value;
	}
	
	public String getNullIfBlank(String key) {
		String value = getProperties().getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return value;
	}
	
	public String getNullIfEmpty(String key) {
		String value = getProperties().getProperty(key);
		if(value == null || "".equals(value)) {
			return null;
		}
		return value;
	}
	
	public String getAndTryFromSystem(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			value = System.getProperty(key);
			if(isBlankString(value)) {
				value = System.getenv(key);
			}
		}
		return value;
	}
	
	public Integer getInteger(String key) {
		String v = getProperty(key);
		if(v == null){
			return null;
		}
		return Integer.parseInt(v);
	}
	
	public int getInt(String key,int defaultValue) {
		if(getProperty(key) == null) {
			return defaultValue;
		}
		return Integer.parseInt(getRequiredString(key));
	}
	
	public int getRequiredInt(String key) {
		return Integer.parseInt(getRequiredString(key));
	}
	
	public Long getLong(String key) {
		if(getProperty(key) == null) {
			return null;
		}
		return Long.parseLong(getRequiredString(key));
	}
	
	public long getLong(String key,long defaultValue) {
		if(getProperty(key) == null) {
			return defaultValue;
		}
		return Long.parseLong(getRequiredString(key));
	}
	
	public Long getRequiredLong(String key) {
		return Long.parseLong(getRequiredString(key));
	}
	
	public Boolean getBoolean(String key) {
		if(getProperty(key) == null) {
			return null;
		}
		return Boolean.parseBoolean(getRequiredString(key));
	}
	
	public boolean getBoolean(String key,boolean defaultValue) {
		if(getProperty(key) == null) {
			return defaultValue;
		}
		return Boolean.parseBoolean(getRequiredString(key));
	}
	
	public boolean getRequiredBoolean(String key) {
		return Boolean.parseBoolean(getRequiredString(key));
	}
	
	public Float getFloat(String key) {
		if(getProperty(key) == null) {
			return null;
		}
		return Float.parseFloat(getRequiredString(key));
	}
	
	public float getFloat(String key,float defaultValue) {
		if(getProperty(key) == null) {
			return defaultValue;
		}
		return Float.parseFloat(getRequiredString(key));
	}
	
	public Float getRequiredFloat(String key) {
		return Float.parseFloat(getRequiredString(key));
	}
	
	public Double getDouble(String key) {
		if(getProperty(key) == null) {
			return null;
		}
		return Double.parseDouble(getRequiredString(key));
	}
	
	public double getDouble(String key,double defaultValue) {
		if(getProperty(key) == null) {
			return defaultValue;
		}
		return Double.parseDouble(getRequiredString(key));
	}
	
	public Double getRequiredDouble(String key) {
		return Double.parseDouble(getRequiredString(key));
	}
	
	/** setProperty(String key,int value) ... start */
	
	public Object setProperty(String key,int value) {
		return setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key,long value) {
		return setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key,float value) {
		return setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key,double value) {
		return setProperty(key, String.valueOf(value));
	}
	
	public Object setProperty(String key,boolean value) {
		return setProperty(key, String.valueOf(value));
	}
	
	/** delegate method start */
	
	public String getProperty(String key, String defaultValue) {
		return p.getProperty(key, defaultValue);
	}

	public String getProperty(String key) {
		return p.getProperty(key);
	}

	public Object setProperty(String key,String value) {
		return p.setProperty(key, value);
	}

	public void clear() {
		p.clear();
	}

	public Set<Entry<Object, Object>> entrySet() {
		return p.entrySet();
	}

	public Enumeration<?> propertyNames() {
		return p.propertyNames();
	}

	public boolean contains(Object value) {
		return p.contains(value);
	}

	public boolean containsKey(Object key) {
		return p.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return p.containsValue(value);
	}

	public Enumeration<Object> elements() {
		return p.elements();
	}

	public Object get(Object key) {
		return p.get(key);
	}

	public boolean isEmpty() {
		return p.isEmpty();
	}

	public Enumeration<Object> keys() {
		return p.keys();
	}

	public Set<Object> keySet() {
		return p.keySet();
	}

	public void list(PrintStream out) {
		p.list(out);
	}

	public void list(PrintWriter out) {
		p.list(out);
	}

	public void load(InputStream inStream) throws IOException {
		p.load(inStream);
	}

	public void load(Reader reader) throws IOException {
		p.load(reader);
	}

	public void loadFromXML(InputStream in) throws IOException,
			InvalidPropertiesFormatException {
		p.loadFromXML(in);
	}

	public Object put(Object key, Object value) {
		return p.put(key, value);
	}

	public void putAll(Map<? extends Object, ? extends Object> t) {
		p.putAll(t);
	}

	public Object remove(Object key) {
		return p.remove(key);
	}

	public void save(OutputStream out, String comments) {
		p.save(out, comments);
	}

	public int size() {
		return p.size();
	}

	public void store(OutputStream out, String comments) throws IOException {
		p.store(out, comments);
	}

	public void store(Writer writer, String comments) throws IOException {
		p.store(writer, comments);
	}

	public void storeToXML(OutputStream os, String comment, String encoding)
			throws IOException {
		p.storeToXML(os, comment, encoding);
	}

	public void storeToXML(OutputStream os, String comment) throws IOException {
		p.storeToXML(os, comment);
	}

	public Set<String> stringPropertyNames() {
		return p.stringPropertyNames();
	}

	public Collection<Object> values() {
		return p.values();
	}
	
	private static boolean isBlankString(String value) {
		return value == null || "".equals(value.trim());
	}
}
