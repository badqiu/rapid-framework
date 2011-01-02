package cn.org.rapid_framework.util.holder;

import java.net.URL;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import cn.org.rapid_framework.util.PropertiesHelper;

/**
 * 用于持有Properties,将Propereis变成静态方法使用. PropertiesHolder只能持有一个properties
 * <pre>
 * PropertiesHolder初始化
 * 
 * <b>spring初始化:</b>
 * &lt;bean class="cn.org.rapid_framework.util.holder.PropertiesHolder">
 * 		&lt;property name="properties" ref="applicationProperties"/>
 * &lt;/bean>
 * 
 * <b>java API初始化:</b>
 * new PropertiesHolder().setProperties(cache)
 * 
 * <b>如果需要重新初始化Properties,需要</b>
 * PropertiesHolder.clearHolder();
 * new PropertiesHolder().setProperties(cache)
 * </pre>
 */
public class PropertiesHolder implements InitializingBean{
	private static Log log = LogFactory.getLog(PropertiesHolder.class);
	
	public static PropertiesHelper properies = null;
	
	public void setProperties(Properties properies) {
		Assert.isNull(PropertiesHolder.properies,"PropertiesHolder alreade hold properties");
		PropertiesHolder.properies = new PropertiesHelper(properies);
	}
	
//	/**
//	 * 重新设置properties
//	 * @param properties
//	 */
//	public void resetProperties(Properties properties) {
//		clearHolder();
//		new PropertiesHolder().setProperties(properties);
//	}
	
	/**
	 * 清空 holder,只有清空Holder才可以重新设置 Properties
	 */
	public static void clearHolder() {
		properies = null;
	}

	public static void setProperty(String key,String value) {
		getRequiredHelper().setProperty(key, value);
	}
	
	/** 
	 * get readonly Properties 
	 **/
	public static Properties getProperies() {
		return (Properties)getRequiredHelper().getProperties().clone();
	}

	public static boolean getBoolean(String key, boolean defaultValue) {
		return getRequiredHelper().getBoolean(key, defaultValue);
	}

	public static Boolean getBoolean(String key) {
		return getRequiredHelper().getBoolean(key);
	}

	public static Object getClassInstance(String key, Object defaultinstance)
			throws IllegalArgumentException {
		return getRequiredHelper().getClassInstance(key, defaultinstance);
	}

	public static Object getClassInstance(String key) throws IllegalArgumentException {
		return getRequiredHelper().getClassInstance(key);
	}

	public static double getDouble(String key, double defaultValue) {
		return getRequiredHelper().getDouble(key, defaultValue);
	}

	public static Double getDouble(String key) {
		return getRequiredHelper().getDouble(key);
	}

	public static float getFloat(String key, float defaultValue) {
		return getRequiredHelper().getFloat(key, defaultValue);
	}

	public static Float getFloat(String key) {
		return getRequiredHelper().getFloat(key);
	}

	public static int getInt(String key, int defaultValue) {
		return getRequiredHelper().getInt(key, defaultValue);
	}

	public static int[] getIntArray(String key) {
		return getRequiredHelper().getIntArray(key);
	}

	public static Integer getInteger(String key) {
		return getRequiredHelper().getInteger(key);
	}

	public static long getLong(String key, long defaultValue) {
		return getRequiredHelper().getLong(key, defaultValue);
	}

	public static Long getLong(String key) {
		return getRequiredHelper().getLong(key);
	}

	public static String getNullIfBlank(String key) {
		return getRequiredHelper().getNullIfBlank(key);
	}

	public static String getNullIfEmpty(String key) {
		return getRequiredHelper().getNullIfEmpty(key);
	}

	public static String getProperty(String key, String defaultValue) {
		return getRequiredHelper().getProperty(key, defaultValue);
	}

	public static String getProperty(String key) {
		return getRequiredHelper().getProperty(key);
	}

	public static boolean getRequiredBoolean(String key) throws IllegalStateException {
		return getRequiredHelper().getRequiredBoolean(key);
	}

	public static double getRequiredDouble(String key) throws IllegalStateException {
		return getRequiredHelper().getRequiredDouble(key);
	}

	public static float getRequiredFloat(String key) throws IllegalStateException {
		return getRequiredHelper().getRequiredFloat(key);
	}

	public static int getRequiredInt(String key) throws IllegalStateException {
		return getRequiredHelper().getRequiredInt(key);
	}

	public static long getRequiredLong(String key) throws IllegalStateException {
		return getRequiredHelper().getRequiredLong(key);
	}

	public static String getRequiredString(String key) throws IllegalStateException {
		return getRequiredHelper().getRequiredString(key);
	}

	public static Properties getStartsWithProperties(String prefix) {
		return getRequiredHelper().getStartsWithProperties(prefix);
	}

	public static String[] getStringArray(String key) {
		return getRequiredHelper().getStringArray(key);
	}

	public static URL getURL(String key) throws IllegalArgumentException {
		return getRequiredHelper().getURL(key);
	}
	
	private static PropertiesHelper getRequiredHelper() {
		assertHolderInited();
		return properies;
	}
	
	private static void assertHolderInited() {
		if(properies == null) 
			throw new IllegalStateException("PropertiesHolder.properties must be not null, PropertiesHolder not yet init.");
	}

	public void afterPropertiesSet() throws Exception {
		assertHolderInited();
		log.info("PropertiesHolder holded properties:"+getRequiredHelper().getProperties());
	}
}
