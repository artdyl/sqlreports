package reporting.sqlReporting;

import java.io.File;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

public class MailSender {
	private ReportConfiguration reportConfiguration;
	
	public MailSender(ReportConfiguration reportConfiguration){
		this.reportConfiguration=reportConfiguration;
	}
	static final Logger logger = Logger.getRootLogger();
	
	
	public void send(){
		if(reportConfiguration.getRunMode().equals("Generete reports only")){
			logger.info("Run mode set to "+"\""+reportConfiguration.getRunMode()+"\""+", no email will be genereted or send. If email should be send, please create tag <mail fields> and specify email mandatory fields.");
			return;
		}
		else if(reportConfiguration.getRunMode().equals("Generete and send reports")){
		Properties props = new Properties();
		Configuration conf=Configuration.getInstance();
		props.put("mail.smtp.host", conf.getProperty("SMTP_HOST"));
		props.put("mail.smtp.port", conf.getProperty("SMTP_PORT"));
		
		Session session = Session.getInstance(props);
		Message message = new MimeMessage(session);
		try {
			logger.info("Creating email.");
			message.setFrom(new InternetAddress(reportConfiguration.getEmailFrom()));
			logger.info("Set email from address as "+"\""+reportConfiguration.getEmailFrom()+"\"");
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(reportConfiguration.getEmailTo()));
			logger.info("Set email recipients as "+"\""+reportConfiguration.getEmailTo()+"\"");
			if(reportConfiguration.getEmailCc()!=null){
			message.setRecipients(Message.RecipientType.CC,InternetAddress.parse(reportConfiguration.getEmailCc()));
			logger.info("Set email recipients in CC as "+"\""+reportConfiguration.getEmailCc()+"\"");
			}
			message.setSubject(reportConfiguration.getEmailSubj());
			logger.info("Set email subject as "+"\""+reportConfiguration.getEmailSubj()+"\"");
            Multipart multipart=new MimeMultipart();
			if(reportConfiguration.getEmailBody()!=null){
			BodyPart messageBodyPart1=new MimeBodyPart();
			messageBodyPart1.setText(reportConfiguration.getEmailBody());
            multipart.addBodyPart(messageBodyPart1);
			}
            for (int i=0;i<reportConfiguration.getAttachments().size();i++){
            MimeBodyPart messageBodyPart=new MimeBodyPart();
			String filename=conf.getProperty("output_dir")+"/"+reportConfiguration.getAttachments().get(i).getReportName()+"."+reportConfiguration.getAttachments().get(0).getReportExtension();
			DataSource source=new FileDataSource(filename);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(reportConfiguration.getAttachments().get(i).getReportName()+"."+reportConfiguration.getAttachments().get(i).getReportExtension());

            multipart.addBodyPart(messageBodyPart);
            logger.info("adding attachment "+"\""+(reportConfiguration.getAttachments().get(i).getReportName()+"."+reportConfiguration.getAttachments().get(i).getReportExtension())+"\"");
            }
            message.setContent(multipart); 
			Transport.send(message);
			logger.info("Email sent.");
			
		} catch (AddressException e) {
			logger.error("Check if from address is correct "+reportConfiguration.getEmailFrom(), e);
		} catch (MessagingException e) {
			logger.error("Error during creation/genereting email:",e);
			System.exit(1);
		}

		
	}
	}

}
