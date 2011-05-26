package cn.org.rapid_framework.spring.beans.factory.support;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultBeanNameGenerator;
import org.springframework.util.StringUtils;

/**
 * 用于生成spring bean名称，可以设置bean需要删除的前缀及后缀
 * @author badqiu
 *
 */
public class SmartBeanNameGenerator extends DefaultBeanNameGenerator{

	String DEFAULT_REMOVE_SUFFIX = "impl";
	String DEFAULT_REMOVE_PERFIX = "";
	
	private String removedSuffix = DEFAULT_REMOVE_SUFFIX;
	private String removedPrefix = DEFAULT_REMOVE_PERFIX;
	
	public void setRemovedSuffix(String removedSuffix) {
		this.removedSuffix = removedSuffix;
	}

	public void setRemovedPrefix(String removedPrefix) {
		this.removedPrefix = removedPrefix;
	}

	public String generateBeanName(BeanDefinition definition,
			BeanDefinitionRegistry registry) {
		String id = super.generateBeanName(definition, registry);
		return generateBeanName(id);
	}

	String generateBeanName(String generatedBeanName) {
		String id = generatedBeanName;
		if(id.toLowerCase().endsWith(removedSuffix.toLowerCase())) {
			id = id.substring(0, id.length() - removedSuffix.length());
		}
		if(id.toLowerCase().startsWith(removedPrefix.toLowerCase())) {
			id = id.substring(removedPrefix.length());
			id = StringUtils.uncapitalize(id);
		}
		return id;
	}
	
}
