package cn.org.rapid_framework.generator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;


/**
 * 用于装载generator.properties文件
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class GeneratorProperties {

	static Properties props;
	
	private GeneratorProperties(){}
	
	private static void loadProperties() {
		try {
			System.out.println("Load [generator.properties] from classpath");
			props = loadAllPropertiesByClassLoader("generator.properties");
			String basepackage = props.getProperty("basepackage");
			String basepackage_dir = basepackage.replace('.', '/');
			
			props.put("basepackage_dir", basepackage_dir);
			
			for(Iterator it = props.entrySet().iterator();it.hasNext();) {
				Map.Entry entry = (Map.Entry)it.next();
				System.out.println("[Property] "+entry.getKey()+"="+entry.getValue());
			}
			
			System.out.println();
			
		}catch(IOException e) {
			throw new RuntimeException("Load Properties error",e);
		}
	}
	
	public static Properties getProperties() {
		if(props == null)
			loadProperties();
		return props;
	}
	
	public static String getProperty(String key, String defaultValue) {
		return getProperties().getProperty(key, defaultValue);
	}
	
	public static String getNullIfBlankProperty(String key) {
		String value = getProperties().getProperty(key);
		if(value == null || "".equals(value.trim())) {
			return null;
		}
		return value;
	}

	public static String getProperty(String key) {
		return getProperties().getProperty(key);
	}
	
	public static String getRequiredProperty(String key) {
		String value = getProperty(key);
		if(value == null || "".equals(value.trim())) {
			throw new IllegalStateException("required property is blank by key="+key);
		}
		return value;
	}

	public static Properties loadAllPropertiesByClassLoader(String resourceName) throws IOException {
		Properties properties = new Properties();
		Enumeration urls = GeneratorProperties.class.getClassLoader().getResources(resourceName);
		while (urls.hasMoreElements()) {
			URL url = (URL) urls.nextElement();
			InputStream input = null;
			try {
				URLConnection con = url.openConnection();
				con.setUseCaches(false);
				input = con.getInputStream();
				properties.load(input);
			}
			finally {
				if (input != null) {
					input.close();
				}
			}
		}
		return properties;
	}
}
