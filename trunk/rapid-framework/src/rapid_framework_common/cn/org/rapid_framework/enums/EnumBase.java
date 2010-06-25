package cn.org.rapid_framework.enums;
/**
 * 用于枚举需要实现的接口
 * 
 * @author badqiu
 */
public interface EnumBase<K,V> {
    
    public K getCode();
    
    public V getDesc();
    
    public String name();
    
}
