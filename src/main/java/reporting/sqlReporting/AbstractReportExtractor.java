package reporting.sqlReporting;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import ccautils.AESencrp;

public abstract class AbstractReportExtractor implements ReportExtractor{
	Configuration config = Configuration.getInstance();
	static final Logger logger = Logger.getRootLogger();
	
	protected Connection getConnection() {
		logger.info("Connecting to "+ReportProcessor.env+" database.");
		logger.info("Using "+(config.getProperty(ReportProcessor.env+".JDBC"))+" connection string.");
		Connection connection=null;
		try {
			connection = DriverManager.getConnection(config.getProperty(ReportProcessor.env+".JDBC"), config.getProperty("USER_NAME"), decryptPass(config.getProperty(ReportProcessor.env+".PASSWORD")));
		} catch (SQLException e) {
			logger.error("Error during connection to database" +ReportProcessor.env+":",e);
			System.exit(1);
		}
		return connection;
	}
	
	private String decryptPass(String password){
		String passwordDecrypted=null;

			try {
				passwordDecrypted = AESencrp.decrypt(password);
				logger.info("Decrypting database password");
			} catch (Exception e) {
				logger.error("Password decription failed, please check if password correct", e);
				System.exit(1);
			}

		
		return passwordDecrypted;
		
		
	}
	public String genereteSql(String sqlFile) {
		File file = new File (sqlFile);
		BufferedReader reader = null;
		StringBuilder sql=null;
		String line = null;
		
		try {
			logger.info("Reading SQL query from "+file);
			reader = new BufferedReader(new FileReader (file));
			sql = new StringBuilder();
			try {
				while((line=reader.readLine())!=null){
					sql.append(line);
					sql.append("\n");
				}

			} catch (IOException e) {
				logger.error("Reading of SQL query from "+file+" interupted, please check if file not used by another process",e);
			}
		} catch (FileNotFoundException e) {
			logger.error(file+" not found, please check if such file exist or modify report configuration xml");
			e.printStackTrace();
		}

		String query=sql.toString();

		while(query.endsWith(";")||(query.endsWith("\n"))||query.endsWith(" ")){
			query=query.substring(0, query.length()-1);
		}
		return query;


	}
}



