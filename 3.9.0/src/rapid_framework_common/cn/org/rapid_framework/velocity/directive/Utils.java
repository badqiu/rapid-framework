package cn.org.rapid_framework.velocity.directive;

import org.apache.velocity.context.InternalContextAdapter;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.runtime.parser.node.Node;
import org.apache.velocity.runtime.parser.node.SimpleNode;

/**
 * @author badqiu
 */
class Utils {
	
	static String BLOCK = "__vm_override__";
	
	static String getOverrideVariableName(String name) {
		return BLOCK + name;
	}
	
	static String getRequiredArgument(InternalContextAdapter context,Node node,int argumentIndex,String directive) throws ParseErrorException {
		SimpleNode sn_value = (SimpleNode)node.jjtGetChild(argumentIndex);
        if ( sn_value == null){
        	throw new ParseErrorException("required argument is null with directive:#"+directive+"()");
        }
        
		String value = (String)sn_value.value(context);
		if ( value == null){
			throw new ParseErrorException("required argument is null with directive:#"+directive+"()");
        }
		return value;
	}
	
}
