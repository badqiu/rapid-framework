package cn.org.rapid_framework.generator.provider.java.model;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class MethodParameterTest extends TestCase {
	
	public void test() {
		JavaClass javaClass = new JavaClass(Table.class);
        JavaMethod[] methods = javaClass.getMethods();
		for(JavaMethod m : methods) {
			System.out.print(m.getMethodName()+"(");
			for(MethodParameter p : m.getParameters()) {
				System.out.print(p.getName()+",");
			}
			System.out.println(")");
		}
		JavaMethod m = javaClass.getMethod("setOwnerSynonymName");
		System.out.println("toGenericString()"+m.method.toGenericString());
		System.out.println("toString()"+m.method.toString());
	}
	
	public String blog(String name,String sex) {
		return "";
	}
}
