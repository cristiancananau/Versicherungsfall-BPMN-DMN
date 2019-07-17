# Implementierung

## 1. Prozessbeschreibung

Der modellierte BPMN Prozess zeigt den Prozessablauf der Angebotserstellung einer KFZ-Versicherung. Der Prozess beginnt mit der Übertragung der Kundendaten. Im zweiten Prozessschritt werden diese Kundendaten für die Risikobewertung in das System übertragen. Dies ist einer festen Position zugeordnet, daher ist dieser Prozessschritt ein User-Task. Im Anschluss splittet sich der Prozess in zwei Sequenzflüsse auf. Hierbei wird ein paralleles Gateway verwendet, dass im späteren Prozessverlauf die beiden Sequenzflüsse wiedervereinigt. 

Im ersten der beiden Sequenzflüsse wird das Risiko des jeweiligen Fahrzeuges bewertet. Dies geschieht mithilfe einer DMN Tabelle, die die Entscheidung nach der Collect Hit Policy trifft. Das heißt, dass mehrere Ereignisse zutreffen können. Deren Output-Ergebnisse werden im Anschluss zu einem Ausgabewert summiert. Bei der vorliegenden DMN Tabelle für die Risikobewertung des Fahrzeuges, bilden die Anzahl an PS, die Länge des Fahrzeuges sowie der Fahrzeugtyp die Input Werte. Der Input Wert PS ist als Boolean umgesetzt und bietet die Auswahlmöglichkeit „mehr als 300 PS“ - „true" oder „false“. Der zweite Input Wert bildet die Länge des Fahrzeuges, das als Double umgesetzt ist. Hierbei gibt es die Auswahl <450.00 cm bzw. >=450.00 cm. Der letzte Input Wert ist als String umgesetzt und bildet verschiedene Fahrzeugtypen als Auswahlmöglichkeit an. Die Besonderheit bei dem Fahrzeugtyp „Oldtimer“ ist, dass hier die PS Zahl sowie die Fahrzeuglänge keinen Einfluss auf die Risikobewertung ausübt, da die Fahrzeuge grundsätzlich die maximale Anzahl an Punkten im Risikoscore erhalten. Der Risikoscore bildet den Output Wert. 15 Punkte sind dabei der niedrigste Wert und 150 der maximale Wert, der erreicht werden kann. Um so höher die Score-Punktzahl, umso höher wird das Risiko eingestuft. 

Im zweiten Sequenzfluss wird die Risikobewertung der Fahrerin bzw. des Fahrers bestimmt. Hierbei bildet eine Call Activity den ersten Prozessschritt. In der Call Activity „Register checken“ wird zum einen das Fahreignungsregister gecheckt und zum anderen das Vorstrafenregister. Dieser Teilprozess ist als separater BPMN-Prozess dargestellt. Im ersten User-Task wird das Vorstrafenregister gecheckt. Sollten hier Vorstrafen vorhanden sein, wird im nächsten Prozessschritt nach einem exklusiven Gateway eine Absage versendet. Im Anschluss erfolgt mithilfe eines Fehler-Ereignisses die Meldung, dass der Prozess damit abgebrochen wird. Im Hauptprozess wird dann ein Terminierungs-Ereignis ausgelöst, dass den gesamten Prozess sofort abbricht. Wenn keine Vorstrafen vorliegen, wird das Fahreignungsregister gecheckt, ob und wie viele Punkte die Fahrerin bzw. der Fahrer hat. Dieses Ergebnis wird dann an den Hauptprozess zurückgegeben. 

Im zweiten Prozessschritt vom zweiten Sequenzfluss erfolgt mithilfe zweier DMN Tabellen in einem Decision Requirements Diagram, die automatisierte Risikobewertung der Fahrerin bzw. des Fahrers. Als Input-Werte der ersten DMN-Tabelle sind hier das Geschlecht, dass als String die Möglichkeiten weiblich, männlich und divers bietet sowie das Alter, dass als Integer die Werte zwischen 18 bis >70 abdeckt, definiert. Der letzte Input Wert ist die Dauer des Führerscheinbesitzes, dass als Date definiert ist. Die DMN Tabelle verfolgt dabei die Unique Hit Policy, das heißt, dass nur eine Regel zutrifft. Als Output Ergebnis wird ein Risikofaktor ausgegeben, der zwischen 1 und 2 liegen kann. 

Die zweite DMN Tabelle verfolgt ebenfalls die Unique Hit Policy. Hier dient das Ergebnis des Fahreignungsregister als Input Wert. Als Output Wert ergibt sich ein Punkte Faktor. So haben bspw. null Punkte den Faktor 1.0 und sieben Punkte den Faktor 1.7. Acht Punkte bedeuten einen Faktor von 0.0. Dieses Ergebnis wird im späteren Prozess relevant. Die Ergebnisse beider DMN Tabellen werden im Anschluss zu einem „Gesamtrisiko Fahrer“ multipliziert. 

