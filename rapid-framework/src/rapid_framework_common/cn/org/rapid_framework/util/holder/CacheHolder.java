package cn.org.rapid_framework.util.holder;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.InitializingBean;

import cn.org.rapid_framework.cache.Cache;


/**
 * The Cache. Mainly an interface to memcached.
 */
public abstract class CacheHolder implements InitializingBean{
	static Log logger = LogFactory.getLog(CacheHolder.class);
    /**
     * The underlying cache implementation
     */
	private static Cache cache;
    
	public void afterPropertiesSet() throws Exception {
		if(cache == null) throw new BeanCreationException("not found 'cache' for CacheHolder ");
	}
	
    public void setCache(Cache c) {
    	if(this.cache != null) {
			throw new IllegalStateException("CacheHolder already holded 'cache'");
		}
    	cache = c;
    }
    
    /**
     * Add an element only if it doesn't exist.
     * @param key Element key
     * @param value Element value
     * @param expiration Ex: 10s, 3mn, 8h
     */
    public static void add(String key, Object value, String expiration) {
        checkSerializable(value);
        cache.add(key, value, parseDuration(expiration));
    }

    /**
     * Add an element only if it doesn't exist, and return only when 
     * the element is effectivly cached.
     * @param key Element key
     * @param value Element value
     * @param expiration Ex: 10s, 3mn, 8h
     * @return If the element an eventually been cached
     */
    public static boolean safeAdd(String key, Object value, String expiration) {
        checkSerializable(value);
        return cache.safeAdd(key, value, parseDuration(expiration));
    }

    /**
     * Add an element only if it doesn't exist and store it indefinitly.
     * @param key Element key
     * @param value Element value
     */
    public static void add(String key, Object value) {
        checkSerializable(value);
        cache.add(key, value, parseDuration(null));
    }

    /**
     * Set an element.
     * @param key Element key
     * @param value Element value
     * @param expiration Ex: 10s, 3mn, 8h
     */
    public static void set(String key, Object value, String expiration) {
        checkSerializable(value);
        cache.set(key, value, parseDuration(expiration));
    }

    /**
     * Set an element and return only when the element is effectivly cached.
     * @param key Element key
     * @param value Element value
     * @param expiration Ex: 10s, 3mn, 8h
     * @return If the element an eventually been cached
     */
    public static boolean safeSet(String key, Object value, String expiration) {
        checkSerializable(value);
        return cache.safeSet(key, value, parseDuration(expiration));
    }

    /**
     * Set an element and store it indefinitly.
     * @param key Element key
     * @param value Element value
     */
    public static void set(String key, Object value) {
        checkSerializable(value);
        cache.set(key, value, parseDuration(null));
    }

    /**
     * Replace an element only if it already exists.
     * @param key Element key
     * @param value Element value
     * @param expiration Ex: 10s, 3mn, 8h
     */
    public static void replace(String key, Object value, String expiration) {
        checkSerializable(value);
        cache.replace(key, value, parseDuration(expiration));
    }

    /**
     * Replace an element only if it already exists and return only when the 
     * element is effectivly cached.
     * @param key Element key
     * @param value Element value
     * @param expiration Ex: 10s, 3mn, 8h
     * @return If the element an eventually been cached
     */
    public static boolean safeReplace(String key, Object value, String expiration) {
        checkSerializable(value);
        return cache.safeReplace(key, value, parseDuration(expiration));
    }

    /**
     * Replace an element only if it already exists and store it indefinitly.
     * @param key Element key
     * @param value Element value
     */
    public static void replace(String key, Object value) {
        checkSerializable(value);
        cache.replace(key, value, parseDuration(null));
    }

    /**
     * Increment the element value (must be a Number).
     * @param key Element key 
     * @param by The incr value
     * @return The new value
     */
    public static long incr(String key, int by) {
        return cache.incr(key, by);
    }

    /**
     * Increment the element value (must be a Number) by 1.
     * @param key Element key 
     * @return The new value
     */
    public static long incr(String key) {
        return cache.incr(key, 1);
    }

    /**
     * Decrement the element value (must be a Number).
     * @param key Element key 
     * @param by The decr value
     * @return The new value
     */
    public static long decr(String key, int by) {
        return cache.decr(key, by);
    }

    /**
     * Decrement the element value (must be a Number) by 1.
     * @param key Element key 
     * @return The new value
     */
    public static long decr(String key) {
        return cache.decr(key, 1);
    }

    /**
     * Retrieve an object.
     * @param key The element key
     * @return The element value or null
     */
    public static Object get(String key) {
        return cache.get(key);
    }

    /**
     * Bulk retrieve.
     * @param key List of keys
     * @return Map of keys & values
     */
    public static Map<String, Object> get(String... key) {
        return cache.get(key);
    }

    /**
     * Delete an element from the cache.
     * @param key The element key     * 
     */
    public static void delete(String key) {
        cache.delete(key);
    }

    /**
     * Delete an element from the cache and return only when the 
     * element is effectivly removed.
     * @param key The element key
     * @return If the element an eventually been deleted
     */
    public static boolean safeDelete(String key) {
        return cache.safeDelete(key);
    }

    /**
     * Clear all data from cache.
     */
    public static void clear() {
        cache.clear();
    }

    /**
     * Convenient clazz to get a value a class type;
     * @param <T> The needed type
     * @param key The element key
     * @param clazz The type class
     * @return The element value or null
     */
    public static <T> T get(String key, Class<T> clazz) {
        return (T) cache.get(key);
    }

    /**
     * Stop the cache system.
     */
    public static void stop() {
        cache.stop();
    }
    
    
	public static void cleanHolder() {
		cache = null;
	}
	
    /**
     * Utility that check that an object is serializable.
     */
    static void checkSerializable(Object value) {
        if(!(value instanceof Serializable)) {
            throw new IllegalStateException("Cannot cache a non-serializable value of type " + value.getClass().getName(), new NotSerializableException(value.getClass().getName()));
        }
    }

    static Pattern days = Pattern.compile("^([0-9]+)d$");
    static Pattern hours = Pattern.compile("^([0-9]+)h$");
    static Pattern minutes = Pattern.compile("^([0-9]+)mn$");
    static Pattern seconds = Pattern.compile("^([0-9]+)s$");
    /**
     * Parse a duration
     * @param duration 3h, 2mn, 7s
     * @return The number of seconds
     */
    public static int parseDuration(String duration) {
        if (duration == null) {
            return 60 * 60 * 24 * 30;
        }
        int toAdd = -1;
        if (days.matcher(duration).matches()) {
            Matcher matcher = days.matcher(duration);
            matcher.matches();
            toAdd = Integer.parseInt(matcher.group(1)) * (60 * 60) * 24;
        } else if (hours.matcher(duration).matches()) {
            Matcher matcher = hours.matcher(duration);
            matcher.matches();
            toAdd = Integer.parseInt(matcher.group(1)) * (60 * 60);
        } else if (minutes.matcher(duration).matches()) {
            Matcher matcher = minutes.matcher(duration);
            matcher.matches();
            toAdd = Integer.parseInt(matcher.group(1)) * (60);
        } else if (seconds.matcher(duration).matches()) {
            Matcher matcher = seconds.matcher(duration);
            matcher.matches();
            toAdd = Integer.parseInt(matcher.group(1));
        }
        if (toAdd == -1) {
            throw new IllegalArgumentException("Invalid duration pattern : " + duration);
        }
        return toAdd;
    }
}

