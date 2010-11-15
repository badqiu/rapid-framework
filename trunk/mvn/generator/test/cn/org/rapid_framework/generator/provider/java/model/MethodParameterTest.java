package cn.org.rapid_framework.generator.provider.java.model;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.provider.db.table.model.Table;

public class MethodParameterTest extends TestCase {
	
	public void test() {
		JavaMethod[] methods = new JavaClass(Table.class).getMethods();
		for(JavaMethod m : methods) {
			System.out.print(m.getMethodName()+"(");
			for(MethodParameter p : m.getParameters()) {
				System.out.print(p.getName()+",");
			}
			System.out.println(")");
		}
	}
	
	public String blog(String name,String sex) {
		return "";
	}
}
