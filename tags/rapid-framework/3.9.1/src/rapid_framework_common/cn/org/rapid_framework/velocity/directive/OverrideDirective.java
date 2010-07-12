package cn.org.rapid_framework.velocity.directive;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;

/**
 * @author badqiu
 */
public class OverrideDirective extends org.apache.velocity.runtime.directive.Directive{

	@Override
	public String getName() {
		return "override";
	}

	@Override
	public int getType() {
		return BLOCK;
	}

	@Override
	public boolean render(InternalContextAdapter context, Writer writer, Node node)
			throws IOException, ResourceNotFoundException, ParseErrorException,MethodInvocationException {
		
		String name = Utils.getRequiredArgument(context, node, 0,getName());
        if(isOverrided(context,name)) {
        	return true;
        }
        
		Node body = node.jjtGetChild(1);
    	context.put(Utils.getOverrideVariableName(name), body);
		return true;
	}

	private boolean isOverrided(InternalContextAdapter context,String name) {
		return context.get(Utils.getOverrideVariableName(name)) != null;
	}

}
