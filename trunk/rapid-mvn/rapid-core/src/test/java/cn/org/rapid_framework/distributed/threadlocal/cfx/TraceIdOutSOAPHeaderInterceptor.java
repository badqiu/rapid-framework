package cn.org.rapid_framework.distributed.threadlocal.cfx;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.xml.namespace.QName;

import org.apache.cxf.binding.soap.SoapHeader;
import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.DOMUtils;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;



public class TraceIdOutSOAPHeaderInterceptor extends AbstractSoapInterceptor {
	
	public TraceIdOutSOAPHeaderInterceptor() {
		super(Phase.WRITE);
	}

	public void handleMessage(SoapMessage message) throws Fault {
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