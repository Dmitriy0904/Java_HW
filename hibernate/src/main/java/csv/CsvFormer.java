package csv;

import entity.Operation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CsvFormer {
    //Логи???

    public List<String[]> formCsvData(List<Operation> operations){
        List<String[]> csvData = new ArrayList<>();
        String []header = formHeader();
        csvData.add(header);
        for(Operation operation : operations){
            String []csvRecord = formOperationCsvRecord(operation);
            csvData.add(csvRecord);
        }

        return csvData;
    }


    private String[] formHeader(){
        Field[] fields = Operation.class.getDeclaredFields();
        String []header = new String[fields.length];
        for(int i = 0; i < fields.length; i++){
            header[i] = fields[i].getName();
        }
        return header;
    }


    private String[] formOperationCsvRecord(Operation operation){
        Field[] fields = operation.getClass().getDeclaredFields();
        String []csvRecord = new String[fields.length];

        try {
            for(int i = 0; i < fields.length; i++){
                Field field = fields[i];
                field.setAccessible(true);
                String data = (String) field.get(operation);
                csvRecord[i] = data;
            }

            return csvRecord;
        } catch (IllegalAccessException exception){
            throw new RuntimeException(exception);
        }
    }
}
