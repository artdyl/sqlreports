package reporting.sqlReporting;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.opencsv.CSVWriter;



public class CsvReportExtractor extends AbstractReportExtractor{
	static final Logger logger = Logger.getRootLogger();

	@Override
	public void extractReport(Attachment attachment) {
		try (Connection con = getConnection()){
			logger.info("Connected to "+ReportProcessor.env+" database.");
			PreparedStatement ps =con.prepareStatement(genereteSql(attachment.getPathToSql()));
			logger.info("Executing genereted query on "+ReportProcessor.env+" database.");
			ResultSet rs = ps.executeQuery();
			logger.info("Sql query executed on "+ReportProcessor.env+" database.");
			CSVWriter writer = null;

			try {
				logger.info("Writing report to file "+(Configuration.getInstance().getProperty("output_dir")+File.separator+attachment.getReportName()+".csv"));
				if(attachment.getApplyQuotesToAll()){
				writer = new CSVWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Configuration.getInstance().getProperty("output_dir")+File.separator+attachment.getReportName()+".csv"),"UTF-8"),65536), attachment.getCsvSeparator());
				}else{
				writer = new CsvWriterAplyQuotestoAllOff(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(Configuration.getInstance().getProperty("output_dir")+File.separator+attachment.getReportName()+".csv"),"UTF-8"),65536), attachment.getCsvSeparator());
				}
				writer.writeAll(rs, true);
				writer.close();
				logger.info((Configuration.getInstance().getProperty("output_dir")+File.separator+attachment.getReportName()+".csv")+ " saved.");
			} catch (IOException e) {
				logger.error("Error during writing of report "+(Configuration.getInstance().getProperty("output_dir")+File.separator+attachment.getReportName()+".csv")+":",e);
				System.exit(1);
			}
			
		} catch (SQLException e) {
			logger.error("Error during SQL query execution, please check query in " +attachment.getPathToSql().toString()+ "file:", e);
			System.exit(1);
		}
	}

		
	}

