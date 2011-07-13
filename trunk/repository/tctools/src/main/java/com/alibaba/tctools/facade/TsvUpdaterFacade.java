/**
 * Project: tctools
 * 
 * File Created at 2011-7-13
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
package com.alibaba.tctools.facade;

import java.io.File;

import com.alibaba.tctools.util.JavaReadUtil;
import com.alibaba.tctools.util.TsvWriterUtil;

/**
 * 根据java文件覆盖tsv文件
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-13
 */
public class TsvUpdaterFacade {

    public void updateTsvByJava(File javaFile,File tsvFile) throws Exception {
        TsvWriterUtil.write(tsvFile,
                JavaReadUtil.parse(javaFile, tsvFile));
    }
    
}
