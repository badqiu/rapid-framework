package com.alibaba.tctools.facade;

import static org.junit.Assert.*;

import org.junit.Test;

public class JavaGeneratorFacadeTest {

    @Test
    public void generatorJavaByTsv() throws Exception{
        try{
            JavaGeneratorFacade.generatorJavaByTsv(FileProvider4Test.tsvFile,
                FileProvider4Test.configFile);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

}
