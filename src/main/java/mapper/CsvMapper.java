package mapper;

import model.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import table.CsvTable;
import java.util.*;


public class CsvMapper {
    private Creator creator = new Creator();
    private String PATH;
    private static final Logger log = LoggerFactory.getLogger(CsvMapper.class);


    public CsvMapper(String path) {
        this.PATH = path;
    }


    public CsvTable mapObjects(){
        CsvTable csvTable = new CsvTable();
        Map<String, String> headerValue = new HashMap<>();
        List<Student> students = new ArrayList<>();

        log.info("Starting to map objects. Initializing csv reader");
        CsvReader csvReader = new CsvReader(PATH);
        List<String[]> data = csvReader.readFile();
        csvTable.setData(data);

        log.info("Forming header");
        List<String> header = Arrays.asList(data.get(0));
        csvTable.setHeader(header);

        log.info("Starting to create objects");
        for(int i = 1; i < data.size(); i++){
            for(int j = 0; j < data.size(); j++){
                headerValue.put(header.get(j), data.get(i)[j]);
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
