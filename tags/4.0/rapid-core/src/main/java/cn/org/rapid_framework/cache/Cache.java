package cn.org.rapid_framework.cache;

import java.util.Map;

/**
 * A cache interface
 * 
 * expiration的时间单位为秒
 * @see CacheHolder.cache
 */
public interface Cache {
	/** expiration的时间单位为秒 */
    public void add(String key, Object value, int expiration);

    /** expiration的时间单位为秒 */
    public boolean safeAdd(String key, Object value, int expiration);

    /** expiration的时间单位为秒 */
    public void set(String key, Object value, int expiration);

    /** expiration的时间单位为秒 */
    public boolean safeSet(String key, Object value, int expiration);

    /** expiration的时间单位为秒 */
    public void replace(String key, Object value, int expiration);

    /** expiration的时间单位为秒 */
    public boolean safeReplace(String key, Object value, int expiration);

    public Object get(String key);

    public Map<String, Object> get(String[] keys);

    public long incr(String key, int by);

    public long decr(String key, int by);

    public void clear();

    public void delete(String key);

    public boolean safeDelete(String key);

    public void stop();
}
