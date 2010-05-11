/*
 * @(#)XMLUtils.java	1.6 05/11/17
 *
 * Copyright 2006 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package cn.org.rapid_framework.util.config.group;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.InvalidPropertiesFormatException;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * A class used to aid in Properties load and save in XML. Keeping this
 * code outside of Properties helps reduce the number of classes loaded
 * when Properties is loaded.
 *
 * @version 1.9, 01/23/03
 * @author  Michael McCloskey
 * @since   1.3
 */
class XMLUtils {

    // XML loading and saving methods for Properties

    // The required DTD URI for exported properties
    private static final String PROPS_DTD_URI =
    "http://rapid-framework.googlecode.com/svn/trunk/dtd/groups.dtd";

    private static final String PROPS_DTD =
    "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
    "<!ELEMENT groups ( comment?,group* ) >" +
    "<!ELEMENT comment (#PCDATA) >" +
    "<!ELEMENT group ( property* ) >" +
    "<!ELEMENT property (#PCDATA) >" +
    "<!ATTLIST property name CDATA #REQUIRED>" +
    "<!ATTLIST group name CDATA #REQUIRED>" ;
 
    /**
     * Version number for the format of exported properties files.
     */
    private static final String EXTERNAL_XML_VERSION = "1.0";

    static void load(Groups props, InputStream in)
        throws IOException
    {
        Document doc = null;
        try {
            doc = getLoadingDoc(in);
        } catch (SAXException saxe) {
            throw new InvalidPropertiesFormatException(saxe);
        }
        Element propertiesElement = (Element)doc.getChildNodes().item(1);
        String xmlVersion = propertiesElement.getAttribute("version");
        if (xmlVersion.compareTo(EXTERNAL_XML_VERSION) > 0)
            throw new IllegalStateException(
                "Exported group file format version " + xmlVersion +
                " is not supported. This java installation can read" +
                " versions " + EXTERNAL_XML_VERSION + " or older. You" +
                " may need to install a newer version of JDK.");
        importGroups(props, propertiesElement);
    }

    static Document getLoadingDoc(InputStream in)
        throws SAXException, IOException
    {
	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	dbf.setIgnoringElementContentWhitespace(true);
	dbf.setValidating(true);
        dbf.setCoalescing(true);
        dbf.setIgnoringComments(true);
	try {
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    db.setEntityResolver(new Resolver());
	    db.setErrorHandler(new EH());
            InputSource is = new InputSource(in);
	    return db.parse(is);
	} catch (ParserConfigurationException x) {
	    throw new Error(x);
	}
    }

    static void importGroups(Groups props, Element propertiesElement) {
        NodeList entries = propertiesElement.getChildNodes();
        int numEntries = entries.getLength();
        boolean hasCommentElement = numEntries > 0 && entries.item(0).getNodeName().equals("comment");
        if(hasCommentElement) {
//        	props.setComment(entries.item(0).getFirstChild().getNodeValue());
        }
        
        int start = hasCommentElement ? 1 : 0;
        for (int i=start; i<numEntries; i++) {
            Element entry = (Element)entries.item(i);
            if (entry.hasAttribute("name")) {
                NodeList nodeList = entry.getChildNodes();
                String groupName = entry.getAttribute("name");
				props.addGroup(groupName, importProperties(nodeList));
            }
        }
    }

    private static Properties importProperties(NodeList nodeList) {
    	Properties properties = new Properties();
    	for(int i = 0; i < nodeList.getLength(); i++) {
    		Element entry = (Element)nodeList.item(i);
    		if(entry.hasAttribute("name")) {
    			String val = (entry == null) ? "" : entry.getFirstChild().getNodeValue();
    			String key = entry.getAttribute("name");
				properties.setProperty(key, val);
    		}
    	}
		return properties;
	}

	static void save(Groups groups, OutputStream os, String comment, 
                     String encoding) 
        throws IOException
    {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = null;
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException pce) {
            assert(false);
        }
        Document doc = db.newDocument();
        Element groupsElement =  (Element)
            doc.appendChild(doc.createElement("groups"));

        if (comment != null) {
            Element comments = (Element)groupsElement.appendChild(
                doc.createElement("comment"));
            comments.appendChild(doc.createTextNode(comment));
        }

        Set keys = groups.getGroupNames();
        Iterator i = keys.iterator();
        while(i.hasNext()) {
            String key = (String)i.next();
            Element groupElement = (Element)groupsElement.appendChild(
                doc.createElement("group"));
            groupElement.setAttribute("name", key);
            
            Properties props = groups.getGroup(key);
            for(Map.Entry prop : props.entrySet()) {
            	Element propertyElement = doc.createElement("property");
            	propertyElement.setAttribute("name", (String)prop.getKey());
            	propertyElement.appendChild(doc.createTextNode((String)prop.getValue()));
				groupElement.appendChild(propertyElement);
//				groupElement.appendChild(doc.createTextNode("    \n"));
            }
        }
        emitDocument(doc, os, encoding);
    }
	
	//TODO 美化groups config的xml输出
    static void emitDocument(Document doc, OutputStream os, String encoding)
        throws IOException
    {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = null;
        try {
            t = tf.newTransformer();
            t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, PROPS_DTD_URI);
            t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.setOutputProperty(OutputKeys.METHOD, "xml");
            t.setOutputProperty(OutputKeys.ENCODING, encoding);
        } catch (TransformerConfigurationException tce) {
            assert(false);
        }
        DOMSource doms = new DOMSource(doc);
        StreamResult sr = new StreamResult(os);
        try {
            t.transform(doms, sr);
        } catch (TransformerException te) {
            IOException ioe = new IOException();
            ioe.initCause(te);
            throw ioe;
        }
    }

    private static class Resolver implements EntityResolver {
        public InputSource resolveEntity(String pid, String sid)
            throws SAXException
        {
            if (sid.equals(PROPS_DTD_URI)) {
                InputSource is;
                is = new InputSource(new StringReader(PROPS_DTD));
                is.setSystemId(PROPS_DTD_URI);
                return is;
            }
            throw new SAXException("Invalid system identifier: " + sid);
        }
    }

    private static class EH implements ErrorHandler {
        public void error(SAXParseException x) throws SAXException {
            throw x;
        }
        public void fatalError(SAXParseException x) throws SAXException {
            throw x;
        }
        public void warning(SAXParseException x) throws SAXException {
            throw x;
        }
    }

}
