package cn.org.rapid_framework.generator.provider.java.model;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.JavaMethodInvokeFlows;
import cn.org.rapid_framework.generator.provider.java.model.testservicebean.BlogServiceBean;
import cn.org.rapid_framework.generator.util.IOHelper;


public class JavaMethodInvokeFlowsTest extends TestCase{
	
	public void test_get_JavaMethodInvokeFlows() {
		JavaClass clazz = new JavaClass(GeneratorFacade.class);
		JavaMethod method = clazz.getMethod("deleteOutRootDir");
		JavaMethodInvokeFlows executor = new JavaMethodInvokeFlows(method.method,clazz.getMavenJavaSourceFileContent(),clazz);
		
		executor.execute();
		List<JavaMethod> invokes = executor.methodInvokeFlows;
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,"deleteOutRootDir"); //FIXME 还没有相关联的 field
	}
	
	public void test_get_JavaMethodInvokeFlows_with_BlogServiceBean() {
		JavaClass clazz = new JavaClass(BlogServiceBean.class);
		JavaMethod method = clazz.getMethod("testSay");
		String file = "E:/svnroot/google_code/rapid-mvn/rapid-generator/rapid-generator/src/test/java/cn/org/rapid_framework/generator/provider/java/model/testservicebean/BlogServiceBean.java";
		JavaMethodInvokeFlows executor = new JavaMethodInvokeFlows(method.method,IOHelper.readFile(new File(file)),clazz);
		
		executor.execute();
		List<JavaMethod> invokes = executor.methodInvokeFlows;
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,"say","bb","hello","aa","say","cc");
	}

	public void test_get_JavaMethodInvokeFlows_with_blogjava() {
		JavaClass clazz = new JavaClass(BlogServiceBean.class);
		JavaMethod method = clazz.getMethod("blogjava");
		String file = "E:/svnroot/google_code/rapid-mvn/rapid-generator/rapid-generator/src/test/java/cn/org/rapid_framework/generator/provider/java/model/testservicebean/BlogServiceBean.java";
		JavaMethodInvokeFlows executor = new JavaMethodInvokeFlows(method.method,IOHelper.readFile(new File(file)),clazz);
		
		executor.execute();
		List<JavaMethod> invokes = executor.methodInvokeFlows;
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,"bb","say","hello","aa","cc");
	}

	public void test_get_JavaMethodInvokeFlows_with_chain_call() {
		JavaClass clazz = new JavaClass(BlogServiceBean.class);
		JavaMethod method = clazz.getMethod("chain_call");
		String file = "E:/svnroot/google_code/rapid-mvn/rapid-generator/rapid-generator/src/test/java/cn/org/rapid_framework/generator/provider/java/model/testservicebean/BlogServiceBean.java";
		JavaMethodInvokeFlows executor = new JavaMethodInvokeFlows(method.method,IOHelper.readFile(new File(file)),clazz);
		
		executor.execute();
		List<JavaMethod> invokes = executor.methodInvokeFlows;
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,"bb","say","hello","aa","cc");
	}
	
	private void verifyInvokeFlows(List<JavaMethod> invokes, String... expected) {
		assertEquals("real:"+invokes+" expected:"+expected,invokes.size(),expected.length);
		for(int i = 0; i < invokes.size(); i++) {
			assertEquals(invokes.get(i).getMethodName(),expected[i]);
		}
	}
	

}
