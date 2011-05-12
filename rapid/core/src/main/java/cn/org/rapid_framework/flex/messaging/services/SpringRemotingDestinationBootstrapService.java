package cn.org.rapid_framework.flex.messaging.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import cn.org.rapid_framework.flex.messaging.annotation.RemotingObject;
import flex.messaging.config.ConfigMap;
import flex.messaging.services.AbstractBootstrapService;
import flex.messaging.services.Service;
import flex.messaging.services.ServiceException;
/**
 * This BootstrapService is used to dynamicaly create a Remoting Service along 
 * with its Remoting Destinations without the need for any configuration files.
 * 
 * @author badqiu
 */
public class SpringRemotingDestinationBootstrapService extends AbstractBootstrapService {

    public static final String DEFAULT_INCLUDE_END_WITH_BEANS = "FlexService";
    
	private String destChannel;
	private String destSecurityConstraint;
	private String destScope;
	private String destAdapter;
	private String destFactory;
	
	private String serviceId;
	
	private String includeEndsWithBeans;
	/**
     * Called by the <code>MessageBroker</code> after all of the server 
     * components are created but right before they are started. This is 
     * usually the place to create dynamic components.
     * 
     * @param id Id of the <code>AbstractBootstrapService</code>.
     * @param properties Properties for the <code>AbstractBootstrapService</code>. 
     */
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
		System.out.println("[Auto Export Spring to BlazeDS RemotingDestination],beanNames="+addedBeanNames);
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

    /**
     * Called by the <code>MessageBroker</code> as server starts. Useful for
     * custom code that needs to run after all the components are initialized
     * and the server is starting up. 
     */    
    public void start()
    {
        // No-op        
    }

    /**
     * Called by the <code>MessageBroker</code> as server stops. Useful for 
     * custom code that needs to run as the server is shutting down.
     */
    public void stop()
    {
        // No-op        
    }

}
