/**
 * 
 */
package cn.org.rapid_framework.plugins.gen;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

/**
 * @author hunhun
 * @requiresDependencyResolution runtime
 */
public abstract class AbstarctGeneratorMojo extends AbstractMojo {

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
	 * 改变plugin classloader，使其可以加载当前工程的classpath以及dependency
	 * 
	 * @return ClassLoader
	 * @noinspection unchecked
	 */
	@SuppressWarnings(value = "all")
	protected ClassLoader getClassLoader() {
		try {
			Collection<URL> urls=new ArrayList<URL>();
			//为了获得当前project的src/resources/generaot.xml
			for(String elements:(Collection<String>)project.getCompileClasspathElements()){
				urls.add(new File(elements).toURI().toURL());
			}
			
			//为了获得当前project的依赖:jdbc driver
			for(Artifact artifact:(Collection<Artifact>)project.getArtifacts()){
				urls.add(artifact.getFile().toURI().toURL());
			}
			
			System.out.println("urls = \n" + urls.toString().replace(",","\n")) ;
			return new URLClassLoader((urls.toArray(new URL[urls.size()])), this.getClass().getClassLoader());
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Couldn't get the classloader.");
			return this.getClass().getClassLoader();
		}
	}

	public MavenProject getProject() {
		return project;
	}
}
