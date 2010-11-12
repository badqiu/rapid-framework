package cn.org.rapid_framework.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 抛出指定的异常,如果assert fail.
 * 如果不想抛出指定的异常,请使用spring提供的Assert
 * 
 * @see Assert
 * @author badqiu
 */
public class Asserter {

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.isTrue(i &gt; 0, "The value must be greater than zero");</pre>
	 * @param expression a boolean expression
	 * @param throwIfAssertFail 
	 */
	public static void isTrue(boolean expression, RuntimeException throwIfAssertFail) {
		if (!expression) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.isNull(value, "The value must be null");</pre>
	 * @param object the object to check
	 * @param throwIfAssertFail 
	 */
	public static void isNull(Object object, RuntimeException throwIfAssertFail) {
		if (object != null) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.notNull(clazz, "The class must not be null");</pre>
	 * @param object the object to check
	 * @param throwIfAssertFail 
	 */
	public static void notNull(Object object, RuntimeException throwIfAssertFail) {
		if (object == null) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.hasLength(name, "Name must not be empty");</pre>
	 * @param text the String to check
	 * @param throwIfAssertFail 
	 * @see StringUtils#hasLength
	 */
	public static void notEmpty(String text, RuntimeException throwIfAssertFail) {
		if (!StringUtils.hasLength(text)) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.hasText(name, "'name' must not be empty");</pre>
	 * @param text the String to check
	 * @param throwIfAssertFail 
	 * @see StringUtils#hasText
	 */
	public static void notBlank(String text, RuntimeException throwIfAssertFail) {
		if (!StringUtils.hasText(text)) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.doesNotContain(name, "rod", "Name must not contain 'rod'");</pre>
	 * @param textToSearch the text to search
	 * @param substring the substring to find within the text
	 * @param throwIfAssertFail 
	 */
	public static void doesNotContain(String textToSearch, String substring, RuntimeException throwIfAssertFail) {
		if (StringUtils.hasLength(textToSearch) && StringUtils.hasLength(substring) &&
				textToSearch.indexOf(substring) != -1) {
			throw throwIfAssertFail;
		}
	}


	/**
	 * 抛出指定的异常,如果assert fail.
	 * Assert that an array has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Asserter.notEmpty(array, "The array must have elements");</pre>
	 * @param array the array to check
	 * @param throwIfAssertFail 
	 */
	public static void notEmpty(Object[] array, RuntimeException throwIfAssertFail) {
		if (ObjectUtils.isEmpty(array)) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * Assert that an array has no null elements.
	 * Note: Does not complain if the array is empty!
	 * <pre class="code">Asserter.noNullElements(array, new BlogException("The array must have non-null elements"));</pre>
	 * @param array the array to check
	 * @param throwIfAssertFail 
	 */
	public static void noNullElements(Object[] array, RuntimeException throwIfAssertFail) {
		if (array != null) {
			for (int i = 0; i < array.length; i++) {
				if (array[i] == null) {
					throw throwIfAssertFail;
				}
			}
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * Assert that a collection has elements; that is, it must not be
	 * <code>null</code> and must have at least one element.
	 * <pre class="code">Asserter.notEmpty(collection, "Collection must have elements");</pre>
	 * @param collection the collection to check
	 * @param throwIfAssertFail 
	 */
	public static void notEmpty(Collection collection, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(collection)) {
			throw throwIfAssertFail;
		}
	}

	/**
	 * 抛出指定的异常,如果assert fail.
	 * Assert that a Map has entries; that is, it must not be <code>null</code>
	 * and must have at least one entry.
	 * <pre class="code">Asserter.notEmpty(map, "Map must have entries");</pre>
	 * @param map the map to check
	 * @param throwIfAssertFail 
	 */
	public static void notEmpty(Map map, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(map)) {
			throw throwIfAssertFail;
		}
	}

}
