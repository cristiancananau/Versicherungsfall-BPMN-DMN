package org.camunda.versicherung;

import java.io.IOException;

import javax.mail.MessagingException;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;


public class AbsageFahreignungsregisterSenden implements JavaDelegate {

	
	public static void absageFahreignungsregister(String subject, String content, String email) throws MessagingException, IOException {
		EmailKonfigurationen.sendMail(subject, content, email, null, null);
	}

	public void execute(DelegateExecution execution) throws Exception {
				
		  String email = (String) execution.getVariable("KundenEmail");
		  String kundeName = (String) execution.getVariable("KundenName");
		  String kundeVorname = (String) execution.getVariable("KundenVorname");
		  String subject = "Ihre Anfrage bei der Loop GmbH";
		  String content = ("\nGuten Tag liebe "+kundeVorname+" "+kundeName+ ","
				  			+"\n\nleider müssen wir Ihnen aufgrund Prüfung Ihres Fahreignungsregisters mitteilen, dass wir Ihnen kein Angebot unterbreiten können."
			  				+"\n\nLiebe Grüße"
				  			+"\nLoop GmbH");
		  
		  absageFahreignungsregister(subject, content, email);
	}

}
