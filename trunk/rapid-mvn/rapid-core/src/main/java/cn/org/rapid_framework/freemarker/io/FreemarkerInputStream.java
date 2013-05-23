package cn.org.rapid_framework.freemarker.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * 经过freemarker 模板引擎处理过的InputStream
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
public class FreemarkerInputStream extends InputStream {
	private InputStream delegateInput;
	private Configuration conf;
	private ByteArrayInputStream freemarkerProcessedInput;
	/**
	 * 编码
	 */
	private String encoding;
	/**
	 * freemarker模板可以引用的变量
	 */
	private Map<String, Object> variables = new HashMap<String, Object>();
	
	public FreemarkerInputStream(InputStream input) {
		this(input,Util.newDefaultConfirutaion(),null,null);
	}

	public FreemarkerInputStream(InputStream input,Map<String,Object> variables) {
		this(input,Util.newDefaultConfirutaion(),null,variables);
	}
	
	public FreemarkerInputStream(InputStream input,Configuration conf) {
		this(input,conf,null,null);
	}
	
	public FreemarkerInputStream(InputStream input,Configuration conf,Map<String,Object> variables) {
		this(input,conf,null,variables);
	}
	
	public FreemarkerInputStream(InputStream input,String encoding) {
		this(input,Util.newDefaultConfirutaion(),encoding,null);
	}

	public FreemarkerInputStream(InputStream input,String encoding,Map<String,Object> variables) {
		this(input,Util.newDefaultConfirutaion(),encoding,variables);
	}
	
	public FreemarkerInputStream(InputStream input,Configuration conf,String encoding,Map<String,Object> variables) {
		this.delegateInput = input;
		this.conf = conf;
		this.encoding = encoding;
		this.variables = variables;
		this.freemarkerProcessedInput = processByFreemarker(input);
	}
	
	private ByteArrayInputStream processByFreemarker(InputStream input) {
		try {
			
			Map<String,Object> rootMap = newTemplateVariables();
			
			InputStreamReader reader = StringUtils.isEmpty(encoding) ? new InputStreamReader(input) : new InputStreamReader(input,encoding);
			Template template = new Template(""+input, reader, conf);
			StringWriter cachedTemplateOutput = new StringWriter();
			template.process(rootMap, cachedTemplateOutput);
			return new ByteArrayInputStream(StringUtils.isEmpty(encoding) ? cachedTemplateOutput.toString().getBytes() : cachedTemplateOutput.toString().getBytes(encoding));
		}catch(IOException e) {
			throw new RuntimeException(e);
		}catch(TemplateException e) {
			throw new RuntimeException(e);
		}
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
	
	@Override
	public int hashCode() {
		return freemarkerProcessedInput.hashCode();
	}
	
	@Override
	public int read(byte[] b) throws IOException {
		return freemarkerProcessedInput.read(b);
	}

	@Override
	public boolean equals(Object obj) {
		return freemarkerProcessedInput.equals(obj);
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		return freemarkerProcessedInput.read(b, off, len);
	}

	@Override
	public long skip(long n) throws IOException {
		return freemarkerProcessedInput.skip(n);
	}

	@Override
	public int available() throws IOException {
		return freemarkerProcessedInput.available();
	}

	@Override
	public String toString() {
		return freemarkerProcessedInput.toString();
	}

	@Override
	public void close() throws IOException {
		freemarkerProcessedInput.close();
		delegateInput.close();
	}

	@Override
	public void mark(int readlimit) {
		freemarkerProcessedInput.mark(readlimit);
	}

	@Override
	public void reset() throws IOException {
		freemarkerProcessedInput.reset();
	}

	@Override
	public boolean markSupported() {
		return freemarkerProcessedInput.markSupported();
	}

	@Override
	public int read() throws IOException {
		return freemarkerProcessedInput.read();
	}

}
