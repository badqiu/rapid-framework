package cn.org.rapid_framework.util;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
/**
 * @author badqiu
 */
public class CollectionHelper {
	
	private CollectionHelper(){}
	
	public static LinkedHashSet asLinkedHashSet(Collection c) {
		return (LinkedHashSet)asTargetTypeCollection(c,LinkedHashSet.class);
	}
	
	public static HashSet asHashSet(Collection c) {
		return (HashSet)asTargetTypeCollection(c,HashSet.class);
	}
	
	public static ArrayList asArrayList(Collection c) {
		return (ArrayList)asTargetTypeCollection(c,ArrayList.class);
	}
	
	public static Collection asTargetTypeCollection(Collection c,Class targetCollectionClass) {
		if(targetCollectionClass == null) 
			throw new IllegalArgumentException("'targetCollectionClass' must be not null");
		if(c == null)
			return null;
		if(targetCollectionClass.isInstance(c)) 
			return c;
		
		Collection result = null;
		
		try {
			result = (Collection)targetCollectionClass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException("targetCollectionClass="+targetCollectionClass.getName()+" is not correct!",e);
		}
		
		result.addAll(c);
		return result;
	}

	public static List selectProperty(Iterable from,String propertyName) {
		if(propertyName == null) throw new IllegalArgumentException("'propertyName' must be not null");
		if(from == null) return null;
		
		List result = new ArrayList();
		for(Object o : from) {
			try {
				if(o == null) {
					result.add(null);
				}else {
					Object value = PropertyUtils.getSimpleProperty(o, propertyName);
					result.add(value);
				}
			} catch (IllegalAccessException e) {
				throw new IllegalArgumentException("Cannot get propertyValue by propertyName:"+propertyName+" on class:"+o.getClass(),e);
			} catch (InvocationTargetException e) {
				throw new IllegalArgumentException("Cannot get propertyValue by propertyName:"+propertyName+" on class:"+o.getClass(),e.getTargetException());
			} catch (NoSuchMethodException e) {
				throw new IllegalArgumentException("no such property:"+propertyName+" on class:"+o.getClass(),e);
			}
		}
		return result;
	}
	
	public static Object findSingleObject(Collection c) {
		if(c == null || c.isEmpty())
			return null;
		if(c.size() > 1)
			throw new IllegalStateException("found more than one object when single object requested");
		return c.iterator().next();
	}

	public static double avg(Iterable objects,String propertyName) {
		List<Number> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return avg(propertyValues);
	}
	
	public static double avg(Collection<Number> values) {
		if(values == null) return 0;
		if(values.isEmpty()) return 0;
		return sum(values) / values.size();
	}
	
	public static double sum(Iterable objects,String propertyName) {
		if(objects == null) return 0;
		List<Number> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return sum(propertyValues);
	}

	public static double sum(Iterable<Number> values) {
		if(values == null) return 0;
		
		double sum = 0;
		for(Number num : values) {
			if(num == null) continue;
			sum += num.doubleValue();
		}
		return sum;
	}
	
	public static Object max(Collection objects,String propertyName) {
		List<Comparable> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return Collections.max(propertyValues);
	}

	public static Object min(Collection objects,String propertyName) {
		List<Comparable> propertyValues = CollectionHelper.selectProperty(objects, propertyName);
		return Collections.min(propertyValues);
	}

}
