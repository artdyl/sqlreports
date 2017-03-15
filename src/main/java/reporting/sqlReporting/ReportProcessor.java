package reporting.sqlReporting;

import java.util.ArrayList;
import java.util.Properties;

public class ReportProcessor {
	static String env;


	public static void main(String[] args) {
		env = args[0];
		String reportConfXml = args[1];
		ReportConfiguration reportConfiguration = new XMLParser().parse(reportConfXml);
		ReportExtractorFactory reportFactory = new ReportExtractorFactory();
		ArrayList <Attachment> att=reportConfiguration.getAttachments();
		for (int i=0;i<att.size();i++){
			ReportExtractor extractor = reportFactory.getReportExtractor(att.get(i));
			extractor.extractReport(att.get(i));
		}
		MailSender mail = new MailSender(reportConfiguration);
		mail.send();
		
	}

}
