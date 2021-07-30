package csv;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CsvFileWriter {
    //Логи

    public void writeDataToCsv(List<String[]> csvData, String path){

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(path))){
            csvWriter.writeAll(csvData);
        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }
}
