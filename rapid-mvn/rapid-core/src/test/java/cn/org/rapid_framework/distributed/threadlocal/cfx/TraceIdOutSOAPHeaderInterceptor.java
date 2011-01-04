package cn.org.rapid_framework.distributed.threadlocal.cfx;

import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;

import org.jcp.xml.dsig.internal.dom.DOMUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;


/**
 * 拦截器，用于将DistributedThreadLocal中的信息存放在 WebService的Header中
 * 
 * @author zhongxuan
 * @version $Id: TraceIdOutSOAPHeaderInterceptor.java,v 0.1 2011-1-4 下午02:10:50 zhongxuan Exp $
 */
public class TraceIdOutSOAPHeaderInterceptor extends AbstractSoapInterceptor {
	
	public TraceIdOutSOAPHeaderInterceptor() {
		super(Phase.WRITE);
	}

	public void handleMessage(SoapMessage message) throws Fault {
		DistributedThreadLocal.onBeforeRemoteCall();
		
		List<Header> headers = message.getHeaders();
		Map<String,String> threadlocalMap = DistributedThreadLocal.getMap();
		
		for(Map.Entry<String, String> entry : threadlocalMap.entrySet()) {
			headers.add(getHeader(entry.getKey(), entry.getValue()));
		}
	}

	private Header getHeader(String key, String value) {
		QName qName = new QName(key);
		Document document = DOMUtils.createDocument();
		Element element = document.createElement(key);
		element.appendChild(document.createTextNode(value));
		SoapHeader header = new SoapHeader(qName, element);
		return (header);
	}
}