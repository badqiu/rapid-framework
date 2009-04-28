package cn.org.rapid_framework.generator;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import cn.org.rapid_framework.generator.util.FileHelper;
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
	private static final String WEBAPP_GENERATOR_INSERT_LOCATION = "webapp-generator-insert-location";
	private List templateRootDirs = new ArrayList();
	public String outRootDir;
	
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
	
	public void generateByModelProvider(IGeneratorModelProvider modelProvider) throws Exception {
		if(templateRootDirs.size() == 0) throw new IllegalStateException("'templateRootDirs' is empty");
		
		System.out.println("***************************************************************");
		System.out.println("* BEGIN generate "+modelProvider.getDisaplyText());
		System.out.println("***************************************************************");
		for(int i = 0; i < this.templateRootDirs.size(); i++) {
			File templateRootDir = (File)templateRootDirs.get(i);
			generateByModelProvider(templateRootDir,modelProvider);
		}
	}
	
	private void generateByModelProvider(File templateRootDir, IGeneratorModelProvider modelProvider) throws Exception {
		if(templateRootDir == null) throw new IllegalStateException("'templateRootDir' must be not null");
		System.out.println("-------------------load template from templateRootDir = '"+templateRootDir.getAbsolutePath()+"'");
		
		List templateFiles = new ArrayList();
		FileHelper.listFiles(templateRootDir, templateFiles);
		
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
				Map map = getFilePathDataModel(modelProvider);
				Object expressionValue = map.get(testExpressionKey);
				if(!"true".equals(expressionValue.toString())) {
					System.out.println("[not-generate]\t test expression '@"+testExpressionKey+"' is false,template:"+templateRelativePath);
					continue;
				}
			}
			try {
				generateNewFileOrInsertIntoFile(modelProvider, newFreeMarkerConfiguration(), templateRelativePath,outputFilePath);
			}catch(Exception e) {
				throw new RuntimeException("generate '"+modelProvider.getDisaplyText()+"' oucur error,template is:"+templateRelativePath,e);
			}
		}
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
		return config;
	}

	private void generateNewFileOrInsertIntoFile(IGeneratorModelProvider modelProvider, Configuration config, String templateRelativePath,String outputFilePath) throws Exception {
		Template template = config.getTemplate(templateRelativePath);
		
		String targetFilename = getTargetFilename(modelProvider, outputFilePath);
		
		Map templateDataModel = getTemplateDataModel(modelProvider);
		File absoluteOutputFilePath = getAbsoluteOutputFilePath(targetFilename);
		if(absoluteOutputFilePath.exists()) {
			StringWriter newFileContentCollector = new StringWriter();
			if(isFoundInsertLocation(template, templateDataModel, absoluteOutputFilePath, newFileContentCollector)) {
				System.out.println("[insert]\t generate content into:"+targetFilename);
				IOHelper.saveFile(absoluteOutputFilePath, newFileContentCollector.toString());
				return;
			}
		}
		
		System.out.println("[generate]\t template:"+templateRelativePath+" to "+targetFilename);
		saveNewOutputFileContent(template, templateDataModel, absoluteOutputFilePath);
	}

	private String getTargetFilename(IGeneratorModelProvider modelProvider, String templateFilepath) throws Exception {
		Map fileModel = getFilePathDataModel(modelProvider);
		StringWriter out = new StringWriter();
		Template template = new Template("filePath",new StringReader(templateFilepath),newFreeMarkerConfiguration());
		template.process(fileModel, out);
		return out.toString();
	}
	/**
	 * 得到生成"文件目录/文件路径"的Model
	 **/
	private Map getFilePathDataModel(IGeneratorModelProvider modelProvider) throws Exception {
		Map model = new HashMap();
		model.putAll(PropertiesProvider.getProperties()); //generator.properties的内容
		modelProvider.mergeFilePathModel(model);
		return model;
	}
	/**
	 * 得到FreeMarker的Model
	 **/
	private Map getTemplateDataModel(IGeneratorModelProvider modelProvider) throws Exception {
		Map model = new HashMap();
		model.putAll(PropertiesProvider.getProperties()); //generator.properties的内容
		modelProvider.mergeTemplateModel(model);
		return model;
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
			if(!isFoundInsertLocation && line.indexOf(WEBAPP_GENERATOR_INSERT_LOCATION) >= 0) {
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
		FileWriter out = new FileWriter(outputFile);
		template.process(model,out);
		out.close();
	}

	public void clean() throws IOException {
		String outRoot = getOutRootDir();
		FileUtils.deleteDirectory(new File(outRoot));
		System.out.println("[Delete Dir]	"+outRoot);
	}

	private String getOutRootDir() {
		if(outRootDir == null) throw new IllegalStateException("'outRootDir' property must be not null.");
		return outRootDir;
	}
	
}
