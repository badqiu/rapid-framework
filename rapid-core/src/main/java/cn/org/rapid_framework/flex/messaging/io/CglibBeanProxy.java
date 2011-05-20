package cn.org.rapid_framework.flex.messaging.io;

import java.io.Serializable;
import java.util.Map;
import java.util.WeakHashMap;

import cn.org.rapid_framework.util.HibernateBeanSerializerProxy;

import flex.messaging.io.BeanProxy;

public class CglibBeanProxy extends BeanProxy{

	private static final long serialVersionUID = 2939121420898802988L;
	static {
		addIgnoreProperty(HibernateBeanSerializerProxy.class, "callbacks");
	}
	@Override
	public String getAlias(Object instance) {
		return removeCglibClassSuffix(super.getAlias(instance));
	}

	static String removeCglibClassSuffix(String alias) {
		String result = alias; ;
		int indexOf = result.indexOf("$$EnhancerByCGLIB$$");
		if(indexOf != -1) {
			result = result.substring(0,indexOf);
		}
		return result;
	}

}
