package com.test.tsv;

import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvListReader;
import org.supercsv.io.CsvMapReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvListReader;
import org.supercsv.io.ICsvMapReader;
import org.supercsv.prefs.CsvPreference;

import com.alibaba.tctools.custom.TsvPreference;

public class TSVOpTest {

    private InputStreamReader inReader;

    @Before
    public void init() throws Exception {
        inReader = new InputStreamReader(TSVOpTest.class.getClassLoader().getResourceAsStream(
                "LoginCase.tsv"), "GB2312");
    }

    @After
    public void end() throws Exception {
        inReader.close();

    }

    /**
     * 读取tsv标题行信息
     * 
     * @throws Exception
     */
    @Test
    public void testGetHeader() throws Exception {
        ICsvBeanReader inFile = new CsvBeanReader(inReader, TsvPreference.EXCEL_PREFERENCE);
        final String[] header = inFile.getCSVHeader(true);
        for (int i = 0; i < header.length; i++) {
            System.out.print(header[i] + '\t');
        }
        inFile.close();
    }

    /**
     * 读取一行的具体信息,标题行（即第0行）除外
     * 
     * @throws Exception
     */
    @Test
    public void testGetLine() throws Exception {
        ICsvListReader inFile = new CsvListReader(inReader, TsvPreference.EXCEL_PREFERENCE);
        List<String> linedatalist = inFile.read();//一行
        int lineNumber = inFile.getLineNumber();//当前行号
        System.out.println("行号：" + lineNumber);
        System.out.println("此行列数: " + linedatalist.size());
        System.out.println("此行内容： ");
        for (String content : linedatalist) {
            System.out.print(content + '\t');
        }
        inFile.close();
    }

    /**
     * 遍历所有行
     * 
     * @throws Exception
     */
    @Test
    public void testGetAllLine() throws Exception {
        ICsvListReader inFile = new CsvListReader(inReader, CsvPreference.EXCEL_PREFERENCE);
        List<String> linedatalist = inFile.read();
        while (linedatalist != null) {
            printLineData(inFile.getLineNumber(), linedatalist);
            linedatalist = inFile.read();
        }
        inFile.close();
    }

    /**
     * 遍历所有行到HashMap
     * 
     * @throws Exception
     */
    @Test
    public void testGetAllDataToMap() throws Exception {
        ICsvMapReader inFile = new CsvMapReader(inReader, CsvPreference.EXCEL_PREFERENCE);
        String[] header = inFile.getCSVHeader(true);
        Map<String, String> linedatamap = inFile.read(header);
        while (linedatamap != null) {
            System.out.println(linedatamap);
            linedatamap = inFile.read(header);
        }
        inFile.close();
    }

    @Test
    public void testPlain() {
        System.out.println(StringUtils.capitalise("testcase"));
    }

    private static void printLineData(int lineNumber, List<String> as) {
        System.out.println("Line " + lineNumber + " has " + as.size() + " values:");
        for (String s : as) {
            System.out.println("\t|" + s + "|");
        }
        System.out.println();
    }

}
