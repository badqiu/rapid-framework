package cn.org.rapid_framework.generator;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModel;
import cn.org.rapid_framework.generator.util.FileHelper;

public class ClassGeneratorTest extends TestCase{
    GeneratorFacade g = new GeneratorFacade();
    
    public void test() throws Exception  {
        Generator g = new Generator();
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test/template/clazz"));
        g.setOutRootDir(GeneratorProperties.getRequiredProperty("outRoot"));
        GeneratorModel gm = GeneratorModel.newFromClass(Generator.class);
        g.generateBy(gm.templateModel,gm.filePathModel);
    }
}
