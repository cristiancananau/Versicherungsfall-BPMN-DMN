package org.camunda.versicherung;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;

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