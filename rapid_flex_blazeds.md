

# 介绍 #

rapid扩展了spring,可以将spring bean导出为BlazeDS的RemotingDestination以供flex调用.


# rapid扩展 #
## 扩展说明 ##
BlazeDS本身提供一个[AbstractBootstrapService](http://livedocs.adobe.com/livecycle/es/sdkHelp/programmer/lcdsjavadoc/flex/messaging/services/AbstractBootstrapService.html)的类,该类主要是在BlazeDS初始化时用于动态创建 services, destinations, and adapters.

rapid扩展了该类,用于将spring applicationContext的bean自动导出为destination,以供flex客户端调用.
SpringRemotingDestinationBootstrapService 自动导出包含"@RemoteObject标注及以FlexService结
尾"的Spring Bean为RemotingDestination
```
public class SpringRemotingDestinationBootstrapService extends AbstractBootstrapService {

    public static final String DEFAULT_INCLUDE_END_WITH_BEANS = "FlexService";
    
	private String destChannel;
	private String destSecurityConstraint;
	private String destScope;
	private String destAdapter;
	private String destFactory;
	
	private String serviceId;
	
	private String includeEndsWithBeans;

    public void initialize(String id, ConfigMap properties)
    {
    	serviceId = properties.getPropertyAsString("service-id", "remoting-service");
    	
		destFactory = properties.getPropertyAsString("dest-factory", "spring");
		destAdapter = properties.getProperty("dest-adapter");
		destScope = properties.getProperty("dest-scope");
		destSecurityConstraint = properties.getProperty("dest-security-constraint");
		destChannel = properties.getPropertyAsString("dest-channel","my-amf");
		
		includeEndsWithBeans = properties.getPropertyAsString("includeEndsWithBeans",DEFAULT_INCLUDE_END_WITH_BEANS);
		
		Service remotingService = broker.getService(serviceId);
		if(remotingService == null) {
			throw createServiceException("not found Service with serviceId:"+serviceId);
		}
    	
        createSpringDestinations(remotingService);
    }

	private ServiceException createServiceException(String message) {
		ServiceException ex = new ServiceException();
		ex.setMessage(message);
		return ex;
	}

	private void createSpringDestinations(Service remotingService) {
		WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(broker.getInitServletContext());
		List<String> addedBeanNames = new ArrayList();
		for(String beanName : wac.getBeanDefinitionNames()) {
			Class type = wac.getType(beanName);
			
			boolean isCreateSpringDestination = type.isAnnotationPresent(RemotingObject.class) 
										|| beanName.endsWith(includeEndsWithBeans) 
										|| isCreateDestination(beanName,type);
			
			if(isCreateSpringDestination) {
				createSpringDestination(remotingService, beanName);
				addedBeanNames.add(beanName);
			}
		}
		System.out.println("[Auto Export Spring to RemotingDestination],beanNames="+addedBeanNames);
	}

	protected boolean isCreateDestination(String beanName,Class type) {
		return false;
	}

    /*
    <!--
        A verbose example using child tags.
    -->
    <destination id="sampleVerbose">
        <channels>
            <channel ref="my-secure-amf" />
        </channels>
        <adapter ref="java-object" />
        <security>
            <security-constraint ref="sample-users" />
        </security>
        <properties>
            <source>my.company.SampleService</source>
            <scope>session</scope>
            <factory>myJavaFactory</factory>
        </properties>
    </destination>     
     */
	protected void createSpringDestination(Service service, String destinationId) {
		flex.messaging.services.remoting.RemotingDestination destination = (flex.messaging.services.remoting.RemotingDestination)service.createDestination(destinationId);
        
        destination.setSource(destinationId);
        destination.setFactory(destFactory);
        
        if(destAdapter != null) 
        	destination.createAdapter(destAdapter);
        if(destScope != null) 
        	destination.setScope(destScope);
        if(destSecurityConstraint != null)
        	destination.setSecurityConstraint(destSecurityConstraint);
        if(destChannel != null)
        	destination.addChannel(destChannel);
        
        service.addDestination(destination);
	}

}
```


# 配置 #
将该类与网上的SpringFactory结合,即可使用.
以下为service-config.xml中关于自动导出的配置.
```
	<!-- 创建Spring RemotingDestination使用,与spring-remoting-service配合使用 -->
	<factories>
		<factory id="spring" class="cn.org.rapid_framework.flex.messaging.factories.SpringFactory"/>
	</factories>
	
    <services>
        <service-include file-path="remoting-config.xml" />
        <service-include file-path="proxy-config.xml" />
        <service-include file-path="messaging-config.xml" />
        
        <!-- 
        	自动导出包含"@RemoteObject标注及以FlexService结尾"的Spring Bean为RemotingDestination
        	FlexService结尾可以通过includeEndsWithBeans变量指定
        -->
        <service id="spring-remoting-service" class="cn.org.rapid_framework.flex.messaging.services.SpringRemotingDestinationBootstrapService">
	        <!-- 其它生成的RemotingDestination默认属性 -->
	        <properties>
	        	<!-- 
	        	<service-id></service-id>
	        	<dest-factory></dest-factory>
	        	<dest-adapter></dest-adapter>
	        	<dest-scope></dest-scope>
	        	<dest-channel></dest-channel>
	        	<dest-security-constraint></dest-security-constraint>
	        	<includeEndsWithBeans></includeEndsWithBeans>
	        	 -->
	        </properties>
        </service>
                
    </services>
```

# flex客户端调用 #
**简单示例调用**
```

this.blogFlexService = new RemoteObject("blogFlexService");

//这里需要指定endpoint,因为是动态的RemotingDestination,而静态的RemotingDestination ,flex编译器会将endpoint编译进源代码.
//这个也是flex编译器需要指定配置文件而导致使用flex经常会犯的错误之一.
this.blogFlexService.endpoint = '../messagebroker/amf';
```


---


# 推荐的Flex分层结构 #
```
                RemoteCall
  flex      <===============> FlexService => Manager(即Service) => Dao
----------                    ----------------------------------------
Flex客户端                         java服务端
```

FlexService相当于StrutsAction的角色,在Manager进行事务的控制，因为在FlexService不好做事务控制.
所以使用如上结构。