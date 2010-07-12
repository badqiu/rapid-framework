/*
 * Copyright 2008 [CompanyName], Inc. All rights reserved.
 * Website: http://www.pomer.org.cn/
 */
package cn.org.rapid_framework.flex.messaging.services;

import java.lang.reflect.Modifier;
import java.rmi.server.RemoteObject;
import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import cn.org.rapid_framework.util.ScanClassUtils;

import flex.messaging.config.ConfigMap;
import flex.messaging.services.AbstractBootstrapService;
import flex.messaging.services.Service;
import flex.messaging.services.ServiceException;
import flex.messaging.services.remoting.RemotingDestination;

/**
 * 
 * @author badqiu,Linlin Yu
 * 
 */
public class JaveBeanRemotingDestinationBootstrapService extends AbstractBootstrapService {

	private String destChannel;
	private String destSecurityConstraint;
	private String destScope;
	private String destAdapter;
	private String destFactory;
	private String serviceId;
	
	private String packageToScan = null;
	
	@Override
	public void initialize(String id, ConfigMap properties) {
    	serviceId = properties.getPropertyAsString("service-id", "remoting-service");
    	
		destFactory = properties.getProperty("dest-factory");
		destAdapter = properties.getProperty("dest-adapter");
		destScope = properties.getProperty("dest-scope");
		destChannel = properties.getPropertyAsString("dest-channel","my-amf");
		destSecurityConstraint = properties.getProperty("dest-security-constraint");
		
		packageToScan = properties.getProperty("package-to-scan");
		if(packageToScan == null || "".equals(packageToScan.trim())) {
			throw createServiceException("'package-to-scan' property must be specify");
		}
		
		Service remotingService = broker.getService(serviceId);
		if(remotingService == null) {
			throw createServiceException("not found Service with serviceId:"+serviceId);
		}

		createJavaBeanDestinations(remotingService);

	}
	
	private ServiceException createServiceException(String message) {
		ServiceException ex = new ServiceException();
		ex.setMessage(message);
		return ex;
	}

	private void createJavaBeanDestinations(Service remotingService) {
		List<String> addedBeanNames = new ArrayList();
		List<Class> roJavaBeans = getRemoteObjectJavaBean();
		for (Class clazz : roJavaBeans) {
			createJavaBeanDestination(remotingService,clazz);
			addedBeanNames.add(StringUtils.uncapitalize(ClassUtils.getShortName(clazz)));
		}
		System.out.println("[Auto Export JavaBean to BlazeDS RemotingDestination],beans="+ addedBeanNames);
	}
	
	protected void createJavaBeanDestination(Service service, Class clazz) {
		String destinationId = StringUtils.uncapitalize(ClassUtils.getShortName(clazz));
		RemotingDestination destination = (RemotingDestination) service.createDestination(destinationId);

		destination.setSource(clazz.getName());
		
		if(destFactory != null)
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

	@Override
	public void start() {
		// no op
	}

	@Override
	public void stop() {
		// no op
	}

	private List<Class> getRemoteObjectJavaBean() {
		ResourcePatternResolver rl = new PathMatchingResourcePatternResolver();
		List<Class> result = new ArrayList();
		try {
			List<String> classNames = ScanClassUtils.scanPackages(packageToScan);
			for(String className : classNames) {
				Class type = Class.forName(className);
				if (isRemoteObject(type)) {
					result.add(type);
				}
			}
		} catch (Exception e) {
			throw new IllegalStateException("scan JavaBean RemoteObject error",e);
		}
		return result;
	}

	private boolean isRemoteObject(Class clazz) {
		if (clazz.isAnnotationPresent(RemoteObject.class)
				&& !clazz.isInterface() && !isAbstract(clazz)
				&& !clazz.isAnonymousClass() && !clazz.isMemberClass()) {
			return true;
		}
		return false;
	}

	boolean isAbstract(Class type) {
		return (type.getModifiers() ^ Modifier.ABSTRACT) == 0;
	}
	
	public static void main(String[] args) {
		JaveBeanRemotingDestinationBootstrapService b = new JaveBeanRemotingDestinationBootstrapService();
		List<Class> lst = b.getRemoteObjectJavaBean();
		for (Class class1 : lst) {
			System.out.println(class1.getName());
		}
	}

}
