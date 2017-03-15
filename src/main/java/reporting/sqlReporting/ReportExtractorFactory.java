package reporting.sqlReporting;



public class ReportExtractorFactory {
	

	
	public ReportExtractor getReportExtractor(Attachment attachment){
		if(attachment.getReportExtension().equalsIgnoreCase("csv")){
			return new CsvReportExtractor();
			
		}

		return null;
	}
	
	
	
}
