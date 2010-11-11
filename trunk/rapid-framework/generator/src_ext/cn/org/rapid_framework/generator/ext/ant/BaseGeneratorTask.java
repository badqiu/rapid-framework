package cn.org.rapid_framework.generator.ext.ant;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.util.SystemHelper;

public abstract class BaseGeneratorTask extends Task{
    protected File shareInput;
    protected File input;
    protected File output;
    private boolean openOutputDir = false;
    
    public static Properties toProperties(Hashtable properties) {
        Properties props = new Properties();
        props.putAll(properties);
        return props;
    }
    
    private void error(Exception e) {
        StringWriter out = new StringWriter();
        e.printStackTrace(new PrintWriter(out));
        log(out.toString(),Project.MSG_ERR);
    }
    
    protected GeneratorFacade createGeneratorFacade(File input,File output) {
        if(input == null) throw new IllegalArgumentException("input must be not null");
        if(output == null) throw new IllegalArgumentException("output must be not null");
        
        GeneratorFacade gf = new GeneratorFacade();
        GeneratorProperties.setProperties(new Properties());
        Properties properties = toProperties(getProject().getProperties());
        properties.setProperty("basedir", getProject().getBaseDir().getAbsolutePath());
        GeneratorProperties.setProperties(properties);
        gf.g.addTemplateRootDir(input);
        if(shareInput != null) {
            gf.g.addTemplateRootDir(shareInput);
        }
        gf.g.setOutRootDir(output.getAbsolutePath());
        return gf;
    }
    
    public File getShareInput() {
        return shareInput;
    }

    public void setShareInput(File shareInput) {
        this.shareInput = shareInput;
    }

    public void setInput(File input) {
        this.input = input;
    }

    public void setOutput(File output) {
        this.output = output;
    }
    
    public void setOpenOutputDir(boolean openOutputDir) {
        this.openOutputDir = openOutputDir;
    }

    @Override
    public void execute() throws BuildException {
        super.execute();
        try {
        executeInternal();
        }catch(Exception e) {
            error(e);
            throw new BuildException(e);
        }
    }

    protected void executeInternal() throws Exception {
        GeneratorFacade generator = createGeneratorFacade(input,output);
        List<Map> maps = getGeneratorContexts();
        for(Map map : maps) {
            generator.generateByMap(map, input.getAbsolutePath());
        }
        if(openOutputDir && SystemHelper.isWindowsOS) {
            Runtime.getRuntime().exec("cmd.exe /c start "+output.getAbsolutePath());
        }
    }

    protected abstract List<Map> getGeneratorContexts() throws Exception;
    
}
