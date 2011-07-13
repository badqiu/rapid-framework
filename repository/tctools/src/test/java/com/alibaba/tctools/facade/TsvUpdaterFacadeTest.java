package com.alibaba.tctools.facade;

import static org.junit.Assert.*;

import org.junit.Test;

import com.alibaba.tctools.util.JavaReadUtil;
import com.alibaba.tctools.util.TsvWriterUtil;

public class TsvUpdaterFacadeTest {

    @Test
    public void updateTsvByJava() throws Exception {
        try {
            TsvUpdaterFacade.updateTsvByJava(FileProvider4Test.javaFile, FileProvider4Test.tsvFile);
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
