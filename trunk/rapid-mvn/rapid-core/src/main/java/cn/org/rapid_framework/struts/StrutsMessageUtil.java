package cn.org.rapid_framework.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
/**
 * @author badqiu
 */
public class StrutsMessageUtil {
	/**
	 * ����Ƿ�����Ϣ
	 * 
	 * @param request
	 * @return
	 * @roseuid 4398DA2A039E
	 */
	public static boolean hasMessage(HttpServletRequest request) {
		ActionMessages messages = getMessages(request);
		if (messages == null || messages.isEmpty())
			return false;
		else
			return true;
	}
	
	/**
	 * ����Ƿ��д���
	 * 
	 * @param request
	 * @return
	 * @roseuid 4398DA2A0358
	 */
	public static  boolean hasError(HttpServletRequest request) {
		ActionMessages errors = getErrors(request);
		if (errors == null || errors.isEmpty())
			return false;
		else
			return true;
	}
	
	/**
	 * @param request
	 * @param key
	 * @param error
	 * @roseuid 4398DA2A0235
	 */
	public static void addError(HttpServletRequest request, String key,
			ActionError error) {
		ActionMessages errors = getErrors(request);
		if (errors == null) {
			errors = new ActionErrors();
		}
		errors.add(key, error);
		saveErrors(request, errors);
	}
	
	/**
	 * @param request
	 * @param key
	 * @param errorKey
	 */
	public static void addError(HttpServletRequest request, String key, String errorKey){
		ActionMessages errors = getErrors(request);
		if (errors == null) {
			errors = new ActionErrors();
		}
		errors.add(key, new ActionError(errorKey));
		saveErrors(request, errors);		
	}
	
	/**
	 * @param request
	 * @param message
	 * @roseuid 4398DA2A02A3
	 */
	public static void addMessage(HttpServletRequest request,ActionMessage message) {
		addMessage(request, ActionMessages.GLOBAL_MESSAGE, message);
	}
	
	public static void addMessage(HttpServletRequest request,String msgKey) {
		addMessage(request, new ActionMessage(msgKey));
	}
	
	/**
	 * @param request
	 * @param key
	 * @param message
	 * @roseuid 4398DA2A01B3
	 */
	public static void addMessage(HttpServletRequest request, String key,
			ActionMessage message) {
		ActionMessages messages = getMessages(request);
		if (messages == null) {
			messages = new ActionMessages();
		}
		messages.add(key, message);
		saveMessages(request, messages);
	}

	/**
	 * @param request
	 * @param error
	 * @roseuid 4398DA2A02FE
	 */
	public static void addError(HttpServletRequest request, ActionError error) {
		addError(request, ActionErrors.GLOBAL_ERROR, error);
	}

	/**
	 * @param request
	 * @param error
	 * @roseuid 4398DA2A02FE
	 */
	public static void addError(HttpServletRequest request, String msgKey) {
		addError(request, ActionErrors.GLOBAL_ERROR, new ActionError(msgKey));
	}
	/**
	 * @param request
	 * @return org.apache.struts.action.ActionErrors
	 * @roseuid 4398DA2A03E4
	 */
	public static ActionMessages getErrors(HttpServletRequest request) {
        ActionErrors errors = null;
        HttpSession session = request.getSession(false);
 
        if (request.getAttribute(Globals.ERROR_KEY) != null) {
            errors = (ActionErrors) request.getAttribute(Globals.ERROR_KEY);
        } else if (session != null && session.getAttribute(Globals.ERROR_KEY) != null) {
            errors = (ActionErrors) session.getAttribute(Globals.ERROR_KEY);
        } else {
            errors = new ActionErrors();
            saveErrors(request, errors);
        }

        return errors;
	}
	
    /**
     * Convenience method to initialize messages in a subclass.
     *
     * @param request the current request
     * @return the populated (or empty) messages
     */
    public static ActionMessages getMessages(HttpServletRequest request) {
        ActionMessages messages = null;
        HttpSession session = request.getSession();

        if (request.getAttribute(Globals.MESSAGE_KEY) != null) {
            messages = (ActionMessages) request.getAttribute(Globals.MESSAGE_KEY);
        } else if (session.getAttribute(Globals.MESSAGE_KEY) != null) {
            messages = (ActionMessages) session.getAttribute(Globals.MESSAGE_KEY);
        } else {
            messages = new ActionMessages();
            saveMessages(request, messages);
        }

        return messages;
    }
    
    /**
     * <p>Save the specified error messages keys into the appropriate request
     * attribute for use by the &lt;html:errors&gt; tag, if any messages
     * are required. Otherwise, ensure that the request attribute is not
     * created.</p>
     *
     * @param request The servlet request we are processing
     * @param errors Error messages object
     * @since Struts 1.2
     */
    public static void saveErrors(HttpServletRequest request, ActionMessages errors) {

        // Remove any error messages attribute if none are required
        if ((errors == null) || errors.isEmpty()) {
            request.removeAttribute(Globals.ERROR_KEY);
            return;
        }

        // Save the error messages we need
        request.getSession().setAttribute(Globals.ERROR_KEY, errors);

    }


    /**
     * <p>Save the specified messages keys into the appropriate request
     * attribute for use by the &lt;html:messages&gt; tag (if
     * messages="true" is set), if any messages are required. Otherwise,
     * ensure that the request attribute is not created.</p>
     *
     * @param request The servlet request we are processing.
     * @param messages The messages to save. <code>null</code> or empty
     * messages removes any existing ActionMessages in the request.
     *
     * @since Struts 1.1
     */
    public static void saveMessages(
        HttpServletRequest request,
        ActionMessages messages) {

        // Remove any messages attribute if none are required
        if ((messages == null) || messages.isEmpty()) {
            request.removeAttribute(Globals.MESSAGE_KEY);
            return;
        }

        // Save the messages we need
        request.getSession().setAttribute(Globals.MESSAGE_KEY, messages);
    }

}
