package cn.org.rapid_framework.util;

import java.util.Collection;
import java.util.Map;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

/**
 * 与Spring Assert相同功能的类,
 * 不同的是如果assert fail将抛出指定的异常. 
 * 这样在做业务检查时，可以根据该类抛出指定的业务异常。
 * 
 * 如果不想抛出指定的异常,请使用spring提供的Assert
 * 
 * @see Assert
 * @author badqiu
 */
public class Asserter {

	/**
	 * 抛出指定的异常,如果assert fail.
	 * <pre class="code">Asserter.isTrue(age &gt; 0, new IllegalBizArgumentsException(ErrorCode.AGE_ERROR));</pre>
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
	 * <pre class="code">Asserter.isNull(username, new IllegalBizArgumentsException(ErrorCode.USERNAME_ERROR));</pre>
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
	 * <pre class="code">Asserter.notNull(username, new IllegalBizArgumentsException(ErrorCode.USERNAME_ERROR));</pre>
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
	 * <pre class="code">Asserter.notEmpty(username, new IllegalBizArgumentsException(ErrorCode.USERNAME_ERROR));</pre>
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
	 * <pre class="code">Asserter.notBlank(username, new IllegalBizArgumentsException(ErrorCode.USERNAME_ERROR));</pre>
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
	 * <pre class="code">Asserter.doesNotContain(name, "rod",, new IllegalBizArgumentsException(ErrorCode.NAME_MUST_NOT_CONTAIN_ROD_ERROR));</pre>
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
	 * <pre class="code">Asserter.notEmpty(array, new IllegalBizArgumentsException(ErrorCode.ARRAY_MUST_HAVE_ELEMENTS_ERROR));</pre>
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
	 * <pre class="code">Asserter.noNullElements(array, new IllegalBizArgumentsException(ErrorCode.ARRAY_MUST_HAVE_NOT_NULL_ELEMENTS_ERROR));</pre>
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
	 * <pre class="code">Asserter.notEmpty(userList, new IllegalBizArgumentsException(ErrorCode.USER_LIST_MUST_NOT_EMPTY_ERROR));</pre>
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
	 * <pre class="code">Asserter.notEmpty(userMap, new IllegalBizArgumentsException(ErrorCode.USER_MAP_MUST_NOT_EMPTY_ERROR));</pre>
	 * @param map the map to check
	 * @param throwIfAssertFail 
	 */
	public static void notEmpty(Map map, RuntimeException throwIfAssertFail) {
		if (CollectionUtils.isEmpty(map)) {
			throw throwIfAssertFail;
		}
	}

}
