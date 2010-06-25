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
 * &lt;bean class="cn.org.rapid_framework.beanvalidation.BeanValidatorHolder">
 * 	 &lt;preperty name="validator" ref="validator"/>
 * &lt;/bean>
 * </pre> 
 * @author badqiu
 *
 */
@SuppressWarnings("all")
public class BeanValidatorHolder implements InitializingBean{
	private static Validator validator;

	public void afterPropertiesSet() throws Exception {
		if(validator == null) throw new IllegalStateException("not found JSR303(HibernateValidator) 'validator' for BeanValidatorHolder ");
	}
	
	public void setValidator(Validator v) {
		if(this.validator != null) {
			throw new IllegalStateException("BeanValidatorHolder already holded 'validator'");
		}
		this.validator = v;
	}

	public static Validator getValidator() {
		if(validator == null)
			throw new IllegalStateException("'validator' property is null,BeanValidatorHolder not yet init.");
		return validator;
	}

	public static final <T> Set<ConstraintViolation<T>> validate(T object, Class<?>... groups) {
		return getValidator().validate(object, groups);
	}

	public static final <T> void validate(T object) throws ConstraintViolationException {
		Set constraintViolations = getValidator().validate(object);
		String msg = "validate failure on object:"+object.getClass().getSimpleName();
		throw new ConstraintViolationException(msg,constraintViolations);
	}
	
	public static final <T> Set<ConstraintViolation<T>> validateProperty(T object, String propertyName, Class<?>... groups) {
		return getValidator().validateProperty(object, propertyName,groups);
	}

	public static final <T> void validateProperty(T object, String propertyName) throws ConstraintViolationException {
		Set constraintViolations = getValidator().validateProperty(object, propertyName);
		String msg = "validate property failure on object:"+object.getClass().getSimpleName()+"."+propertyName+"";
		throw new ConstraintViolationException(msg,constraintViolations);
	}
	
	public static final <T> Set<ConstraintViolation<T>> validateValue(Class<T> beanType, String propertyName, Object value, Class<?>... groups) {
		return getValidator().validateValue(beanType, propertyName,value,groups);
	}

	public static final <T> void validateValue(Class<T> beanType, String propertyName, Object value) throws ConstraintViolationException {
		Set constraintViolations = getValidator().validateValue(beanType, propertyName,value);
		String msg = "validate value failure on object:"+beanType.getSimpleName()+"."+propertyName+" value:"+value;
		throw new ConstraintViolationException(msg,constraintViolations);
	}
	
	public static final BeanDescriptor getConstraintsForClass(Class<?> clazz) {
		return getValidator().getConstraintsForClass(clazz);
	}
	
	public static final <T> T unwrap(Class<T> type) {
		return getValidator().unwrap(type);
	}
	
	public static void cleanHolder() {
		validator = null;
	}
}
