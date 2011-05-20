package cn.org.rapid_framework.flex.messaging.io;

import cn.org.rapid_framework.flex.messaging.io.amf.translator.decoder.NumberDecoder;
import flex.messaging.io.amf.translator.ASTranslator;
import flex.messaging.io.amf.translator.decoder.DecoderFactory;

/**
 * 用于修正flex的number数据类型转换错误： NaN(AS) => 0(Java) 变为 NaN(AS) => null(Java)
 * 
 * <br />
 * 配置:
 * <pre>
 * &lt;channel-definition id="my-amf" class="mx.messaging.channels.AMFChannel">
 *   &lt;endpoint url="http://{server.name}:{server.port}/{context.root}/messagebroker/amf" 
 *    class="flex.messaging.endpoints.AMFEndpoint"/>
 *       &lt;properties>	
 *	        &lt;serialization>
 *             &lt;type-marshaller>com.farata.messaging.io.CustomTypeMarshaller</type-marshaller>
 *          &lt;/serialization>
 *      &lt;/properties>	
 * &lt;/channel-definition>
 * </pre>
 * @author badqiu
 *
 */
public class CustomTypeMarshaller extends ASTranslator {

	private static final NumberDecoder numberDecoder = new NumberDecoder();
	
	public CustomTypeMarshaller() {
		System.out.println(CustomTypeMarshaller.class.getSimpleName()+" fixed number convert error: 'NaN(AS) => 0(Java)' to 'NaN(AS) => null(Java)'");
	}
	
	public Object convert(Object source, Class desiredClass) {
		if (DecoderFactory.isNumber(desiredClass)) {
			return numberDecoder.decodeObject(source, desiredClass);
		} else {
			return super.convert(source, desiredClass);
		}
	}

}
