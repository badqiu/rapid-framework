package cn.org.rapid_framework.lang.enums;
/**
 * 用于枚举需要实现的接口
 * 
 * @author badqiu
 */
public interface EnumBase<K> {

	/** 得到枚举对应的code,一般保存这个code至数据库 */
    public K getCode();
    /** 得到枚举描述 */
    public String getDesc();
    /** 枚举名称 */
    public String name();
    
}
