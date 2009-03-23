package cn.org.rapid_framework.spring.web.servlet.mvc.support;


import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.mvc.support.AbstractControllerUrlHandlerMapping;

public class ControllerClassNameHandlerMapping extends AbstractControllerUrlHandlerMapping{

	/**
	 * Common suffix at the end of controller implementation classes.
	 * Removed when generating the URL path.
	 */
	private static final String CONTROLLER_SUFFIX = "Controller";


//	private boolean caseSensitive = false;

	private String pathPrefix;

	private String basePackage;


	/**
	 * Set whether to apply case sensitivity to the generated paths,
	 * e.g. turning the class name "BuyForm" into "buyForm".
	 * <p>Default is "false", using pure lower case paths,
	 * e.g. turning the class name "BuyForm" into "buyform".
	 */
//	public void setCaseSensitive(boolean caseSensitive) {
//		this.caseSensitive = caseSensitive;
//	}

	/**
	 * Specify a prefix to prepend to the path generated from the controller name.
	 * <p>Default is a plain slash ("/"). A path like "/mymodule" can be specified
	 * in order to have controller path mappings prefixed with that path, e.g.
	 * "/mymodule/buyform" instead of "/buyform" for the class name "BuyForm".
	 */
	public void setPathPrefix(String prefixPath) {
		this.pathPrefix = prefixPath;
		if (StringUtils.hasLength(this.pathPrefix)) {
			if (!this.pathPrefix.startsWith("/")) {
				this.pathPrefix = "/" + this.pathPrefix;
			}
			if (this.pathPrefix.endsWith("/")) {
				this.pathPrefix = this.pathPrefix.substring(0, this.pathPrefix.length() - 1);
			}
		}
	}

	/**
	 * Set the base package to be used for generating path mappings,
	 * including all subpackages underneath this packages as path elements.
	 * <p>Default is <code>null</code>, using the short class name for the
	 * generated path, with the controller's package not represented in the path.
	 * Specify a base package like "com.mycompany.myapp" to include subpackages
	 * within that base package as path elements, e.g. generating the path
	 * "/mymodule/buyform" for the class name "com.mycompany.myapp.mymodule.BuyForm".
	 * Subpackage hierarchies are represented as individual path elements,
	 * e.g. "/mymodule/mysubmodule/buyform" for the class name
	 * "com.mycompany.myapp.mymodule.mysubmodule.BuyForm".
	 */
	public void setBasePackage(String basePackage) {
		this.basePackage = basePackage;
		if (StringUtils.hasLength(this.basePackage) && !this.basePackage.endsWith(".")) {
			this.basePackage = this.basePackage + ".";
		}
	}


	protected String[] buildUrlsForHandler(String beanName, Class beanClass) {
		return generatePathMappings(beanClass);
	}

	/**
	 * Generate the actual URL paths for the given controller class.
	 * <p>Subclasses may choose to customize the paths that are generated
	 * by overriding this method.
	 * @param beanClass the controller bean class to generate a mapping for
	 * @return the URL path mappings for the given controller
	 */
	protected String[] generatePathMappings(Class beanClass) {
		StringBuffer pathMapping = buildPathPrefix(beanClass);
		String className = ClassUtils.getShortName(beanClass);
		String path = (className.endsWith(CONTROLLER_SUFFIX) ?
				className.substring(0, className.indexOf(CONTROLLER_SUFFIX)) : className);
		if (path.length() > 0) {
			pathMapping.append(path);
		}
		if (isMultiActionControllerType(beanClass)) {
			return new String[] {pathMapping.toString(), pathMapping.toString() + "/*"};
		}
		else {
			return new String[] {pathMapping.toString() + "*"};
		}
	}

	/**
	 * Build a path prefix for the given controller bean class.
	 * @param beanClass the controller bean class to generate a mapping for
	 * @return the path prefix, potentially including subpackage names as path elements
	 */
	private StringBuffer buildPathPrefix(Class beanClass) {
		StringBuffer pathMapping = new StringBuffer();
		if (this.pathPrefix != null) {
			pathMapping.append(this.pathPrefix);
			pathMapping.append("/");
		}
		else {
			pathMapping.append("/");
		}
		if (this.basePackage != null) {
			String packageName = ClassUtils.getPackageName(beanClass);
			if (packageName.startsWith(this.basePackage)) {
				String subPackage = packageName.substring(this.basePackage.length()).replace('.', '/');
				pathMapping.append(subPackage.toLowerCase());
				pathMapping.append("/");
			}
		}
		return pathMapping;
	}

	
	
	
}
