package cn.org.rapid_framework.biz;



public class BizContext {
	private static ThreadLocal<ActionsStack> actionContext = new ThreadLocal();
	public BizContext() {
	}
	
	public static ActionsStack getActionContext() {
		return actionContext.get();
	}
	
	public static void setActionContext(ActionsStack ac) {
		actionContext.set(ac);
	}
	
	public static void finish() {
		ActionsStack ac = getActionContext();
		if(ac != null) {
			ac.finish();
		}
	}
	
	public static String getCurrentAction() {
		ActionsStack actionsStack = actionContext.get();
		if(actionsStack == null) return null;
		return actionsStack.getCurrentAction().toString();
	}
	
	public static void setCurrentAction(String action) {
		ActionsStack actionsStack = actionContext.get();
		if(actionsStack == null) {
			actionsStack = new ActionsStack();
			actionContext.set(actionsStack);
		}
		actionsStack.setCurrentAction(action);
	}
	
}
