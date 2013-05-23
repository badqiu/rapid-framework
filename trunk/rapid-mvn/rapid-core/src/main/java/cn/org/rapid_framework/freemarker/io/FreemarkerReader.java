package cn.org.rapid_framework.freemarker.io;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 经过freemarker 模板引擎处理过的Reader
 * 
 * <pre>
 * 默认可以引用的变量有: 
 * 1. 系统属性:	System.getProperties(),直接引用, 点号.用下划线_代替,如 java.version 用 ${java_version}引用
 * 2. 环境变量: env.JAVA_HOME
 * </pre>
 * 
 * @author badqiu
 * 
 */
public class FreemarkerReader extends Reader {
	/**
	 * freemarker配置
	 */
	private Configuration conf;

	private Reader delegateReader;
	private StringReader freemarkerProcessedReader;
	/**
	 * freemarker模板可以引用的变量
	 */
	private Map<String, Object> variables = new HashMap<String, Object>();

	public FreemarkerReader(Reader reader) {
		this(reader, Util.newDefaultConfirutaion());
	}

	public FreemarkerReader(Reader reader,Map<String, Object> variables) {
		this(reader, Util.newDefaultConfirutaion(),variables);
	}
	
	public FreemarkerReader(Reader reader, Configuration conf){
		this(reader,conf,null);
	}
	
	public FreemarkerReader(Reader reader, Configuration conf,Map<String, Object> variables) {
		this.conf = conf;
		this.delegateReader = reader;
		this.variables = variables;
		this.freemarkerProcessedReader = processByFreemarker(reader);
	}

	private StringReader processByFreemarker(Reader reader) {
		try {

			Map rootMap = newTemplateVariables();
			
			Template template = newTemplate(reader);

			StringWriter cachedTemplateOutput = new StringWriter();
			template.process(rootMap, cachedTemplateOutput);
			return new StringReader(cachedTemplateOutput.toString());
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (TemplateException e) {
			throw new RuntimeException("FreemarkerTemplateException", e);
		}
	}

	protected Template newTemplate(Reader reader)
			throws IOException {
		return new Template("" + reader, reader, conf);
	}

	protected Map newTemplateVariables() {
		Map rootMap = new HashMap();
		rootMap.putAll(Util.relaceAllKeyChar(System.getProperties(), '.','_'));
		rootMap.put("env", System.getenv());
		if(variables != null) {
			rootMap.putAll(variables);
		}
		return rootMap;
	}

	public int read() throws IOException {
		return freemarkerProcessedReader.read();
	}

	public int read(char[] cbuf, int off, int len) throws IOException {
		return freemarkerProcessedReader.read(cbuf, off, len);
	}

	public int hashCode() {
		return freemarkerProcessedReader.hashCode();
	}

	public int read(CharBuffer target) throws IOException {
		return freemarkerProcessedReader.read(target);
	}

	public long skip(long ns) throws IOException {
		return freemarkerProcessedReader.skip(ns);
	}

	public boolean ready() throws IOException {
		return freemarkerProcessedReader.ready();
	}

	public int read(char[] cbuf) throws IOException {
		return freemarkerProcessedReader.read(cbuf);
	}

	public boolean equals(Object obj) {
		return freemarkerProcessedReader.equals(obj);
	}

	public boolean markSupported() {
		return freemarkerProcessedReader.markSupported();
	}

	public void mark(int readAheadLimit) throws IOException {
		freemarkerProcessedReader.mark(readAheadLimit);
	}

	public void reset() throws IOException {
		freemarkerProcessedReader.reset();
	}

	public void close() throws IOException {
		freemarkerProcessedReader.close();
		delegateReader.close();
	}

	public String toString() {
		return freemarkerProcessedReader.toString();
	}

}
