package cn.org.rapid_framework.plugins.gen;

import java.io.IOException;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import cn.org.rapid_framework.generator.GeneratorFacade;

/**
 * 打印数据库中的表名称
 * 
 * @goal print
 */
public class PrintMojo extends BaseMojo {

	private GeneratorFacade g;

	public void execute() throws MojoExecutionException, MojoFailureException {
		Thread currentThread = Thread.currentThread();
		ClassLoader oldClassLoader = currentThread.getContextClassLoader();

		try {
			currentThread.setContextClassLoader(getClassLoader());
			System.out.println("current project to build: " + getProject().getName() + "\n"
					+ getProject().getFile().getParent());
			g = new GeneratorFacade();
			g.getGenerator().setTemplateRootDirs("classpath:template");
			try {
				g.deleteOutRootDir();
			} catch (IOException e) {
				e.printStackTrace();
			}
			printAllTableNames();
		} finally {
			currentThread.setContextClassLoader(oldClassLoader);
		}

	}

	public static void printAllTableNames() {

		try {
			GeneratorFacade.printAllTableNames();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		printAllTableNames();
	}

}
