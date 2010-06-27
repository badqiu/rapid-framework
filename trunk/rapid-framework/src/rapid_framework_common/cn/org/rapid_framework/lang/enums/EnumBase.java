package cn.org.rapid_framework.lang.enums;
/**
 * 用于枚举需要实现的接口
 * 
 * @author badqiu
 */
public interface EnumBase<CODE,DESC> {
    
    public CODE getCode();
    
    public DESC getDesc();
    
    public String name();
    
//    public EnumBase values();
    
}
