package cn.org.rapid_framework.cache.aop.config;

import org.springframework.beans.factory.xml.NamespaceHandler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;
import org.w3c.dom.Element;

public class MethodCacheNamespaceHandler extends NamespaceHandlerSupport implements NamespaceHandler  {

	static final String METHOD_CACHE_ATTRIBUTE = "method-cache";

	static final String DEFAULT_METHOD_CACHE_BEAN_NAME = "methodCache";

	static String getMethodCacheName(Element element) {
		return (element.hasAttribute(METHOD_CACHE_ATTRIBUTE) ?
				element.getAttribute(METHOD_CACHE_ATTRIBUTE) : DEFAULT_METHOD_CACHE_BEAN_NAME);
	}


	public void init() {
		registerBeanDefinitionParser("annotation-driven", new AnnotationDrivenBeanDefinitionParser());
	}

}