package cn.org.rapid_framework.util;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.metadata.ConstraintDescriptor;

import org.springframework.beans.NotReadablePropertyException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
/**
 * 
 * 将Set<ConstraintViolation>转换为 spring Errors的工具类
 * 
 * @author badqiu
 *
 */
public class ValidationErrorsUtils {
	
	/**
	 * 将exception.getConstraintViolations()填充至 Errors
	 */	
	public static void convert(ConstraintViolationException exception,Errors errors) {
		ValidationErrorsUtils.convert(exception.getConstraintViolations(),errors);
	}
	
	/**
	 * 将constraintViolations填充至 Errors
	 */
	public static void convert(Set<ConstraintViolation<?>> constraintViolations,Errors errors) {
		
		for (ConstraintViolation<?> violation : constraintViolations) {
			String field = violation.getPropertyPath().toString();
			FieldError fieldError = errors.getFieldError(field);
			if (fieldError == null || !fieldError.isBindingFailure()) {
				try {
					errors.rejectValue(field,
							violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
							getArgumentsForConstraint(errors.getObjectName(), field, violation.getConstraintDescriptor()),
							violation.getMessage());
				}
				catch (NotReadablePropertyException ex) {
					throw new IllegalStateException("JSR-303 validated property '" + field +
							"' does not have a corresponding accessor for Spring data binding - " +
							"check your DataBinder's configuration (bean property versus direct field access)", ex);
				}
			}
		}
	}
	
	/**
	 * Return FieldError arguments for a validation error on the given field.
	 * Invoked for each violated constraint.
	 * <p>The default implementation returns a first argument indicating the field name
	 * (of type DefaultMessageSourceResolvable, with "objectName.field" and "field" as codes).
	 * Afterwards, it adds all actual constraint annotation attributes (i.e. excluding
	 * "message", "groups" and "payload") in alphabetical order of their attribute names.
	 * <p>Can be overridden to e.g. add further attributes from the constraint descriptor.
	 * @param objectName the name of the target object
	 * @param field the field that caused the binding error
	 * @param descriptor the JSR-303 constraint descriptor
	 * @return the Object array that represents the FieldError arguments
	 * @see org.springframework.validation.FieldError#getArguments
	 * @see org.springframework.context.support.DefaultMessageSourceResolvable
	 * @see org.springframework.validation.DefaultBindingErrorProcessor#getArgumentsForBindError
	 */
	protected static Object[] getArgumentsForConstraint(String objectName, String field, ConstraintDescriptor<?> descriptor) {
		List<Object> arguments = new LinkedList<Object>();
		String[] codes = new String[] {objectName + Errors.NESTED_PATH_SEPARATOR + field, field};
		arguments.add(new DefaultMessageSourceResolvable(codes, field));
		// Using a TreeMap for alphabetical ordering of attribute names
		Map<String, Object> attributesToExpose = new TreeMap<String, Object>();
		for (Map.Entry<String, Object> entry : descriptor.getAttributes().entrySet()) {
			String attributeName = entry.getKey();
			Object attributeValue = entry.getValue();
			if (!internalAnnotationAttributes.contains(attributeName)) {
				attributesToExpose.put(attributeName, attributeValue);
			}
		}
		arguments.addAll(attributesToExpose.values());
		return arguments.toArray(new Object[arguments.size()]);
	}
	
	private static final Set<String> internalAnnotationAttributes = new HashSet<String>(3);

	static {
		internalAnnotationAttributes.add("message");
		internalAnnotationAttributes.add("groups");
		internalAnnotationAttributes.add("payload");
	}
}
