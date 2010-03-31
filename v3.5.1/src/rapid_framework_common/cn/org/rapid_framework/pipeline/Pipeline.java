package cn.org.rapid_framework.pipeline;

import java.io.IOException;
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
import freemarker.template.TemplateException;

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
 * <pre>
 * new VelocityPipeline(velocityEngine).pipeline("first.vm | second.vm | three.vm", model, writer); <br />
 * new FreemarkerPipeline(configuration).pipeline("first.vm | second.vm | three.vm", model, writer);
 * </pre>
 * <br />
 * 
 * @author badqiu
 *
 */
public interface Pipeline {
	
	public static final String PIPELINE_CONTENT_VAR_NAME = "pipeline_content";
	
	public static final String PIPELINE_TEMPLATE_SEPERATORS = ",| ";
	
	public static final int DEFAULT_PIPELINE_BUFFER_SIZE = 4096;
	
	public Writer pipeline(String pipeTemplates,Map model,Writer writer) throws PipeException;
	
	public Writer pipeline(String pipeTemplates,Object model,Writer writer) throws PipeException;
	
	// TODO expose interface for handle pipeline exception, etc: TemplateNotFound
}
