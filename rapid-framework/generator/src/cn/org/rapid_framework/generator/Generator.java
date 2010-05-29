package cn.org.rapid_framework.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.IOHelper;
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.MultiTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 
 * @author badqiu
 * @email badqiu(a)gmail.com
 */
public class Generator {
	private static final String GENERATOR_INSERT_LOCATION = "generator-insert-location";
	private List templateRootDirs = new ArrayList();
	private String outRootDir;
	private boolean ignoreTemplateGenerateException = true;
	private String removeExtensions = ".gen";
	
	String encoding = "UTF-8";
	public Generator() {
	}
	
	public void setTemplateRootDir(File templateRootDir) {
		setTemplateRootDirs(new File[]{templateRootDir});
	}

	public void setTemplateRootDirs(File[] templateRootDirs) {
		this.templateRootDirs = Arrays.asList(templateRootDirs);
	}
	
	public void addTemplateRootDir(File f) {
		templateRootDirs.add(f);
	}
	
	public boolean isIgnoreTemplateGenerateException() {
        return ignoreTemplateGenerateException;
    }

    public void setIgnoreTemplateGenerateException(boolean ignoreTemplateGenerateException) {
        this.ignoreTemplateGenerateException = ignoreTemplateGenerateException;
    }

    public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String v) {
		if(v == null) throw new IllegalArgumentException("encoding must be not null");
		this.encoding = v;
	}
	
	public void setOutRootDir(String v) {
		if(v == null) throw new IllegalArgumentException("outRootDir must be not null");
		this.outRootDir = v;
	}
	
	private String getOutRootDir() {
		if(outRootDir == null) throw new IllegalStateException("'outRootDir' property must be not null.");
		return outRootDir;
	}

	public void setRemoveExtensions(String removeExtensions) {
		this.removeExtensions = removeExtensions;
	}

    /**
     * 生成器的生成入口
     * @param templateModel 生成器模板可以引用的变量
     * @param filePathModel 文件路径可以引用的变量
     * @throws Exception
     */
	public List generateBy(Map templateModel,Map filePathModel) throws Exception {
		if(templateRootDirs.size() == 0) throw new IllegalStateException("'templateRootDirs' cannot empty");
		List allExceptions = new ArrayList();
		for(int i = 0; i < this.templateRootDirs.size(); i++) {
			File templateRootDir = (File)templateRootDirs.get(i);
			List exceptions = generateBy(templateRootDir,templateModel,filePathModel);
			allExceptions.addAll(exceptions); 
		}
		return allExceptions;
	}
	
	private List<Exception> generateBy(File templateRootDir, Map templateModel,Map filePathModel) throws Exception {
		if(templateRootDir == null) throw new IllegalStateException("'templateRootDir' must be not null");
		System.out.println("-------------------load template from templateRootDir = '"+templateRootDir.getAbsolutePath()+"'");
		
		List templateFiles = new ArrayList();
		FileHelper.listFiles(templateRootDir, templateFiles);
		
		List exceptions = new ArrayList();
		for(int i = 0; i < templateFiles.size(); i++) {
			File srcFile = (File)templateFiles.get(i);
			String templateFile = FileHelper.getRelativePath(templateRootDir, srcFile);
			
			templateModel.put("gg", new GeneratorControl());
			
			if(FreemarkerUtils.isIgnoreTemplateProcess(srcFile, templateFile)) {
				continue;
			}
			
			String outputFilepath = null;
			try {
				outputFilepath = proceeForOutputFilepath(filePathModel,templateFile);
				if(outputFilepath != null ) {
					generateNewFileOrInsertIntoFile(templateModel,outputFilepath, templateFile);
				}
			}catch(Exception e) {
                RuntimeException throwException = new RuntimeException("generate oucur error,templateFile is:" + templateFile+" => "+ outputFilepath, e);
			    if (ignoreTemplateGenerateException) {
			        GLogger.warn("iggnore generate error,template is:" + templateFile+" cause:"+e);
                    exceptions.add(throwException);
                } else {
					throw throwException;
                }
			}
		}
		return exceptions;
	}
	
	/** 处理文件路径的变量变成输出路径 */
	private String proceeForOutputFilepath(Map filePathModel,String templateFile) throws IOException {
		String outputFilePath = templateFile;
		int testExpressionIndex = -1;
		if((testExpressionIndex = templateFile.indexOf('@')) != -1) {
			outputFilePath = templateFile.substring(0, testExpressionIndex);
			String testExpressionKey = templateFile.substring(testExpressionIndex+1);
			Object expressionValue = filePathModel.get(testExpressionKey);
			if(expressionValue == null) {
				System.err.println("[not-generate] WARN: test expression is null by key:["+testExpressionKey+"] on template:["+templateFile+"]");
					return null;
			}
			if(!"true".equals(String.valueOf(expressionValue))) {
				System.out.println("[not-generate]\t test expression '@"+testExpressionKey+"' is false,template:"+templateFile);
					return null;
			}
		}
		if(outputFilePath.endsWith(removeExtensions)) {
			outputFilePath = outputFilePath.substring(0,outputFilePath.length() - removeExtensions.length());
		}
		return FreemarkerUtils.processTemplateString(filePathModel, outputFilePath,getFreeMarkerConfiguration());
	}
	

	private Configuration getFreeMarkerConfiguration() throws IOException {
		return FreemarkerUtils.newFreeMarkerConfiguration(templateRootDirs, encoding);
	}

	private void generateNewFileOrInsertIntoFile( Map templateModel,String outputFilepath, String templateFile) throws Exception {
		Template template = getFreeMarkerConfiguration().getTemplate(templateFile);
		template.setOutputEncoding(encoding);
		
		File absoluteOutputFilePath = FileHelper.mkdir(getOutRootDir(),outputFilepath);
		if(absoluteOutputFilePath.exists()) {
			StringWriter newFileContentCollector = new StringWriter();
			if(FreemarkerUtils.isFoundInsertLocation(template, templateModel, absoluteOutputFilePath, newFileContentCollector)) {
				System.out.println("[insert]\t generate content into:"+outputFilepath);
				IOHelper.saveFile(absoluteOutputFilePath, newFileContentCollector.toString());
				return;
			}
		}
		
		System.out.println("[generate]\t template:"+templateFile+" to "+outputFilepath);
		FreemarkerUtils.processTemplate(template, templateModel, absoluteOutputFilePath,encoding);
	}

	public void clean() throws IOException {
		String outRoot = getOutRootDir();
		System.out.println("[Delete Dir]	"+outRoot);
		FileHelper.deleteDirectory(new File(outRoot));
	}
	
	static class FreemarkerUtils {
		public static boolean isIgnoreTemplateProcess(File srcFile,String templateFile) {
			if(srcFile.isDirectory() || srcFile.isHidden())
				return true;
			if(templateFile.trim().equals(""))
				return true;
			if(srcFile.getName().toLowerCase().endsWith(".include")){
				System.out.println("[skip]\t\t endsWith '.include' template:"+templateFile);
				return true;
			}
			return false;
		}		
		private static boolean isFoundInsertLocation(Template template, Map model, File outputFile, StringWriter newFileContent) throws IOException, TemplateException {
			LineNumberReader reader = new LineNumberReader(new FileReader(outputFile));
			String line = null;
			boolean isFoundInsertLocation = false;
			
			PrintWriter writer = new PrintWriter(newFileContent);
			while((line = reader.readLine()) != null) {
				writer.println(line);
				// only insert once
				if(!isFoundInsertLocation && line.indexOf(GENERATOR_INSERT_LOCATION) >= 0) {
					template.process(model,writer);
					writer.println();
					isFoundInsertLocation = true;
				}
			}
			
			writer.close();
			reader.close();
			return isFoundInsertLocation;
		}	
		
		public static void processTemplate(Template template, Map model, File outputFile,String encoding) throws IOException, TemplateException {
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
			template.process(model,out);
			out.close();
		}
		public static String processTemplateString(Map model, String templateString,Configuration conf) {
			StringWriter out = new StringWriter();
			try {
				Template template = new Template("templateString...",new StringReader(templateString),conf);
				template.process(model, out);
				return out.toString();
			}catch(Exception e) {
				throw new IllegalStateException("cannot process templateString:"+templateString+" cause:"+e,e);
			}
		}
		public static Configuration newFreeMarkerConfiguration(List<File> templateRootDirs,String defaultEncoding) throws IOException {
			Configuration config = new Configuration();
			
			FileTemplateLoader[] templateLoaders = new FileTemplateLoader[templateRootDirs.size()];
			for(int i = 0; i < templateRootDirs.size(); i++) {
				templateLoaders[i] = new FileTemplateLoader((File)templateRootDirs.get(i));
			}
			MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(templateLoaders);
			
			config.setTemplateLoader(multiTemplateLoader);
			config.setNumberFormat("###############");
			config.setBooleanFormat("true,false");
			config.setDefaultEncoding(defaultEncoding);
			return config;
		}
	}
}
