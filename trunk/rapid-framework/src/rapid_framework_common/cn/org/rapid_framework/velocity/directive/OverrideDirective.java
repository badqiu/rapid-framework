package cn.org.rapid_framework.velocity.directive;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

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
		OverrideNodeWrapper override = (OverrideNodeWrapper)context.get(Utils.getOverrideVariableName(name));
        if(override == null) {
    		Node body = node.jjtGetChild(1);
        	context.put(Utils.getOverrideVariableName(name), new OverrideNodeWrapper(body));
        }else {
        	override.parentNode = new OverrideNodeWrapper(node.jjtGetChild(1));
        }
        return true;
	}

	private boolean isOverrided(InternalContextAdapter context,String name) {
		return context.get(Utils.getOverrideVariableName(name)) != null;
	}
	
	static class OverrideNodeWrapper extends SimpleNode {
		Node current;
		OverrideNodeWrapper parentNode;
		public OverrideNodeWrapper(Node node) {
			super(1);
			this.current = node;
		}
		public boolean render(InternalContextAdapter context, Writer writer)
				throws IOException, MethodInvocationException,
				ParseErrorException, ResourceNotFoundException {
			OverrideNodeWrapper preNode = (OverrideNodeWrapper)context.get(Utils.OVERRIDE_CURRENT_NODE);
			try {
				context.put(Utils.OVERRIDE_CURRENT_NODE,this);
				return current.render(context, writer);
			}finally {
				context.put(Utils.OVERRIDE_CURRENT_NODE,preNode);
			}
		}
	}

}
