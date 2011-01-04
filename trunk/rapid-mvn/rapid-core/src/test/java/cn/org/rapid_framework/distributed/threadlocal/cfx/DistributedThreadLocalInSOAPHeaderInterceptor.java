package cn.org.rapid_framework.distributed.threadlocal.cfx;

import java.util.HashMap;
import java.util.Map;

import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;

import org.apache.cxf.binding.soap.SoapMessage;
import org.apache.cxf.binding.soap.interceptor.AbstractSoapInterceptor;
import org.apache.cxf.binding.soap.saaj.SAAJInInterceptor;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.phase.Phase;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import cn.org.rapid_framework.distributed.threadlocal.DistributedThreadLocal;


/**
 * 输入(In)拦截器，用于从 WebService SOAP 的Header中取回DistributedThreadLocal中的信息,并存放在DistributedThreadLocal中
 * 
 * @author badqiu
 */
public class DistributedThreadLocalInSOAPHeaderInterceptor extends AbstractSoapInterceptor {
	
    private SAAJInInterceptor saajIn = new SAAJInInterceptor();  
    
    public DistributedThreadLocalInSOAPHeaderInterceptor() {  
        super(Phase.PRE_PROTOCOL);  
        getAfter().add(SAAJInInterceptor.class.getName());  
    }  

	public void handleMessage(SoapMessage message) throws Fault {
		SOAPMessage doc = message.getContent(SOAPMessage.class);  
        if (doc == null) {  
            saajIn.handleMessage(message);  
            doc = message.getContent(SOAPMessage.class);  
        }  
        
        Map<String,String> headers = toHeadersMap(doc);  
		DistributedThreadLocal.putAll(headers);
		
		DistributedThreadLocal.onReceivedDistributedThreadLocal();
	}

	private Map toHeadersMap(SOAPMessage doc) {
		SOAPHeader header = getSOAPHeader(doc);  
        if (header == null) {  
            return new HashMap(0);  
        } 
        
        Map<String,String> headersMap = new HashMap();
        NodeList nodes = header.getChildNodes();
        for(int i=0; i<nodes.getLength(); i++) {  
        	Node item = nodes.item(i);
        	if(item.hasChildNodes()) {
        		headersMap.put(item.getLocalName(), item.getFirstChild().getNodeValue());
        	}
        }
        return headersMap;
	}

	private SOAPHeader getSOAPHeader(SOAPMessage doc) {
		SOAPHeader header;
		try {
			header = doc.getSOAPHeader();
		} catch (SOAPException e) {
			throw new RuntimeException(e);
		}
		return header;
	}




}