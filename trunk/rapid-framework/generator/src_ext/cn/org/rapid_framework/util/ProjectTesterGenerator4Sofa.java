package cn.org.rapid_framework.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

import cn.org.rapid_framework.generator.GeneratorFacade;

public class ProjectTesterGenerator4Sofa {
    
    //扫描包名
    private final static String scanClassPackage;
    //项目扫描主路径
    private final static String projectMainPath;
    //maven仓库路径
    private final static String mavenRepository;
    
    static {
        scanClassPackage = "com.alipay.mcenter.**";
        projectMainPath = "D:/dev/clearcase/zhongxuan_mquery_jycf4_upgrade/vobs/mcenter/mcenter";
        mavenRepository = System.getProperty("user.home")+"/.m2/repository";
    }
    
    
    public static void main(String args []) throws Exception {
        for(Object key : System.getProperties().keySet()) {
            System.out.println(key+"="+System.getProperty(key.toString()));
        }
        List<String> classPathFiles = scanClassPathFiles();
        
        //分析目标项目classpath文件，获得class编译路径及项目依赖
        List<File> projectClassesOutputPaths = scanProjectsOutputPaths(classPathFiles);
        ClassLoader classloader = genDependenciesUrlsClassLoader(classPathFiles);
        Thread.currentThread().setContextClassLoader(classloader);   
        
        //查询出所有spring配置文件
        List<String> xmlFiles = new ArrayList<String>();
        scanProjectsWithFilePrefixName(projectClassesOutputPaths,xmlFiles,".xml");
        
        //根据xml文件查询出spring bean定义
//      Map beanDefinations = analysisSpringBeanConfig(xmlFiles);
        
        //根据spring xml配置文件生成BaseTestCase
        generateBaseTestCaseBySpringConfigs(projectClassesOutputPaths,xmlFiles);
        //根据通配符扫描需要生成TestCase的目标测试类，并生成TestCase
        generateTestCaseByPackage(scanClassPackage);
    }

    private static ClassLoader genDependenciesUrlsClassLoader(
                                                              List<String> classPathFiles) {
        Set<URL> dependenciesUrls = scanDependenciesUrls(classPathFiles);        
        //把类编译路径添加到classLoader中
        URL [] urlArray = new URL[dependenciesUrls.size()];
        ClassLoader classloader = new URLClassLoader(dependenciesUrls.toArray(urlArray));
        return classloader;
    }

    private static Set<URL> scanDependenciesUrls(List<String> classPathFiles) {
        Set<URL> dependenciesUrls = new LinkedHashSet<URL>();
        for(String classPathFile:classPathFiles){
            System.out.println("===========================================================");
            System.out.println("analysis project class path file:"+classPathFile);
            
            URL clazzTargetPath = analysisProjectclazzTargetPath(classPathFile);
            dependenciesUrls.add(clazzTargetPath);
            System.out.println("project class target path:"+clazzTargetPath);
            List<URL> dependencyFiles = analysisProjectDependencyJar(classPathFile);
            for(URL dependencyFile:dependencyFiles){
                System.out.println("project dependency file:"+dependencyFile);
                dependenciesUrls.add(dependencyFile);
            }
            System.out.println("===========================================================");
        }
        return dependenciesUrls;
    }

    private static List<File> scanProjectsOutputPaths(
                                                      List<String> classPathFiles) {
        List<File> projectClassOutputPaths = new ArrayList<File>();
        for(String classPathFile:classPathFiles){
            URL clazzTargetPath = analysisProjectclazzTargetPath(classPathFile);
            projectClassOutputPaths.add(new File(clazzTargetPath.getFile()));
        }
        return projectClassOutputPaths;
    }

    private static List<String> scanClassPathFiles() {
        List<String> classPathFiles = new ArrayList<String>();
        //查找目标项目
        File project = new File(projectMainPath);
        scanProjectWithFilePrefixName(project,classPathFiles,".classpath");
        return classPathFiles;
    }

    public static void scanProjectWithFilePrefixName(File project,List<String> classPathFiles,String prefixName){
        if(project.isFile()){
            if(project.getName().toLowerCase().endsWith(prefixName.toLowerCase())){
                classPathFiles.add(project.getAbsolutePath());
            }
        } else{
            File [] childFiles = project.listFiles();
            for(File childFile:childFiles){
                scanProjectWithFilePrefixName(childFile,classPathFiles,prefixName);
            }
        }
    }
    
    public static void scanProjectsWithFilePrefixName(List<File> projects,List<String> classPathFiles,String prefixName){
        for(File project:projects){
            scanProjectWithFilePrefixName(new File(project.getAbsolutePath()+"\\META-INF\\spring\\"),classPathFiles,prefixName);
        }
    }
    
