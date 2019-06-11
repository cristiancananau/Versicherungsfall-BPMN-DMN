package org.camunda.versicherung;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTPPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTSpacing;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STLineSpacingRule;
import org.apache.poi.xwpf.model.XWPFHeaderFooterPolicy;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.UnderlinePatterns;

public class CreateWordDocument {
	
   private static XWPFDocument document;

public static void creatFile(String wordDocPath, String name, String einstufungRisiko)throws Exception {
      
      document = new XWPFDocument(); 
      
      //Write the Document in file system
      FileOutputStream out = new FileOutputStream(new File(wordDocPath));
        
      XWPFParagraph paragraph = document.createParagraph();

      XWPFRun paragraph1Run1 = paragraph.createRun();
      paragraph1Run1.setUnderline(UnderlinePatterns.SINGLE);
      paragraph1Run1.setFontSize(10);
      paragraph1Run1.setFontFamily("Arial");
      paragraph1Run1.setText("Loop GmbH | Loopingweg 1 | 12345 Loophausen");
      paragraph1Run1.addBreak();
      paragraph1Run1.addBreak();

        
      XWPFRun paragraph2Run2 = paragraph.createRun();
      paragraph2Run2.setFontSize(10);
      paragraph2Run2.setFontFamily("Arial");
      paragraph2Run2.setText(name);
      paragraph2Run2.addBreak();
      
      XWPFRun paragraph1Run3 = paragraph.createRun();
      paragraph1Run3.setFontSize(10);
      paragraph1Run3.setFontFamily("Arial");
      paragraph1Run3.setText("Musterstraße 1");
      paragraph1Run3.addBreak();
      
      XWPFRun paragraph1Run4 = paragraph.createRun();
      paragraph1Run4.setFontSize(10);
      paragraph1Run4.setFontFamily("Arial");
      paragraph1Run4.setText("12345 Musterstadt");
      paragraph1Run4.addBreak();
      
      
      XWPFParagraph paragraph2 = document.createParagraph();
      paragraph2.setAlignment(ParagraphAlignment.RIGHT);
      
      XWPFRun paragraph2Run5 = paragraph2.createRun();
      paragraph2Run5.setFontSize(10);
      paragraph2Run5.setFontFamily("Arial");
      paragraph2Run5.setText("Angebotsdatum:   "+  LocalDate.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
      paragraph2Run5.addBreak();
      
      XWPFRun paragraph2Run6 = paragraph2.createRun();
      paragraph2Run6.setFontSize(10);
      paragraph2Run6.setFontFamily("Arial");
      paragraph2Run6.setText("Gültig bis:   31.12.2019");
      paragraph2Run6.addBreak();
      
      XWPFRun paragraph2Run7 = paragraph2.createRun();
      paragraph2Run7.setFontSize(10);
      paragraph2Run7.setFontFamily("Arial");
      paragraph2Run7.setText("Ust-IdNr.:   DE123456789");
      paragraph2Run7.addBreak();
      
      XWPFParagraph paragraph3 = document.createParagraph();

      XWPFRun paragraph3Run1 = paragraph3.createRun();
      paragraph3Run1.setBold(true);
      paragraph3Run1.setFontSize(12);
      paragraph3Run1.setFontFamily("Arial");
      paragraph3Run1.setText("Angebot für Ihre Anfrage");
      paragraph3Run1.addBreak();

      XWPFParagraph paragraph4 = document.createParagraph();	     
        
      CTPPr ppr = paragraph4.getCTP().getPPr();
      if (ppr == null) ppr = paragraph4.getCTP().addNewPPr();
      CTSpacing spacing = ppr.isSetSpacing()? ppr.getSpacing() : ppr.addNewSpacing();
      spacing.setAfter(BigInteger.valueOf(0));
      spacing.setBefore(BigInteger.valueOf(0));
      spacing.setLineRule(STLineSpacingRule.AUTO);
      spacing.setLine(BigInteger.valueOf(360));
      
      XWPFRun paragraph4Run1 = paragraph4.createRun();
      paragraph4Run1.setFontSize(11);
      paragraph4Run1.setFontFamily("Arial");
      paragraph4Run1.setText(einstufungRisiko);
      paragraph4Run1.addBreak();
      paragraph4Run1.addBreak();
      
      XWPFRun paragraph4Run2 = paragraph4.createRun();
      paragraph4Run2.setFontSize(11);
      paragraph4Run2.setFontFamily("Arial");
      paragraph4Run2.setText("Wir würden uns sehr freuen, wenn unser Angebot Ihre Zustimmung findet.");
      paragraph4Run2.addBreak();
      
      XWPFRun paragraph4Run3 = paragraph4.createRun();
      paragraph4Run3.setFontSize(11);
      paragraph4Run3.setFontFamily("Arial");
      paragraph4Run3.setText("Sie haben Fragen oder wünschen weitere Informationen? Rufen Sie uns an");
      paragraph4Run3.addBreak();
      
      XWPFRun paragraph4Run4 = paragraph4.createRun();
      paragraph4Run4.setFontSize(11);
      paragraph4Run4.setFontFamily("Arial");
      paragraph4Run4.setText("– wir sind für Sie da.");
      paragraph4Run4.addBreak();
      paragraph4Run4.addBreak();


      XWPFRun paragraph4Run5 = paragraph4.createRun();
      paragraph4Run5.setFontSize(11);
      paragraph4Run5.setFontFamily("Arial");
      paragraph4Run5.setText("Mit freundlichen Grüßen");
      paragraph4Run5.addBreak();
      paragraph4Run5.addBreak();


      XWPFRun paragraph4Run6 = paragraph4.createRun();
      paragraph4Run6.setFontSize(11);
      paragraph4Run6.setFontFamily("Arial");
      paragraph4Run6.setText("Ihre");
      paragraph4Run6.addBreak();

      XWPFRun paragraph4Run7 = paragraph4.createRun();
      paragraph4Run7.setFontSize(11);
      paragraph4Run7.setFontFamily("Arial");
      paragraph4Run7.setText("Loop GmbH");
      paragraph4Run7.addBreak();

      
      XWPFHeaderFooterPolicy headerFooterPolicy = document.createHeaderFooterPolicy();
      XWPFFooter footer = headerFooterPolicy.createFooter(XWPFHeaderFooterPolicy.DEFAULT);
      
      XWPFParagraph paragraph5 = footer.createParagraph();
      
      XWPFRun paragraph5Run1 = paragraph5.createRun();
      paragraph5Run1.setFontSize(10);
      paragraph5Run1.setFontFamily("Calibri");
      paragraph5Run1.setText("Telefon:       01234/987654");
      paragraph5Run1.addTab();
      paragraph5Run1.addTab();
      paragraph5Run1.addTab();
      paragraph5Run1.addTab();
      paragraph5Run1.setText("Loop GmbH");
      paragraph5Run1.addBreak();
      
      
      XWPFRun paragraph5Run2 = paragraph5.createRun();
      paragraph5Run2.setFontSize(10);
      paragraph5Run2.setFontFamily("Calibri");
      paragraph5Run2.setText("Fax:               01234/987655");
      paragraph5Run2.addTab();
      paragraph5Run2.addTab();
      paragraph5Run2.addTab();
      paragraph5Run2.addTab();
      paragraph5Run2.setText("Bank Loophausen");
      paragraph5Run2.addBreak();
      paragraph5Run2.addBreak();

      
      XWPFRun paragraph5Run3 = paragraph5.createRun();
      paragraph5Run3.setFontSize(10);
      paragraph5Run3.setFontFamily("Calibri");
      paragraph5Run3.setText("Email:       loopgmbhwithservice@gmail.com");
      paragraph5Run3.addTab();
      paragraph5Run3.addTab();
      paragraph5Run3.setText("BLZ: 30033301");
      paragraph5Run3.addBreak();
      
      XWPFRun paragraph5Run4 = paragraph5.createRun();
      paragraph5Run4.setFontSize(10);
      paragraph5Run4.setFontFamily("Calibri");
      paragraph5Run4.setText("Web:         www.loop-gmbh.com");
      paragraph5Run4.addTab();
      paragraph5Run4.addTab();
      paragraph5Run4.addTab();
      paragraph5Run4.addTab();
      paragraph5Run4.setText("KTO: 32165498");
      
      document.write(out);
      out.close();
      
      System.out.println(out+"wurde erfolgreich erstellt");
   }
}