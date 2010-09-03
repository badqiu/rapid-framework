package cn.org.rapid_framework.generator.ext.ant;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;

import org.hsqldb.lib.HsqlTimer.Task;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

public class GeneratorTask extends Task {
	List<String> tables = new ArrayList<String>();
	String templateRootDir = null;
	String outRoot = null;
	
	@Override
	public void execute() throws BuildException {
		try {
			execute0();
		} catch (Exception e) {
			throw new BuildException(e);
		}
	}

	private void execute0() throws Exception {
		GeneratorFacade gf = new GeneratorFacade();
		GeneratorProperties.setProperties(new Properties());
		Properties properties = toProperties(getProject().getProperties());
		properties.setProperty("basedir", getProject().getBaseDir().getAbsolutePath());
		GeneratorProperties.setProperties(properties);
		
		gf.g.setOutRootDir(outRoot);
		for(String table : tables) {
			gf.generateByTable(table, templateRootDir);
		}
	}
	
	public void setTables(List<String> tables) {
		this.tables = tables;
	}
	
	public void setOutRoot(String outRoot) {
		this.outRoot = outRoot;
	}
	
	public void setTemplateRootDir(String templateRootDir) {
		this.templateRootDir = templateRootDir;
	}

	private static Properties toProperties(Hashtable properties) {
		Properties props = new Properties();
		props.putAll(properties);
		return props;
	}
	
}
