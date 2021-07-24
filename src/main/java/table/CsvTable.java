package table;

import model.Student;
import java.util.List;
import java.util.Map;

public class CsvTable{
    private Map<String, Integer> header;
    private List<Student> models;
    private List<String[]> data;

    public String get(int row, int column) {
        if(validateIndexes(row, column)){
            return data.get(row)[column];
        }
        throw new RuntimeException("Incorrect indexes entered");
    }

    public String get(int row, String headerName) {
        int column = header.get(headerName);
        if(validateIndexes(row, column)){
            return data.get(row)[column];
        }
        throw new RuntimeException("Incorrect data entered");
    }


    public Map<String, Integer> getHeader() {
        return header;
    }

    public List<Student> getModels() {
        return models;
    }


    public List<String[]> getData() {
        return data;
    }


    public void setHeader(Map<String, Integer> header) {
        this.header = header;
    }

    public void setModels(List<Student> models) {
        this.models = models;
    }


    public void setData(List<String[]> data) {
        this.data = data;
    }

    private boolean validateIndexes(int row, int column){
        if(row < 0 || row > data.size() - 1){
            return false;
        }
        else if(column < 0 || column > data.get(row).length - 1){
            return false;
        }
        return true;
    }
}