package cn.org.rapid_framework.freemarker.template;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.core.Environment;
import freemarker.ext.beans.BeansWrapper;
import freemarker.ext.beans.StringModel;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author badqiu
 */
public class ExtendsDirective implements TemplateDirectiveModel {
	
	public String getName() {
		return "extends";
	}
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = Utils.getRequiredParam(params, "name", env);
		String encoding = Utils.getParam(params, "encoding", env);
		env.include(name, encoding, true);
	}

}
