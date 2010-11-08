package ${basepackage}.ibatis;

import ${basepackage}.daointerface.SeqDAO;
import org.springframework.dao.DataAccessException;
import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

public class IbatisSeqDAO extends SqlMapClientDaoSupport implements SeqDAO {
    
<#list tableConfigSet.sequences as seq>
    
    public long getNext${StringHelper.tableNameToClassName(seq)}() throws DataAccessException {
        return ((Number) getSqlMapClientTemplate().queryForObject("SEQ.${seq}", null)).longValue();
    }    
</#list>
}