package cn.org.rapid_framework.pipeline;

import java.util.Queue;
import java.util.Stack;

import org.apache.commons.lang.StringUtils;

import cn.org.rapid_framework.util.StringTokenizerUtils;
import flex.messaging.io.ArrayList;

public class PipelineUtils {

	public static String getPipelinesForPath(String path, String pipelineName) {
		ArrayList list = getPipelineListForPath(path, pipelineName);
		return StringUtils.join(list.iterator(),"|");
	}

	public static ArrayList getPipelineListForPath(String path,String pipelineName) {
		String[] array = StringTokenizerUtils.split(path, "/\\");
		boolean endWith = path.endsWith("/") || path.endsWith("\\");
		boolean startsWith = path.startsWith("/") || path.startsWith("\\");
		
		Stack result = new Stack();
		StringBuilder dirLevel = null;
		if(startsWith) {
			dirLevel = new StringBuilder("/");
			result.add("/"+pipelineName);
		}else {
			result.add(pipelineName);
			dirLevel = new StringBuilder();
		}
		for(int i = 0; i < array.length; i++) {
			if(endWith) {
				dirLevel.append(array[i]);
				dirLevel.append("/");
				String p = dirLevel+pipelineName;
				result.add(p);
			}else {
				if(i != array.length - 1) {
					dirLevel.append(array[i]);
					dirLevel.append("/");
					String p = dirLevel+pipelineName;
					result.add(p);
				}
			}
		}
		
		return toArrayList(result);
	}

	private static ArrayList toArrayList(Stack stack) {
		ArrayList result = new ArrayList(stack.size());
		while(!stack.isEmpty()) {
			result.add(stack.pop());
		}
		return result;
	}
	
	private static ArrayList toArrayList(Queue queue) {
		ArrayList result = new ArrayList(queue.size());
		while(!queue.isEmpty()) {
			result.add(queue.poll());
		}
		return result;
	}

}
