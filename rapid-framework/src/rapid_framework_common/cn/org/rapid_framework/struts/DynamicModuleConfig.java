package cn.org.rapid_framework.struts;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.struts.config.FormBeanConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.impl.ModuleConfigImpl;
import org.apache.commons.lang.StringUtils;

/**
 * 动态的ModuleConfig,将name尝试作为classname装载以创建formBean配置
 * @author badqiu(badqiu@gmail.com)
 */
public class DynamicModuleConfig extends ModuleConfigImpl implements ModuleConfig {
	Map invalidClassName = Collections.synchronizedMap(new WeakHashMap());
	
	public DynamicModuleConfig() {
		super();
	}

	public DynamicModuleConfig(String prefix) {
		super(prefix);
	}

	public FormBeanConfig findFormBeanConfig(String name) {
		FormBeanConfig result = super.findFormBeanConfig(name);
		if(result == null) {
			addFormBeanConfigByClassName(name);
			return super.findFormBeanConfig(name);
		}
		return result;
	}
	
	void addFormBeanConfigByClassName(String className) {
		if(!isValidClassName(className)) {
			return;
		}
		defreeze();
		FormBeanConfig formBeanConfig = new FormBeanConfig();
		formBeanConfig.setName(className);
		formBeanConfig.setType(className);
		addFormBeanConfig(formBeanConfig);
	}

	boolean isValidClassName(String clazzName) {
		if(invalidClassName.containsKey(clazzName)) {
			return false;
		}
		boolean isValidClassName =  isValidClassName0(clazzName);
		int MAX_CACHE_SIZE = 1000;
		if(!isValidClassName && invalidClassName.size() < MAX_CACHE_SIZE) {
			invalidClassName.put(clazzName, null);
		}
		return isValidClassName;
	}

	boolean isValidClassName0(String clazzName) {
		try {
			if(StringUtils.isNotBlank(clazzName)){
				Class.forName(clazzName).newInstance();
				return true;
			}
		}catch(Exception e) {
		}
		return false;
	}
	
	public void defreeze() {
		this.configured = false;
	}
}
