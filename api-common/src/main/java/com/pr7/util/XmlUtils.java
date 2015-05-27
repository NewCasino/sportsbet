/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pr7.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Properties;

import javax.xml.bind.DataBindingException;
import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Node;

/**
 *
 * @author Admin
 */
public class XmlUtils {

    public static String toXMLString(Object obj) {
        StringWriter stringWriter = new StringWriter();
        StreamResult streamResult = new StreamResult(stringWriter);
        JAXB.marshal(obj, streamResult);
        return stringWriter.toString();
    }

    public static String toXMLString(Object obj, boolean omitXmlDeclare) {
    	Properties properties = new Properties();
    	
    	if (omitXmlDeclare)
    		properties.put(OutputKeys.OMIT_XML_DECLARATION, "yes");
		
        return toXMLString(obj, properties);
    }
    
    public static  <T> T xmlToObject(Class<T> clazz, String content) {
        StringReader reader = new StringReader(content);
        return (T) JAXB.unmarshal(reader, clazz);
    }

    public static String toXMLString(Object obj, Properties properties){
        try {
            StreamResult streamResult = new StreamResult(new StringWriter());

            JAXBContext newInstance = JAXBContext.newInstance(obj.getClass());
            Marshaller createMarshaller = newInstance.createMarshaller();

            TransformerFactory transFact = TransformerFactory.newInstance();
            TransformerHandler handler =((SAXTransformerFactory) transFact).newTransformerHandler();
            Transformer transformer = handler.getTransformer();

            if(properties != null){
                transformer.setOutputProperties(properties);
            }

            handler.setResult(streamResult);
            createMarshaller.marshal(obj,handler );

            return streamResult.getWriter().toString();
        } catch (TransformerConfigurationException ex) {
            throw new DataBindingException(ex);
        } catch (JAXBException ex) {
            throw new DataBindingException(ex);
        }
    }
    
    public static String toXMLString(Node node) throws Exception {
    	Transformer transformer = TransformerFactory.newInstance().newTransformer();
    	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    	
    	StreamResult result = new StreamResult(new StringWriter());
    	DOMSource source = new DOMSource(node);
    	transformer.transform(source, result);
    	
    	return result.getWriter().toString();
    }
}
