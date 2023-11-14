package com.casapellas.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

public class MailHelper {

	private static Properties getDefaultProperties() {
		Properties props = new Properties();

		props.put("mail.smtp.auth", PropertiesSystem.MAIL_SMTP_AUTH);

		props.put("mail.smtp.ssl.trust", PropertiesSystem.MAIL_SMTP_SSL_TRUST);

		props.put("mail.smtp.ssl.protocols", PropertiesSystem.MAIL_SMTP_SSL_PROTOCOLS);
		props.put("mail.smtp.host", PropertiesSystem.MAIL_SMTP_HOST);
		props.put("mail.smtp.port", PropertiesSystem.MAIL_SMTP_PORT);

		return props;
	}

	private static Session getMailSession() throws Exception {
		MailSSLSocketFactory sf = null;

		sf = new MailSSLSocketFactory();

		Properties props = getDefaultProperties();
		props.put("mail.smtp.ssl.socketFactory", sf);

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(PropertiesSystem.MAIL_SMTP_USER, PropertiesSystem.MAIL_SMTP_PASSWORD);
			}
		});

		return session;
	}
	
	public static void SendHtmlEmail(CustomEmailAddress from, List<CustomEmailAddress> to, String subject, String body) {
		SendEmail(from, to, null, null, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, CustomEmailAddress to, String subject, String body) {
		SendEmail(from, new ArrayList<CustomEmailAddress>() { { add(to); } }, null, null, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, List<CustomEmailAddress> to, List<CustomEmailAddress> copy, CustomEmailAddress bounce, String subject, String body) {
		SendEmail(from, to, copy, new ArrayList<CustomEmailAddress>() { { add(bounce); } }, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, CustomEmailAddress to, List<CustomEmailAddress> copy, List<CustomEmailAddress> bounce, String subject, String body) {
		SendEmail(from, new ArrayList<CustomEmailAddress>() { { add(to); } }, copy, bounce, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, CustomEmailAddress to, CustomEmailAddress copy, List<CustomEmailAddress> bounce, String subject, String body) {
		SendEmail(from, new ArrayList<CustomEmailAddress>() { { add(to); } }, new ArrayList<CustomEmailAddress>() { { add(copy); } }, bounce, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, CustomEmailAddress to, CustomEmailAddress copy, String subject, String body) {
		SendEmail(from, new ArrayList<CustomEmailAddress>() { { add(to); } }, new ArrayList<CustomEmailAddress>() { { add(copy); } }, null, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, CustomEmailAddress to, CustomEmailAddress copy, CustomEmailAddress bounce, String subject, String body) {
		SendEmail(from, new ArrayList<CustomEmailAddress>() { { add(to); } }, new ArrayList<CustomEmailAddress>() { { add(copy); } }, new ArrayList<CustomEmailAddress>() { { add(bounce); } }, "text/html", subject, body, new String[] {});
	}
	
	@SuppressWarnings("serial")
	public static void SendHtmlEmail(CustomEmailAddress from, CustomEmailAddress to, List<CustomEmailAddress> copy, String subject, String body) {
		SendEmail(from, new ArrayList<CustomEmailAddress>() { { add(to); } }, copy, null, "text/html", subject, body, new String[] {});
	}
	
	public static void SendHtmlEmail(CustomEmailAddress from, List<CustomEmailAddress> to, List<CustomEmailAddress> copy, List<CustomEmailAddress> bounce, String subject, String body) {
		SendEmail(from, to, copy, bounce, "text/html", subject, body, new String[] {});
	}
	
	public static void SendHtmlEmail(CustomEmailAddress from, List<CustomEmailAddress> to, List<CustomEmailAddress> copy, List<CustomEmailAddress> bounce, String subject, String body, String[] attachmentFileUri) {
		SendEmail(from, to, copy, bounce, "text/html", subject, body, attachmentFileUri);
	}
	
	private static void SendEmail(CustomEmailAddress from, List<CustomEmailAddress> to, List<CustomEmailAddress> copy, List<CustomEmailAddress> bounce, String contentType, String subject, String body, String[] attachmentFileUri) {
		try {
			String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
			Pattern pattern = Pattern.compile(regex);
			
			Session session = getMailSession();

			MimeMessage message = new MimeMessage(session);
			
			if (!pattern.matcher(from.getEmail().trim()).matches()) {
				System.out.println("El email proporcionado no es valido, Valor:" + from.getEmail());
				return;
			}
			
			message.setFrom(new InternetAddress(from.getEmail(), from.getName()));
			
			Boolean hasValidTo = false;
			for (Iterator<CustomEmailAddress> iterator = to.iterator(); iterator.hasNext();) {
				CustomEmailAddress customEmailAddress = (CustomEmailAddress) iterator.next();
				
				if (pattern.matcher(customEmailAddress.getEmail()).matches()) {
					message.addRecipient(Message.RecipientType.TO, new InternetAddress(customEmailAddress.getEmail(), customEmailAddress.getName()));
					hasValidTo = true;
				}
			}
			
			if (!hasValidTo) {
				System.out.println("No existen destinatarios validos");
			}
			
			if (copy != null && copy.size() > 0) {
				for (Iterator<CustomEmailAddress> iterator = copy.iterator(); iterator.hasNext();) {
					CustomEmailAddress customEmailAddress = (CustomEmailAddress) iterator.next();
					if (customEmailAddress != null && pattern.matcher(customEmailAddress.getEmail()).matches()) {
						message.addRecipient(Message.RecipientType.CC, new InternetAddress(customEmailAddress.getEmail(), customEmailAddress.getName()));	
					}
				}
			}
			
			if (bounce != null && bounce.size() > 0) {
				for (Iterator<CustomEmailAddress> iterator = bounce.iterator(); iterator.hasNext();) {
					CustomEmailAddress customEmailAddress = (CustomEmailAddress) iterator.next();
					if (customEmailAddress != null && pattern.matcher(customEmailAddress.getEmail()).matches()) {
						message.addRecipient(Message.RecipientType.BCC, new InternetAddress(customEmailAddress.getEmail(), customEmailAddress.getName()));	
					}
				}
			}
			
			message.setSubject(subject);
			
			if (contentType.trim().toLowerCase().equals("text/html")) {
				Multipart mp = new MimeMultipart();
				MimeBodyPart htmlPart = new MimeBodyPart();
				
				htmlPart.setContent(body, "text/html");
				mp.addBodyPart(htmlPart);
				
				if (attachmentFileUri.length > 0) {
					for (String str : attachmentFileUri) {
						mp.addBodyPart(getMultiPartAttachment(str));
					}
				}
				
				message.setContent(mp);
			} else {
				message.setText(body);	
			}
			
			// send the message
			Transport.send(message);
		} catch (Exception e) {
			LogCajaService.CreateLog("SendEmail", "ERR", e.getMessage());
		}
	}
	
	private static MimeBodyPart getMultiPartAttachment(String uriPath) throws Exception {
		MimeBodyPart messageBodyPart = new MimeBodyPart();
		String fileName = uriPath.substring(uriPath.lastIndexOf("\\") + 1);
		
		DataSource source = new FileDataSource(uriPath);
		messageBodyPart.setDataHandler(new DataHandler(source));
		messageBodyPart.setFileName(fileName);
		
		return messageBodyPart;
	}
}
