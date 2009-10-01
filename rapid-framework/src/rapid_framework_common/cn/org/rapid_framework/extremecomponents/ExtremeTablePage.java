package cn.org.rapid_framework.extremecomponents;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.extremecomponents.table.context.Context;
import org.extremecomponents.table.context.HttpServletRequestContext;
import org.extremecomponents.table.limit.Limit;
import org.extremecomponents.table.limit.LimitFactory;
import org.extremecomponents.table.limit.Sort;
import org.extremecomponents.table.limit.TableLimit;
import org.extremecomponents.table.limit.TableLimitFactory;

/**
 *
 * @author badqiu
 */
public class ExtremeTablePage {

    static public Limit getLimit(HttpServletRequest request, int defautPageSize) {
    	return getLimit(request,Integer.MAX_VALUE, defautPageSize,null);
    }
    
    static public Limit getLimit(HttpServletRequest request, int totalRows,int defautPageSize) {
    	return getLimit(request,totalRows, defautPageSize,null);
    }
    
    static public Limit getLimit(HttpServletRequest request, int totalRows,int defautPageSize,String tableId) {
        Context context = new HttpServletRequestContext(request);
        LimitFactory limitFactory = null;
        if(tableId == null)
        	limitFactory = new TableLimitFactory(context);
        else
        	limitFactory = new TableLimitFactory(context,tableId);
        TableLimit limit = new TableLimit(limitFactory);
        limit.setRowAttributes(totalRows, defautPageSize);
        return limit;
    }

}
