package csv;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {
    private final String csvHeader = "id, account_id, category_id, amount, date";
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");

    public void writeDataToCsv(List<String> csvData, String path){
        info.info("Start writing data to csv file path:{}", path);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path, false))){
            writer.write(csvHeader);
            for(String info : csvData){
                writer.write(info);
            }

            info.info("Data was written successfully to csv file path:{}", path);

        } catch (IOException exception) {
            error.error("IOException in writing data to csv file. Path: {} Reason{}", path, exception.getMessage());
            exception.printStackTrace();
        }
    }
}
