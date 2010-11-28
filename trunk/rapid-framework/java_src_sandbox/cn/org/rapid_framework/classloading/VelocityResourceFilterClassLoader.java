package cn.org.rapid_framework.classloading;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.struts2.interceptor.ApplicationAware;
import org.springframework.core.io.UrlResource;

import cn.org.rapid_framework.io.filter.VelocityFilterResource;
import cn.org.rapid_framework.io.filter.VelocityHelper;
import cn.org.rapid_framework.jar.ManifestUtils;

public class VelocityResourceFilterClassLoader extends ClassLoader {

//	protected URL findResource(String name) {
//		return super.findResource(name);
//	}
//
//	protected Enumeration<URL> findResources(String name) throws IOException {
//		return filterByVelocityIfRequired(super.findResources(name));
//	}
	
	@Override
	public URL getResource(String name) {
		URL resource = super.getResource(name);
		return filterByVelocityIfRequired(resource);
	}

	@Override
	public InputStream getResourceAsStream(String name) {
		InputStream in = super.getResourceAsStream(name);
		return VelocityHelper.getInstance().evaluate(in);
	}
	
	@Override
	public Enumeration<URL> getResources(String name) throws IOException {
		Enumeration<URL> urls = super.getResources(name);
		return filterByVelocityIfRequired(urls);
	}

	private URL filterByVelocityIfRequired(URL resource) {
		UrlResource decoratedResource = new UrlResource(resource);
		try {
			return new VelocityFilterResource(decoratedResource).getURL();
		}catch(IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Enumeration<URL> filterByVelocityIfRequired(Enumeration<URL> urls) {
		Vector<URL> list = new Vector<URL>();
		while(urls.hasMoreElements()) {
			list.add(filterByVelocityIfRequired(urls.nextElement()));
		}
		return list.elements();
	}
	
	
	public static void main(String[] args) throws Exception {
		Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(ClassLoader.getSystemClassLoader());
		Package pkg = VelocityResourceFilterClassLoader.class.getPackage();
		ClassLoader.getSystemClassLoader();
		System.out.println(ToStringBuilder.reflectionToString(pkg));
		printCodeSource(VelocityResourceFilterClassLoader.class);
		printCodeSource(ApplicationAware.class);
		printCodeSource(VelocityResourceFilterClassLoader.class);
		
		printManifest(ApplicationAware.class);
		printManifest(ApplicationAware.class);
		printManifest(VelocityResourceFilterClassLoader.class);
		String resource = VelocityResourceFilterClassLoader.class.getName().replace('.', '/');
		printManifest(resource+".class");
		
		printManifest(ApplicationAware.class.getName().replace('.', '/')+".class");
	}

	private static void printCodeSource(Class clazz) {
		CodeSource codeSource = clazz.getProtectionDomain().getCodeSource();
		System.out.println(codeSource.getLocation());
		System.out.println(codeSource.getCertificates());
		System.out.println(codeSource.getCodeSigners());
	}
	
	private static void printManifest(Class clazz) {
		Attributes attrs = ManifestUtils.getManifest(clazz).getMainAttributes();
		System.out.println("manifest:"+Arrays.toString(attrs.values().toArray()));
	}
	
	private static void printManifest(String resourceName) {
		System.out.println("resourceName:"+resourceName);
		Manifest manifest = ManifestUtils.getManifest(Thread.currentThread().getContextClassLoader(), resourceName);
		if(manifest != null) {
			Attributes attrs = manifest.getMainAttributes();
			System.out.println("manifest:"+Arrays.toString(attrs.values().toArray()));
		}
	}
}
