/**
 * 
 */
package com.alibaba.tctools.mojo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.project.MavenProject;

/**
 * @author  lai.zhoul
 * @email  hhlai1990@gmail.com
 * @requiresDependencyResolution  runtime
 */
public abstract class AbstarctBaseMojo extends AbstractMojo {

	/**
     * <i>Maven Internal</i>: Project to interact with.
     * @parameter  expression="${project}"
     * @required
     * @readonly
     * @noinspection  UnusedDeclaration
     * @uml.property  name="project"
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
			//为了获得当前project的classpath
			for(String elements:(Collection<String>)project.getCompileClasspathElements()){
				urls.add(new File(elements).toURI().toURL());
			}
			urls.add(new File(project.getBuild().getTestOutputDirectory()).toURI().toURL());
			
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
	
	 public static File getFileByClassLoader(String resourceName) throws IOException {
	        String pathToUse = resourceName;
	        if (pathToUse.startsWith("/")) {
	            pathToUse = pathToUse.substring(1);
	        }
	        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader()
	                .getResources(pathToUse);
	        while (urls.hasMoreElements()) {
	            return new File(urls.nextElement().getFile());
	        }
	        urls = Thread.currentThread().getContextClassLoader().getResources(pathToUse);
	        while (urls.hasMoreElements()) {
	            return new File(urls.nextElement().getFile());
	        }
	        urls = ClassLoader.getSystemResources(pathToUse);
	        while (urls.hasMoreElements()) {
	            return new File(urls.nextElement().getFile());
	        }
	        throw new FileNotFoundException("classpath:" + resourceName);
	    }
}
