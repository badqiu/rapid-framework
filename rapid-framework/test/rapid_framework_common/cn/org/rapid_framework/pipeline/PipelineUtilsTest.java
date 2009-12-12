package cn.org.rapid_framework.pipeline;

import org.junit.Test;
import static org.junit.Assert.*;

public class PipelineUtilsTest {
	
	@Test
	public void getPipelinesForPath() {
		String path = "";
		String pipelineName = "_layout_.vm";
		
		String result = PipelineUtils.getPipelinesForPath("/user/blog/sex/",pipelineName);
		assertEquals("/user/blog/sex/_layout_.vm|/user/blog/_layout_.vm|/user/_layout_.vm|/_layout_.vm",result);
		
		result = PipelineUtils.getPipelinesForPath("/user/blog/abc.name",pipelineName);
		assertEquals("/user/blog/_layout_.vm|/user/_layout_.vm|/_layout_.vm",result);
		
		result = PipelineUtils.getPipelinesForPath("user/blog/abc.name",pipelineName);
		assertEquals("user/blog/_layout_.vm|user/_layout_.vm|_layout_.vm",result);
		
		result = PipelineUtils.getPipelinesForPath("\\user\\blog\\abc.name",pipelineName);
		assertEquals("/user/blog/_layout_.vm|/user/_layout_.vm|/_layout_.vm",result);
	}
}
