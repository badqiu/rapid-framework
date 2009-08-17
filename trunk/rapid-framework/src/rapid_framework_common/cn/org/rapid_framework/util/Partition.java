package cn.org.rapid_framework.util;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 数据分区的工具类，可以用于数据根据不同的key分区保存。
 * 或是叫基于列的存储。
 * <pre>
 * 		Partition p = new Partition("d:/temp/partition/",new String[]{"date","app"});
 *		
 *		for(Map row : rows) {
 *			String path = p.getPartitionString(row);
 *			System.out.println("[write] savePath:"+path+" data:"+row);
 *			System.out.println("[read] partitionMap:"+p.parseRartition(path)+" from path:["+path+"] data:"+row);
 *		}
 * </pre>
 * @author badqiu
 *
 */
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
	
	public List<String> query(String where,final String[] partitionLines) {
		return query(where,new Iterator<String>() {
			private int index = 0;
			public boolean hasNext() {
				return  index < partitionLines.length;
			}
			public String next() {
				return partitionLines[index++];
			}
			public void remove() {
				throw new UnsupportedOperationException("remove() not support");
			}
		});
	}
	
	/**
	 * 支持根据where条件查询，where语法为freemarker的条件表达式讲法
	 * @param where freemarker的条件表达式讲法
	 * @param partitionLines 
	 * @return
	 * 
	 * TODO 支持数据类型，如date,int,string
	 */
	public List<String> query(String where,Iterator<String> partitionLines) {
		if(partitionLines == null) throw new IllegalArgumentException("'partitionLines' must be not null");
		if(where == null) throw new IllegalArgumentException("'where' string must be not null");
		
		//<#if user = "Big Joe">true</#if>
		String freemarkerExpression = String.format("<#if "+where+">true</#if>");
		List results = new ArrayList();
		Template template = newFreemarkerTemplate(freemarkerExpression);
		while(partitionLines.hasNext()) {
			String line = partitionLines.next();
			Map row = parseRartition(line);
			StringWriter out = new StringWriter(4);
			try {
				template.process(row, out);
				if("true".equals(out.toString())) {
					results.add(line);
				}
				//System.out.println("query:"+freemarkerExpression+"row:"+row+" result:"+out.toString());
			} catch (TemplateException e) {
				throw new IllegalStateException("process query error,where="+where + " line:"+line,e);
			} catch (IOException e) {
				throw new IllegalStateException("process query error,where="+where + " line:"+line,e);
			}
		}
		return results;
	}

	private Template newFreemarkerTemplate(String freemarkerExpression) {
		try {
			return new Template(freemarkerExpression,new StringReader(freemarkerExpression),new Configuration());
		} catch (IOException e) {
			throw new IllegalArgumentException(e);
		}
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
			Object value = getProperty(map, key);
			if(i == keys.length - 1) {
				sb.append(value);
			}else {
				sb.append(value).append(seperator);
			}
		}
		return sb.toString();
	}
	
	private static Object getProperty(Object obj,String key) {
		if(obj instanceof Map) {
			Map map = (Map)obj;
			return map.get(key);
		}else {
			try {
				return PropertyUtils.getProperty(obj, key);
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
	
	public String toString() {
		return String.format("prefix:%s seperator:%s keys:%s", prefix,seperator,Arrays.toString(keys));
	}
}
