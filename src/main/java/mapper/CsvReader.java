package mapper;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CsvReader {
    private String PATH;
    private static final Logger log = LoggerFactory.getLogger(CsvReader.class);

    CsvReader(String path){
        this.PATH = path;
    }

    public List<String[]> readFile(String path){
        List<String[]> info;

        log.info("Try to read from csv file: {}", path);

        try(CSVReader reader = new CSVReader(new FileReader(path))) {

            info = reader.readAll();

        } catch (FileNotFoundException exception) {
            log.error("FileNotFoundException in read csv file: {}", path);
            throw new RuntimeException(exception);

        } catch (IOException exception) {
            log.error("IOException in read csv file: {}", path);
            throw new RuntimeException(exception);

        } catch (CsvException exception) {
            log.error("CsvException in read csv file: {}", path);
            throw new RuntimeException(exception);
        }

        log.info("Info from file was read successfully");
        return info;
    }


    public String getPATH() {
        return PATH;
    }


    public void setPATH(String PATH) {
        this.PATH = PATH;
    }
}