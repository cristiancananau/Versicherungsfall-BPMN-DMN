package org.camunda.versicherung;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

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
