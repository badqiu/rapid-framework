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
package com.alibaba.tctools.custom;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 自定义Map,使用uid来唯一标识Map的内容
 * @author lai.zhoul
 * @email  hhlai1990@gmail.com
 * @date   2011-7-13
 */
public class UniqueUIDMap<K,V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 3801124242820219131L;

    
    //覆盖equals必须同时覆盖hashCode
    
    public UniqueUIDMap() {
        super();
        // TODO Auto-generated constructor stub
    }

    public UniqueUIDMap(Map<? extends K, ? extends V> m) {
        super(m);
        // TODO Auto-generated constructor stub
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;

        if (!(o instanceof Map))
            return false;
        Map<String,String> m = (Map<String,String>) o;
        if(!m.containsKey(TsvColumnConstants.JAVA_UID_NAME))
            return false;
        if("".equals(m.get(TsvColumnConstants.JAVA_UID_NAME)))
            return false;
        
        if(!this.get(TsvColumnConstants.JAVA_UID_NAME).equals(m.get(TsvColumnConstants.JAVA_UID_NAME)))
        return false;
        
        return true;
    }

    @Override
    public int hashCode() {
        
        return this.get(TsvColumnConstants.JAVA_UID_NAME).hashCode();
    }
    
    
    
}
