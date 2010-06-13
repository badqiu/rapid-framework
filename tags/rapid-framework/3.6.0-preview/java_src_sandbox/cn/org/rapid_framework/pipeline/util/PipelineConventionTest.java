package cn.org.rapid_framework.pipeline.util;

import org.junit.Test;

import cn.org.rapid_framework.pipeline.util.PipelineConvention;
import static org.junit.Assert.*;

public class PipelineConventionTest {
	
	@Test
	public void getPipelinesForPath() {
		String path = "";
		String pipelineName = "_layout_.vm";
		
		String result = PipelineConvention.getPipelinesForPath("/user/blog/sex/",pipelineName);
		assertEquals("/user/blog/sex/_layout_.vm|/user/blog/_layout_.vm|/user/_layout_.vm|/_layout_.vm",result);
		
		result = PipelineConvention.getPipelinesForPath("/user/blog/abc.name",pipelineName);
		assertEquals("/user/blog/_layout_.vm|/user/_layout_.vm|/_layout_.vm",result);
		
		result = PipelineConvention.getPipelinesForPath("user/blog/abc.name",pipelineName);
		assertEquals("user/blog/_layout_.vm|user/_layout_.vm|_layout_.vm",result);
		
		result = PipelineConvention.getPipelinesForPath("\\user\\blog\\abc.name",pipelineName);
		assertEquals("/user/blog/_layout_.vm|/user/_layout_.vm|/_layout_.vm",result);
	}
}
