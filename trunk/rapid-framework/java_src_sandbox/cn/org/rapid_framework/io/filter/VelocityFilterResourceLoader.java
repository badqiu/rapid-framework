package cn.org.rapid_framework.io.filter;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;


public class VelocityFilterResourceLoader implements ResourceLoader{
    private ResourceLoader delegate;
    
    public VelocityFilterResourceLoader(ResourceLoader delegate) {
        super();
        this.delegate = delegate;
    }

    public Resource getResource(String location) {
        return new VelocityFilterResource(delegate.getResource(location));
    }

    public ClassLoader getClassLoader() {
        return delegate.getClassLoader();
    }

}
