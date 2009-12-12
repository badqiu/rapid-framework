package cn.org.rapid_framework.pipeline;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

import cn.org.rapid_framework.util.StringTokenizerUtils;
import freemarker.core.Environment;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class VelocityPipeline {
	
	VelocityEngine engine;
	
	public VelocityPipeline(){}
	
	public VelocityPipeline(VelocityEngine engine) {
		setVelocityEngine(engine);
	}
	
	public VelocityEngine getVelocityEngine() {
		return engine;
	}

	public void setVelocityEngine(VelocityEngine engine) {
		this.engine = engine;
	}

	public void pipeline(String pipeTemplates[],Map model,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		
		VelocityContext context = new VelocityContext(new HashMap(model));
		for(int i = 0; i < pipeTemplates.length; i++) {
			String templateName = pipeTemplates[i];
			org.apache.velocity.Template template = engine.getTemplate(templateName);
			if(i == pipeTemplates.length - 1) {
				template.merge(context, writer);
			}else {
				Writer tempOutput = new StringWriter(512);
				template.merge(context, tempOutput);
				context.put(Pipeline.PIPELINE_CONTENT_VAR_NAME, tempOutput.toString());
			}
		}
		
	}
	
	public void pipeline(String pipeTemplates,Map model,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		pipeline(StringTokenizerUtils.split(pipeTemplates,Pipeline.PIPELINE_SEPERATORS), model, writer);
	}
	
	public String pipeline(String pipeTemplates,Map model) throws ResourceNotFoundException, ParseErrorException, Exception  {
		StringWriter result = new StringWriter(512);
		pipeline(pipeTemplates, model, result);
		return result.toString();
	}	
	
	
}
