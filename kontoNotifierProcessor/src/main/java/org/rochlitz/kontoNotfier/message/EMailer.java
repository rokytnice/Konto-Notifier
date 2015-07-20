package org.rochlitz.kontoNotfier.message;

import java.util.Properties;

import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Named("EMailer")
public class EMailer {
	
	
	public static void mail(String usage, String email) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
 
		Session session = Session.getInstance(props,
			new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {//TODO put ist to properties file and dont check in
					return new PasswordAuthentication("rochlitz.consulting@gmail.com","A7uj89oil");//TODO ohnae passw
				}
			});
 
		try {
 
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("from@kontoagent.de"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(email));
			message.setSubject("Kontoagenten-Benachrichtigung");
			message.setText(" ," +
					"\n\n  "+usage);
 
			Transport.send(message);
 
			System.out.println("Mail an "+ email +" versendet.");
 
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

}
