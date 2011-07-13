
package ${basepackage};


import org.junit.Test;

import static junit.framework.Assert.*;



public class ${className}Test {
	

    <#list model.dataList as map>
    
    /**
     * @uid    ${map.uid}
     * @前置条件 ${map.preCondition}
     * @期望结果 ${map.expectResult}
     * @步骤    ${map.step}      
     */
      @Test
      public void ${map.methodName}(){
          
      }
        
        
    </#list>
		
}
