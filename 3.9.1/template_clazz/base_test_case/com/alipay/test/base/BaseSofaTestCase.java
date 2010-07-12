package com.alipay.test.base;
import junit.framework.*;
import java.util.*;

import org.nuxeo.runtime.test.AnnotatedAutowireSofaTestCase;

public class BaseSofaTestCase extends AnnotatedAutowireSofaTestCase {

	@Override
	public String[] getConfigurationLocations() {
		return new String[] {<#list springConfigs as springConfig>"${springConfig?replace("\\", "/")}",</#list>};
	}

	
	@Override
	public String[] getResourceFilterNames() {
		return new String[] {<#list springReplaceConfigs as springReplaceConfig>"${springReplaceConfig?replace("\\", "/")}",</#list>};
	}
	

	/**
	 * return new String[] { "beans-test-client.xml", "org/codehaus/xfire/spring/xfire.xml" };
	 */
	@Override
	public String[] getWebServiceConfigurationLocations() {
		return new String[] {};
	}
}