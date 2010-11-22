package cn.org.rapid_framework.generator;

import junit.framework.TestCase;
import cn.org.rapid_framework.generator.Generator.GeneratorModel;
import cn.org.rapid_framework.generator.GeneratorFacade.GeneratorModelUtils;
import cn.org.rapid_framework.generator.util.FileHelper;

public class ClassGeneratorTest extends GeneratorTestCase{
    GeneratorFacade g = new GeneratorFacade();
    
    public void test() throws Exception  {
        Generator g = new Generator();
        g.setTemplateRootDir(FileHelper.getFileByClassLoader("for_test/template/clazz"));
        g.setOutRootDir(getTempDir());
        GeneratorModel gm = GeneratorModelUtils.newFromClass(Generator.class);
        g.generateBy(gm.templateModel,gm.filePathModel);
    }
}
