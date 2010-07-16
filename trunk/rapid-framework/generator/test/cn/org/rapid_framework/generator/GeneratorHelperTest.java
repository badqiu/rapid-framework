package cn.org.rapid_framework.generator;

import java.util.List;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorHelper;

public class GeneratorHelperTest extends TestCase {
	
	public void test() {
		List list = GeneratorHelper.getParentPaths("/1/2/3", "macro.include");
		assertEquals("[macro.include, \\macro.include, \\1\\macro.include, \\1\\2\\macro.include, \\1\\2\\3\\macro.include]",list.toString());
		
		list = GeneratorHelper.getParentPaths("/1/2", "macro.include");
		assertEquals("[macro.include, \\macro.include, \\1\\macro.include, \\1\\2\\macro.include]",list.toString());
		
		list = GeneratorHelper.getParentPaths("/1", "macro.include");
		assertEquals("[macro.include, \\macro.include, \\1\\macro.include]",list.toString());
		
		list = GeneratorHelper.getParentPaths("1", "macro.include");
		assertEquals("[macro.include, \\macro.include, \\1\\macro.include]",list.toString());
		
		list = GeneratorHelper.getParentPaths(null, "macro.include");
		assertEquals("[macro.include, \\macro.include]",list.toString());
	}
}
