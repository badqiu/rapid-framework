package cn.org.rapid_framework.generator.provider.java.model;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.FieldMethodInvocation;
import cn.org.rapid_framework.generator.provider.java.model.JavaMethod.JavaMethodInvokeSequencesParser;
import cn.org.rapid_framework.generator.provider.java.model.testservicebean.AbstractBlogServiceBean;
import cn.org.rapid_framework.generator.provider.java.model.testservicebean.BlogServiceBean;
import cn.org.rapid_framework.generator.util.IOHelper;


public class JavaMethodInvokeFlowsTest extends TestCase{
	
	public void test_get_JavaMethodInvokeFlows() {
		JavaClass clazz = new JavaClass(GeneratorFacade.class);
		JavaMethod method = clazz.getMethod("deleteOutRootDir");
		JavaMethodInvokeSequencesParser executor = new JavaMethodInvokeSequencesParser(method,clazz.getMavenJavaSourceFileContent());
		
		executor.execute();
		List<FieldMethodInvocation> invokes = executor.getMethodInvokeSequences();
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,"generator.deleteOutRootDir"); 
	}
	
	public void test_get_JavaMethodInvokeFlows_no_execute() {
		JavaClass clazz = new JavaClass(GeneratorFacade.class);
		JavaMethod method = clazz.getMethod("deleteOutRootDir");
		JavaMethodInvokeSequencesParser executor = new JavaMethodInvokeSequencesParser(method,clazz.getMavenJavaSourceFileContent());
		try {
			List<FieldMethodInvocation> invokes = executor.getMethodInvokeSequences();
			fail("还没有执行execute()方法");
		}catch(IllegalStateException e) {
		}
	}
	
	public void test_get_JavaMethodInvokeFlows_with_BlogServiceBean() {
		test_getMethodInvokeSequences("testSay",BlogServiceBean.class,"esb.say","csb.bb","esb.hello","csb.aa","esb.say","csb.cc");
	}

	public void test_get_JavaMethodInvokeFlows_with_blogjava() {
		test_getMethodInvokeSequences("blogjava",BlogServiceBean.class,"csb.bb","esb.say","esb.hello","csb.aa","csb.cc");
	}

	public void test_get_JavaMethodInvokeFlows_with_chain_call() {
		test_getMethodInvokeSequences("chain_call",BlogServiceBean.class,"csb.bb","esb.say","csb.dd","csb.aa","csb.cc");
	}
	
	
	public void test_get_JavaMethodInvokeFlows_with_array_args() {
        test_getMethodInvokeSequences("array_args",BlogServiceBean.class,"csb.bb","csb.aa");
    }
	
	public void test_get_JavaMethodInvokeFlows_with_call_other_method() {
        //TODO JavaMethodInvokeFlows 方法调用其它方法还没有修正
//		test_getMethodInvokeSequences("call_other_method",BlogServiceBean.class,"csb.bb","esb.say","csb.dd","csb.aa","csb.cc","csb.bb","esb.say","esb.hello","csb.aa","csb.cc");
    }
	
	public void test_get_JavaMethodInvokeFlows_with_SuperClass() {
		test_getMethodInvokeSequences("absInvoke",AbstractBlogServiceBean.class,"topicServiceBean.a1","topicServiceBean.a2");
		 //TODO JavaMethodInvokeFlows test_get_JavaMethodInvokeFlows_with_SuperClass还没有修正
//		try {
//		test_getMethodInvokeSequences("absInvoke",BlogServiceBean.class,"emailServiceBean.say","emailServiceBean.hello");
//		fail("shoud not found method");
//		}catch(Exception e){
//		}
	}
	
	public void test_getMethodInvokeSequences(String methodName,Class clazz, String... expected) {
		JavaClass javaClazz = new JavaClass(clazz);
		JavaMethod method = javaClazz.getMethod(methodName);
		JavaMethodInvokeSequencesParser executor = new JavaMethodInvokeSequencesParser(method,IOHelper.readFile(new File(javaClazz.getMavenJavaSourceFile())));
		
		executor.execute();
		List<FieldMethodInvocation> invokes = executor.getMethodInvokeSequences();
		System.out.println(invokes);
		
		verifyInvokeFlows(invokes,expected);
	}
	
	private void verifyInvokeFlows(List<FieldMethodInvocation> invokes, String... expected) {
		assertEquals("real:"+invokes+" expected:"+Arrays.toString(expected),invokes.size(),expected.length);
		for(int i = 0; i < invokes.size(); i++) {
			String methodName = invokes.get(i).getMethod().getMethodName();
			String fieldName = invokes.get(i).getField().getFieldName();
			assertEquals(fieldName+"."+methodName,expected[i]);
//			assertEquals(methodName,expected[i]);
//			assertEquals(fieldName,expected[i]);
		}
	}
	

}
