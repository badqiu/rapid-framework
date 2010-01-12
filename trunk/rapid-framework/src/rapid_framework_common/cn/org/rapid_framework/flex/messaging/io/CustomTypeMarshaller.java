package cn.org.rapid_framework.flex.messaging.io;

import cn.org.rapid_framework.flex.messaging.io.amf.translator.decoder.NumberDecoder;
import flex.messaging.io.Java15TypeMarshaller;
import flex.messaging.io.amf.translator.decoder.DecoderFactory;

public class CustomTypeMarshaller extends Java15TypeMarshaller {

	private static final NumberDecoder numberDecoder = new NumberDecoder();
	
	public Object convert(Object source, Class desiredClass) {
		if (DecoderFactory.isNumber(desiredClass)) {
			return numberDecoder.decodeObject(source, desiredClass);
		} else {
			return super.convert(source, desiredClass);
		}
	}

}
