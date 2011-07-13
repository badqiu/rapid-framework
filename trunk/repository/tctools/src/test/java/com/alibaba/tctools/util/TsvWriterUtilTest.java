package com.alibaba.tctools.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.alibaba.tctools.facade.FileProvider4Test;

public class TsvWriterUtilTest {

    @Test
    public void write() throws Exception{
        try{
       TsvWriterUtil.write(FileProvider4Test.tsvFile, JavaReadUtil.parse(FileProvider4Test.javaFile,FileProvider4Test.tsvFile));
        }catch (Exception e) {
           e.printStackTrace();
           fail();
        }
    }

}
