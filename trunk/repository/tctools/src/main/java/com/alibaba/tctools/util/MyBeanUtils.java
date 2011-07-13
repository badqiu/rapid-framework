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

import org.apache.commons.beanutils.BeanUtils;

/**
 * print bean info
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-12
 */
public class MyBeanUtils {

    public static void  describe(Object bean) {
        if(bean instanceof String) System.out.println(bean);
        try{
        System.out.println(BeanUtils.describe(bean));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
