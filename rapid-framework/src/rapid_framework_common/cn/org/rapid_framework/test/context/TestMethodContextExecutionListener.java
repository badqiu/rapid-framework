package cn.org.rapid_framework.test.context;

import java.lang.reflect.Method;

import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;

public class TestMethodContextExecutionListener  implements TestExecutionListener {
    
    public void beforeTestMethod(TestContext testContext) throws Exception {
        Method m = testContext.getTestMethod();
        TestMethodContext.setMethod(m);
    }
    
    public void afterTestMethod(TestContext testContext) throws Exception {
        TestMethodContext.clear();
    }

    public void afterTestClass(TestContext context) throws Exception {
    }

    public void beforeTestClass(TestContext context) throws Exception {
    }

    public void prepareTestInstance(TestContext context) throws Exception {
    }
}
