package cn.org.rapid_framework.generator.ext;

import java.io.File;
import java.util.Scanner;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.util.ArrayHelper;
import cn.org.rapid_framework.generator.util.StringHelper;
/**
 * 命令行工具类
 * 
 * @author badqiu
 */
public class CommandLine {
	
	public static void main(String[] args) throws Exception {
		//disable freemarker logging
		freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
		
		startProcess();
	}

	private static void startProcess() throws Exception {
		Scanner sc = new Scanner(System.in);
		System.out.println("templateRootDir:"+new File(getTemplateRootDir()).getAbsolutePath());
		printUsages();
		GeneratorFacade g = new GeneratorFacade();
		while(sc.hasNextLine()) {
			try {
				processLine(sc, g);
				Thread.sleep(700);
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				printUsages();
			}
		}
	}

	private static void processLine(Scanner sc, GeneratorFacade facade) throws Exception {
		
		String cmd = sc.next();
		if("gen".equals(cmd)) {
			String[] args = nextArguments(sc);
			facade.g.setIncludes(getIncludes(args,1));
			facade.generateByTable(args[0],getTemplateRootDir());
			Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));
		}else if("del".equals(cmd)) {
			String[] args = nextArguments(sc);
			facade.g.setIncludes(getIncludes(args,1));
			facade.deleteByTable(args[0], getTemplateRootDir());
		}else if("quit".equals(cmd)) {
		    System.exit(0);
		}else {
			System.out.println(" [ERROR] unknow command:"+cmd);
		}
	}

	private static String getIncludes(String[] args, int i) {
		String includes = ArrayHelper.getValue(args, i);
		if(includes == null) return null;
		return includes.indexOf("*") >= 0 || includes.indexOf(",") >= 0 ? includes : includes+"/**";
	}
	
	private static String getTemplateRootDir() {
		return System.getProperty("templateRootDir", "template");
	}

	private static void printUsages() {
		System.out.println("Usage:");
		System.out.println("\tgen table_name [include_path]: generate files by table_name");
		System.out.println("\tdel table_name [include_path]: delete files by table_name");
		System.out.println("\tgen * [include_path]: search database all tables and generate files");
		System.out.println("\tdel * [include_path]: search database all tables and delete files");
		System.out.println("\tquit : quit");
		System.out.println("\t[include_path] example: 1. dao  2. dao/**,service/**");
		System.out.print("please input command:");
	}
	
	private static String[] nextArguments(Scanner sc) {
		return StringHelper.tokenizeToStringArray(sc.nextLine()," ");
	}
}
