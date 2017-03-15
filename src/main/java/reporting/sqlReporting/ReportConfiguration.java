package reporting.sqlReporting;

import java.util.ArrayList;

public class ReportConfiguration {
	
	private String emailTo;
	private String emailFrom;
	private String emailBody;
	private String emailCc;
	private String emailSubj;
	private ArrayList<Attachment> attachments;
	private String runMode;
	private char csvSeparator;

	
	public String getEmailTo() {
		return emailTo;
	}

	public void setEmailTo(String emailTo) {
		this.emailTo = emailTo;
	}

	public String getEmailFrom() {
		return emailFrom;
	}

	public void setEmailFrom(String emailFrom){
		this.emailFrom = emailFrom;
	}

	public String getEmailBody() {
		return emailBody;
	}

	public void setEmailBody(String emailBody) {
		this.emailBody = emailBody;
	}

	public String getEmailCc() {
		return emailCc;
	}

	public void setEmailCc(String emailCc) {
		this.emailCc = emailCc;
	}

	public String getEmailSubj() {
		return emailSubj;
	}

	public void setEmailSubj(String emailSubj) {
		this.emailSubj = emailSubj;
	}

	public ArrayList<Attachment> getAttachments() {
		ArrayList<Attachment> copy = new ArrayList<Attachment>(this.attachments);
		  return copy;
	}

	public void setAttachments(ArrayList<Attachment> attachments) {
		this.attachments = attachments;
	}

	public String getRunMode() {
		return runMode;
	}

	public void setRunMode(String runMode) {
		this.runMode = runMode;
	}

	public char getCsvSeparator() {
		return csvSeparator;
	}

	public void setCsvSeparator(char csvSeparator) {
		this.csvSeparator = csvSeparator;
	}
	


}
