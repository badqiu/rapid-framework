/**
 * 
 */
package cn.org.rapid_framework.plugins.gen;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

/**
 * 代码生成器插件的主要goal
 * 
 * @author hunhun
 * @goal gen
 */
public class GenMojo extends BaseMojo {

	/**
	 * -Dtable=* -Dtable=user_info
	 * 
	 * @parameter expression="${table}"
	 * 
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
			System.out.println("current project to build: " + getProject().getName() + "\n"
					+ getProject().getFile().getParent());
			g = new GeneratorFacade();
			g.getGenerator().setTemplateRootDirs("classpath:template");
			try {
				g.deleteOutRootDir();
			} catch (IOException e) {
				e.printStackTrace();
			}
			genByTable(table);
		} finally {
			currentThread.setContextClassLoader(oldClassLoader);
		}

	}

	public void genByTable(String... table) {
		try {
			g.generateByTable(table);
			Runtime.getRuntime().exec("cmd.exe /c start " + GeneratorProperties.getRequiredProperty("outRoot"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
