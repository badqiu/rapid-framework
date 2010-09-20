package cn.org.rapid_framework.linq;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.org.rapid_framework.beanutils.PropertyUtils;

public class Linq {
    
    public <T> List<Map> groupBy(Iterable<T> iter,String[] groupBys)  {
        List<Map> result = new ArrayList();
        Map list = new HashMap(); 
        long countAll = 0;
        long count = 0;
        Object min = null;
        Object max = null;
        double avg = 0;
        double sum = 0;
        for(T bean : iter) {
            if(bean != null) countAll++;
            
            String key = "";
            Map row = new LinkedHashMap(); 
            for(String groupBy : groupBys) {
                Object value = PropertyUtils.getSimpleProperty(bean, groupBy);
                key += value;
                row.put(groupBy, value);
            }
        }
        return result;
    }
    
}
