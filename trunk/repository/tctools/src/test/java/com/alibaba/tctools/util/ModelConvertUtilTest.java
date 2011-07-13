package com.alibaba.tctools.util;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;

import org.junit.Test;

import com.alibaba.tctools.facade.FileProvider4Test;

public class ModelConvertUtilTest {

    @Test
    public void testConvert() throws Exception {
        Map<String, String> configMap = GeneratorConfigUtil.parse(FileProvider4Test.configFile);

        List<Map<String, String>> dataList = TsvReadUtil.parse(FileProvider4Test.tsvFile).getDataList();
        List<Map<String, String>> result = ModelConvertUtil.convert(dataList, configMap);
        Assert.assertNotNull(result);
        System.out.println(result);
    }

}
