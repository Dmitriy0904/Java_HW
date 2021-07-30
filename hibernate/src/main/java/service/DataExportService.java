package service;

import csv.CsvFileWriter;
import csv.CsvFormer;
import entity.Operation;
import java.util.List;

public class DataExportService {
    private CsvFormer csvFormer = new CsvFormer();
    private CsvFileWriter csvFileWriter = new CsvFileWriter();

    public void exportDataToCsvFile(List<Operation> operations, String path){
        List<String[]> csvData = csvFormer.formCsvData(operations);
        csvFileWriter.writeDataToCsv(csvData, path);
    }
}
