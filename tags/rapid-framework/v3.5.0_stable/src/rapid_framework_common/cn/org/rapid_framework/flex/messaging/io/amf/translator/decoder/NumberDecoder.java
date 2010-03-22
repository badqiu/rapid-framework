/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2002 - 2007 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package cn.org.rapid_framework.flex.messaging.io.amf.translator.decoder;

import java.math.BigDecimal;
import java.math.BigInteger;

import flex.messaging.io.SerializationContext;
import flex.messaging.io.amf.translator.decoder.ActionScriptDecoder;
import flex.messaging.io.amf.translator.decoder.DecoderFactory;

/**
 * 
 * 用于修正flex的number数据类型转换错误： NaN => 0 变为 NaN => null
 * 
 * <br />
 * Decode an ActionScript type (a string or a double) to a Java number (of any type).
 * 
 * <br />
 * <h3>使用方法:</h3>
 * 在services-config.xml中增加如下配置:
 * <pre>
 *        &lt;channel-definition id="my-amf" class="mx.messaging.channels.AMFChannel">
 *            &lt;endpoint url="http://{server.name}:{server.port}/{context.root}/messagebroker/amf" class="flex.messaging.endpoints.AMFEndpoint"/>
 *            
 *            &lt;properties>
 *            	&lt;serialization>
 *            		&lt;type-marshaller>cn.org.rapid_framework.flex.messaging.io.CustomTypeMarshaller</type-marshaller>
 *            	&lt;/serialization>
 *            &lt;/properties>
 *        &lt;/channel-definition>
 * </pre>
 * @author badqiu
 */
public class NumberDecoder extends ActionScriptDecoder
{
    public Object decodeObject(Object shell, Object encodedObject, Class desiredClass)
    {
        Object result = null;

        if (encodedObject != null && encodedObject instanceof String)
        {
            String str = ((String)encodedObject).trim();
            try
            {
                // Short-ciruit String -> BigInteger,BigDecimal to avoid loss
                // of precision that occurs if we go through Double
                if (!SerializationContext.getSerializationContext().legacyBigNumbers)
                {
                    if (BigInteger.class.equals(desiredClass))
                    {
                        result = new BigInteger(str);
                        return result;
                    }
                    else if (BigDecimal.class.equals(desiredClass))
                    {
                        result = new BigDecimal(str);
                        return result;
                    }
                }

                Double dbl = new Double(str);
                encodedObject = dbl;
            }
            catch (NumberFormatException nfe)
            {
                DecoderFactory.invalidType(encodedObject, desiredClass);
            }
        }

        if (encodedObject instanceof Number || encodedObject == null)
        {
            Double dbl;
            if (desiredClass.isPrimitive())
            {
                if (encodedObject == null)
                    dbl = new Double(0);
                else
                    dbl = new Double(((Number)encodedObject).doubleValue());

                if ( dbl.equals(Double.NaN) )
                	result = null;	
                
                else if (Object.class.equals(desiredClass) || Double.TYPE.equals(desiredClass))
                	result = dbl;
                else if (Integer.TYPE.equals(desiredClass))
                	result = new Integer(dbl.intValue());                		         	
                else if (Long.TYPE.equals(desiredClass))
                	result = new Long(dbl.longValue());                		               	
                else if (Float.TYPE.equals(desiredClass))
                    result = new Float(dbl.floatValue());  
                else if (Short.TYPE.equals(desiredClass))
                    result = new Short(dbl.shortValue());  
                else if (Byte.TYPE.equals(desiredClass))
                    result = new Byte(dbl.byteValue());  
            }
            else if (encodedObject != null)
            {
                dbl = new Double(((Number)encodedObject).doubleValue());
                if ( dbl.equals(Double.NaN) )
            		result = null;	
                
                else if (Object.class.equals(desiredClass) || Number.class.equals(desiredClass) ||
                        Double.class.equals(desiredClass)) 
                	result = dbl;                		         	 
                else if (Integer.class.equals(desiredClass))
                    result = new Integer(dbl.intValue());                		
                else if (Long.class.equals(desiredClass))
                    result = new Long(dbl.longValue());                		               	
                else if (Float.class.equals(desiredClass))
                    result = new Float(dbl.floatValue());  
                else if (Short.class.equals(desiredClass))
                	result = new Short(dbl.shortValue());  
                else if (BigDecimal.class.equals(desiredClass))
                {
                    // Though this is a little slower than using the
                    // double constructor for BigDecimal, it yields a rounded
                    // result which is more predicable (see the Javadoc for
                    // BigDecimal and bug: 182378).
                    if (SerializationContext.getSerializationContext().legacyBigNumbers)
                        result = new BigDecimal(dbl.doubleValue());
                    else
                        result = new BigDecimal(String.valueOf(dbl));
                }
                else if (Byte.class.equals(desiredClass))
                    result = new Byte(dbl.byteValue());  
                else if (BigInteger.class.equals(desiredClass))
                {
                    // Must have no special characters or whitespace
                    String val = null;
                    long l = dbl.longValue();
                    if (l > Integer.MAX_VALUE)
                    {
                        Long lo = new Long(dbl.longValue());
                        val = lo.toString().toUpperCase();
                        int suffix = val.indexOf("L");
                        if (suffix != -1)
                            val = val.substring(0, suffix);
                    }
                    else
                    {
                        Integer i = new Integer(dbl.intValue());
                        val = i.toString();
                    }
                    result = new BigInteger(val.trim());
                }
            }
            else
            {
                result = null;
            }
        }
        else
        {
            DecoderFactory.invalidType(encodedObject, desiredClass);
        }

        return result;
    }
}
