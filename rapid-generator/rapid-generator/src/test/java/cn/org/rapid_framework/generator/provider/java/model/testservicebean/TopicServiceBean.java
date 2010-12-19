package cn.org.rapid_framework.generator.provider.java.model.testservicebean;

public class TopicServiceBean {
    
    public String a1() {
        System.out.println("a1()");
        return a1();
    }
    
    public String a2(String nam) {
        System.out.println("a2() nam:"+nam);
        return "a2():"+nam;
    }
    
    public String a3(String nam,int[] array) {
        System.out.println("a3() nam:"+nam);
        return "a3():"+nam;
    }
}
