package cn.org.rapid_framework.flex.messaging.factories;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import flex.messaging.FactoryInstance;
import flex.messaging.FlexFactory;
import flex.messaging.config.ConfigMap;
import flex.messaging.services.ServiceException;

/**
 * <pre>
 * This interface is implemented by factory components which provide
 * instances to the flex messaging framework.  To configure flex data services
 * to use this factory, add the following lines to your services-config.xml
 * file (located in the WEB-INF/flex directory of your web application).
 *
 *	&lt;factories&gt;
 *     &lt;factory id="spring" class="flex.samples.factories.SpringFactory" /&gt;
 *  &lt;/factories&gt;
 *
 * You also must configure the web application to use spring and must copy the spring.jar
 * file into your WEB-INF/lib directory.  To configure your app server to use spring,
 * you add the following lines to your WEB-INF/web.xml file:
 *
 *   &lt;context-param&gt;
 *        &lt;param-name&gt;contextConfigLocation&lt;/param-name&gt;
 *        &lt;param-value&gt;/WEB-INF/applicationContext.xml&lt;/param-value&gt;
 *   &lt;/context-param&gt;
 *
 *   &lt;listener&gt;
 *       &lt;listener-class&gt;org.springframework.web.context.ContextLoaderListener&lt;/listener-class&gt;
 *   &lt;/listener&gt;
 * 
 * Then you put your spring bean configuration in WEB-INF/applicationContext.xml (as per the
 * line above).  For example:
 * 
 *  &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 *  &lt;!DOCTYPE beans PUBLIC "-//SPRING//DTD BEAN//EN" "http://www.springframework.org/dtd/spring-beans.dtd"&gt;
 *   
 *  &lt;beans&gt;
 *    &lt;bean name="weatherBean" class="dev.weather.WeatherService" singleton="true"/&gt;
 *  &lt;/beans&gt;
 *  
 * Now you are ready to define a destination in flex that maps to this existing service. 
 * To do this you'd add this to your WEB-INF/flex/remoting-config.xml:
 *
 *  &lt;destination id="WeatherService"&gt;
 *      &lt;properties&gt;
 *          &lt;factory&gt;spring&lt;/factory&gt;
 *          &lt;source&gt;weatherBean&lt;/source&gt;
 *      &lt;/properties&gt;
 *  &lt;/destination&gt;
 * </pre>
 * @author Jeff Vroom
 */
public class SpringFactory implements FlexFactory
{
    private static final String SOURCE = "source";

    /**
     * This method can be used to initialize the factory itself.  It is called with configuration
     * parameters from the factory tag which defines the id of the factory.  
     */
    public void initialize(String id, ConfigMap configMap) {}

    /**
     * This method is called when we initialize the definition of an instance 
     * which will be looked up by this factory.  It should validate that
     * the properties supplied are valid to define an instance.
     * Any valid properties used for this configuration must be accessed to 
     * avoid warnings about unused configuration elements.  If your factory 
     * is only used for application scoped components, this method can simply
     * return a factory instance which delegates the creation of the component
     * to the FactoryInstance's lookup method.
     */
    public FactoryInstance createFactoryInstance(String id, ConfigMap properties)
    {
        SpringFactoryInstance instance = new SpringFactoryInstance(this, id, properties);
        instance.setSource(properties.getPropertyAsString(SOURCE, instance.getId()));
        return instance;
    } // end method createFactoryInstance()

    /**
     * Returns the instance specified by the source
     * and properties arguments.  For the factory, this may mean
     * constructing a new instance, optionally registering it in some other
     * name space such as the session or JNDI, and then returning it
     * or it may mean creating a new instance and returning it.
     * This method is called for each request to operate on the
     * given item by the system so it should be relatively efficient.
     * <p>
     * If your factory does not support the scope property, it
     * report an error if scope is supplied in the properties
     * for this instance.
     */
    public Object lookup(FactoryInstance inst)
    {
        SpringFactoryInstance factoryInstance = (SpringFactoryInstance) inst;
        return factoryInstance.lookup();
    } 


    static class SpringFactoryInstance extends FactoryInstance
    {
        SpringFactoryInstance(SpringFactory factory, String id, ConfigMap properties)
        {
            super(factory, id, properties);
        }


        public String toString()
        {
            return "SpringFactory instance for id=" + getId() + " source=" + getSource() + " scope=" + getScope();
        }

        public Object lookup() 
        {
            ApplicationContext appContext = WebApplicationContextUtils.getWebApplicationContext(flex.messaging.FlexContext.getServletConfig().getServletContext());
            String beanName = getSource();

            try
            {
                return appContext.getBean(beanName);
            }
            catch (NoSuchBeanDefinitionException nexc)
            {
                ServiceException e = new ServiceException();
                String msg = "Spring service named '" + beanName + "' does not exist.";
                e.setMessage(msg);
                e.setRootCause(nexc);
                e.setDetails(msg);
                e.setCode("Server.Processing");
                throw e;
            }
            catch (BeansException bexc)
            {
                ServiceException e = new ServiceException();
                String msg = "Unable to create Spring service named '" + beanName + "' ";
                e.setMessage(msg);
                e.setRootCause(bexc);
                e.setDetails(msg);
                e.setCode("Server.Processing");
                throw e;
            } 
        }
        
    } 

} 
