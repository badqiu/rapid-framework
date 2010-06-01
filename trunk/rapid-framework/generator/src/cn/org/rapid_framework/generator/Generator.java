package cn.org.rapid_framework.generator;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.generator.provider.db.DbTableFactory;
import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.FreemarkerHelper;
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
	private String removeExtensions = ".ftl";
	private boolean isCopyBinaryFile = true;
	
	String sourceEncoding = "UTF-8";
	String outputEncoding = "UTF-8";
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

    public boolean isCopyBinaryFile() {
		return isCopyBinaryFile;
	}

	public void setCopyBinaryFile(boolean isCopyBinaryFile) {
		this.isCopyBinaryFile = isCopyBinaryFile;
	}

	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(String sourceEncoding) {
		if(sourceEncoding == null) throw new IllegalArgumentException("sourceEncoding must be not null");
		this.sourceEncoding = sourceEncoding;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}

	public void setOutputEncoding(String outputEncoding) {
		if(outputEncoding == null) throw new IllegalArgumentException("outputEncoding must be not null");
		this.outputEncoding = outputEncoding;
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

    public void clean() throws IOException {
        String outRoot = getOutRootDir();
        GLogger.println("[Delete Dir]    "+outRoot);
        FileHelper.deleteDirectory(new File(outRoot));
    }
	   
    /**
     * 生成器的生成入口
     * @param templateModel 生成器模板可以引用的变量
     * @param filePathModel 文件路径可以引用的变量
     * @throws Exception
     */
	public List<Exception> generateBy(Map templateModel,Map filePathModel) throws Exception {
		if(templateRootDirs.size() == 0) throw new IllegalStateException("'templateRootDirs' cannot empty");
		List<Exception> allExceptions = new ArrayList<Exception>();
		for(int i = 0; i < this.templateRootDirs.size(); i++) {
			File templateRootDir = (File)templateRootDirs.get(i);
			List<Exception> exceptions = generateByTemplateRootDir(templateRootDir,templateModel,filePathModel);
			allExceptions.addAll(exceptions); 
		}
		return allExceptions;
	}
	
	private List<Exception> generateByTemplateRootDir(File templateRootDir, Map templateModel,Map filePathModel) throws Exception {
		if(templateRootDir == null) throw new IllegalStateException("'templateRootDir' must be not null");
		GLogger.println("-------------------load template from templateRootDir = '"+templateRootDir.getAbsolutePath()+"'");
		
		List srcFiles = new ArrayList();
		FileHelper.listFiles(templateRootDir, srcFiles);
		
		List<Exception> exceptions = new ArrayList();
		for(int i = 0; i < srcFiles.size(); i++) {
			File srcFile = (File)srcFiles.get(i);
			try {
				new GeneratorProcessor().execute(templateRootDir, templateModel,filePathModel, srcFile);
			}catch(Exception e) {
				if (ignoreTemplateGenerateException) {
			        GLogger.warn("iggnore generate error,template is:" + srcFile+" cause:"+e);
			        exceptions.add(e);
			    } else {
					throw e;
			    }
			}
		}
		return exceptions;
	}
	
	public class GeneratorProcessor {
		private GeneratorControl gg = new GeneratorControl();
		private void execute(File templateRootDir,Map templateModel, Map filePathModel ,File srcFile) throws SQLException, IOException,TemplateException {
			String templateFile = FileHelper.getRelativePath(templateRootDir, srcFile);
			
			if(isCopyBinaryFile && FileHelper.isBinaryFile(srcFile)) {
				String outputFilepath = proceeForOutputFilepath(filePathModel, templateFile);
				GLogger.println("[copy binary file by extention] from:"+srcFile+" => "+new File(getOutRootDir(),outputFilepath));
				IOHelper.copyAndClose(new FileInputStream(srcFile), new FileOutputStream(new File(getOutRootDir(),outputFilepath)));
				return;
			}
			if(GeneratorHelper.isIgnoreTemplateProcess(srcFile, templateFile)) {
				return;
			}
			
            String outputFilepath = null;
            try {
                outputFilepath = proceeForOutputFilepath(filePathModel,templateFile);
                
                initGeneratorControlProperties(srcFile);
                processTemplateForGeneratorControl(templateModel, templateFile);
                
                if(gg.isIgnoreOutput()) {
                    GLogger.println("[not generate] by gg.isIgnoreOutput()=true on template:"+templateFile);
                    return;
                }
                
                if(outputFilepath != null ) {
                    generateNewFileOrInsertIntoFile(templateFile,outputFilepath, templateModel);
                }
			}catch(Exception e) {
			    throw new RuntimeException("generate oucur error,templateFile is:" + templateFile+" => "+ outputFilepath, e);
			}
		}

		private void initGeneratorControlProperties(File srcFile) throws SQLException {
			gg.setSourceFile(srcFile.getAbsolutePath());
			gg.setSourceFileName(srcFile.getName());
			gg.setSourceDir(srcFile.getParent());
			gg.setOutRoot(getOutRootDir());
			gg.setOutputEncoding(outputEncoding);
			gg.setSourceEncoding(sourceEncoding);
			gg.setMergeLocation(GENERATOR_INSERT_LOCATION);
			
			String dbName = DbTableFactory.getInstance().getConnection().getMetaData().getDatabaseProductName();
			gg.setDatabaseType(dbName == null ? null : dbName.toLowerCase()); //FIXME 提供枚举:oracle mysql,sqlserver
		}
	
		private void processTemplateForGeneratorControl(Map templateModel,String templateFile) throws IOException, TemplateException {
			templateModel.put("gg", gg);
			Template template = getFreeMarkerTemplate(templateFile);
			template.process(templateModel, IOHelper.NULL_WRITER);
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
					GLogger.println("[not-generate]\t test expression '@"+testExpressionKey+"' is false,template:"+templateFile);
						return null;
				}
			}
			if(outputFilePath.endsWith(removeExtensions)) {
				outputFilePath = outputFilePath.substring(0,outputFilePath.length() - removeExtensions.length());
			}
			Configuration conf = GeneratorHelper.newFreeMarkerConfiguration(templateRootDirs, sourceEncoding,"/filepath/processor/");
			return FreemarkerHelper.processTemplateString(outputFilePath,filePathModel,conf);
		}
	
		private Template getFreeMarkerTemplate(String templateName) throws IOException {
			return GeneratorHelper.newFreeMarkerConfiguration(templateRootDirs, sourceEncoding,templateName).getTemplate(templateName);
		}
	
		private void generateNewFileOrInsertIntoFile( String templateFile,String outputFilepath, Map templateModel) throws Exception {
			Template template = getFreeMarkerTemplate(templateFile);
			template.setOutputEncoding(outputEncoding);
			
			File absoluteOutputFilePath = FileHelper.mkdir(gg.getOutRoot(),outputFilepath);
			if(absoluteOutputFilePath.exists()) {
				StringWriter newFileContentCollector = new StringWriter();
				if(GeneratorHelper.isFoundInsertLocation(template, templateModel, absoluteOutputFilePath, newFileContentCollector)) {
					GLogger.println("[insert]\t generate content into:"+outputFilepath);
					IOHelper.saveFile(absoluteOutputFilePath, newFileContentCollector.toString(),outputEncoding);
					return;
				}
			}
			
			if(absoluteOutputFilePath.exists() && !gg.isOverride()) {
				GLogger.println("[not generate]\t by gg.isOverride()=false and outputFile exist:"+outputFilepath);
				return;
			}
			
			GLogger.println("[generate]\t template:"+templateFile+" to "+outputFilepath);
			FreemarkerHelper.processTemplate(template, templateModel, absoluteOutputFilePath,outputEncoding);
		}
	}

	static class GeneratorHelper {
		public static boolean isIgnoreTemplateProcess(File srcFile,String templateFile) {
			if(srcFile.isDirectory() || srcFile.isHidden())
				return true;
			if(templateFile.trim().equals(""))
				return true;
			if(srcFile.getName().toLowerCase().endsWith(".include")){
				GLogger.println("[skip]\t\t endsWith '.include' template:"+templateFile);
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
		public static Configuration newFreeMarkerConfiguration(List<File> templateRootDirs,String defaultEncoding,String templateName) throws IOException {
		    Configuration conf = new Configuration();
			
			FileTemplateLoader[] templateLoaders = new FileTemplateLoader[templateRootDirs.size()];
			for(int i = 0; i < templateRootDirs.size(); i++) {
				templateLoaders[i] = new FileTemplateLoader((File)templateRootDirs.get(i));
			}
			MultiTemplateLoader multiTemplateLoader = new MultiTemplateLoader(templateLoaders);
			
			conf.setTemplateLoader(multiTemplateLoader);
			conf.setNumberFormat("###############");
			conf.setBooleanFormat("true,false");
			conf.setDefaultEncoding(defaultEncoding);
			
			String autoIncludes = new File(new File(templateName).getParent(),"macro.include").getPath();
			List<String> availableAutoInclude = FreemarkerHelper.getAvailableAutoInclude(conf, "macro.include",autoIncludes);
			conf.setAutoIncludes(availableAutoInclude);
			GLogger.debug("[set Freemarker.autoIncludes]"+availableAutoInclude+" for templateName:"+templateName);
			return conf;
		}
	}

}
