package org.camunda.versicherung;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

public class EmailKonfigurationen {

	public static void sendMail(String subject, String content, String email, String filePath, String docuentName) throws MessagingException, IOException {
		final String username = "loopgmbhwithservice@gmail.com";
	    final String password = "loopgmbh";

	    Properties props = new Properties();
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    Session session = Session.getInstance(props,
	      new javax.mail.Authenticator() {
	        protected PasswordAuthentication getPasswordAuthentication() {
	            return new PasswordAuthentication(username, password);
	        }
	      });
	    
	    try {

	        Message message = new MimeMessage(session);
	        message.setFrom(new InternetAddress("loopgmbhwithservice@gmail.com"));
	        message.setRecipients(Message.RecipientType.TO,
	            InternetAddress.parse(email));
	        message.setSubject(subject);
	        message.setText(content);

	        if(filePath != null) {
		         BodyPart messageBodyPart = new MimeBodyPart();
		         
		         messageBodyPart.setText(content);

		         Multipart multipart = new MimeMultipart();

		         multipart.addBodyPart(messageBodyPart);

		         messageBodyPart = new MimeBodyPart();
		         
	             InputStream uploadfile = new FileInputStream(filePath);
	             
		         ByteArrayDataSource ds = new ByteArrayDataSource(uploadfile, "application/msword"); 	         

		         messageBodyPart.setDataHandler(new DataHandler(ds));
		         messageBodyPart.setFileName(docuentName);
		         multipart.addBodyPart(messageBodyPart);

		         message.setContent(multipart);
	        }
	        
	        Transport.send(message);

	        System.out.println("E-Mail wurde erfolgreich gesendet!");

	    } catch (MessagingException e) {
	        throw new RuntimeException(e);
	    }    
	}

	
}
