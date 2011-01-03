package cn.org.rapid_framework.util.holder;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
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
 * &lt;bean class="cn.org.rapid_framework.util.holder.BeanValidatorHolder">
 * 	 &lt;property name="validator" ref="validator"/>
 * &lt;/bean>
 * </pre> 
 * @author badqiu
 *
 */
public class BeanValidatorHolder implements InitializingBean{
	private static Validator validator;

	public void afterPropertiesSet() throws Exception {
		if(validator == null) throw new IllegalStateException("not found JSR303(HibernateValidator) 'validator' for BeanValidatorHolder ");
	}
	
	public void setValidator(Validator v) {
		if(validator != null) {
			throw new IllegalStateException("BeanValidatorHolder already holded 'validator'");
		}
		validator = v;
	}

	private static Validator getRequiredValidator() {
		if(validator == null)
			throw new IllegalStateException("'validator' property is null,BeanValidatorHolder not yet init.");
		return validator;
	}

	public static final <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
		return getRequiredValidator().validate(object, groups);
	}
	
	/**
	 * 验证失败将抛出异常
	 */
	public static final <T> void validateWithException(T object, Class<?>... groups) throws ConstraintViolationException {
		Set constraintViolations = getRequiredValidator().validate(object,groups);
		String msg = "validate failure on object:"+object.getClass().getSimpleName();
		if(!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(msg,constraintViolations);
		}
	}
	
	public static final <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
		return getRequiredValidator().validateProperty(object, propertyName,groups);
	}
	
	/**
	 * 验证失败将抛出异常
	 */
	public static final <T> void validatePropertyWithException(T object, String propertyName, Class<?>... groups) throws ConstraintViolationException {
		Set constraintViolations = getRequiredValidator().validateProperty(object, propertyName);
		String msg = "validate property failure on object:"+object.getClass().getSimpleName()+"."+propertyName+"";
		if(!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(msg,constraintViolations);
		}
	}
	
	public static final <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
		return getRequiredValidator().validateValue(beanType, propertyName,value,groups);
	}

	/**
	 * 验证失败将抛出异常
	 */
	public static final <T> void validateValueWithException(Class<T> beanType, String propertyName, Object value, Class<?>... groups) throws ConstraintViolationException {
		Set constraintViolations = getRequiredValidator().validateValue(beanType, propertyName,value);
		String msg = "validate value failure on object:"+beanType.getSimpleName()+"."+propertyName+" value:"+value;
		if(!constraintViolations.isEmpty()) {
			throw new ConstraintViolationException(msg,constraintViolations);
		}
	}
	
	public static final BeanDescriptor getConstraintsForClass(Class<?> clazz) {
		return getRequiredValidator().getConstraintsForClass(clazz);
	}
	
	public static final <T> T unwrap(Class<T> type) {
		return getRequiredValidator().unwrap(type);
	}
	
	public static void cleanHolder() {
		validator = null;
	}
}
