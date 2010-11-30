package cn.org.rapid_framework.generator;

import java.io.File;

import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.provider.java.model.JavaClass;
import cn.org.rapid_framework.generator.provider.java.model.testservicebean.BlogServiceBean;

public class ClassGeneratorTest extends GeneratorTestCase{
    GeneratorFacade g = new GeneratorFacade();
    
    public void test() throws Exception  {
        Generator g = new Generator();
        g.setTemplateRootDir(new File("src/template/clazz/jmock"));
        g.setOutRootDir(getTempDir());
        GeneratorModel gm = GeneratorModelUtils.newGeneratorModel("clazz",new JavaClass(BlogServiceBean.class));
        g.generateBy(gm.templateModel,gm.filePathModel);
    }
}
