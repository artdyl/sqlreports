package reporting.sqlReporting;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.InvalidPropertiesFormatException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Configuration {
	static final Logger logger = Logger.getRootLogger();
	
    private static Configuration _instance = null;

    private Properties props = null;

    private Configuration() {
        props = new Properties();
        File file = new File("D:/SqlReportsTool/Ver2/config.xml");/////path to global config
        logger.info("Parsing global configuration parameters from "+file.toString()+" file.");
    	try {
	    
   		FileInputStream fis = new FileInputStream(file);

    		
    		
	    props.loadFromXML(fis);
    	}
    	catch (FileNotFoundException ex) {
    		logger.error("File with global configuration parameters not found "+file.toString(), ex);
    		System.exit(1);
    	}
    	catch(InvalidPropertiesFormatException e){
    		logger.error("Invalid XML properties format in global configuration parameters file - "+file.toString(), e);
    		System.exit(1);
    	
    	} catch (IOException e) {
			logger.error("Load of global configuration parameters from "+file.toString()+" interupted");
			System.exit(1);
		}
    	logger.info("Global configuration parameters parsed from "+file.toString()+" file.");
    }

    public synchronized static Configuration getInstance() {
        if (_instance == null)
            _instance = new Configuration();
        return _instance;
    }

  
    public synchronized String getProperty(String key) {
        String value = null;
        if (props.containsKey(key))
            value = (String) props.get(key);
        else {
           
        }
        return value;
    }
}

