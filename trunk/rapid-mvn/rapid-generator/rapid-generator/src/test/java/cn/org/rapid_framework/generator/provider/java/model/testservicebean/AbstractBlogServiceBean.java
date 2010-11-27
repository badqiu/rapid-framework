package cn.org.rapid_framework.generator.provider.java.model.testservicebean;

public class AbstractBlogServiceBean {
	
	private EmailServiceBean emailServiceBean;

	public void setEmailServiceBean(EmailServiceBean emailServiceBean) {
		this.emailServiceBean = emailServiceBean;
	}
	
	public void absInvoke() {
		emailServiceBean.say("123");
		emailServiceBean.hello("123",1);
	}
	
}
