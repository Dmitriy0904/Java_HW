package service;

import csv.CsvFileWriter;
import db.DataExportWorker;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

public class DataExportService {
    private final DataExportWorker dataExportWorker;
    private final CsvFileWriter csvFileWriter;

    public DataExportService(Connection connection) {
        this.dataExportWorker = new DataExportWorker(connection);
        this.csvFileWriter = new CsvFileWriter();
    }

    public void exportDataToCsvFile(List<String> data, String path){
        isNotNull(data);
        isNotNull(path);
        csvFileWriter.writeDataToCsv(data, path);
    }

    public List<String> getOperationsDataBetweenDates(Long accountId, Timestamp dateFrom, Timestamp dateTo){
        isNotNull(accountId);
        isNotNull(dateFrom);
        isNotNull(dateTo);
        return dataExportWorker.exportData(accountId, dateFrom, dateTo);
    }

    public List<String> getUserAccountsInfo(Long userId){
        isNotNull(userId);
        return dataExportWorker.getUserAccountsInfo(userId);
    }

    private void isNotNull(Object objectToCheck){
        if(objectToCheck == null){
            throw new RuntimeException("Object is empty");
        }
    }
}
