package cn.org.rapid_framework.generator.provider.java.model.testservicebean;

public class EmailServiceBean {
	public String say(String name) {
		return "say:" + name;
	}

	public String hello(String name, int sex) {
		return "hello:" + name + " sex:" + sex;
	}
}