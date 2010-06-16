package cn.org.rapid_framework.generator.ext;

import java.util.Scanner;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.util.StringHelper;

public class CommandLine {
	
	public static void main(String[] args) throws Exception {
		printUsages();
		startProcess(args.length > 0 ? args[0] : "template");
	}

	private static void startProcess(String templateRootDir) throws Exception {
		Scanner sc = new Scanner(System.in);
		GeneratorFacade g = new GeneratorFacade();
		while(sc.hasNextLine()) {
			try {
				processLine(templateRootDir,sc, g);
			}catch(Exception e) {
				e.printStackTrace();
			}finally {
				printUsages();
			}
		}
	}

	private static void processLine(String templateRootDir,Scanner sc, GeneratorFacade g) throws Exception {
		String cmd = sc.next();
		if("gen".equals(cmd)) {
			String[] args = nextArguments(sc);
			g.generateByTable(args[0],templateRootDir);
			Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot"));
		}else if("del".equals(cmd)) {
			String[] args = nextArguments(sc);
			g.deleteByTable(args[0], templateRootDir);
		}else {
			System.out.println("unknow command:"+cmd);
		}
	}

	private static void printUsages() {
		System.out.println("Usage: cmd [args1] [args2] [args...]");
		System.out.println("\tgen [table_name] : generate files by table_name");
		System.out.println("\tdel [table_name] : delete files by table_name");
		System.out.println("\tgen * : search database all tables and generate files");
		System.out.println("\tdel * : search database all tables and delete files");
		System.out.print("please input command:");
	}
	
	private static String[] nextArguments(Scanner sc) {
		return StringHelper.tokenizeToStringArray(sc.next()," ");
	}
}
