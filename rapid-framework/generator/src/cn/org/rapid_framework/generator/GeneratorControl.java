package cn.org.rapid_framework.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.swing.JOptionPane;

import cn.org.rapid_framework.generator.util.FileHelper;
import cn.org.rapid_framework.generator.util.GLogger;
import cn.org.rapid_framework.generator.util.IOHelper;
import cn.org.rapid_framework.generator.util.SystemHelper;
import freemarker.ext.dom.NodeModel;

/**
 * 生成器模板控制器,用于模板中可以控制生成器执行相关控制操作
 * 如: 是否覆盖目标文件
 * 
 * <pre>
 * 使用方式:
 * 可以在freemarker或是veloctiy中直接控制模板的生成
 * 
 * ${gg.generateFile('d:/g_temp.log','info_from_generator')}
 * ${gg.setIgnoreOutput(true)}
 * </pre>
 * 
 * ${gg.setIgnoreOutput(true)}将设置为true如果不生成
 * 
 * @author badqiu
 *
 */
public class GeneratorControl {
	private boolean isOverride = true; 
	private boolean isAppend = false;
	private boolean ignoreOutput = false; // or isGenerate
	private boolean isMergeIfExists = true;
	private String mergeLocation; //no pass
	private String outRoot; 
	private String outputEncoding; //no pass
	private String sourceFile; 
	private String sourceDir; 
	private String sourceFileName; 
	private String sourceEncoding; //no pass //? 难道process两次确定sourceEncoding
	
	
	
	/** load xml data */
	public NodeModel loadXml(String file,boolean ignoreError) {
		try {
			return NodeModel.parse(new File(file));
		} catch (Exception e) {
			GLogger.error("loadXml error,file:"+file+" cause:"+e);
			if(ignoreError) {
				return null;
			}else {
				throw new IllegalArgumentException("loadXml error,file:"+file+" cause:"+e,e);
			}
		}
	}

	/** load Properties data */
	public Properties loadProperties(String file,boolean ignoreError) {
		try {
			Properties p = new Properties();
			FileInputStream in = new FileInputStream(new File(file));
			p.load(in);
			in.close();
			return p;
		} catch (Exception e) {
			GLogger.error("loadProperties error,file:"+file+" cause:"+e);
			if(ignoreError) {
				return null;
			}else {
				throw new IllegalArgumentException("loadProperties error,file:"+file+" cause:"+e,e);
			}
		}
	}

    public void generateFile(String outputFile,String content) {
       generateFile(outputFile,content,false);
    }
	/**
	 * 生成文件   
	 * @param outputFile
	 * @param content
	 * @param append
	 */
	public void generateFile(String outputFile,String content,boolean append) {
		//TODO /root/hello.txt使用绝对路径, root/hello.txt使用相对路径
		try {
			if(deleteGeneratedFile) {
				GLogger.println("[delete gg.generateFile()] file:"+outputFile+" by template:"+getSourceFile());
				new File(outputFile).delete();
			}else {
				File file = new File(outputFile);
				FileHelper.parnetMkdir(file);
				GLogger.println("[gg.generateFile()] outputFile:"+outputFile+" append:"+append+" by template:"+getSourceFile());
				IOHelper.saveFile(file, content,getOutputEncoding(),append);
			}
		} catch (Exception e) {
			GLogger.warn("gg.generateFile() occer error,outputFile:"+outputFile+" caused by:"+e,e);
			throw new RuntimeException("gg.generateFile() occer error,outputFile:"+outputFile+" caused by:"+e,e);
		}
	}
	
	public boolean isOverride() {
		return isOverride;
	}
	/**如果目标文件存在,控制是否要覆盖文件 */
	public void setOverride(boolean isOverride) {
		this.isOverride = isOverride;
	}

	public boolean isIgnoreOutput() {
		return ignoreOutput;
	}
	/** 控制是否要生成文件  */
	public void setIgnoreOutput(boolean ignoreOutput) {
		this.ignoreOutput = ignoreOutput;
	}

	public boolean isMergeIfExists() {
		return isMergeIfExists;
	}

	public void setMergeIfExists(boolean isMergeIfExists) {
		this.isMergeIfExists = isMergeIfExists;
	}

	public String getMergeLocation() {
		return mergeLocation;
	}

	public void setMergeLocation(String mergeLocation) {
		this.mergeLocation = mergeLocation;
	}

	public String getOutRoot() {
		return outRoot;
	}
	/** 生成的输出根目录 */
	public void setOutRoot(String outRoot) {
		this.outRoot = outRoot;
	}

	public String getOutputEncoding() {
		return outputEncoding;
	}
	/** 设置输出encoding */
	public void setOutputEncoding(String outputEncoding) {
		this.outputEncoding = outputEncoding;
	}
	/** 得到源文件 */
	public String getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(String sourceFile) {
		this.sourceFile = sourceFile;
	}

	/** 得到源文件所在目录 */
	public String getSourceDir() {
		return sourceDir;
	}

	public void setSourceDir(String sourceDir) {
		this.sourceDir = sourceDir;
	}

	/** 得到源文件的文件名称 */
	public String getSourceFileName() {
		return sourceFileName;
	}

	public void setSourceFileName(String sourceFileName) {
		this.sourceFileName = sourceFileName;
	}

	/** 得到源文件的encoding */
	public String getSourceEncoding() {
		return sourceEncoding;
	}

	public void setSourceEncoding(String sourceEncoding) {
		this.sourceEncoding = sourceEncoding;
	}
	
	/** 得到property,查到不到则使用defaultValue */
	public String getProperty(String key,String defaultValue){
		return GeneratorProperties.getProperty(key, defaultValue);
	}

//	public String getRequiredProperty(String key){
//		return GeneratorProperties.getRequiredProperty(key);
//	}

	/** 让用户输入property,windows则弹出输入框，linux则为命令行输入 */
	public String getInputProperty(String key) throws IOException {
		return getInputProperty(key, "Please input value for "+key+":");
	}
	
	public String getInputProperty(String key,String message) throws IOException {
		String v = GeneratorProperties.getProperty(key);
		if(v == null) {
			if(SystemHelper.isWindowsOS) {
				v = JOptionPane.showInputDialog(null,message,"template:"+getSourceFileName(),JOptionPane.OK_OPTION);
			}else {
				System.out.print("template:"+getSourceFileName()+","+message);
				v = new BufferedReader(new InputStreamReader(System.in)).readLine();
			}
			GeneratorProperties.setProperty(key, v);
		}
		return v;
	}
	
	boolean deleteGeneratedFile = false;
}
