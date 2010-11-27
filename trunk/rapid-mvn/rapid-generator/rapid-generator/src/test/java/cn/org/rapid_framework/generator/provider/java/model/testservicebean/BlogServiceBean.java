package cn.org.rapid_framework.generator.provider.java.model.testservicebean;

public class BlogServiceBean extends AbstractBlogServiceBean{
	public CommentServiceBean csb;
	private EmailServiceBean esb;
	private String name;

	public String testSay(String sex) {
		if(true) {
			esb.say(sex);
		}else {
			csb.bb(sex);
		}
		int v = 1;
		switch(v) {
		case 3:
			esb.hello(sex, 1);
		case 4:
			csb.aa(sex, 1);
		default:
			esb.say(sex);
		}
		return csb.cc(sex);
	}

	public String blogjava(String sex) {
		if(true) {
			csb.bb(sex);
		}else {
			esb.say(sex);
		}
		int v = 1;
		switch(v) {
		case 3:
			esb.hello(sex, 1);
		case 4:
			csb.aa(sex, 1);
		default:
		}
		return csb.cc(sex);
	}
	
	public String chain_call(String sex) {
		if(true) {
			csb.bb(sex);
		}else {
			esb.say(csb.dd(csb.aa("123", 1)));
		}
		return csb.cc(sex);
	}

	public void setCsb(CommentServiceBean csb) {
		this.csb = csb;
	}

	public void setEsb(EmailServiceBean esb) {
		this.esb = esb;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
