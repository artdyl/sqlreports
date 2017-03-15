package reporting.sqlReporting;

import java.io.File;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import org.apache.log4j.*;;


public class XMLParser {
	static final Logger logger = Logger.getRootLogger();


	public ReportConfiguration parse(String xmlFile){
	ReportConfiguration reportconf= new ReportConfiguration();
	File inputFile =null;
	ArrayList<Attachment> attachments = null;
	try{
	inputFile = new File(xmlFile);
	attachments= new ArrayList<Attachment>();
	logger.info("Parsing report configuration from "+inputFile.toString()+" file.");
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
    Document doc = builder.parse(inputFile);
    doc.getDocumentElement().normalize();
    NodeList nList = doc.getElementsByTagName("mail_fields");
    if(nList.getLength()==1){
    reportconf.setRunMode("Generete and send reports");

    ///settings fields for email message
    Element emailFields = (Element) nList.item(0);
    if(emailFields.getElementsByTagName("email_to").getLength()==0){
    throw new InvalidReportConfException("\"email_to\" tag not set in "+xmlFile.toString());
    }else{
    reportconf.setEmailTo(emailFields.getElementsByTagName("email_to").item(0).getTextContent());
    }
  
    if(emailFields.getElementsByTagName("email_from").getLength()==0){
    throw new InvalidReportConfException("\"email_from\" tag not set in "+xmlFile.toString());
    }else{
    reportconf.setEmailFrom(emailFields.getElementsByTagName("email_from").item(0).getTextContent());
    }
  
    if(emailFields.getElementsByTagName("email_body").getLength()==1){
    reportconf.setEmailBody(emailFields.getElementsByTagName("email_body").item(0).getTextContent());
    }
    if(emailFields.getElementsByTagName("email_cc").getLength()==1){
    reportconf.setEmailCc(emailFields.getElementsByTagName("email_cc").item(0).getTextContent());
    }
    if(emailFields.getElementsByTagName("email_subject").getLength()==0){
    throw new InvalidReportConfException("\"email_subject\" tag not set in "+xmlFile.toString());
    }else{
    reportconf.setEmailSubj(emailFields.getElementsByTagName("email_subject").item(0).getTextContent());
    }
    }else{
    	reportconf.setRunMode("Generete reports only");
    }
    ///Filing attachments array with attachments
    nList = doc.getElementsByTagName("attachemnt");
    
    for(int i=0;i<nList.getLength();i++){
    Element at = (Element) nList.item(i);
    
    Attachment att = new Attachment();
    att.setPathToSql(at.getElementsByTagName("sql_file").item(0).getTextContent());
    att.setReportExtension(at.getElementsByTagName("output_file_type").item(0).getTextContent().toLowerCase());
    att.setReportName(reportNameDate(at.getElementsByTagName("output_file_name").item(0).getTextContent()));
    
    if(at.getElementsByTagName("csv_separator").getLength()==0){
    	att.setCsvSeparator(',');
    }else{
    	if(at.getElementsByTagName("csv_separator").item(0).getTextContent().toCharArray().length>1){
    		char[]numberValueArray=at.getElementsByTagName("csv_separator").item(0).getTextContent().toCharArray();
    		String numValueStr="";
    		for (char c:numberValueArray){
    			numValueStr=numValueStr+c;
    		}
    		att.setCsvSeparator((char) Integer.parseInt(numValueStr));
    		
    	
    	}else{
    		att.setCsvSeparator(at.getElementsByTagName("csv_separator").item(0).getTextContent().charAt(0));
    	}

    }
    if(at.getElementsByTagName("applyQuotesToAll").getLength()==0){
    	att.setApplyQuotesToAll(true);
    }else{
    	if(at.getElementsByTagName("applyQuotesToAll").item(0).getTextContent().toUpperCase().equals("FALSE")){
    	att.setApplyQuotesToAll(false);
    	}else{
    		att.setApplyQuotesToAll(true);
    	}
    		
    }
    
    
    attachments.add(att);

    }

    
	}
		catch (SAXException sax){
	logger.error("Unable to parse provided "+inputFile.toString()+" check if file is corect.",sax);
	System.exit(1);
	}
	catch(IOException io){
		logger.error("Unable to find or open "+inputFile.toString()+" file. Check if such file exist or if it not used by antoher processs.", io);
		System.exit(1);
	}
			
	catch(ParserConfigurationException pc){
		logger.error("Cannot create javax.xml.parsers.DocumentBuilder.",pc);

	} catch (DOMException e) {
		System.out.println("Dom");
		e.printStackTrace();
	} catch (InvalidReportConfException e) {
		logger.error("One of mandatory tags not set in "+xmlFile+" file, exiting.", e);
		System.exit(1);
	} 
	
	reportconf.setAttachments(attachments);
	logger.info("Report configuration sucesfully parsed from "+inputFile.toString()+" file.");
    logger.info("Run mode set to "+"\""+reportconf.getRunMode()+"\""+".");
	return reportconf;


	}
	
	private String reportNameDate(String report){
		Date date = null;
		if(report.contains("${date}")){
		date = new Date();
		Format formatter = new SimpleDateFormat("YYYY-MM-dd");
		report=report.replace("${date}", formatter.format(date));

		}
		if(report.contains("${time}")){
		date = new Date();
		Format formatter = new SimpleDateFormat("HHmmss");
		report=report.replace("${time}", formatter.format(date));
			}

		else{
			return report;
		}
		return report;
	}
	

}