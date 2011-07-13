/**
 * Project: alibaba-qa-tc-tools
 * 
 * File Created at 2011-7-8
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
package com.alibaba.tctools.custom;

import org.supercsv.prefs.CsvPreference;

/**
 * TODO Comment of TsvPreference
 * 
 * @author lai.zhoul
 * @email hhlai1990@gmail.com
 * @date 2011-7-8
 */
public class TsvPreference extends CsvPreference {

    public TsvPreference(char quoteChar, int delimiterChar, String endOfLineSymbols) {
        super(quoteChar, delimiterChar, endOfLineSymbols);
        // TODO Auto-generated constructor stub
    }

    /**
     * Ready to use configuration for reading Windows Excel exported CSV files.
     */
    public static final CsvPreference EXCEL_PREFERENCE = new CsvPreference('"', '\t', "\n");

}
