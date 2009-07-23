package cn.org.rapid_framework.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

public class Partition {
	public static final char DEFAULT_SEPERATOR = '/';
	
	String[] keys;
	char seperator;
	
	public Partition(String... keys) {
		this(DEFAULT_SEPERATOR,keys);
	}
	
	public Partition(char seperator, String... keys) {
		super();
		this.seperator = seperator;
		this.keys = keys;
	}

	public String getPartitionString(Map row) {
		return getPartitionString(row,seperator,keys);
	}
	
	public String[] getKeys() {
		return keys;
	}

	public char getSeperator() {
		return seperator;
	}

	public Map parseRartition(String partitionString) {
		return parseRartition(partitionString, seperator, keys);
	}
	
	private static String getPartitionString(Map row,char seperator,String... keys) {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < keys.length; i++) {
			String key = keys[i];
			Object value = row.get(key);
			if(i == keys.length - 1) {
				sb.append(value);
			}else {
				sb.append(value).append(seperator);
			}
		}
		return sb.toString();
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
		final Partition other = (Partition) obj;
		if (!Arrays.equals(keys, other.keys))
			return false;
		if (seperator != other.seperator)
			return false;
		return true;
	}

	
	
}
