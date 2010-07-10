package cn.org.rapid_framework.biz;

import java.util.Arrays;
import java.util.Collection;
import java.util.Stack;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BizContextExecutor {
	static Log log = LogFactory.getLog(BizContextExecutor.class);
	
	//如果这种操作,放在一个循环中,会挂掉
	public Object execute(String groupAction,BizContextCommand cmd) throws Exception {
		ActionsStack beforeActionStack = BizContext.getActionContext();
		BizContext.setActionContext(null);
		try {
			return cmd.execute();
		}catch(Exception e) {
			String local = String.format("执行[%s]时发生错误,错误原因:[%s],一共执行的相关操作:\n%s",
					BizContext.getCurrentAction(),e,BizContext.getActionContext().toString());
			log.error(local,e);
			throw e;
		}finally {
			BizContext.finish();
			BizContext.setActionContext(beforeActionStack);
		}
	}
	


	public static void main(String[] args) throws Exception {
		new BizContextExecutor().execute("招商银行",new BizContextCommand() {
			public Object execute() throws Exception {
				BizContext.setCurrentAction("删除中文");
				Thread.sleep(100);
				BizContext.setCurrentAction("查询用户");
				Thread.sleep(1000);
				BizContext.setCurrentAction("删除文章");
				Thread.sleep(500);
				if(true) throw new RuntimeException();
				BizContext.setCurrentAction("删除BLOG");
				return null;
			}
		});
	}
}
