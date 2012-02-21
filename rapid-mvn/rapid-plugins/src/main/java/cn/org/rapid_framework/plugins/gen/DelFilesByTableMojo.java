/**
 * 
 */
package cn.org.rapid_framework.plugins.gen;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

import cn.org.rapid_framework.generator.GeneratorFacade;

/**
 * 代码生成器插件的主要goal
 * 
 * @author hunhun
 * @goal del
 */
public class DelFilesByTableMojo extends AbstarctGeneratorMojo {

	/**
	 * -Dtable=* -Dtable=user_info
	 * 
	 * @parameter expression="${table}"
	 */
	public String tableParameter;

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

			System.out.println("current project to build: "
					+ getProject().getName());
			g = new GeneratorFacade();
			g.getGenerator().setTemplateRootDirs("classpath:template");
			delByTable(parseStringArray(tableParameter));
		} finally {
			currentThread.setContextClassLoader(oldClassLoader);
		}
	}

	public void delByTable(String... table) {
		try {
			g.deleteByTable(table);
			MojoUtil.openFileIfOnWindows();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
