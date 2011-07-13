package com.alibaba.tctools.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.custom.TsvPreference;

import cn.org.rapid_framework.generator.util.FileHelper;

/**
 * 读取.tsv原始信息到TsvModel
 * 
 * @author lai.zhoul 2011-7-8
 */
public class TsvReadUtil {


    /**
     * 返回未经配置的TsvModel
     * @param file
     * @return
     * @throws IOException
     */
    public static TsvModel parse(File file) throws IOException {
        return parse(new FileInputStream(file), file.getName());
    }

    public static TsvModel parse(InputStream in, String filename) throws IOException {
        //.tsv默认GBK编码
        return parse(new InputStreamReader(in, "GBK"), filename);
    }

    /**
     * @param inReader
     * @param filename
     * @return
     * @throws IOException
     */
    public static TsvModel parse(InputStreamReader inReader, String filename) throws IOException {
        TsvModel model = new TsvModel();
        ICsvMapReader inFile = new CsvMapReader(inReader, TsvPreference.EXCEL_PREFERENCE);
        //构造Tsvmodel
        model.setClassName(getClassNameByFileName(filename));
        System.out.println("[DEBUG]parsing文件名: " + filename + "=>java类名为 :" + model.getClassName());

        model.setHeader(inFile.getCSVHeader(true));
        System.out.print("[DEBUG]parsing 标题 :Tsv标题列数=" + model.getHeader().length + ",标题信息=");
        System.out.println(ArrayUtils.toString(model.getHeader()));

        Map<String, String> map = inFile.read(model.getHeader());
        while (map != null) {
            //对每一行map进行自定义处理？TsvLineMapProcessor待完善
            model.getDataList().add(map);
            System.out.println("[DEBUG] 行号=" + inFile.getLineNumber() + ",内容=" + map);
            map = inFile.read(model.getHeader());
        }
        System.out.println("[DEBUG] Tsv行数=" + inFile.getLineNumber());
        inFile.close();
        return model;
    }
    
    public static String getClassNameByFileName(String filename){
        return StringUtils.capitalise(filename).replaceFirst(".tsv", "");
    }
    
    /**
     * 单独解析tsv的标题数组信息
     * @param tsvfile
     * @return
     * @throws IOException
     */
    public static String[] parseHeader(File tsvfile)throws IOException{
        InputStreamReader inReader=new InputStreamReader(new FileInputStream(tsvfile),"GB2312");
        ICsvMapReader inFile = new CsvMapReader(inReader, TsvPreference.EXCEL_PREFERENCE);
        String[] header=inFile.getCSVHeader(true);
        System.out.print("[DEBUG]parsing 标题 :Tsv标题列数=" + header.length + ",标题信息=");
        System.out.println(ArrayUtils.toString(header));
        inFile.close();
        return  header;
    }

}
