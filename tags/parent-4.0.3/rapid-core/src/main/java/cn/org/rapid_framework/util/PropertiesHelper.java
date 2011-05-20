package cn.org.rapid_framework.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

/**
 * Properties的操作的工具类,为Properties提供一个代理增加相关工具方法如 getRequiredString(),getInt(),getBoolean()等方法
 * 并可以通过systemPropertiesMode属性指定是否搜索System.getProperty()及System.getenv()来查找值.
 * 默认不搜索系统属性
 * 
 * <pre>
 * 使用1:
 * public class ConnectionUtils {
 *     static Properties properties = new Properties(); 
 *     // ... do load properties 
 *     
 *     // delegate to properties
 * 	   static PropertiesHelper props = new PropertiesHelper(properties);
 *     public static Connection getConnection() {
 *     		// use getRequiredProperty() 
 *     		DriverManager.getConnection(props.getRequiredString("jdbc.url"));
 *     }
 * }
 * 指定是否搜索系统属性:
 * new PropertiesHelper(properties,PropertiesHelper.SYSTEM_PROPERTIES_MODE_OVERRIDE)
 * </pre>
 * @author badqiu
 */
public class PropertiesHelper {
	/** 
	 * 不使用系统属性,这个是默认值
	 **/
	public static final int SYSTEM_PROPERTIES_MODE_NEVER = 0;

	/**
	 * 如果在properties中没有找到属性值,则查找系统属性
	 */
	public static final int SYSTEM_PROPERTIES_MODE_FALLBACK = 1;

	/**
	 * 首先查找系统属性,如果没有找到值,再查找properties,这可以用于系统属性覆盖properties中的值
	 */
	public static final int SYSTEM_PROPERTIES_MODE_OVERRIDE = 2;
	
	private int systemPropertiesMode = SYSTEM_PROPERTIES_MODE_NEVER;
	
	private Properties p;

	public PropertiesHelper(Properties p) {
		setProperties(p);
	}
	
	public PropertiesHelper(Properties p,int systemPropertiesMode) {
		setProperties(p);
		if(systemPropertiesMode != SYSTEM_PROPERTIES_MODE_NEVER && systemPropertiesMode != SYSTEM_PROPERTIES_MODE_FALLBACK && systemPropertiesMode != SYSTEM_PROPERTIES_MODE_OVERRIDE) {
			throw new IllegalArgumentException("error systemPropertiesMode mode:"+systemPropertiesMode);
		}
		this.systemPropertiesMode = systemPropertiesMode;
	}
	
	public Properties getProperties() {
		return p;
	}
	
	public void setProperties(Properties props) {
		if(props == null) throw new IllegalArgumentException("properties must be not null");
		this.p = props;
	}

	/**
	 * 必须存在这个key的值,不然抛 IllegalStateException异常
	 **/
	public String getRequiredProperty(String key) throws IllegalStateException {
		String value = getProperty(key);
		if(isBlankString(value)) {
			throw new IllegalStateException("required property is blank by key="+key);
		}
		return value;
	}
	
	/**
	 * 返回null,如果查值的属性值是blank
	 * @param key
	 * @return
	 */
	public String getNullIfBlank(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return value;
	}
	/**
	 * 返回null,如果查值的属性值是empty
	 * @param key
	 * @return
	 */	
	public String getNullIfEmpty(String key) {
		String value = getProperty(key);
		if(value == null || "".equals(value)) {
			return null;
		}
		return value;
	}
	
