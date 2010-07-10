package cn.org.rapid_framework.biz;

import java.sql.Timestamp;
import java.util.Stack;

public class ActionsStack implements java.io.Serializable{
	Stack<ActionRecord> actionsStack = new Stack();
	
	
	long fullActionsStartTime = -1;
	long costTime = -1;
	public void setCurrentAction(String action) {
		long now = System.currentTimeMillis();
		actionsStack.push(new ActionRecord(action,now,-1));
		if(actionsStack.isEmpty()) {
			fullActionsStartTime = now;
		}
		
		//computeBeforeActionCostTime();
	}

	private void computeBeforeActionCostTime() {
		if(actionsStack.isEmpty()) return;
		long now = System.currentTimeMillis();
		ActionRecord beforeAction = actionsStack.peek();
		if(beforeAction != null) {
			beforeAction.costTime = now - beforeAction.startTime;
		}
	}
	
	public ActionRecord getCurrentAction() {
		return actionsStack.peek();
	}
	
	public void finish() {
		computeBeforeActionCostTime();
		costTime = System.currentTimeMillis() - fullActionsStartTime;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		int count = 0;
		for(ActionRecord str : actionsStack) {
			sb.append(++count).append(".").append(str).append("\n");
		}
		return sb.toString();
	}
	
	public static class ActionRecord{
		String action;
		long startTime;
		long costTime;
		Exception exception;
		public ActionRecord(String action, long startTime, long costTime) {
			super();
			this.action = action;
			this.startTime = startTime;
			this.costTime = costTime;
		}
		
		public String toString() {
			return action+" startTime:"+new Timestamp(startTime)+" costTime:"+costTime;
		}
		
	}

	public Stack<ActionRecord> getActionsStack() {
		return actionsStack;
	}

	public long getFullActionsStartTime() {
		return fullActionsStartTime;
	}

	public long getCostTime() {
		return costTime;
	}

}
