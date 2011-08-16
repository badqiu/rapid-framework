package cn.org.rapid_framework.generator.paranamer;

import java.util.List;

import org.junit.Test;

import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.provider.java.model.MethodParameter;

public class NameIfaceTest {
	
	@Test
	public void say() {
		printMethodParams(NameIface.class, "say");
		printMethodParams(NameIfaceImpl.class, "say");
	}

	private void printMethodParams(Class<?> clazz, String method) {
		System.out.print(method+" ( ");
		List<MethodParameter> params = new JavaClass(clazz).getMethod(method).getParameters();
		for(MethodParameter p : params) {
			System.out.print(p.getName()+" , ");
		}
		System.out.println(" ) ");
	}
	
}
