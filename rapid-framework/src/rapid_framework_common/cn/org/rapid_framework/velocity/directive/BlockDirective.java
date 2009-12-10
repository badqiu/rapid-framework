package cn.org.rapid_framework.velocity.directive;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.MethodInvocationException;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

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
		SimpleNode sn_name = (SimpleNode)node.jjtGetChild(0);
		
        if ( sn_name == null){
            rsvc.getLog().error("#block() null argument");
            return false;
        }
        
		String name = (String)sn_name.value(context);
		
        if(isOverrided(context,name)) {
        	String overrideContent = (String)context.get(Utils.getOverrideVariableName(name));
        	writer.write(overrideContent);
        }else {
    		Node body = node.jjtGetChild(1);
//    		String body_tpl = body.literal();
            body.render(context, writer);
        }
        
        return true;
	}

	private boolean isOverrided(InternalContextAdapter context,String name) {
		return context.get(Utils.getOverrideVariableName(name)) != null;
	}
}
