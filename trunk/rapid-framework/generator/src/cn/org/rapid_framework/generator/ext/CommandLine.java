package cn.org.rapid_framework.generator.ext;

import java.util.Scanner;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.util.StringHelper;

public class CommandLine {
	
	public static void main(String[] args) throws Exception {
		printUsages();
		startProcess();
	}

	private static void startProcess() throws Exception {
		Scanner sc = new Scanner(System.in);
		GeneratorFacade g = new GeneratorFacade();
		while(sc.hasNextLine()) {
			try {
				processLine(sc, g);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	static String templateRootDir = "template";
	private static void processLine(Scanner sc, GeneratorFacade g) throws Exception {
		String cmd = sc.next();
		if("gen".equals(cmd)) {
			String[] args = nextArguments(sc);
			g.generateByTable(args[0],templateRootDir);
		}else if("del".equals(cmd)) {
			String[] args = nextArguments(sc);
			g.deleteByTable(args[0], templateRootDir);
		}else if("genall".equals(cmd)) {
			g.generateByAllTable(templateRootDir);
		}else if("delall".equals(cmd)) {
			g.deleteByAllTable(templateRootDir);				
		}else {
			System.out.println("unknow command:"+cmd);
		}
	}

	private static void printUsages() {
		System.out.println("Usage: cmd [args1] [args2] [args...]");
		System.out.println("\tgen table_name : generate files by table_name");
		System.out.println("\tdel table_name : delete files by table_name");
		System.out.println("\tgenall : search database all tables and generate");
		System.out.println("\tdelall : search database all tables and delete");
	}
	
	private static String[] nextArguments(Scanner sc) {
		return StringHelper.tokenizeToStringArray(sc.next()," ");
	}
}
