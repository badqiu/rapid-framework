<#include '/java_copyright.include'/>
package ${tableConfig.basepackage}.daointerface;
import org.springframework.dao.DataAccessException;
import ${tableConfig.basepackage}.operation.${tableConfig.className?lower_case}.*;
import ${tableConfig.basepackage}.dataobject.*;

<#include '/java_import.include'/>

/**
 * ${tableConfig.className}DAO
<#include '/java_description.include'/>
 */
public interface ${tableConfig.className}DAO {

<#list tableConfig.sqls as sql>

	/**
	 * ${sql.remarks!}
	 * sql:
	 * <pre>${StringHelper.removeCrlf(sql.executeSql)?trim}</pre> 
	 */
	public <@generateResultClassName sql 'DO'/> ${sql.operation}(<@generateOperationArguments sql/>) throws DataAccessException;
</#list>

}



