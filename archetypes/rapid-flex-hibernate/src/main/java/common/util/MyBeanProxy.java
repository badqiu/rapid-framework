package common.util;

import flex.messaging.io.BeanProxy;


/**
 * Uses Bean introspection to collect the properties for a given instance.
 * 对blazeds的序列化问题进行修复： by creating a BeanProxy class which overrides setValue
 * and getValue. In there we return NaN to the flex side if the value is a
 * Number and null, and we return null to the Java side if it's a Double and
 * NaN. Job done:
 * 
 * @author hunhun
 */
public class MyBeanProxy extends BeanProxy {

	@Override
	public void setValue(Object instance, String propertyName, Object value) {
		if ((value instanceof Double)) {
			Double doubleValue = (Double) value;
			if (doubleValue != null && doubleValue.isNaN()) {
				super.setValue(instance, propertyName, null);
			}
		}else{
		      super.setValue(instance, propertyName, value);
	        }
	}

	@Override
	public Object getValue(Object obj, String propertyName) {
		final Class classType = super.getType(obj, propertyName);
		if (isNumber(classType) && super.getValue(obj, propertyName) == null) {
			return Double.NaN;
		}
		return super.getValue(obj, propertyName);
	}
	
	public boolean isNumber(Class classType){
		return (Number.class).isAssignableFrom(classType);
	}

}
