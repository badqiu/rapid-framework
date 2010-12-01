package cn.org.rapid_framework.generator.ext.ant;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import cn.org.rapid_framework.generator.ext.tableconfig.builder.TableConfigXmlBuilder;
import cn.org.rapid_framework.generator.ext.tableconfig.model.TableConfigSet;
import cn.org.rapid_framework.generator.util.SystemHelper;

public abstract class BaseGeneratorTask extends Task{
    protected File shareInput;
    protected File input;
    protected File output;
    private boolean openOutputDir = false;
    private String _package;
    
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
        
        GeneratorProperties.setProperties(new Properties());
        Properties properties = toProperties(getProject().getProperties());
        properties.setProperty("basedir", getProject().getBaseDir().getAbsolutePath());
        GeneratorProperties.setProperties(properties);
        
        GeneratorFacade gf = new GeneratorFacade();
        gf.getGenerator().addTemplateRootDir(input);
        if(shareInput != null) {
            gf.getGenerator().addTemplateRootDir(shareInput);
        }
        gf.getGenerator().setOutRootDir(output.getAbsolutePath());
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

    public String getPackage() {
        return _package;
    }

    public void setPackage(String _package) {
        this._package = _package;
    }
    
    static TableConfigSet parseForTableConfigSet(String _package,File basedir,String[] tableConfigFilesArray) {
        TableConfigSet tableConfigSet = new TableConfigXmlBuilder().parseFromXML(basedir, Arrays.asList(tableConfigFilesArray));
        tableConfigSet.setPackage(_package);
        return tableConfigSet;
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
        freemarker.log.Logger.selectLoggerLibrary(freemarker.log.Logger.LIBRARY_NONE);
        
        GeneratorFacade facade = createGeneratorFacade(input,output);
        List<Map> maps = getGeneratorContexts();
        if(maps == null) return;
        for(Map map : maps) {
            facade.generateByMap(map);
        }
        if(openOutputDir && SystemHelper.isWindowsOS) {
            Runtime.getRuntime().exec("cmd.exe /c start "+output.getAbsolutePath());
        }
    }

    protected abstract List<Map> getGeneratorContexts() throws Exception;
    
}
