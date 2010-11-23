package cn.org.rapid_framework.io.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.web.context.support.XmlWebApplicationContext;

public class VelocityFilterXmlWebApplicationContext extends XmlWebApplicationContext{

    @Override
    protected ResourcePatternResolver getResourcePatternResolver() {
        return new VelocityFilterResourcePatternResolver(super.getResourcePatternResolver());
    }
    
    @Override
    protected Resource getResourceByPath(String path) {
        return new VelocityFilterResource(super.getResourceByPath(path));
    }
    
}
