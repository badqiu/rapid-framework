package cn.org.rapid_framework.io.filter;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public class VelocityFilterXmlApplicationContext extends AbstractXmlApplicationContext implements ConfigurableApplicationContext {
    
    @Override
    protected Resource getResourceByPath(String path) {
        return new VelocityFilterResource(super.getResourceByPath(path));
    }
    
    @Override
    protected ResourcePatternResolver getResourcePatternResolver() {
        return new VelocityFilterResourcePatternResolver(super.getResourcePatternResolver());
    }
    
}
