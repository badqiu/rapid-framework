package cn.org.rapid_framework.test.context;

import java.lang.reflect.Method;

public class TestMethodContext {
    
    static ThreadLocal<Method> context = new ThreadLocal<Method>();

    public static String getMethodName() {
        return getMethod() == null ? null : getMethod().getName();
    }
    
    public static Method getMethod() {
        return context.get();
    }
    
    public static void setMethod(Method m) {
        context.set(m);
    }
    
    public static void clear() {
        context.set(null);
    }
    
}
