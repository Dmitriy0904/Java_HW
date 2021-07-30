package csv;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");

    public void writeDataToCsv(List<String[]> csvData, String path){
        info.info("Writing data to file {}", path);

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(path))){
            csvWriter.writeAll(csvData);
            info.info("Data was written to the file {} successfully", path);

        } catch (IOException exception){
            error.error("IOException in writing data to the file {} Reason:{}", path, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }
}
