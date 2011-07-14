package com.alibaba.tctools.util;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.junit.Test;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.facade.FileProvider4Test;

public class JavaReadUtilTest {

    
    @Test
    public void parse() throws IOException {
        TsvModel model=  JavaReadUtil.parse(FileProvider4Test.javaFile,FileProvider4Test.tsvFile);
        assertEquals(3, model.getDataList().size());
        System.out.println(model.getDataList().get(1));
        System.out.println("标题信息："+Arrays.toString(model.getHeader()));
       
    }

}
