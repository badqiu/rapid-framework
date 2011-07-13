/**
 * Project: tctools
 * 
 * File Created at 2011-7-12
 * $Id$
 * 
 * Copyright 1999-2100 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.alibaba.tctools.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Map;

import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;

import com.alibaba.tctools.custom.TsvModel;
import com.alibaba.tctools.custom.TsvPreference;

/**
 * TODO Comment of TsvWriterUtil
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-12
 */
public class TsvWriterUtil {

    public static void write(File tsvfile, TsvModel model) throws IOException {

        FileOutputStream out = new FileOutputStream(tsvfile);
        OutputStreamWriter ow = new OutputStreamWriter(out, "GBK");//指定tsv默认编码GBK

        ICsvMapWriter writer = new CsvMapWriter(ow, TsvPreference.EXCEL_PREFERENCE);
        System.out.println(model.getDataList().size());
        writer.writeHeader(model.getHeader());
        for (Map<String, String> map : model.getDataList()) {
            writer.write(map,getNameMapping(map));//可以植入CellProcessor？未完善
        }
        writer.close();
    }
    
    
    public static String[] getNameMapping(Map<String, String> map){
        String[] thisHeader=map.keySet().toArray(new String[map.keySet().size()]);
        System.out.println(Arrays.toString(thisHeader));
        return thisHeader;
    }
}