    public static URL analysisProjectclazzTargetPath(String classPathFile){
        String projectPath = classPathFile.substring(0, classPathFile.indexOf(".classpath"));
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new File(classPathFile));
            XPath classpathentryPath = XPath.newInstance("//classpath/classpathentry");
            List classpathentrys = classpathentryPath.selectNodes(doc);
            Iterator classpathentryIterator = classpathentrys.iterator();
            while(classpathentryIterator.hasNext()){
                Element classpathentry = (Element) classpathentryIterator.next();
                Attribute kind = classpathentry.getAttribute("kind");
                if("output".equalsIgnoreCase(kind.getValue())){
                    Attribute path = classpathentry.getAttribute("path");
                    return new File(projectPath+path.getValue().replace("/", "\\")).toURL();
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static List<URL> analysisProjectDependencyJar(String classPathFile){
        List<URL> dependencyPaths = new ArrayList<URL>();
        SAXBuilder builder = new SAXBuilder();
        try {
            Document doc = builder.build(new File(classPathFile));
            XPath classpathentryPath = XPath.newInstance("//classpath/classpathentry");
            List classpathentrys = classpathentryPath.selectNodes(doc);
            Iterator classpathentryIterator = classpathentrys.iterator();
            while(classpathentryIterator.hasNext()){
                Element classpathentry = (Element) classpathentryIterator.next();
                Attribute kind = classpathentry.getAttribute("kind");
                if("var".equalsIgnoreCase(kind.getValue())){
                    Attribute path = classpathentry.getAttribute("path");
                    dependencyPaths.add(new File(path.getValue().replace("M2_REPO", mavenRepository)).toURL());
                    Attribute sourcepath = classpathentry.getAttribute("sourcepath");
                    if(sourcepath != null) {
                        dependencyPaths.add(new File(sourcepath.getValue().replace("M2_REPO", mavenRepository)).toURL());
                    }
                }
            }
        } catch (JDOMException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return dependencyPaths;
    }
    
    private static void generateTestCaseByPackage(String packageName) throws ClassNotFoundException,Exception {
        List<String> classes = ScanClassUtils.scanPackages(packageName);
        for(String className:classes){
            System.out.println("generate TestCase by class named:"+className);
            Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
            //如果该类不属于一般服务类，则不生成TestCase
            if(clazz.isAnnotation()||clazz.isAnonymousClass()||clazz.isArray()||clazz.isEnum()||clazz.isLocalClass()||clazz.isPrimitive()||clazz.isInterface()){
                continue;
            }
            //如果该类属于内部类，则不生成TestCase
            if(clazz.getSimpleName().contains("$")){
                continue;
            }
            //如果该类不含有任何public方法，则不生成TestCase
            if(!hasPublicMethod(clazz)){
                continue;
            }
            new GeneratorFacade().generateByClass(clazz, "test_template\\test_case");
        }
    }
    
    private static void generateBaseTestCaseBySpringConfigs(List<File> projectClassesOutputPaths,List<String> xmlConfigs) throws Exception{
        List<String> springConfigs = new ArrayList<String>();
        List<String> resourceFilters = new ArrayList<String>();
        
        List<String> springReplaceConfigs = new ArrayList<String>();
        //查询项目中的MF文件,解析manifest中的Resource-Filter属性
        for(File projectTargetPath : projectClassesOutputPaths){
            Manifest manifest = new Manifest(new FileInputStream(projectTargetPath.getAbsolutePath()+"\\META-INF\\MANIFEST.MF"));
            Attributes attrs =  manifest.getMainAttributes();
            String resourceFiltersStrings = attrs.getValue("Resource-Filter");
            if(resourceFiltersStrings!=null && resourceFiltersStrings.trim().length()>0){
                String[] originalResourceFilters = resourceFiltersStrings.split(",");
                if(originalResourceFilters!=null&&originalResourceFilters.length>0){
                    for(String originalResourceFilter:originalResourceFilters){
                        resourceFilters.add(originalResourceFilter.trim());
                    }
                }
            }
        }

        //过滤xml文件，选出spring配置文件及需要替换properties配置的spring配置文件
        for(String xmlConfig:xmlConfigs){
            String springPath = xmlConfig.substring(xmlConfig.indexOf("META-INF\\spring\\"));
            springConfigs.add(springPath);
            for(String resourceFilter : resourceFilters){
                if(springPath.contains(resourceFilter)){
                    springReplaceConfigs.add(resourceFilter);
                }
            }
        }
        
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        params.put("springConfigs", springConfigs);
        params.put("springReplaceConfigs", springReplaceConfigs);
        new GeneratorFacade().generateByMap(params, "test_template\\base_test_case");
    }
    
//    private static Map analysisSpringBeanConfig(List<String> springConfigs){
//      Map beandefinations = new HashMap();
//      String [] springXmlConfigs = new String[springConfigs.size()];
//      ApplicationContext context = new FileSystemXmlApplicationContext(springConfigs.toArray(springXmlConfigs));
//      String[] beanNames = context.getBeanDefinitionNames();
//      for(String beanName:beanNames){
//          Object bean = context.getBean(beanName);
//          beandefinations.put(bean.getClass(), beanName);
//      }
//      return beandefinations;
//    }
    
    private static boolean hasPublicMethod(Class<?> clazz){
        Method[] methods = clazz.getDeclaredMethods();
        for(Method method:methods){
            if(Modifier.isPublic(method.getModifiers())){
                return true;
            }
        }
        return false;
    }
}
