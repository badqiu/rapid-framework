package ${basepackage}.daointerface;

import org.springframework.dao.DataAccessException;

/**
 * A dao interface provides methods to access all <tt>SEQUENCE</tt> objects.
 *
#parse("description-java.vm")
 */
public interface SeqDAO {
    
<#list tableConfigSet.sequences as seq>
    public long getNext${StringHelper.tableNameToClassName(seq)}() throws DataAccessException;
    
</#list>
}