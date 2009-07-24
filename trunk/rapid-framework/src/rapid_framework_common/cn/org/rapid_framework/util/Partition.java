package cn.org.rapid_framework.util;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

public class Partition implements Serializable{
	public static final char DEFAULT_SEPERATOR = '/';
	
	String[] keys;
	char seperator;
	String prefix;
	
	public Partition(String... keys) {
		this("",DEFAULT_SEPERATOR,keys);
	}
	
	public Partition(String prefix,String[] keys) {
		this(prefix,DEFAULT_SEPERATOR,keys);
	}
	
	public Partition(String prefix,char seperator, String... keys) {
		super();
		if(prefix == null) throw new IllegalArgumentException("'prefix' must be not null");
		this.prefix = prefix;
		this.seperator = seperator;
		this.keys = keys;
	}

	public String getPartitionString(Map map) {
		return prefix+getPartitionString(map,seperator,keys);
	}
	
	public String getPartitionString(Object row) {
		return prefix+getPartitionString(row,seperator,keys);
	}
	
	public String getPrefix() {
		return prefix;
	}

	public String[] getKeys() {
		return keys;
	}

	public char getSeperator() {
		return seperator;
	}

	public Map parseRartition(String partitionString) {
		if(partitionString.startsWith(prefix)) {
			return parseRartition(partitionString.substring(prefix.length()), seperator, keys);
		}else {
			throw new IllegalArgumentException(" partitionString:["+partitionString+"] must be startWith prefix:"+prefix);
		}
	}
	
	private static String getPartitionString(Object map,char seperator,String... keys) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < keys.length; i++) {
			String key = keys[i];
			Object value = getSimpleProperty(map, key);
			if(i == keys.length - 1) {
				sb.append(value);
			}else {
				sb.append(value).append(seperator);
			}
		}
		return sb.toString();
	}
	
	private static Object getSimpleProperty(Object obj,String key) {
		if(obj instanceof Map) {
			Map map = (Map)obj;
			return map.get(key);
		}else {
			try {
				return PropertyUtils.getSimpleProperty(obj, key);
			} catch (Exception e) {
				throw new IllegalStateException("cannot get property:"+key+" on class:"+obj.getClass(),e);
			}
		}
	}
	
	private static Map parseRartition(String partitionString,char seperator,String...keys ) {
		if(partitionString == null) return new HashMap();
		
		String[] values = StringUtils.split(partitionString, seperator);
		return ArrayUtils.toMap(values, keys);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(keys);
		result = prime * result + ((prefix == null) ? 0 : prefix.hashCode());
		result = prime * result + seperator;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Partition other = (Partition) obj;
		if (!Arrays.equals(keys, other.keys))
			return false;
		if (prefix == null) {
			if (other.prefix != null)
				return false;
		} else if (!prefix.equals(other.prefix))
			return false;
		if (seperator != other.seperator)
			return false;
		return true;
	}

}
