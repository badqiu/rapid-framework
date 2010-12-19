package cn.org.rapid_framework.test.util;

import junit.framework.TestCase;

/**
 * 得到当前被调用的方法
 * 
 * <pre>
 * public class CurrentMethodUtilsTest extends TestCase{
 *    
 *    public void test_java_bb() {
 *        assertEquals("test_java_bb",CurrentMethodUtils.getCurrentMethodName());
 *    }
 *    
 *  }
 * </pre>
 * @author badqiu
 * @version $Id: CurrentMethodUtils.java,v 0.1 2010-6-9 下午07:21:30 zhongxuan Exp $
 */
public class CurrentMethodUtils {

    public static String getCurrentMethodName() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        return stack[1].getMethodName();
    }
    
    public static String getCurrentClassName() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        return stack[1].getClassName();
    }
    
    public static String getCurrentFileName() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        return stack[1].getFileName();
    }
    
    public static int getCurrentLineNumber() {
        StackTraceElement stack[] = (new Throwable()).getStackTrace();
        return stack[1].getLineNumber();
    }
    
}
