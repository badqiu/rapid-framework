package cn.org.rapid_framework.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 与Spring Assert类似功能的类,代码基本从org.springframework.util.Assert复制, 增加如下功能:
 * 1. 可抛出指定的业务异常类,而不是通用的IllegalArgumentException.
 * 2. 函数会返回输入值, 这样就可以把判断语句与赋值语句写成一句 , 与Google Guava中的Preconditions一致.
 * 3. 修改类名,免得一天到晚和org.junit.Assert冲突.
 * 
 * 代码示例:
 * <pre class="code">userName=Asserter.hasText(userName, new IllegalBizArgumentsException(ErrorCode.USERNAME_ERROR));</pre>
 *   
 * @author badqiu
 * @author calvin
 */
public class Asserter {

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression, String message) {
		if (!expression) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalArgumentException</code>
	 * if the test result is <code>false</code>.
	 * <pre class="code">Assert.isTrue(i &gt; 0);</pre>
	 * @param expression a boolean expression
	 * @throws IllegalArgumentException if expression is <code>false</code>
	 */
	public static void isTrue(boolean expression) {
		isTrue(expression, "[Assertion failed] - this expression must be true");
	}

	public static void isTrue(boolean expression, RuntimeException throwIfAssertFail) {
		if (!expression) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is not <code>null</code>
	 */
	public static void isNull(Object object, String message) {
		if (object != null) {
			throw new IllegalArgumentException(message);
		}
	}

	/**
	 * Assert that an object is <code>null</code> .
	 * <pre class="code">Assert.isNull(value);</pre>
	 * @param object the object to check
	 * @throws IllegalArgumentException if the object is not <code>null</code>
	 */
	public static void isNull(Object object) {
		isNull(object, "[Assertion failed] - the object argument must be null");
	}

	public static void isNull(Object object, RuntimeException throwIfAssertFail) {
		if (object != null) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public static <T> T notNull(T object, String message) {
		if (object == null) {
			throw new IllegalArgumentException(message);
		}
		return object;
	}

	/**
	 * Assert that an object is not <code>null</code> .
	 * <pre class="code">Assert.notNull(clazz);</pre>
	 * @param object the object to check
	 * @throws IllegalArgumentException if the object is <code>null</code>
	 */
	public static <T> T notNull(T object) {
		return notNull(object, "[Assertion failed] - this argument is required; it must not be null");
	}

	public static <T> T notNull(T object, RuntimeException throwIfAssertFail) {
		if (object == null) {
			throw throwIfAssertFail;
		}
		return object;
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be <code>null</code> and not the empty String.
	 * <pre class="code">Assert.hasLength(name, "Name must not be empty");</pre>
	 * @param text the String to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasLength
	 */
	public static String hasLength(String text, String message) {
		if (!StringUtils.hasLength(text)) {
			throw new IllegalArgumentException(message);
		}
		return text;
	}

	/**
	 * Assert that the given String is not empty; that is,
	 * it must not be <code>null</code> and not the empty String.
	 * <pre class="code">Assert.hasLength(name);</pre>
	 * @param text the String to check
	 * @see StringUtils#hasLength
	 */
	public static String hasLength(String text) {
		return hasLength(text,
				"[Assertion failed] - this String argument must have length; it must not be null or empty");
	}

	public static String hasLength(String text, RuntimeException throwIfAssertFail) {
		if (!StringUtils.hasLength(text)) {
			throw throwIfAssertFail;
		}
		return text;
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @param message the exception message to use if the assertion fails
	 * @see StringUtils#hasText
	 */
	public static String hasText(String text, String message) {
		if (!StringUtils.hasText(text)) {
			throw new IllegalArgumentException(message);
		}
		return text;
	}

	/**
	 * Assert that the given String has valid text content; that is, it must not
	 * be <code>null</code> and must contain at least one non-whitespace character.
	 * <pre class="code">Assert.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @see StringUtils#hasText
	 */
	public static String hasText(String text) {
		return hasText(text,
				"[Assertion failed] - this String argument must have text; it must not be null, empty, or blank");
	}

	public static String hasText(String text, RuntimeException throwIfAssertFail) {
		if (!StringUtils.hasText(text)) {
			throw throwIfAssertFail;
		}
		return text;
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param message the exception message to use if the assertion fails
	 */
	public static String doesNotContain(String textToSearch, String substring, String message) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			throw new IllegalArgumentException(message);
		}

		return textToSearch;
	}

	/**
	 * Assert that the given text does not contain the given substring.
	 * <pre class="code">Assert.doesNotContain(name, "rod");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 */
	public static String doesNotContain(String textToSearch, String substring) {
		return doesNotContain(textToSearch, substring,
				"[Assertion failed] - this String argument must not contain the substring [" + substring + "]");
	}

	public static String doesNotContain(String textToSearch, String substring, RuntimeException throwIfAssertFail) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring)
				&& textToSearch.indexOf(substring) != -1) {
			throw throwIfAssertFail;
		}

		return textToSearch;
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array is <code>null</code> or has no elements
	 */
	public static Object[] notEmpty(Object[] array, String message) {
		if (ObjectUtils.isEmpty(array)) {
			throw new IllegalArgumentException(message);
		}
		return array;
	}

	/**
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(array);</pre>
	 * @param array the array to check
	 * @throws IllegalArgumentException if the object array is <code>null</code> or has no elements
	 */
	public static Object[] notEmpty(Object[] array) {
		return notEmpty(array, "[Assertion failed] - this array must not be empty: it must contain at least 1 element");
	}

	public static Object[] notEmpty(Object[] array, RuntimeException throwIfAssertFail) {
		if (ObjectUtils.isEmpty(array)) {
			throw throwIfAssertFail;
		}
		return array;
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array, "The array must have non-null elements");</pre>
	 * @param array the array to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the object array contains a <code>null</code> element
	 */
	public static Object[] noNullElements(Object[] array, String message) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					throw new IllegalArgumentException(message);
				}
			}
		}
		return array;
	}

	/**
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Assert.noNullElements(array);</pre>
	 * @param array the array to check
	 * @throws IllegalArgumentException if the object array contains a <code>null</code> element
	 */
	public static Object[] noNullElements(Object[] array) {
		return noNullElements(array, "[Assertion failed] - this array must not contain any null elements");
	}

	public static Object[] noNullElements(Object[] array, RuntimeException throwIfAssertFail) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					throw throwIfAssertFail;
				}
			}
		}
		return array;
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the collection is <code>null</code> or has no elements
	 */
	public static Collection notEmpty(Collection collection, String message) {
		if (CollectionUtils.isEmpty(collection)) {
			throw new IllegalArgumentException(message);
		}
		return collection;
	}

	/**
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Assert.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @throws IllegalArgumentException if the collection is <code>null</code> or has no elements
	 */
	public static Collection notEmpty(Collection collection) {
		return notEmpty(collection,
				"[Assertion failed] - this collection must not be empty: it must contain at least 1 element");
	}

	public static Collection notEmpty(Collection collection, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(collection)) {
			throw throwIfAssertFail;
		}
		return collection;
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalArgumentException if the map is <code>null</code> or has no entries
	 */
	public static Map notEmpty(Map map, String message) {
		if (CollectionUtils.isEmpty(map)) {
			throw new IllegalArgumentException(message);
		}

		return map;
	}

	/**
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Assert.notEmpty(map);</pre>
	 * @param map the map to check
	 * @throws IllegalArgumentException if the map is <code>null</code> or has no entries
	 */
	public static Map notEmpty(Map map) {
		return notEmpty(map, "[Assertion failed] - this map must not be empty; it must contain at least one entry");
	}

	public static Map notEmpty(Map map, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(map)) {
			throw throwIfAssertFail;
		}

		return map;
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param type the type to check against
	 * @param obj the object to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws IllegalArgumentException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static Object isInstanceOf(Class type, Object obj, String message) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw new IllegalArgumentException(message + "Object of class ["
					+ (obj != null ? obj.getClass().getName() : "null") + "] must be an instance of " + type);
		}

		return obj;
	}

	/**
	 * Assert that the provided object is an instance of the provided class.
	 * <pre class="code">Assert.instanceOf(Foo.class, foo);</pre>
	 * @param clazz the required class
	 * @param obj the object to check
	 * @throws IllegalArgumentException if the object is not an instance of clazz
	 * @see Class#isInstance
	 */
	public static Object isInstanceOf(Class clazz, Object obj) {
		return isInstanceOf(clazz, obj, "");
	}

	public static Object isInstanceOf(Class type, Object obj, RuntimeException throwIfAssertFail) {
		notNull(type, "Type to check against must not be null");
		if (!type.isInstance(obj)) {
			throw throwIfAssertFail;
		}

		return obj;
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is <code>true</code>.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check against
	 * @param subType the sub type to check
	 * @param message a message which will be prepended to the message produced by
	 * the function itself, and which may be used to provide context. It should
	 * normally end in a ": " or ". " so that the function generate message looks
	 * ok when prepended to it.
	 * @throws IllegalArgumentException if the classes are not assignable
	 */
	public static void isAssignable(Class superType, Class subType, String message) {
		notNull(superType, "Type to check against must not be null");
		if (subType == null || !superType.isAssignableFrom(subType)) {
			throw new IllegalArgumentException(message + subType + " is not assignable to " + superType);
		}
	}

	/**
	 * Assert that <code>superType.isAssignableFrom(subType)</code> is <code>true</code>.
	 * <pre class="code">Assert.isAssignable(Number.class, myClass);</pre>
	 * @param superType the super type to check
	 * @param subType the sub type to check
	 * @throws IllegalArgumentException if the classes are not assignable
	 */
	public static void isAssignable(Class superType, Class subType) {
		isAssignable(superType, subType, "");
	}

	/**
	 * Assert a boolean expression, throwing <code>IllegalStateException</code>
	 * if the test result is <code>false</code>. Call isTrue if you wish to
	 * throw IllegalArgumentException on an assertion failure.
	 * <pre class="code">Assert.state(id == null, "The id property must not already be initialized");</pre>
	 * @param expression a boolean expression
	 * @param message the exception message to use if the assertion fails
	 * @throws IllegalStateException if expression is <code>false</code>
	 */
	public static void state(boolean expression, String message) {
		if (!expression) {
			throw new IllegalStateException(message);
		}
	}

	/**
	 * Assert a boolean expression, throwing {@link IllegalStateException}
	 * if the test result is <code>false</code>.
	 * <p>Call {@link #isTrue(boolean)} if you wish to
	 * throw {@link IllegalArgumentException} on an assertion failure.
	 * <pre class="code">Assert.state(id == null);</pre>
	 * @param expression a boolean expression
	 * @throws IllegalStateException if the supplied expression is <code>false</code>
	 */
	public static void state(boolean expression) {
		state(expression, "[Assertion failed] - this state invariant must be true");
	}
}
