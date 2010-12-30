package com.company.project.generator;


import java.util.Scanner;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

public class TableGeneratorMain {
	/**
	 * ��ֱ���޸����´�����ò�ͬ�ķ�����ִ�������������.
	 */
	public static void main(String[] args) throws Exception {
		GeneratorFacade g = new GeneratorFacade();
		
		g.getGenerator().setTemplateRootDirs(
				"src/main/generator_template/share/basic",
				"classpath:generator/template/rapid/table/dao_mybatis",
				"classpath:generator/template/rapid/table/dao_share_query_object",
				"classpath:generator/template/rapid/table/dao_test"
				);
		
		g.deleteOutRootDir();	//ɾ�������������Ŀ¼
		while(true) {
			System.out.println("������Ҫ���ɵ�[����],����[*��]�������ݿ����б�����,[�س�]��������:");
			String nextLine = new Scanner(System.in).nextLine();
			if("".equals(nextLine.trim())) {
				break;
			}else {
				g.generateByTable(nextLine);	//�Զ��������ݿ��е����б������ļ�,templateΪģ��ĸ�Ŀ¼
				Runtime.getRuntime().exec("cmd.exe /c start "+GeneratorProperties.getRequiredProperty("outRoot")); //���ļ���
			}
		}
//		g.deleteByTable("table_name", "template"); //ɾ�����ɵ��ļ�
		
	}
}
