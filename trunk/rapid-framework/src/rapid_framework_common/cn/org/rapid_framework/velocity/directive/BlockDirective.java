package cn.org.rapid_framework.velocity.directive;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

import cn.org.rapid_framework.velocity.directive.OverrideDirective.OverrideNodeWrapper;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;

/**
 * @author badqiu
 */
public class BlockDirective extends org.apache.velocity.runtime.directive.Directive{

	@Override
	public String getName() {
		return "block";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,MethodInvocationException {
		String name = Utils.getRequiredArgument(context, node, 0,getName());
		
		OverrideNodeWrapper overrideNode = getOverrideNode(context,name);
		Node topNode = node.jjtGetChild(1);
        if(overrideNode == null) {
        	return topNode.render(context, writer);
        }else {
        	Utils.setParentForTop(new OverrideNodeWrapper(topNode),overrideNode);
        	return overrideNode.render(context, writer);
        }
	}

	private OverrideNodeWrapper getOverrideNode(InternalContextAdapter context,String name) {
		return (OverrideNodeWrapper)context.get(Utils.getOverrideVariableName(name));
	}


	
}
