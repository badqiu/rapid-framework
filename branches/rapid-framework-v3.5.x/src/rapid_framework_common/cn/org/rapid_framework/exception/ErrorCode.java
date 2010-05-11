package cn.org.rapid_framework.exception;

/**
 * 错误码,ERROR_CODE的设计目标是从google根据error code查到到原因.
 * 如 ORA-95539
 * @author badqiu
 * @see ErrorCodeException
 */
public interface ErrorCode extends java.io.Serializable{

	public String getErrorCode();

	public String getErrorDetails();

}
