package mapper;

import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import table.CsvTable;
import java.util.*;

public class CsvMapper {
    private String csvPath;
    private Creator creator = new Creator();
    private static final Logger log = LoggerFactory.getLogger(CsvMapper.class);

    public CsvMapper(String csvPath) {
        this.csvPath = csvPath;
    }

    public CsvTable mapObjects(){
        CsvTable csvTable = new CsvTable();
        Map<String, String> headerValue = new HashMap<>();
        List<Student> students = new ArrayList<>();

        log.info("Starting to map objects. Initializing csv reader");
        CsvReader csvReader = new CsvReader();
        List<String[]> data = csvReader.readFile(csvPath);
        csvTable.setData(data);

        log.info("Forming header");
        Map<String, Integer> header = new LinkedHashMap<>();
        for(int i = 0; i < data.get(0).length; i++){
            header.put(data.get(0)[i], i);
        }
        csvTable.setHeader(header);

        log.info("Starting to create objects");
        for(int i = 1; i < data.size(); i++){
            for(int j = 0; j < data.size(); j++){
                headerValue.put(data.get(0)[j], data.get(i)[j]);
            }
            Student student = creator.createObject(Student.class, headerValue);
            log.info("Object {} was created successfully", student.toString());
            students.add(student);
        }

        csvTable.setModels(students);
        log.info("Objects were mapped successfully");
        return csvTable;
    }
}