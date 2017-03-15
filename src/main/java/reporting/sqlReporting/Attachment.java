package reporting.sqlReporting;

public class Attachment {
	private String reportName;
	private String pathToSql;
	private String reportExtension;
	private char csvSeparator;
	private boolean applyQuotesToAll;
	

	
	public String getReportName() {
		return reportName;
	}
	public void setReportName(String reportName) {
		this.reportName = reportName;
	}
	public String getPathToSql() {
		return pathToSql;
	}
	public void setPathToSql(String pathToSql) {
		this.pathToSql = pathToSql;
	}

	public String getReportExtension() {
		return reportExtension;
	}
	public void setReportExtension(String reportExtension) {
		this.reportExtension = reportExtension;
	}
	public char getCsvSeparator() {
		return csvSeparator;
	}
	public void setCsvSeparator(char csvSeparator) {
		this.csvSeparator = csvSeparator;
	}
	public boolean getApplyQuotesToAll() {
		return applyQuotesToAll;
	}
	public void setApplyQuotesToAll(boolean applyQuotesToAll) {
		this.applyQuotesToAll = applyQuotesToAll;
	}
	
	
	

}
