package cn.org.rapid_framework.biz;

import cn.org.rapid_framework.exception.ErrorCodeException;

public interface BizCommand {

	public void execute() throws ErrorCodeException,Exception;
	
}
