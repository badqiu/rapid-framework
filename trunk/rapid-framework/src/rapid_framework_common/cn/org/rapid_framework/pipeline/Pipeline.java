package cn.org.rapid_framework.pipeline;

import java.io.File;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.springframework.util.ResourceUtils;

import cn.org.rapid_framework.util.StringTokenizerUtils;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 模板之间可以完成类似操作系统的管道操作
 * 
 * <pre>
 * 即将前一个模板的输出作为后一个模板的输入
 * 如
 * 
 * first.flt|second.flt|three.flt
 * 
 * second.flt模板要引用first.flt模板的输出的变量 名称为:${pipeline_content}
 * </pre>
 * 
 * <h2>管道概览</h2>
 * 管道定义为: first.flt | secnond.flt
 * <br />
 * 
 * <h3>first.flt模板内容</h3>
 * <pre>
 * &lt;div>
 *  first
 * &lt;/div>
 * </pre>
 * <h3>second.flt模板内容,${pipeline_content}为前一个模板生成的内容</h3>
 * <pre>
 * &lt;html>
 *   &lt;body>
 *    ${pipeline_content}
 *   &lt;/body>
 * &lt;/html>
 * </pre>
 * <h3>输出</h3>
 * <pre>
 * &lt;html>
 *   &lt;body>
 *       &lt;div>
 *         first
 *      &lt;/div>
 *   &lt;/body>
 * &lt;/html>
 * </pre>
 * 
 * <h2>API 使用</h2>
 * 
 * Pipeline.pipeline(VelocityEngine,"first.vm | second.vm | three.vm", model, writer);
 * 
 * <br />
 * 
 * @author badqiu
 *
 */
public class Pipeline {
	
	public static final String PIPELINE_CONTENT_VAR_NAME = "pipeline_content";
	private static final String PIPELINE_SEPERATORS = ",| ";

	public void pipeline(VelocityEngine engine,String pipeTemplates[],Map model,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		
		VelocityContext context = new VelocityContext(model);
		for(int i = 0; i < pipeTemplates.length; i++) {
			String templateName = pipeTemplates[i];
			org.apache.velocity.Template template = engine.getTemplate(templateName);
			if(i == pipeTemplates.length - 1) {
				template.merge(context, writer);
			}else {
				Writer out = new StringWriter(512);
				template.merge(context, out);
				context.put(PIPELINE_CONTENT_VAR_NAME, out.toString());
			}
		}
		
	}
	
	public void pipeline(VelocityEngine engine,String pipeTemplates,Map model,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		pipeline(engine, StringTokenizerUtils.split(pipeTemplates,PIPELINE_SEPERATORS), model, writer);
	}
	
	public String pipeline(VelocityEngine engine,String pipeTemplates,Map model) throws ResourceNotFoundException, ParseErrorException, Exception  {
		StringWriter result = new StringWriter(512);
		pipeline(engine, pipeTemplates, model, result);
		return result.toString();
	}	
	
	public void pipeline(Configuration conf,String pipeTemplates[],Map model,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		
		for(int i = 0; i < pipeTemplates.length; i++) {
			String templateName = pipeTemplates[i];
			Template template = conf.getTemplate(templateName);
			if(i == pipeTemplates.length - 1) {
				template.process(model, writer);
			}else {
				Writer out = new StringWriter(512);
				template.process(model, writer);
				model.put(PIPELINE_CONTENT_VAR_NAME, out.toString());
			}
		}
		
	}
	
	public void pipeline(Configuration conf,String pipeTemplates,Map model,Writer writer) throws ResourceNotFoundException, ParseErrorException, Exception  {
		pipeline(conf, StringTokenizerUtils.split(pipeTemplates,PIPELINE_SEPERATORS), model, writer);
	}
	
	public String pipeline(Configuration conf,String pipeTemplates,Map model) throws ResourceNotFoundException, ParseErrorException, Exception  {
		StringWriter result = new StringWriter(512);
		pipeline(conf, pipeTemplates, model, result);
		return result.toString();
	}
	
}
