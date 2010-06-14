package cn.org.rapid_framework.holder;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.metadata.BeanDescriptor;

import org.springframework.beans.factory.InitializingBean;
/**
 * 用于持有JSR303 Validator(Hibernate Validator),使调用Validator可以当静态方法使用.
 * 
 * <pre>
 * static 方法调用:
 * BeanValidatorHolder.validate(object);
 * </pre>
 * <pre>
 * spring配置:
 * &lt;bean class="cn.org.rapid_framework.beanvalidation.BeanValidatorHolder">
 * 	 &lt;preperty name="validator" ref="validator"/>
 * &lt;/bean>
 * </pre> 
 * @author badqiu
 *
 */
public class BeanValidatorHolder implements InitializingBean{
	private static Validator validator;

	public void afterPropertiesSet() throws Exception {
		if(validator == null) throw new IllegalStateException("not found JSR303 'validator' for BeanValidatorHolder ");
	}
	
	public void setValidator(Validator validator) {
		if(this.validator != null) {
			throw new IllegalStateException("BeanValidatorHolder already holded 'validator'");
		}
		this.validator = validator;
	}

	public static Validator getRequiredValidator() {
		if(validator == null)
			throw new IllegalStateException("'validator' property is null,BeanValidatorHolder not yet init.");
		return validator;
	}
	
	public static Validator getValidator() {
		return validator;
	}

	public static final <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
		return getRequiredValidator().validate(object, groups);
	}

	public static final <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
		return getRequiredValidator().validateProperty(object, propertyName,groups);
	}

	public static final <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
		return getRequiredValidator().validateValue(beanType, propertyName,groups);
	}

	public static final BeanDescriptor getConstraintsForClass(Class<?> clazz) {
		return getRequiredValidator().getConstraintsForClass(clazz);
	}
	
	public static final <T> T unwrap(Class<T> type) {
		return getRequiredValidator().unwrap(type);
	}

}
