package javacommon.mail;

import java.util.Map;

import org.springframework.mail.SimpleMailMessage;

public class OrderMailer {
	
	public static SimpleMailMessage createConfirmOrder(Map order) {
		return null;
	}
	
	public static void sendConfirmOrder(Map order) {
		SimpleMailMessage msg = createConfirmOrder(order);
	}
}
