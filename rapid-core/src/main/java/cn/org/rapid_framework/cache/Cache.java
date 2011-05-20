package cn.org.rapid_framework.cache;

import java.util.Map;

import cn.org.rapid_framework.util.holder.CacheHolder;

/**
 * A cache interface
 * 
 * expiration的时间单位为秒
 * @see CacheHolder.cache
 */
public interface Cache {
    /** 
     * 添加元素只有当它不存在，有异常则抛出
     **/
    public void add(String key, Object value, int expiration);

    /** 
     * 添加元素只有当它不存在，并忽略掉任何异常
     * @return true则成功，false则发生异常
     **/
    public boolean safeAdd(String key, Object value, int expiration);

    /** 
     * 设置cache值
     * */
    public void set(String key, Object value, int expiration);

    /** 
     * 设置cache值，并忽略掉任何异常
     * @return true则成功，false则发生异常
     **/
    public boolean safeSet(String key, Object value, int expiration);

    /** 
     * 更换一个元素如果它已经存在，有异常则抛出
     **/
    public void replace(String key, Object value, int expiration);

    /** 
     * 更换一个元素如果它已经存在，并忽略掉任何异常
     * @return true则成功，false则发生异常
     **/
    public boolean safeReplace(String key, Object value, int expiration);
    /**
     * 根据key得到一个元素
     * @param key
     * @return
     */
    public Object get(String key);
    /**
     * 批量查找元素
     * @param key
     * @return
     */
    public Map<String, Object> get(String[] keys);
    /**
     * 递增元素的值
     * @param key
     * @param by
     * @return
     */
    public long incr(String key, int by);
    /**
     * 递减的元素的值
     * @param key
     * @param by
     * @return
     */
    public long decr(String key, int by);

    public void clear();
    /**
     * 从缓存中删除一个元素
     * @param key
     */
    public void delete(String key);
    /**
     * 从缓存中删除一个元素,忽略掉任何异常，返回true则删除成功，否则失败
     * @param key
     */
    public boolean safeDelete(String key);

    public void stop();
}
