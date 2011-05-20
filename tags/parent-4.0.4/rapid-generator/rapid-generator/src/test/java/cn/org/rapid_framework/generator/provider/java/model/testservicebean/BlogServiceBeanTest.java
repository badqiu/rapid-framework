


package cn.org.rapid_framework.generator.provider.java.model.testservicebean;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JMock.class)
public class BlogServiceBeanTest{

    private Mockery  context = new JUnit4Mockery(){
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    protected BlogServiceBean blogServiceBean = new BlogServiceBean();

    
    //dependence class
    CommentServiceBean csb = context.mock(CommentServiceBean.class);
    EmailServiceBean esb = context.mock(EmailServiceBean.class);
    String name = "";
    TopicServiceBean topicServiceBean = context.mock(TopicServiceBean.class);
    
    @Before
    public void setUp() throws Exception {
        
        blogServiceBean.setCsb(csb);
        blogServiceBean.setEsb(esb);
        blogServiceBean.setName(name);
        blogServiceBean.setTopicServiceBean(topicServiceBean);
        
        // 请将 context.checking(new Expectations(){ }) 相关方法迁移至具体的各个测试方法中.
        // 以下注释掉的方法可以根据需要手工拷贝使用，不需要则请删除
        /*

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).aa(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).dd(with(any(String.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).bb(with(any(String.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).cc(with(any(String.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).say(with(any(String.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).hello(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(topicServiceBean).a1();
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(topicServiceBean).a2(with(any(String.class)));
		        will(returnValue(first));
		    }
		});

		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(topicServiceBean).a3(with(any(String.class)),with(any(int[].class)));
		        will(returnValue(first));
		    }
		});
        */
    }
    
    @After
    public void tearDown() throws Throwable{
        context.assertIsSatisfied();
    }
    
    @Test
    public void test_testSay() throws Throwable{
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).say(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).bb(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).hello(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).aa(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).say(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).cc(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
        
        String sex = "";
        
        String result = blogServiceBean.testSay(sex );
        
        assertNotNull(result);
    }
    
    @Test
    public void test_blogjava() throws Throwable{
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).bb(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).say(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).hello(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).aa(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).cc(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
        
        String sex = "";
        
        String result = blogServiceBean.blogjava(sex );
        
        assertNotNull(result);
    }
    
    @Test
    public void test_chain_call() throws Throwable{
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).bb(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(esb).say(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).dd(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).aa(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).cc(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
        
        String sex = "";
        
        String result = blogServiceBean.chain_call(sex );
        
        assertNotNull(result);
    }
    
//    @Test
//    public void test_call_other_method() throws Throwable{
//        
//        
//        
//        String result = blogServiceBean.call_other_method();
//        
//        assertNotNull(result);
//    }
    
    @Test
    public void test_array_args() throws Throwable{
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).bb(with(any(String.class)));
		        will(returnValue(first));
		    }
		});
        
		context.checking(new Expectations() {
		    {
		        String first = "";
		        
		        allowing(csb).aa(with(any(String.class)),with(any(int.class)));
		        will(returnValue(first));
		    }
		});
        
        
        String[] names = new String[]{};
        List sexes = new ArrayList();
        
        String result = blogServiceBean.array_args(names ,sexes );
        
        assertNotNull(result);
    }
    
    
}
