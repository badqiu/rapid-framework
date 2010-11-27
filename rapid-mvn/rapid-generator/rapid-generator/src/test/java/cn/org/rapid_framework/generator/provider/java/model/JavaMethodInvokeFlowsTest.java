package cn.org.rapid_framework.generator.provider.java.model;

import java.io.File;
import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.FieldMethodInvocation;
import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.JavaMethodInvokeSequences;
import cn.org.rapid_framework.generator.provider.java.model.testservicebean.BlogServiceBean;
import cn.org.rapid_framework.generator.util.IOHelper;


public class JavaMethodInvokeFlowsTest extends TestCase{
	
	public void test_get_JavaMethodInvokeFlows() {
		JavaClass clazz = new JavaClass(GeneratorFacade.class);
		JavaMethod method = clazz.getMethod("deleteOutRootDir");
		JavaMethodInvokeSequences executor = new JavaMethodInvokeSequences(method,clazz.getMavenJavaSourceFileContent());
		
		executor.execute();
		List<FieldMethodInvocation> invokes = executor.getMethodInvokeSequences();
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,"g.deleteOutRootDir"); 
	}
	
	public void test_get_JavaMethodInvokeFlows_no_execute() {
		JavaClass clazz = new JavaClass(GeneratorFacade.class);
		JavaMethod method = clazz.getMethod("deleteOutRootDir");
		JavaMethodInvokeSequences executor = new JavaMethodInvokeSequences(method,clazz.getMavenJavaSourceFileContent());
		try {
			List<FieldMethodInvocation> invokes = executor.getMethodInvokeSequences();
			fail("还没有执行execute()方法");
		}catch(IllegalStateException e) {
		}
	}
	
	public void test_get_JavaMethodInvokeFlows_with_BlogServiceBean() {
		String file = "E:/svnroot/google_code/rapid-mvn/rapid-generator/rapid-generator/src/test/java/cn/org/rapid_framework/generator/provider/java/model/testservicebean/BlogServiceBean.java";
		test_getMethodInvokeSequences(file,"testSay",BlogServiceBean.class,"esb.say","csb.bb","esb.hello","csb.aa","esb.say","csb.cc");
	}

	public void test_get_JavaMethodInvokeFlows_with_blogjava() {
		String file = "E:/svnroot/google_code/rapid-mvn/rapid-generator/rapid-generator/src/test/java/cn/org/rapid_framework/generator/provider/java/model/testservicebean/BlogServiceBean.java";
		test_getMethodInvokeSequences(file,"blogjava",BlogServiceBean.class,"csb.bb","esb.say","esb.hello","csb.aa","csb.cc");
	}

	public void test_get_JavaMethodInvokeFlows_with_chain_call() {
		String file = "E:/svnroot/google_code/rapid-mvn/rapid-generator/rapid-generator/src/test/java/cn/org/rapid_framework/generator/provider/java/model/testservicebean/BlogServiceBean.java";
		test_getMethodInvokeSequences(file,"chain_call",BlogServiceBean.class,"csb.bb","esb.say","csb.dd","csb.aa","csb.cc");
	}
	
	public void test_getMethodInvokeSequences(String javaSourceFile,String methodName,Class clazz, String... expected) {
		JavaClass javaClazz = new JavaClass(clazz);
		JavaMethod method = javaClazz.getMethod(methodName);
		JavaMethodInvokeSequences executor = new JavaMethodInvokeSequences(method,IOHelper.readFile(new File(javaSourceFile)));
		
		executor.execute();
		List<FieldMethodInvocation> invokes = executor.getMethodInvokeSequences();
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,expected);
	}
	
	private void verifyInvokeFlows(List<FieldMethodInvocation> invokes, String... expected) {
		assertEquals("real:"+invokes+" expected:"+expected,invokes.size(),expected.length);
		for(int i = 0; i < invokes.size(); i++) {
			String methodName = invokes.get(i).getMethod().getMethodName();
			String fieldName = invokes.get(i).getField().getFieldName();
			assertEquals(fieldName+"."+methodName,expected[i]);
//			assertEquals(methodName,expected[i]);
//			assertEquals(fieldName,expected[i]);
		}
	}
	

}
