package reporting.sqlReporting;

import java.io.Writer;

import com.opencsv.CSVWriter;

public class CsvWriterAplyQuotestoAllOff extends CSVWriter{

	public CsvWriterAplyQuotestoAllOff(Writer writer, char separator) {
		super(writer, separator);
		
	}
	@Override
	   public void writeNext(String[] nextLine) {
		      writeNext(nextLine, false);
		   }


}
