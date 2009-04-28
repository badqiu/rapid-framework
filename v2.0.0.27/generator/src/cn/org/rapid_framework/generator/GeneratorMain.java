package cn.org.rapid_framework.generator;


/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */

public class GeneratorMain {
	public static void main(String[] args) throws Exception {
		GeneratorFacade g = new GeneratorFacade();
//		g.printAllTableNames();
		
		g.clean();
//		g.generateByTable("table_name");
		g.generateByAllTable();
//		g.generateByClass(Blog.class);
		
		Runtime.getRuntime().exec("cmd.exe /c start D:\\webapp-generator-output");
	}
}
