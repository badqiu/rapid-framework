package cn.org.rapid_framework.pipeline;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import cn.org.rapid_framework.util.StringTokenizerUtils;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;
/**
 * @see Pipeline
 * @author badqiu
 */
public class FreemarkerPipeline implements Pipeline{
	
	Configuration conf;
	
	public FreemarkerPipeline(){}
	
	public FreemarkerPipeline(Configuration conf) {
		setConfiguration(conf);
	}
	
	public Configuration getConfiguration() {
		return conf;
	}

	public void setConfiguration(Configuration conf) {
		this.conf = conf;
	}
	
	public Writer pipeline(String pipeTemplates[],Object rootMap,Writer writer) throws PipeException   {
		try {
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
			return writer;
		}catch(Exception e) {
			throw new PipeException("process FreeMarker template occer exception,pipeTemplates:"+StringUtils.join(pipeTemplates," | "),e);
		}
	}
	
	public Writer pipeline(String[] pipeTemplates, Map model, Writer writer) throws PipeException {
		return pipeline(pipeTemplates, (Object)model, writer);
	}

	public Writer pipeline(String pipeTemplates,Object rootMap,Writer writer)  {
		return pipeline(StringTokenizerUtils.split(pipeTemplates,Pipeline.PIPELINE_TEMPLATE_SEPERATORS), rootMap, writer);
	}
	
	public Writer pipeline(String pipeTemplates, Map model, Writer writer)throws PipeException {
		return pipeline(pipeTemplates, (Object)model, writer);
	}
	
}
