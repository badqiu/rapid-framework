/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2005 All Rights Reserved.
 */
package cn.org.rapid_framework.io.filter;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.CharArrayWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

/**
 * 
 * Velocity引擎帮助类
 * 
 * @author badqiu
 */
public class VelocityHelper {

    /** 单态实例 */
    private static final VelocityHelper instance = new VelocityHelper();

    /** 私有构造函数 */
    private VelocityHelper() {
        //初始化velocity的信息 主要设置一些Velocity的默认属性

        //初始化
        try {
            Velocity.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static VelocityHelper getInstance() {
        return instance;
    }

    public boolean evaluate(Context context, Writer writer, Reader reader) {
        try {
            return Velocity.evaluate(context, writer, "", reader);
        } catch (Exception e) {
            throw new RuntimeException("velocity evaluate error! detial [" + e.getMessage() + "]");
        }
    }

    /**
     * 通过Map过滤一个输入流
     * 
     * @param map
     * @param reader
     * @return
     */
    @SuppressWarnings("unchecked")
    public InputStream evaluate(Map map, Reader reader) {
        try {
            VelocityContext context = convertVelocityContext(map);
            CharArrayWriter writer = new CharArrayWriter();
            //开始评估
            evaluate(context, writer, reader);
            //把产生的输出流(字符流)，转换成输入流(字节流)
            byte[] dataBytes = writer.toString().getBytes();
            BufferedInputStream bis = new BufferedInputStream(new ByteArrayInputStream(dataBytes));
            return bis;
        } catch (Exception e) {
            throw new RuntimeException("velocity evaluate error! detial [" + e.getMessage() + "]");
        }
    }

    /**
     * 通过Map过滤一个输入流
     * 
     * @param map
     * @param reader
     * @return
     */
    @SuppressWarnings("unchecked")
    public Writer evaluateToWriter(Map map, Reader reader) {
        try {
            VelocityContext context = convertVelocityContext(map);
            CharArrayWriter writer = new CharArrayWriter();
            //开始评估
            evaluate(context, writer, reader);

            return writer;
        } catch (Exception e) {
            throw new RuntimeException("velocity evaluate error! detial [" + e.getMessage() + "]");
        }
    }

    /**
     * 通过系统的Configration过滤一个输入流
     * 
     * @param map
     * @param reader
     * @return
     */
    public InputStream evaluate(Reader reader) {
        return evaluate(getConfigMap(), reader);
    }

    /**
     * 通过系统的Configration过滤一个输入流
     * 
     * @param map
     * @param reader
     * @return
     */
    public Writer evaluateToWriter(Reader reader) {
        return evaluateToWriter(getConfigMap(), reader);
    }
    
    
    /**
     * 通过系统的Configration过滤一个输入流
     * 
     * @param map
     * @param reader
     * @return
     */
    public Writer evaluateToWriter(InputStream inputStream) {
        return evaluateToWriter(getConfigMap(), new InputStreamReader(inputStream));
    }

    /**
     * 通过系统的Configration过滤一个输入流
     * 
     * @param map
     * @param reader
     * @return
     */
    public InputStream evaluate(InputStream inputStream) {
        return evaluate(getConfigMap(), new InputStreamReader(inputStream));
    }

    /**
     * 取得Velocity系统属性
     * 
     * @param key
     * @return
     */
    public Object getProperty(String key) {
        return Velocity.getProperty(key);
    }

    protected Properties getConfigMap() {
        return System.getProperties();
    }
    
    private VelocityContext convertVelocityContext(Map<String, Object> map) {
        VelocityContext context = new VelocityContext();
        if (map == null)
            return context;
        Set<Map.Entry<String, Object>> entrySet = map.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            context.put(entry.getKey(), entry.getValue());
        }
        return context;
    }

}
