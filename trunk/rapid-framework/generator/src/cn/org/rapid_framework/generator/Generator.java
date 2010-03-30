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
	private boolean ignoreTemplateGenerateException = false;
	
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

    /**
     * 生成器的生成入口
     * @param templateModel 生成器模板可以引用的变量
     * @param filePathModel 文件路径可以引用的变量
     * @throws Exception
     */
	public void generateBy(Map templateModel,Map filePathModel) throws Exception {
		if(templateRootDirs.size() == 0) throw new IllegalStateException("'templateRootDirs' cannot empty");
		
		List allExceptions = new ArrayList();
		for(int i = 0; i < this.templateRootDirs.size(); i++) {
			File templateRootDir = (File)templateRootDirs.get(i);
			List exceptions = generateBy(templateRootDir,templateModel,filePathModel);
			allExceptions.addAll(exceptions); 
		}
	}
	
	private List<Exception> generateBy(File templateRootDir, Map templateModel,Map filePathModel) throws Exception {
		if(templateRootDir == null) throw new IllegalStateException("'templateRootDir' must be not null");
		System.out.println("-------------------load template from templateRootDir = '"+templateRootDir.getAbsolutePath()+"'");
		
		List templateFiles = new ArrayList();
		FileHelper.listFiles(templateRootDir, templateFiles);
		
		List exceptions = new ArrayList();
		for(int i = 0; i < templateFiles.size(); i++) {
			File templateFile = (File)templateFiles.get(i);
			String templateRelativePath = FileHelper.getRelativePath(templateRootDir, templateFile);
			String outputFilePath = templateRelativePath;
			if(templateFile.isDirectory() || templateFile.isHidden())
				continue;
			if(templateRelativePath.trim().equals(""))
				continue;
			if(templateFile.getName().toLowerCase().endsWith(".include")){
				System.out.println("[skip]\t\t endsWith '.include' template:"+templateRelativePath);
				continue;
			}
			int testExpressionIndex = -1;
			if((testExpressionIndex = templateRelativePath.indexOf('@')) != -1) {
				outputFilePath = templateRelativePath.substring(0, testExpressionIndex);
				String testExpressionKey = templateRelativePath.substring(testExpressionIndex+1);
				Object expressionValue = filePathModel.get(testExpressionKey);
				if(expressionValue == null) {
					System.err.println("[not-generate] WARN: test expression is null by key:["+testExpressionKey+"] on template:["+templateRelativePath+"]");
					continue;
				}
				if(!"true".equals(String.valueOf(expressionValue))) {
					System.out.println("[not-generate]\t test expression '@"+testExpressionKey+"' is false,template:"+templateRelativePath);
					continue;
				}
			}
			
			String targetFilename = null;
			try {
				targetFilename = getTargetFilename(filePathModel, outputFilePath);
				generateNewFileOrInsertIntoFile(templateModel,targetFilename, newFreeMarkerConfiguration(), templateRelativePath,outputFilePath);
			}catch(Exception e) {
			    if (ignoreTemplateGenerateException) {
			        GLogger.warn("iggnore generate error,template is:" + templateRelativePath,e);
                    exceptions.add(e);
                } else {
                    throw new RuntimeException(
                        "generate oucur error,templateFile is:" + templateRelativePath+" => "+ targetFilename, e);
                }
			}
		}
		return exceptions;
	}

	private Configuration newFreeMarkerConfiguration() throws IOException {
		Configuration config = new Configuration();
		
		FileTemplateLoader[] templateLoaders = new FileTemplateLoader[templateRootDirs.size()];
		for(int i = 0; i < templateRootDirs.size(); i++) {
			templateLoaders[i] = new FileTemplateLoader((File)templateRootDirs.get(i));
		}
		MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(templateLoaders);
		
		config.setTemplateLoader(multiTemplateLoader);
		config.setNumberFormat("###############");
		config.setBooleanFormat("true,false");
		config.setDefaultEncoding(encoding);
		return config;
	}

	private void generateNewFileOrInsertIntoFile( Map templateModel,String targetFilename, Configuration config, String templateFile,String outputFilePath) throws Exception {
		Template template = config.getTemplate(templateFile);
		template.setOutputEncoding(encoding);
		
		File absoluteOutputFilePath = getAbsoluteOutputFilePath(targetFilename);
		if(absoluteOutputFilePath.exists()) {
			StringWriter newFileContentCollector = new StringWriter();
			if(isFoundInsertLocation(template, templateModel, absoluteOutputFilePath, newFileContentCollector)) {
				System.out.println("[insert]\t generate content into:"+targetFilename);
				IOHelper.saveFile(absoluteOutputFilePath, newFileContentCollector.toString());
				return;
			}
		}
		
		System.out.println("[generate]\t template:"+templateFile+" to "+targetFilename);
		saveNewOutputFileContent(template, templateModel, absoluteOutputFilePath);
	}

	private String getTargetFilename(Map filePathModel, String templateFilepath) throws Exception {
		StringWriter out = new StringWriter();
		Template template = new Template("filePath",new StringReader(templateFilepath),newFreeMarkerConfiguration());
		try {
			template.process(filePathModel, out);
			return out.toString();
		}catch(Exception e) {
			throw new IllegalStateException("cannot generate filePath from templateFilepath:"+templateFilepath+" cause:"+e,e);
		}
	}

	private File getAbsoluteOutputFilePath(String targetFilename) {
		String outRoot = getOutRootDir();
		File outputFile = new File(outRoot,targetFilename);
		outputFile.getParentFile().mkdirs();
		return outputFile;
	}

	private boolean isFoundInsertLocation(Template template, Map model, File outputFile, StringWriter newFileContent) throws IOException, TemplateException {
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

	private void saveNewOutputFileContent(Template template, Map model, File outputFile) throws IOException, TemplateException {
		Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile),encoding));
		template.process(model,out);
		out.close();
	}

	public void clean() throws IOException {
		String outRoot = getOutRootDir();
		System.out.println("[Delete Dir]	"+outRoot);
		FileHelper.deleteDirectory(new File(outRoot));
	}

	private String getOutRootDir() {
		if(outRootDir == null) throw new IllegalStateException("'outRootDir' property must be not null.");
		return outRootDir;
	}
	
}
