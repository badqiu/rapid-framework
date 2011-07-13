package com.alibaba.tctools.util;

import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.facade.FileProvider4Test;
import com.alibaba.tctools.util.TsvReadUtil;

public class TsvReadUtilTest {

    @Test
    public void testParse() throws IOException {

        TsvModel model = TsvReadUtil.parse(FileProvider4Test.tsvFile);
        Assert.assertEquals(TsvReadUtil.getClassNameByFileName(FileProvider4Test.tsvFile.getName()), model.getClassName());

    }
    
    @Test
    public void testParseHeader() throws IOException {

        String[] header=TsvReadUtil.parseHeader(FileProvider4Test.tsvFile);
        System.out.println(header.length);
       
    }

}
