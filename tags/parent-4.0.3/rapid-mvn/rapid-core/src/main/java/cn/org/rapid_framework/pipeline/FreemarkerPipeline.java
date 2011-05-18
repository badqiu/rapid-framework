package cn.org.rapid_framework.pipeline;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.util.StringTokenizerUtils;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;
/**
 * @see Pipeline
 * @author badqiu
 */
public class FreemarkerPipeline implements Pipeline{
	
	private Configuration conf;
	private int bufferSize = DEFAULT_PIPELINE_BUFFER_SIZE;
	
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
	
	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}
	
	public Writer pipeline(String pipeTemplates[],Object rootMap,Writer writer) throws PipeException   {
		try {
			Map globalContext = new HashMap();
			for(int i = 0; i < pipeTemplates.length; i++) {
				String templateName = pipeTemplates[i];
				boolean isLastTemplate = i == pipeTemplates.length - 1;
				try {
					Template template = conf.getTemplate(templateName);
					if(isLastTemplate) {
						Environment env = template.createProcessingEnvironment(rootMap, writer);
						env.getCurrentNamespace().putAll(globalContext);
						env.process();
					}else {
						Writer tempOutput = new StringWriter(bufferSize);
						Environment env = template.createProcessingEnvironment(rootMap, tempOutput);
						env.getCurrentNamespace().putAll(globalContext);
						env.process();
						globalContext.putAll(env.getCurrentNamespace().toMap());
						globalContext.put(Pipeline.PIPELINE_CONTENT_VAR_NAME, tempOutput.toString());
					}
				}catch(Exception e){
					handleException(e,templateName,isLastTemplate);
				}
			}
			return writer;
		}catch(Exception e) {
			throw new PipeException("process FreeMarker template occer exception,pipeTemplates:"+StringUtils.join(pipeTemplates," | "),e);
		}
	}
	
	public void handleException(Exception e, String templateName,boolean isLastTemplate) throws Exception {
		throw e;
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
