/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2005 All Rights Reserved.
 */
package cn.org.rapid_framework.io.filter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

/**
 * 使用Velocity过滤资源的装饰类
 *
 * <p>
 *    添加对spring 2.5.5 <code>Resource</code>接口的支持
 * </p>
 * @author badqiu
 */
public class VelocityFilterResource implements Resource{
    private final Log log = LogFactory.getLog(VelocityFilterResource.class);

    /**
     * Prefix for searching inside the owning bundle space. This translates to
     * searching the bundle and its attached fragments. If no prefix is
     * specified, this one will be used.
     */
    public static final String  BUNDLE_URL_PREFIX     = "bundle:";

    /**
     * Prefix for searching only the bundle raw jar. Will ignore attached
     * fragments. Not used at the moment.
     */
    public static final String  BUNDLE_JAR_URL_PREFIX = "bundle-jar:";
    
    /** 对sofa-config.properties文件的内容进行缓存 */
//    public static Map<String, String> sofaConfigs = null;

    /** 被装饰的资源 **/
    private Resource                  decoratedResource;

    public VelocityFilterResource() {
    }

    /**
     * 创建一个<code>VelocityFilterResource</code>对象
     *
     * @param decoratedResource
     * @param bundle
     */
    public VelocityFilterResource(Resource decoratedResource) {
        this.decoratedResource = decoratedResource;
    }

    /**
     * @see org.springframework.core.io.Resource#createRelative(java.lang.String)
     */
    public Resource createRelative(String relativePath) throws IOException {
        Resource resource = this.decoratedResource.createRelative(relativePath);
        return new VelocityFilterResource(resource);
    }

    /**
     * @see org.springframework.core.io.Resource#exists()
     */
    public boolean exists() {
        return this.decoratedResource.exists();
    }

    /**
     * @see org.springframework.core.io.Resource#getDescription()
     */
    public String getDescription() {
        return "Decoration velocity[ " + this.decoratedResource.getDescription() + "]";
    }

    /**
     * @see org.springframework.core.io.Resource#getFile()
     */
    public File getFile() throws IOException {
        return this.decoratedResource.getFile();
    }
    
    /**
     * @see org.springframework.core.io.Resource#getFilename()
     */
    public String getFilename() {
        String fileName = this.decoratedResource.getFilename();

        if(fileName.startsWith(BUNDLE_URL_PREFIX)) {
            fileName = fileName.substring(BUNDLE_URL_PREFIX.length());
        }
        if(fileName.startsWith(BUNDLE_JAR_URL_PREFIX)) {
            fileName = fileName.substring(BUNDLE_JAR_URL_PREFIX.length());
        }
        if(fileName.startsWith(ResourceLoader.CLASSPATH_URL_PREFIX)) {
            fileName = fileName.substring(ResourceLoader.CLASSPATH_URL_PREFIX.length());
        }
        return fileName;
    }

    /**
     * @see org.springframework.core.io.Resource#getURL()
     */
    public URL getURL() throws IOException {
        // 如果如果允许过滤,使用Velocity模板引擎进行过滤，并在临时文件夹中生成过滤完的文件
        if (isAllowedParser()) {
            return parserStreamByVeloctiyToTempFile().toURL();
        }
        return this.decoratedResource.getURL();
    }

    /**
     * @see org.springframework.core.io.Resource#getURI()
     */
    public URI getURI() throws IOException {
        // 如果如果允许过滤,使用Velocity模板引擎进行过滤，并在临时文件夹中生成过滤完的文件
        if (isAllowedParser()) {
            return parserStreamByVeloctiyToTempFile().toURI();
        }
        return this.decoratedResource.getURI();
    }

    /**
     * @see org.springframework.core.io.Resource#isOpen()
     */
    public boolean isOpen() {
        return this.decoratedResource.isOpen();
    }

    /**
     * @see org.springframework.core.io.InputStreamSource#getInputStream()
     */
    public InputStream getInputStream() throws IOException {
        //如果允许过滤,使用Velocity模板引擎进行过滤
        if (isAllowedParser()) {
            return parserStreamByVeloctiy();
        } else {
            //如果不允许，返回原有被装饰类的输出流
            return this.decoratedResource.getInputStream();
        }
    }

    /**
     * @see org.springframework.core.io.Resource#isReadable()
     */
    public boolean isReadable() {
        return this.decoratedResource.isReadable();
    }

    /**
     * @see org.springframework.core.io.Resource#lastModified()
     */
    public long lastModified() throws IOException {
        return this.decoratedResource.lastModified();
    }

    /**
     * 通过Velocity解析
     * @return
     * @throws IOException
     */
    protected InputStream parserStreamByVeloctiy() throws IOException {
        return VelocityHelper.getInstance().evaluate(this.decoratedResource.getInputStream());
    }

    /**
     * 使用<code>Velocity</code>进行文件解析，解析后的流将重新输入到一个新的文件中
     * 这样便于使用<code>getURL</code>方法操作的文件也能使用<code>Velocity</code>进行文件
     * 过滤
     *
     */
    protected File parserStreamByVeloctiyToTempFile() throws IOException {
        Writer writer = VelocityHelper.getInstance().evaluateToWriter(
            this.decoratedResource.getInputStream());

        // 输出到一个临时文件中，以便下次使用
        File tmpFile = File.createTempFile(getFilename(), null);

        FileWriter fileWriter = new FileWriter(tmpFile);
        fileWriter.write(writer.toString());
        fileWriter.flush();

        return tmpFile;
    }

    /**
     * 是否允许过滤解析
     *
     * 可以从Bundle的MANIFEST.MFW文件中取得Resource-Filter属性，判断那些资源需要进行过滤
     */
    protected boolean isAllowedParser() {
        String fileName = this.getFilename();
        List<String> allowedFilterExtensions = new ArrayList<String>();
        allowedFilterExtensions.add(".xml");
        allowedFilterExtensions.add(".properties");
        
        for(String extension : allowedFilterExtensions) {
            if(fileName.endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return decoratedResource.getFilename().toString();
    }

    public Resource getDecoratedResource() {
        return decoratedResource;
    }

    public void setDecoratedResource(Resource decoratedResource) {
        this.decoratedResource = decoratedResource;
    }

}
