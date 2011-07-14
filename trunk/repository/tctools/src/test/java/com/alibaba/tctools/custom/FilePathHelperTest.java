package com.alibaba.tctools.custom;

import static org.junit.Assert.fail;

import java.io.File;

import junit.framework.Assert;

import org.junit.Test;

public class FilePathHelperTest {

    @Test
    public void testGetFileByPath() {
        //fail("Not yet implemented");
      ;
    }

    @Test
    public void testGetDestJavaPath() {
     
        try{
        File file=FilePathHelper.getDestJavaPath("LoginCase.java");
        Assert.assertNotNull(file);
        }catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testGetBasepackageDir() {
        System.out.println(FilePathHelper.getBasepackageDir("com.alibaba.gen"));
    }

}
