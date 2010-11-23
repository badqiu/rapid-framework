package cn.org.rapid_framework.io.filter;

import java.io.IOException;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;

public class VelocityFilterResourcePatternResolver implements ResourcePatternResolver {

    private ResourcePatternResolver delegate;

    public VelocityFilterResourcePatternResolver(ResourcePatternResolver delegate) {
        super();
        this.delegate = delegate;
    }

    public Resource getResource(String location) {
        return new VelocityFilterResource(delegate.getResource(location));
    }

    public Resource[] getResources(String locationPattern) throws IOException {
        Resource[] originalResource = delegate.getResources(locationPattern);
        Resource[] velocityParserResource = new Resource[originalResource.length];
        for (int i = 0; i < originalResource.length; i++) {
            velocityParserResource[i] = new VelocityFilterResource(originalResource[i]);
        }

        return velocityParserResource;
    }

    public ClassLoader getClassLoader() {
        return delegate.getClassLoader();
    }
    
}