Die beiden Sequenzflüsse werden nach Abschluss der vorhergehenden Prozessschritte mithilfe eines parallelen Gateways zusammengeführt. Im wiedervereinigten Prozess wird im nächsten Prozessschritt in einer DMN Tabelle mit Unique Hit Policy eine Risikobewertung für Fahrer und Fahrzeug durchgeführt. Hierbei wird der Risikoscore mit dem „Gesamtrisiko Faktor“ des jeweiligen Fahrers multipliziert. Das geringste Risiko hat dabei eine Punktzahl von 15 und ergibt als Output: “Sowohl Angaben zum Fahrzeug als auch Angaben zum Fahrer deuten auf ein sehr geringes Risiko hin. Der günstigste Versicherungssatz wird empfohlen.“. Die höchste Punktzahl ist 510. Das Output Ergebnis hierbei lautet: "Sowohl Angaben zum Fahrzeug als auch Angaben zum Fahrer deuten auf ein sehr hohes Risiko hin. Der höchste Versicherungssatz wird empfohlen.“ Wenn bedingt durch die 8 Punkte im Fahreignungsregister der Faktor null multipliziert wird, wird das Ergebnis ebenfalls null. Das Output Ereignis hierbei lautet: "Kein Angebot erstellbar.“.

Nach dem Prozessschritt der Gesamtrisiko-Bewertung teilt sich der Prozess durch ein exklusives Gateway auf. Sollte kein Angebot erstellbar sein, wird in dem Sequenzfluss als Endereignis eine Absage versendet. Sollte ein Angebot erstellbar sein, erfolgt der nächste Prozessschritt in zweiten Sequenzfluss. 

Im letzten Prozessschritt, von zweiten Sequenzfluss wird mithilfe eines Service Task ein Angebot für eine KFZ-Versicherung erstellt. Das Angebot wird im Endergebnis als PDF-Datei in einer Mail an den Kunden versendet. Damit endet der gesamte Prozess.


---

## 2. Modelierung

### kfz_Versicherung BPMN

![kfz_Versicherung](/src/main/resources/kfz_Versicherung.svg "kfz_Versicherung BPMN")

### RisikobewertungFahrer BPMN

![Alt text](/src/main/resources/RisikobewertungFahrer.svg "RisikobewertungFahrer BPMN")

### RisikobewertungAuto DMN

![Alt text](/src/main/resources/RisikobewertungAuto.svg "RisikobewertungAuto DMN")

### RisikobewertungFahrer DMN

![Alt text](/src/main/resources/RisikobewertungFahrerDMN.svg "RisikobewertungFahrerDMN DMN")

### Risikobewertunggesamt DMN

![Alt text](/src/main/resources/Risikobewertunggesamt.svg "Risikobewertunggesamt DMN")




---

## 3. Mavenprojekt-Erstellung (Video)
[![BPMN-DMN-Implementierung](http://i.imgur.com/Ot5DWAW.png)](https://www.youtube.com/watch?v=-7oW5CPuX0I "Tutorial ist hier")

  - https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html und https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
  - https://www.eclipse.org/downloads/packages/release/2019-03/r/eclipse-ide-java-developers
  - https://docs.camunda.org/get-started/java-process-app/install/
  - https://docs.camunda.org/get-started/java-process-app/project-setup/
  - Sreenshots und die Schritte
  - Erstellung der leere Klasse JavaServlet
  - Setzen von Umgebungsvariablen
  - Deployment (.war-Datei auf dem Server)

## 4. Beschreibung der Klassen
  ### EmailKonfigurationen
  

``
`
public class EmailKonfigurationen {
	public static void sendMail(String subject, String content, String email, String filePath, String docuentName) throws MessagingException, IOException {
	
	    final String username = "mail@gmail.com";
	    final String password = "password";
	    
	    Properties props = new Properties();
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.host", "smtp.gmail.com");
	    props.put("mail.smtp.port", "587");

	    `Session session = Session.getInstance(props,
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

	        System.out.println("Path is: " + filePath);

	        if(filePath != null) {
		         BodyPart messageBodyPart = new MimeBodyPart();
		         
		         messageBodyPart.setText(content);

		         Multipart multipart = new MimeMultipart();

		         multipart.addBodyPart(messageBodyPart);

		         messageBodyPart = new MimeBodyPart();
		         
	             InputStream uploadfile = new FileInputStream(filePath);
	             
		         ByteArrayDataSource ds = new ByteArrayDataSource(uploadfile, "application/pdf");	         

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
`
## 5. Einbindung der HTML-Forms (das bedeutet Formulare)

 ---
 
## 6. Schluss
