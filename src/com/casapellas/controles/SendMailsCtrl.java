/**
 * 
 */
package com.casapellas.controles;

import java.util.*;

import javax.mail.internet.InternetAddress;
import com.casapellas.util.CustomEmailAddress;
import com.casapellas.util.MailHelper;
import com.casapellas.util.PropertiesSystem;

/**
 * @author CarlosHernandez
 *
 */
public class SendMailsCtrl {

	// private static MultiPartEmail email;
	// private static InternetAddress from;
	// private static InternetAddress bounceAddress;
	private static List<InternetAddress>blindCopy;
	// private static List<InternetAddress>to;

	private static String subject;
	// private static String servermail ="mail.casapellas.com.ni";
	private static String htmlBody;
	
	@SuppressWarnings("unused")
	private static boolean send() {
		boolean sent = true;
		
		try {
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			toList.add(new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS));
			
			List<CustomEmailAddress> bccList = new ArrayList<CustomEmailAddress>();
			if( blindCopy != null && !blindCopy.isEmpty() ) {
				for (InternetAddress bc: blindCopy ) {
					bccList.add(new CustomEmailAddress(bc.getAddress(), bc.getPersonal()));
				}
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
					toList, bccList, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS),
					subject, htmlBody);
			
			
		}catch(Exception e) {
			sent = false;
			e.printStackTrace();
		}
		
		return sent;
	}
	
	
	public static void sendSimpleMail(String body, List<String[]> addresses, String subject ) {
		try {
			
			List<CustomEmailAddress> toList = new ArrayList<CustomEmailAddress>();
			if(addresses != null) {
				for(String[] account: addresses) {
					toList.add(new CustomEmailAddress(account[0],  account[1]));
				}
			}
			
			MailHelper.SendHtmlEmail(
					new CustomEmailAddress(PropertiesSystem.WEBMASTER_EMAIL_ADRESS, "Módulo de Caja"),
					toList, null, new CustomEmailAddress(PropertiesSystem.MAIL_BOUNCEADDRESS),
					subject, htmlBody);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
