package cn.org.rapid_framework.struts;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.actions.MappingDispatchAction;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class AutowireMappingDispatchAction extends MappingDispatchAction {

	protected final Log log = LogFactory.getLog(getClass());
	
	protected static WebApplicationContext wac;
	
	public AutowireMappingDispatchAction() {
		super();
	}

	/**
	 * Initialize the WebApplicationContext for this Action.
	 * Invokes onInit after successful initialization of the context.
	 * @see #onInit
	 */
	public void setServlet(ActionServlet actionServlet) {
		super.setServlet(actionServlet);
		if (actionServlet != null) {
	        wac = WebApplicationContextUtils.getWebApplicationContext(servlet.getServletContext());
	        if(wac != null && isUseAutowire()) {
	        	log.info("start autowire "+getClass().getName()+" autowireMode:"+getAutowireMode()+" isDependecyCheck:"+isDependencyCheck());
	        	wac.getAutowireCapableBeanFactory().autowireBeanProperties(
	    			    this, getAutowireMode(), isDependencyCheck());
	        }
	        onInit();
		}
		else {
			onDestroy();
		}
	}

	protected boolean isUseAutowire() {
		return true;
	}

	protected int getAutowireMode() {
		return AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;
	}

	protected boolean isDependencyCheck() {
		return false;
	}

	/**
	 * Callback for custom initialization after the context has been set up.
	 * @see #setServlet
	 */
	protected void onInit() {
	}

	/**
	 * Callback for custom destruction when the ActionServlet shuts down.
	 * @see #setServlet
	 */
	protected void onDestroy() {
	}
	
	/**
     * Convenience method to get Spring-initialized beans
     *
     * @param name
     * @return Object bean from ApplicationContext
     */
	public Object getBean(String name) {
		if (wac == null) {
			wac = WebApplicationContextUtils.getRequiredWebApplicationContext(servlet.getServletContext());
		}
		return wac.getBean(name);
	}

}