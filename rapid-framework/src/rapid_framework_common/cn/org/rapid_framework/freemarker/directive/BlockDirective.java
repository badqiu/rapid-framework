package cn.org.rapid_framework.freemarker.directive; 

import java.io.IOException;
import java.util.Map;

import cn.org.rapid_framework.freemarker.directive.OverrideDirective.TemplateDirectiveBodyOverrideWraper;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

/**
 * @author badqiu
 */
public class BlockDirective implements TemplateDirectiveModel{
	public final static String DIRECTIVE_NAME = "block";
	
	public void execute(Environment env,
            Map params, TemplateModel[] loopVars,
            TemplateDirectiveBody body) throws TemplateException, IOException {
		String name = DirectiveUtils.getRequiredParam(params, "name");
		TemplateDirectiveBodyOverrideWraper overrideBody = DirectiveUtils.getOverrideBody(env, name);
		if(overrideBody == null && body != null) {
			body.render(env.getOut());
		}else {
			setTopBodyForParentBody(env, body, overrideBody);
			overrideBody.render(env.getOut());
		}
	}

	private void setTopBodyForParentBody(Environment env,
			TemplateDirectiveBody topBody,
			TemplateDirectiveBodyOverrideWraper overrideBody) {
		TemplateDirectiveBodyOverrideWraper parent = overrideBody;
		while(parent.parentBody != null) {
			parent = parent.parentBody;
		}
		parent.parentBody = new TemplateDirectiveBodyOverrideWraper(topBody,env);
	}
		
}
