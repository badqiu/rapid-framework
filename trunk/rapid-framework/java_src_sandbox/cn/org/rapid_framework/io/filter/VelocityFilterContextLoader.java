package cn.org.rapid_framework.io.filter;

import org.springframework.context.support.GenericApplicationContext;
import org.springframework.test.context.ContextLoader;
import org.springframework.test.context.support.GenericXmlContextLoader;

public class VelocityFilterContextLoader extends GenericXmlContextLoader implements ContextLoader {

    @Override
    protected void customizeContext(GenericApplicationContext context) {
        super.customizeContext(context);
        context.setResourceLoader(new VelocityFilterResourcePatternResolver(context));
    }

}
