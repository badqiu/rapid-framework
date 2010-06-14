package cn.org.rapid_framework.holder;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
/**
 * 用于持有Spring Validator,使调用Validator可以当静态方法使用.
 * 
 * <pre>
 * static 方法调用:
 * SpringValidatorHolder.validate(object);
 * </pre>
 * <pre>
 * spring配置:
 * &lt;bean class="cn.org.rapid_framework.beanvalidation.SpringValidatorHolder">
 * 	 &lt;preperty name="validator" ref="validator"/>
 * &lt;/bean>
 * </pre> 
 * @author badqiu
 *
 */
public class SpringValidatorHolder implements InitializingBean{
	private static Validator validator;

	public void afterPropertiesSet() throws Exception {
		if(validator == null) throw new BeanCreationException("not found spring 'validator' for SpringValidatorHolder ");
	}
	
	public void setValidator(Validator validator) {
		if(this.validator != null) {
			throw new IllegalStateException("SpringValidatorHolder already holded 'validator'");
		}
		this.validator = validator;
	}

	public static Validator getRequiredValidator() {
		if(validator == null)
			throw new IllegalStateException("'validator' property is null,SpringValidatorHolder not yet init.");
		return validator;
	}
	
	public static Validator getValidator() {
		return validator;
	}

	public static boolean supports(Class<?> type) {
		return validator.supports(type);
	}

	public static void validate(Object object, Errors errors) {
		validator.validate(object, errors);
	}

	public static void validate(Object object) throws BindException {
		BeanPropertyBindingResult errors = new BeanPropertyBindingResult(object, object.getClass().getSimpleName(), true);
		validator.validate(object, errors);
		if(errors.hasErrors()) {
			throw new BindException(errors);
		}
	}
	
}
