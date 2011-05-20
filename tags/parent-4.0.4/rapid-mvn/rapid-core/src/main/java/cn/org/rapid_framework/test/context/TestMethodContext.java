package cn.org.rapid_framework.test.context;

import java.lang.reflect.Method;
/**
 * <p>
 * 得到当前正在测试运行的方法名称.与TestMethodContextExecutionListener配合使用
 * </p>
 * 
 * <b>使用说明：</b>
 * 
 * <pre>
 * 
 * @RunWith(SpringJUnit4ClassRunner.class)
 * @TestExecutionListeners(listeners = TestMethodContextExecutionListener.class)
 * public class SomeTest {
 *    @Test
 *    public void test_foo() {
 *        Assert.assertEquals("test_foo", TestMethodContext.getMethodName());
 *    }
 * }
 * </pre>
 */
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
