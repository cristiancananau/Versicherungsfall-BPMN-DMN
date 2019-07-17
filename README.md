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

Hier sollen nun der ausmodellierte BPMN-Prozess, sowie die DRD's noch einmal zusammenfassen präsentiert werden.

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

### Notwendige Links:
  - https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html und https://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html
  - https://www.eclipse.org/downloads/packages/release/2019-03/r/eclipse-ide-java-developers
  - https://docs.camunda.org/get-started/java-process-app/install/
  - https://docs.camunda.org/get-started/java-process-app/project-setup/

## 4. Beschreibung der Klassen

Im Folgenden werden nun einige Klassen beschrieben, die zur Umsetzung der gegebenen Anforderungen benötigt wurden.

  ### EmailKonfigurationen
  

	public class EmailKonfigurationen {
	public static void sendMail(String subject, String content, String email, String filePath, String docuentName) throws MessagingException, IOException {

	    final String username = "mail@gmail.com";
	    final String password = "password";
	    
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
	        message.setFrom(new InternetAddress("mail@gmail.com"));
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

### Beschreibung:
In dieser Klasse werden alle Konfiguration vorgenommen, die für das Versenden einer E-Mail benötigt werden. Die Methode `sendMail` erwartet fünf Parameter vom Typ String: `subject`, `content`, `email`, `filePath` und `docuentName`. Dann werden zwei Strings deklariert (`username` und `passwort`). Im Anschluss werden die Properties der E-Mail definiert. Im try-catch-Block wird versucht, eine E-Mail zu versenden. Zuerst wird dabei die E-Mail-Adresse des Absenders angegeben. Mit `message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));`
wird die E-Mail-Adresse abgefangen. Danach folgen mit `subject` und `content` der Betreff und Nachrichtentext der E-Mail. In der If-Else-Bedingung wird überprüft, ob der Pfad für eine angehängte PDF-Datei angegeben wurde oder nicht. Wurde dieser angegeben, so wird die Datei als `ByteArrayDataSource` eingelesen und als Anhang der E-Mail beigefügt. Ist kein Pfad vorhanden, so wird die E-Mail ohne Anhang versendet.

### Angebot erstellen

	public class AngebotErstellen implements JavaDelegate {

	  public void execute(DelegateExecution execution) throws Exception {

	      String kundeName = (String) execution.getVariable("KundenName");
	      String kundeVorname = (String) execution.getVariable("KundenVorname");
	      String einstufungRisiko = (String) execution.getVariable("einstufungRisiko");

	      Path currentRelativePath = Paths.get("");
	      String stringPath = currentRelativePath.toRealPath().toString();
	      stringPath = stringPath.substring(0, stringPath.lastIndexOf("\\")+1);
	      String docuentName = kundeName+"-"+kundeVorname+"-Angebot"+".pdf";
	      String pdfDocPath = stringPath+"webapps\\versicherungsfall\\"+docuentName;

	      boolean checkIfExist = new File(pdfDocPath).exists();

	      if(checkIfExist) {
		  docuentName = docuentName.replaceFirst("[.][^.]+$", "")+"-1.pdf";
		  pdfDocPath = pdfDocPath.replaceFirst("[.][^.]+$", "")+"-1.pdf";
	      }

	      execution.setVariable("pdfDocPath", pdfDocPath);
	      execution.setVariable("docuentName", docuentName);

	      CreatePDFDocument.creatFile(pdfDocPath, kundeName+" "+kundeVorname, einstufungRisiko);

	      System.out.println(pdfDocPath);
	  }
	}

### Beschreibung:
Diese Klasse übernimmt die Variablen von dem laufenden System (Cockpit) und generiert daraus ein PDF. Die `public void execute(DelegateExecution execution)` erwartet einen Parameter `execution` vom Typ `DelegateExecution`
(https://docs.camunda.org/manual/7.8/user-guide/process-engine/delegation-code/). `DelegateExecution` ermöglicht es, im laufenden System die notwendigen Variablen abzurufen (z.B.`execution.getVariable("KundenName")`) . Danach wird ein Pfad für das PDF-Dokument erstellt. Dieser Pfad wird druch `execution.setVariable("pdfDocPath", pdfDocPath);` als Variable deklariert, wobei `"pdfDocPath"` der Variablenname im Camunda-Cockpit und `pdfDocPath` der Pfad (Wert) an sich ist. Es werden die Klasse `CreatePDFDocument` mit der Methode `creatFile` verwendet. Diese sind vor allem für den nächsten Codeausschnitt relevant.

### Create PDFDokument
 	public class CreatePDFDocument {
	
	public static void creatFile(String pdfDocPath, String name, String einstufungRisiko) throws IOException {
				
			
	       	PdfWriter writer = new PdfWriter(pdfDocPath);           
	      
	       	// Erstellen von Pdf-Dokument       
	       	PdfDocument pdf = new PdfDocument(writer);              
	      
	       	// Erstellen von Dokument A4-Größe        
	        Document document = new Document(pdf, PageSize.A4);   
	        document.setMargins(70f, 70f, 70f, 70f);
	        
	        //Erstellen von Text
		Text companyName = new Text("Loop GmbH | Loopingweg 1 | 12345 Loophausen").setFontSize(9).setUnderline();
		Text personData = new Text(name +"\nMusterstraße 1"+"\n12345 Musterstadt").setFontSize(9);
		Text angebotDatumUndUstIdNr = new Text("Angebotsdatum:   "
												+LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
												+"\nGültig bis:   31.12.2019"+"\nUst-IdNr.:   DE123456789").setFontSize(9);
		Text angebot = new Text("Angebot für Ihre Anfrage\n").setFontSize(12).setBold();
		Text angebotText = new Text(einstufungRisiko).setFontSize(11);
		Text endText = new Text("\nWir würden uns sehr freuen, wenn unser Angebot Ihre Zustimmung findet."
								+"\nSie haben Fragen oder wünschen weitere Informationen?"
								+"\nRufen Sie uns an – wir sind für Sie da.").setFontSize(11);
		Text mitFreundlichen = new Text("Mit freundlichen Grüßen").setFontSize(11);
		Text ihreLoopTeam = new Text("Ihre"+"\nLoop GmbH").setFontSize(11);

		//Erstellen von Paragraphs
		Paragraph paragraph1 = new Paragraph().add(companyName);
		Paragraph paragraph2 = new Paragraph().add(personData);
		Paragraph paragraph3 = new Paragraph().setTextAlignment(TextAlignment.RIGHT).add(angebotDatumUndUstIdNr);
		Paragraph paragraph4 = new Paragraph().add(angebot);
		Paragraph paragraph5 = new Paragraph().add(angebotText);
		Paragraph paragraph6 = new Paragraph().add(endText);
		Paragraph paragraph7 = new Paragraph().add(mitFreundlichen);
		Paragraph paragraph8 = new Paragraph().add(ihreLoopTeam);


		//Einfügen der Paragraphe zu dem Dokument
		document.add(paragraph1);
		document.add(paragraph2); 
		document.add(paragraph3); 
		document.add(paragraph4); 
		document.add(paragraph5); 
		document.add(paragraph6); 
		document.add(paragraph7); 
		document.add(paragraph8); 

	        TextFooterEventHandler eventHandler = new TextFooterEventHandler(document);

	        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, eventHandler);

	        //letzte Seite in der Dokument
	        eventHandler.lastPage = pdf.getLastPage();
	        
		// Schliesßen des Dokumentes       
		document.close();      		

		System.out.println("Document wurde erstellt!");
		
	}
	
    static class TextFooterEventHandler implements IEventHandler {
        Document doc;
        PdfPage lastPage = null;

        public TextFooterEventHandler(Document doc) {
            this.doc = doc;
        }

        public void handleEvent(Event event) {
            PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
            PdfCanvas canvas = new PdfCanvas(docEvent.getPage());
            canvas.beginText();
            try {
                canvas.setFontAndSize(PdfFontFactory.createFont(StandardFonts.HELVETICA), 9);
            } catch (IOException e) {
                e.printStackTrace();
            }
            canvas.beginText()
                  .moveText((doc.getRightMargin()+5), (doc.getBottomMargin()+50))
                  .showText("Telefon:   01234/987654                                                  Loop GmbH")
		          .moveText(0, (doc.getBottomMargin()-80))
		          .showText("Fax:         01234/987655                                                  Bank Loophausen")
		          .moveText(0, (doc.getBottomMargin()-90))
		          .showText("Email:       loopgmbhwithservice@gmail.com                   BLZ: 30033301n")
		          .moveText(0, (doc.getBottomMargin()-80))
		          .showText("Web:         www.loop-gmbh.com                                       KTO: 32165498")
                  .endText()
                  .release();
	      
		}
	    }  
	}
	
### Beschreibung:
Durch diese Klasse wird letztlich das PDS-Dokument erstellt, welches dem Kunden als Angebot zugesendet wird. Die Methode `creatFile` erwartet drei Parameter: `pdfDocPath`, `name` und `einstufungRisiko`. Zuerst wird mit `PdfDocument pdf = new PdfDocument(writer);` im angegebenen Pfad ein leeres PDF-Dokument erstellt. Dieses sollte eine A4-Größe haben. Nun werden die Textelemente des Dokumentes (Anschrift Versender, Anschrift Absender, Header,...) als Variablen deklariert. Man deklariert nun die einzelnen Paragraphen (=Formatierung des Dokumentes) und fügt hier die einzelnen Variablen der Textelemente ein. Die befüllten Paragraphen müssen nun in der Reihenfolge dem Dokument beigefügt werden. Jetzt deklariert man die Fußzeile (Footer). Hierzu dient die Klasse `TextFooterEventHandler`.


## 5. Einbindung der HTML-Forms (das bedeutet Formulare)

	<form name="generatedForm" role="form" class="ng-valid ng-scope ng-valid-cam-variable-type ng-valid-parse ng-pristine">
	    <div class="form-group">
		<label for="KundenName">
		    Bitte tragen Sie den Namen des Fahrers ein.
		</label>
		<input class="form-control ng-pristine ng-untouched ng-valid ng-valid-cam-variable-type ng-empty" name="KundenName" cam-variable-type="String" cam-variable-name="KundenName" type="text" ng-model="KundenName" required>
	    </div>
	    <div class="form-group">
		<label for="KundenVorname">
		    Bitte tragen Sie den Vornamen des Fahrers ein.
		</label>
		<input class="form-control ng-pristine ng-untouched ng-valid ng-valid-cam-variable-type ng-empty" name="KundenVorname" cam-variable-type="String" cam-variable-name="KundenVorname" type="text" ng-model="KundenVorname" required>
	    </div>
	    <div class="form-group">
		<label for="KundenEmail">
		    Bitte tragen Sie die Email-Adresse des Fahrers ein.
		</label>
		<input class="form-control ng-pristine ng-untouched ng-valid ng-valid-cam-variable-type ng-empty" name="KundenEmail" type="email" cam-variable-type="String" cam-variable-name="KundenEmail" type="text" ng-model="KundenEmail" required>
	    </div>
	    <div class="form-group">
		<label for="ps">
		    Hat das Auto mehr als 300 PS?
		</label>
		<input class="form-control ng-untouched ng-valid ng-valid-cam-variable-type ng-not-empty ng-pristine" name="ps" cam-variable-type="Boolean" cam-variable-name="ps" type="checkbox" ng-model="ps">
	    </div>
	    <div class="form-group">
		<label for="laenge">
		    Geben Sie bitte die L&#228;nge des Autos ein.
		</label>
		<input class="form-control ng-pristine ng-untouched ng-valid ng-valid-cam-variable-type ng-empty" name="laenge" cam-variable-type="Long" cam-variable-name="laenge" type="text" ng-model="laenge" required>
	    </div>
	    <div class="form-group">
		<label for="autoTyp">
		    Bitte w&#228;hlen Sie den Typ des Autos aus.
		</label>
		<select class="form-control ng-untouched ng-valid ng-valid-cam-variable-type ng-not-empty ng-valid-parse ng-pristine" name="autoTyp" cam-variable-type="String" cam-variable-name="autoTyp" ng-model="autoTyp">
		    <option value="Oldtimer">
			Oldtimer
		    </option>
		    <option value="SUV">
			SUV
		    </option>
		    <option value="Cabrio">
			Cabrio
		    </option>
		    <option value="Kombi">
			Kombi
		    </option>
		    <option value="Sportwagen">
			Sportwagen
		    </option>
		    <option value="Gelaendewagen">
			Gel&#228;ndewagen
		    </option>
		</select>
	    </div>
	    <div class="form-group">
		<label for="geschlechtFahrer">
		    Bitte geben Sie das Geschlecht des Fahrers an.
		</label>
		<select class="form-control ng-untouched ng-valid ng-valid-cam-variable-type ng-not-empty ng-valid-parse ng-pristine" name="geschlechtFahrer" cam-variable-type="String" cam-variable-name="geschlechtFahrer" ng-model="geschlechtFahrer">
		    <option value="maennlich">
			m&#228;nnlich
		    </option>
		    <option value="weiblich">
			weiblich
		    </option>
		    <option value="divers">
			divers
		    </option>
		</select>
	    </div>
	    <div class="form-group">
		<label for="alterFahrer">
		    Bitte geben Sie das Alter des Fahrers ein.
		</label>
		<input class="form-control ng-pristine ng-untouched ng-valid ng-valid-cam-variable-type ng-empty" name="alterFahrer" cam-variable-type="Long" cam-variable-name="alterFahrer" type="text" ng-model="alterFahrer" required>
	    </div>
	    <div class="form-group">
		<label for="ausstellungsdatumFuehrerschein">
		    Bitte tragen Sie das Ausstellungsdatum des Fuehrerscheines ein.
		</label>
		<div class="input-group">
		    <input class="form-control ng-pristine ng-valid ng-valid-cam-variable-type ng-empty ng-touched" name="ausstellungsdatumFuehrerschein" cam-variable-type="String" cam-variable-name="ausstellungsdatumFuehrerschein" type="text" datepicker-popup="dd/MM/yyyy" is-open="dateFieldOpenedausstellungsdatumFuehrerschein" ng-model="ausstellungsdatumFuehrerschein" required>
		    <div class="input-group-btn">
			<button type="button" class="btn btn-default" ng-click="openausstellungsdatumFuehrerschein($event)">
			    <i class="glyphicon glyphicon-calendar"></i>
			</button>
		    </div>
		</div>
	    </div>
	</form>

 
## 6. Schluss