	/**
	 * 尝试从System.getProperty(key)及System.getenv(key)得到值
	 * @return
	 */
	public String getAndTryFromSystem(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			value = getSystemProperty(key);
		}
		return value;
	}

	private String getSystemProperty(String key) {
		String value;
		value = System.getProperty(key);
		if(isBlankString(value)) {
			value = System.getenv(key);
		}
		return value;
	}
	
	public Integer getInteger(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return Integer.parseInt(value);
	}
	
	public int getInt(String key,int defaultValue) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return defaultValue;
		}
		return Integer.parseInt(value);
	}
	
	/**
	 * 必须存在这个key的值,不然抛 IllegalStateException异常
	 **/
	public int getRequiredInt(String key) throws IllegalStateException {
		return Integer.parseInt(getRequiredProperty(key));
	}
	
	public Long getLong(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return Long.parseLong(value);
	}
	
	public long getLong(String key,long defaultValue) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return defaultValue;
		}
		return Long.parseLong(value);
	}
	
	/**
	 * 必须存在这个key的值,不然抛 IllegalStateException异常
	 **/
	public long getRequiredLong(String key) throws IllegalStateException {
		return Long.parseLong(getRequiredProperty(key));
	}
	
	public Boolean getBoolean(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return Boolean.parseBoolean(value);
	}
	
	public boolean getBoolean(String key,boolean defaultValue) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return defaultValue;
		}
		return Boolean.parseBoolean(value);
	}
	
	/**
	 * 必须存在这个key的值,不然抛 IllegalStateException异常
	 **/
	public boolean getRequiredBoolean(String key) throws IllegalStateException {
		return Boolean.parseBoolean(getRequiredProperty(key));
	}
	
	public Float getFloat(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return Float.parseFloat(value);
	}
	
	public float getFloat(String key,float defaultValue) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return defaultValue;
		}
		return Float.parseFloat(value);
	}
	
	/**
	 * 必须存在这个key的值,不然抛 IllegalStateException异常
	 **/	
	public float getRequiredFloat(String key) throws IllegalStateException {
		return Float.parseFloat(getRequiredProperty(key));
	}
	
	public Double getDouble(String key) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return null;
		}
		return Double.parseDouble(value);
	}
	
	public double getDouble(String key,double defaultValue) {
		String value = getProperty(key);
		if(isBlankString(value)) {
			return defaultValue;
		}
		return Double.parseDouble(value);
	}
	
	/**
	 * 必须存在这个key的值,不然抛 IllegalStateException异常
	 **/
	public double getRequiredDouble(String key) throws IllegalStateException {
		return Double.parseDouble(getRequiredProperty(key));
	}
	
	public URL getURL(String key) throws IllegalArgumentException {
        try {
            return new URL(getProperty(key));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Property " + key + " must be a valid URL (" + getProperty(key) + ")");
        }
    }
    
    public Object getClassInstance(String key) throws IllegalArgumentException {
        String s = (String) getProperty(key);
        if (s == null || "".equals(s.trim())) {
            throw new IllegalArgumentException("Property " + key + " must be a valid classname  : " + key);
        }
        try {
            return Class.forName(s).newInstance();
        } catch (ClassNotFoundException nfe) {
            throw new IllegalArgumentException(s + ": invalid class name for key " + key, nfe);
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(s + ": class could not be reflected " + s, e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(s + ": class could not be reflected " + s, e);
        }
    }

    public Object getClassInstance(String key, Object defaultinstance)
            throws IllegalArgumentException {
        return (containsKey(key) ? getClassInstance(key) : defaultinstance);
    }
    
    /**
     * 将一个property按"逗号,空格,换行符"分隔,并返回一个String[]数组
     **/
	public String[] getStringArray(String key) {
		String v = getProperty(key);
		if(v == null) {
			return new String[0];
		}else {
		    return org.springframework.util.StringUtils.tokenizeToStringArray(v, ", \t\n\r\f");
		}
	}
    /**
     * 将一个property按"逗号,空格,换行符"分隔,并返回一个int[]数组
     **/
	public int[] getIntArray(String key) {
		return toIntArray(getStringArray(key));
	}

	/**
	 * 得到以某个前缀开始的所有属性,返回的属性值为移除前缀后的属性值.
	 * 
	 * @param prefix
	 * @return
	 */
	public Properties getStartsWithProperties(String prefix) {
		if(prefix == null) throw new IllegalArgumentException("'prefix' must be not null");
		
		Properties props = getProperties();
		
		Properties result = new Properties();
		for(Map.Entry<Object, Object> entry : props.entrySet()) {
			String key = (String)entry.getKey();
			if(key != null && key.startsWith(prefix)) {
				result.put(key.substring(prefix.length()), entry.getValue());
			}
		}
		return result;
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
	    String value = getProperty(key);
	    if(isBlankString(value)) {
	        return defaultValue;
	    }
		return value;
	}

	public String getProperty(String key) {
		String propVal = null;
		if (systemPropertiesMode == SYSTEM_PROPERTIES_MODE_OVERRIDE) {
			propVal = getSystemProperty(key);
		}
		if (propVal == null) {
			propVal = p.getProperty(key);
		}
		if (propVal == null && systemPropertiesMode == SYSTEM_PROPERTIES_MODE_FALLBACK) {
			propVal = getSystemProperty(key);
		}
		return propVal;
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

	/** @deprecated */
	public void save(OutputStream out, String comments) {
		p.save(out, comments);
	}

	public int size() {
		return p.size();
	}

	public void store(OutputStream out, String comments) throws IOException {
		p.store(out, comments);
	}

	public void storeToXML(OutputStream os, String comment, String encoding)
			throws IOException {
		p.storeToXML(os, comment, encoding);
	}

	public void storeToXML(OutputStream os, String comment) throws IOException {
		p.storeToXML(os, comment);
	}

	public Collection<Object> values() {
		return p.values();
	}
	
	public String toString() {
		return p.toString();
	}
	
	public static Properties restoreFromString(String str) {
		if(str == null) return new Properties();
		Properties p = new Properties();
		try {
			p.load(new ByteArrayInputStream(str.getBytes()));
		}catch(IOException e) {
			throw new IllegalStateException("restore properties from String occer error. str:"+str,e);
		}
		return p;
	}

	private static boolean isBlankString(String value) {
		return value == null || "".equals(value.trim());
	}
	
	private static int[] toIntArray(String[] array) {
		int[] result = new int[array.length];
		for(int i = 0; i < array.length; i++) {
			result[i] = Integer.parseInt(array[i]);
		}
		return result;
	}
}
