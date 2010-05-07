import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;

import cn.org.rapid_framework.page.SortInfo;


/**
 * Ognl工具类，主要是为了在ognl表达式访问静态方法时可以减少长长的类名称编写
 * Ognl访问静态方法的表达式为: @class@method(args)
 * 
 * 示例使用: 
 * <pre>
 * 	&lt;if test="@Ognl@isNotEmpty(userId)">
 *		and user_id = #{userId}
 *	&lt;/if>
 * </pre>
 * @author badqiu
 *
 */
public class Ognl {
	
	/**
	 * 可以用于判断 Map,Collection,String,Array是否为空
	 * @param o
	 * @return
	 */
	public static boolean isEmpty(Object o) throws IllegalArgumentException {
		if(o == null) return true;

		if(o instanceof String) {
			if(((String)o).length() == 0){
				return true;
			}
		} else if(o instanceof Collection) {
			if(((Collection)o).isEmpty()){
				return true;
			}
		} else if(o.getClass().isArray()) {
			if(((Object[])o).length == 0){
				return true;
			}
		} else if(o instanceof Map) {
			if(((Map)o).isEmpty()){
				return true;
			}
		}else {
			throw new IllegalArgumentException("Illegal argument type,must be : Map,Collection,Array,String. but was:"+o.getClass());
		}

		return false;
	}
	
	/**
	 * 可以用于判断 Map,Collection,String,Array是否不为空
	 * @param c
	 * @return
	 */	
	public static boolean isNotEmpty(Object o) {
		return !isEmpty(o);
	}
	
	public static boolean isNotBlank(Object o) {
		return !isBlank(o);
	}
	
	public static boolean isNumber(Object o) {
		if(o == null) return false;
		if(o instanceof Number) {
			return true;
		}
		if(o instanceof String) {
			try {
				Double.parseDouble((String)o);
				return true;
			}catch(NumberFormatException e) {
				return false;
			}
		}
		return false;
	}
	
	public static boolean isBlank(Object o) {
		if(o == null)
			return true;
		if(o instanceof String) {
			String str = (String)o;
			return isBlank(str);
		}
		return false;
	}

	public static boolean isBlank(String str) {
		if(str == null || str.length() == 0) {
			return true;
		}
		
		for (int i = 0; i < str.length(); i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	/**
	 * 用于验证那些列可以排序
	 * 
	 * <pre>
	 * 示例: 
	 * checkOrderby("user asc,pwd desc","user,pwd") 正常
	 * checkOrderby("user asc,pwd desc","user"),pwd不能排序,将抛出异常
	 * </pre>
	 * @param orderby 需要验证的order by字符串
	 * @param validSortColumns 可以排序的列
	 * @throws DataAccessException
	 */
	public static void checkOrderby(String orderby,String validSortColumns) throws DataAccessException{
		if(orderby == null) return;
		if(validSortColumns == null) return;
		
		List<SortInfo> infos = SortInfo.parseSortColumns(orderby);
		String[] conditionsArray = validSortColumns.split(",");
		for(SortInfo info : infos) {
			String columnName = info.getColumnName();
			if(!isPass(conditionsArray, info, columnName)) {
				throw new InvalidDataAccessApiUsageException("orderby:["+orderby+"] is invalid, only can orderby:"+validSortColumns);
			}
		}
	}

	private static boolean isPass(String[] conditionsArray, SortInfo info, String columnName) {
		for(String condition : conditionsArray) {
			if(condition.equalsIgnoreCase(info.getColumnName())) {
				return true;
			}
		}
		return false;
	}
	
}
