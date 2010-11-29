package cn.org.rapid_framework.generator.provider.java.model.testservicebean;

public class AbstractBlogServiceBean {
	
	private TopicServiceBean topicServiceBean;

	public void setTopicServiceBean(TopicServiceBean emailServiceBean) {
		this.topicServiceBean = topicServiceBean;
	}
	
	public void absInvoke() {
	    topicServiceBean.a1();
	    topicServiceBean.a2("123");
	}
	
}
