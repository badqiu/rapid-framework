/**
 * 
 */
package cn.org.rapid_framework.plugins.gen;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

/**
 * @author hunhun
 * @requiresDependencyResolution runtime
 */
public abstract class BaseMojo extends AbstractMojo {

	/**
	 * <i>Maven Internal</i>: Project to interact with.
	 * 
	 * @parameter expression="${project}"
	 * @required
	 * @readonly
	 * @noinspection UnusedDeclaration
	 */
	private MavenProject project;

	/**
	 * Returns the an isolated classloader.
	 * 
	 * @return ClassLoader
	 * @noinspection unchecked
	 */
	@SuppressWarnings(value = "all")
	protected ClassLoader getClassLoader() {
		try {
			List classpathElements = project.getCompileClasspathElements();
			classpathElements.add(project.getBuild().getOutputDirectory());
			classpathElements.add(project.getBuild().getTestOutputDirectory());
			URL urls[] = new URL[classpathElements.size()];
			for (int i = 0; i < classpathElements.size(); ++i) {
				urls[i] = new File((String) classpathElements.get(i)).toURL();
			}
			return new URLClassLoader(urls, this.getClass().getClassLoader());
		} catch (Exception e) {
			System.out.println("Couldn't get the classloader.");
			return this.getClass().getClassLoader();
		}
	}

	public MavenProject getProject() {
		return project;
	}
}
