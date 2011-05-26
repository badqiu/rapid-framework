package common.util;

import java.io.Serializable;

import flex.messaging.io.BeanProxy;
import flex.messaging.io.PropertyProxyRegistry;

/**
 * 
 * 
 * @author hunhun
 * @date 2011-2-4
 * @email hhlai1990@gmail.com
 */
public class FlexFacadeUtil {

	/**
	 * 对balzeds全局注册所有Serializable.class的对象,当flex<=>java序列化时对只读属性gerXX()仍序列化
	 */	
	static {	
		
		System.out.println("common.util.FlexFacadeUtil:对balzeds全局注册所有Serializable.class的对象,当flex<=>java序列化时对只读属性gerXX()仍序列化");
		BeanProxy beanProxy=new MyBeanProxy();
		beanProxy.setIncludeReadOnly(true);
		PropertyProxyRegistry.getRegistry().register(Serializable.class, beanProxy);
	}
}
