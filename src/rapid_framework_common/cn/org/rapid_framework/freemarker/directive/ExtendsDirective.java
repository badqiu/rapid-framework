package cn.org.rapid_framework.freemarker.directive; 

import java.io.IOException;
import java.util.Map;

import freemarker.cache.TemplateCache;
import freemarker.core.Environment;
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
		
		String tempTemplatePath = env.getTemplate().getName();
        String templatePath = tempTemplatePath.lastIndexOf('/') == -1 ? "" : tempTemplatePath.substring(0, tempTemplatePath.lastIndexOf('/') + 1);
        
		String name = DirectiveUtils.getRequiredParam(params, "name");
		String encoding = DirectiveUtils.getParam(params, "encoding",null);
		String includeTemplateName = TemplateCache.getFullTemplatePath(env, templatePath, name);
		env.include(includeTemplateName, encoding, true);
	}

}
