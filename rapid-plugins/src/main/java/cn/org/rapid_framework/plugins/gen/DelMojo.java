/**
 * 
 */
package cn.org.rapid_framework.plugins.gen;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

/**
 * 代码生成器插件的主要goal
 * 
 * @author hunhun
 * @goal del
 */
public class DelMojo extends BaseMojo {

	/**
	 * -Dtable=* -Dtable=user_info
	 * 
	 * @parameter expression="${table}"
	 */
	public String table;

	private GeneratorFacade g;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.apache.maven.plugin.Mojo#execute()
	 */
	public void execute() throws MojoExecutionException, MojoFailureException {

		Thread currentThread = Thread.currentThread();
		ClassLoader oldClassLoader = currentThread.getContextClassLoader();
		try {
			currentThread.setContextClassLoader(getClassLoader());

			System.out.println("current project to build: " + getProject().getName());
			g = new GeneratorFacade();
			g.getGenerator().setTemplateRootDirs("classpath:template");
			delByTable(table);
		} finally {
			currentThread.setContextClassLoader(oldClassLoader);
		}
	}

	public void delByTable(String... table) {
		try {
			g.deleteByTable(table);
			Runtime.getRuntime().exec("cmd.exe /c start " + GeneratorProperties.getRequiredProperty("outRoot"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// public static void main(String[] args) {
	// DelMojo t = new DelMojo();
	// t.g = new GeneratorFacade();
	// t.g.getGenerator().setTemplateRootDirs("classpath:template",
	// "classpath:generator/template/rapid/share");
	//
	// t.delByTable("user_info", "user_role");
	// t.delByTable("*");
	// }

}
