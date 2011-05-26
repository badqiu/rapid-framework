package cn.org.rapid_framework.struts;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionServlet;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.util.ModuleUtils;
/**
 * 使用struts支持开发模式,每次action请求重新reload配置文件
 * 在classpath中放置struts1.properties,并设devMode=true则可开启开发模式
 * @author badqiu(badqiu@gmail.com)
 *
 */
public class DevModeActionServlet extends ActionServlet{
	
    /**
     * �ṩdevMode֧��
     */
    protected void process(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
    	if(devMode) {
    		log.info("start reload struts config");
    		ModuleUtils.getInstance().selectModule(request, getServletContext());
    		ModuleConfig config = getModuleConfig(request);
    		String key = Globals.REQUEST_PROCESSOR_KEY + config.getPrefix();
    		this.destroy();
    		this.init(getServletConfig());
            getServletContext().setAttribute(key, null);
    	}
    	super.process(request, response);
    }
    
    
    private static String DEFAULT_STRUTS_CONFIG_NAME = "struts1.properties";
    private static Properties properties = new Properties();
    static{
    	try {
    		InputStream strutsConfigIn = DevModeActionServlet.class.getClassLoader().getResourceAsStream(DEFAULT_STRUTS_CONFIG_NAME);
    		if(strutsConfigIn != null) {
    			log.info("load "+DEFAULT_STRUTS_CONFIG_NAME+" file in classpath");
    			properties.load(strutsConfigIn);
    			strutsConfigIn.close();
    		}else {
    			log.info("not found "+DEFAULT_STRUTS_CONFIG_NAME+" file in classpath");
    		}
    	}catch(Exception e) {
    		log.warn("count not load "+DEFAULT_STRUTS_CONFIG_NAME+",cause="+e.toString(),e);
    	}
    }
    private static boolean devMode = Boolean.valueOf((properties.getProperty("devMode","false"))).booleanValue();
    static{
    	log.info("struts devMode="+devMode);
    }
}
