package cn.org.rapid_framework.pipeline;

import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;

import cn.org.rapid_framework.util.StringTokenizerUtils;

public class VelocityPipeline implements Pipeline{
	
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

	public Writer pipeline(String pipeTemplates[],Map model,Writer writer) throws PipeException  {
		VelocityContext context = new VelocityContext(new HashMap(model));
		return _pipeline(pipeTemplates,context,writer);
	}

	private Writer _pipeline(String[] pipeTemplates,Context context, Writer writer) throws PipeException {
		try {
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
			return writer;
		}catch(Exception e) {
			throw new PipeException("process Velocity template occer exception,pipeTemplates:"+StringUtils.join(pipeTemplates," | "));
		}
	}
	
	public Writer pipeline(String[] pipeTemplates, Object model, Writer writer) throws PipeException {
		if(model instanceof Context) {
			return _pipeline(pipeTemplates,(Context)model,writer);
		}else {
			throw new UnsupportedOperationException("velocity model instance must be Context or Map object");
		}
	}
	
	public Writer pipeline(String pipeTemplates,Map model,Writer writer) throws PipeException  {
		return pipeline(StringTokenizerUtils.split(pipeTemplates,Pipeline.PIPELINE_TEMPLATE_SEPERATORS), model, writer);
	}

	public Writer pipeline(String pipeTemplates, Object model, Writer writer) throws PipeException {
		if(model instanceof Context) {
			return _pipeline(StringTokenizerUtils.split(pipeTemplates,Pipeline.PIPELINE_TEMPLATE_SEPERATORS),(Context)model,writer);
		}else {
			throw new UnsupportedOperationException("velocity model instance must be Context or Map object");
		}
	}
	
}
