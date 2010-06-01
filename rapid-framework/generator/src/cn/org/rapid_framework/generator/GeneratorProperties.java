package cn.org.rapid_framework.generator;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.PropertiesHelper;


/**
 * 用于装载generator.properties文件
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class GeneratorProperties {

	static final String PROPERTIES_FILE_NAME = "generator.properties";
	
	static PropertiesHelper props;
	private GeneratorProperties(){}
	
	public static void loadProperties() {
		try {
			GLogger.println("Load [generator.properties] from classpath");
			props = new PropertiesHelper(PropertiesHelper.loadAllPropertiesFromClassLoader(PROPERTIES_FILE_NAME));
			
			
	        for(Iterator it = props.entrySet().iterator();it.hasNext();) {
                 Map.Entry entry = (Map.Entry)it.next();
                 GLogger.println("[Property] "+entry.getKey()+"="+entry.getValue());
	        }
	        GLogger.println("");
	         
	        GLogger.println("[Auto Replace] [.] => [/] on generator.properties, key=source_key+'_dir', For example: pkg=com.company ==> pkg_dir=com/company  \n");
	        Properties dirProperties = autoReplacePropertiesValue2DirValue(props.getProperties());
	        
            props.getProperties().putAll(dirProperties);
		}catch(IOException e) {
			throw new RuntimeException("Load Properties error",e);
		}
	}
	
	// 自动替换所有value从 com.company 替换为 com/company,并设置key = key+"_dir"后缀
	private static Properties autoReplacePropertiesValue2DirValue(Properties props) {
        Properties autoReplaceProperties = new Properties();
        for(Object key : getProperties().keySet()) {
            String dir_key = key.toString()+"_dir";
            if(props.entrySet().contains(dir_key)) {
                continue;
            }
            String value = props.getProperty(key.toString());
            String dir_value = value.toString().replace('.', '/');
            autoReplaceProperties.put(dir_key, dir_value);           
        }
        return autoReplaceProperties;
    }
	
	public static Properties getProperties() {
		return getHelper().getProperties();
	}
	
	private static PropertiesHelper getHelper() {
		if(props == null)
			loadProperties();
		return props;
	}
	
	public static String getProperty(String key, String defaultValue) {
		return getHelper().getProperty(key, defaultValue);
	}
	
	public static String getProperty(String key) {
		return getHelper().getProperty(key);
	}
	
	public static String getRequiredProperty(String key) {
		return getHelper().getRequiredProperty(key);
	}
	
	public static int getRequiredInt(String key) {
		return getHelper().getRequiredInt(key);
	}
	
	public static boolean getRequiredBoolean(String key) {
		return getHelper().getRequiredBoolean(key);
	}
	
	public static String getNullIfBlank(String key) {
		return getHelper().getNullIfBlank(key);
	}
	
	public static void setProperty(String key,String value) {
		getHelper().setProperty(key, value);
	}
	
	public static void setProperties(Properties v) {
		props = new PropertiesHelper(v);
	}

}
