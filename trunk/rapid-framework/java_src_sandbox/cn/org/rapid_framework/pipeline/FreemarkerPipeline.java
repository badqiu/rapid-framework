package cn.org.rapid_framework.pipeline;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import cn.org.rapid_framework.util.StringTokenizerUtils;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreemarkerPipeline {
	
	Configuration conf;
	
	public FreemarkerPipeline(){}
	
	public FreemarkerPipeline(Configuration conf) {
		setConfiguration(conf);
	}
	
	public void pipeline(String pipeTemplates[],Object rootMap,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		
		Map globalContext = new HashMap();
		for(int i = 0; i < pipeTemplates.length; i++) {
			String templateName = pipeTemplates[i];
			Template template = conf.getTemplate(templateName);
			if(i == pipeTemplates.length - 1) {
				Environment env = template.createProcessingEnvironment(rootMap, writer);
				env.getCurrentNamespace().putAll(globalContext);
				env.process();
			}else {
				Writer tempOutput = new StringWriter(512);
				Environment env = template.createProcessingEnvironment(rootMap, tempOutput);
				env.getCurrentNamespace().putAll(globalContext);
				env.process();
				globalContext.putAll(env.getCurrentNamespace().toMap());
				globalContext.put(Pipeline.PIPELINE_CONTENT_VAR_NAME, tempOutput.toString());
			}
		}
		
	}
	
	public void pipeline(String pipeTemplates,Object rootMap,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		pipeline(StringTokenizerUtils.split(pipeTemplates,Pipeline.PIPELINE_SEPERATORS), rootMap, writer);
	}
	
	public String pipeline(String pipeTemplates,Object rootMap) throws ResourceNotFoundException, ParseErrorException, Exception  {
		StringWriter result = new StringWriter(512);
		pipeline(pipeTemplates, rootMap, result);
		return result.toString();
	}

	public Configuration getConfiguration() {
		return conf;
	}

	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}
	
}
