package cn.org.rapid_framework.generator;

/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */

public class GeneratorMain {
	public static void main(String[] args) throws Exception {
		GeneratorFacade g = new GeneratorFacade();
		// g.printAllTableNames();
		// g.clean();
		if (args[0].toString().trim().toLowerCase().equals("all")) {
			g.generateByAllTable();
		} else {
			for (int i = 0; args != null && i < args.length; i++) {

				System.out.println("开始生成第" + i + "个表 [" + args[i] + "]的代码：");
				g.generateByTable(args[i].toString().toUpperCase());
			}
		}
		// g.generateByClass(ZdGlobal.class);
		// Runtime.getRuntime().exec("cmd.exe /c start E:\\");
	}
}
