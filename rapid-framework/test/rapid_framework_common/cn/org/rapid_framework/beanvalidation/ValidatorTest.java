package cn.org.rapid_framework.beanvalidation;

import java.util.HashMap;

import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

import junit.framework.TestCase;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.validation.MapBindingResult;
import org.springframework.validation.Validator;

import cn.org.rapid_framework.util.holder.BeanValidatorHolder;

public class ValidatorTest extends TestCase{

	Validator validator = null;
	public void setUp() {
		ApplicationContext ac = new ClassPathXmlApplicationContext("fortest_beanvalidation/applicationContext-validator.xml");
		validator = ac.getBean("validator",Validator.class);
		new BeanValidatorHolder().setValidator((javax.validation.Validator)validator);
	}
	
	public void tearDown() {
		BeanValidatorHolder.cleanHolder();
	}
	
	public void test() {

		MapBindingResult errors = new MapBindingResult(new HashMap(), "vb");
		
		validator.validate(new BeanValidationTestBean(), errors );
		assertEquals("请输入值",errors.getFieldError("age").getDefaultMessage());
		assertEquals("请输入值",errors.getFieldError("blog").getDefaultMessage());
		assertEquals("必须小于或等于 100",errors.getFieldError("height").getDefaultMessage());
		assertEquals("长度必须在 10 至 20 之间",errors.getFieldError("password").getDefaultMessage());
		System.out.println(errors);
//		MessageInterpolator mi = (MessageInterpolator)ac.getBean("messageInterpolator");
//		MessageInterpolatorContext context = new MessageInterpolatorContext(constraintDescriptor,"blog");
//		assertEquals("大小必须在 {min} 至 {max} 之间",mi.interpolate("javax.validation.constraints.Size.message", context));
	} 
	
	public void testBeanValidatorHoldervalidate() {
		try {
			BeanValidatorHolder.validate(new BeanValidationTestBean());
		fail();
		}catch(ConstraintViolationException e) {
			assertEquals("validate failure on object:BeanValidationTestBean",e.getMessage());
		}
	}

	public void testBeanValidatorHoldervalidateProperty() {
		try {
			BeanValidatorHolder.validateProperty(new BeanValidationTestBean(),"username");
		fail();
		}catch(ConstraintViolationException e) {
			assertEquals("validate property failure on object:BeanValidationTestBean.username",e.getMessage());
		}
	}

	public void testBeanValidatorHoldervalidateValue() {
		try {
			BeanValidatorHolder.validateValue(BeanValidationTestBean.class,"username","   ");
		fail();
		}catch(ConstraintViolationException e) {
			assertEquals("validate value failure on object:BeanValidationTestBean.username value:   ",e.getMessage());
		}
	}
	
	public static class BeanValidationTestBean {
		@NotBlank
		private String username;
		@Length(min=10,max=20)
		private String password = "1";
		@NotNull
		private Integer age;
		@NotNull
		private Integer sex;
		@NotBlank @Length(min=10,max=20)
		private String blog;
		@Max(100)
		private int height = 200;
	}
}
