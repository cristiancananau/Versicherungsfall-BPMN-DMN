package org.camunda.versicherung;

import java.io.IOException;

import javax.mail.MessagingException;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class AbsageVorstrafenSenden implements JavaDelegate {

	public static void absageVorstrafen(String subject, String content, String email) throws MessagingException, IOException {
		EmailKonfigurationen.sendMail(subject, content, email, null, null);
	}

	public void execute(DelegateExecution execution) throws Exception {
				
		  String email = (String) execution.getVariable("KundenEmail");
		  String subject = "Ihre Anfrage bei der Loop GmbH";
		  String content = ("\nGuten Tag lieber Kunde,"
				  			+"\n\nleider müssen wir Ihnen aufgrund Ihres Vorstrafenregisters mitteilen, dass wir Ihnen kein Angebot unterbreiten können."
			  				+"\n\nLiebe Grüße"
				  			+"\nLoop GmbH");
		  
		  absageVorstrafen(subject, content, email);
	}

}
