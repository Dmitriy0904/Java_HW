package controller;

import db.ConnectionSetup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DataExportService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.List;

public class DataExportController {
    private DataExportService dataExportService;
    private Long userId;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger warn = LoggerFactory.getLogger("warn");
    private static final Logger error = LoggerFactory.getLogger("error");


    public void exportDataToCSv(Long userId, String dbUsername, String dbPassword){
        this.userId = userId;

        try (ConnectionSetup connectionSetup = new ConnectionSetup(dbUsername, dbPassword);
             Connection connection = connectionSetup.getConnection()) {

            dataExportService = new DataExportService(connection);

            info.info("User id:{} trying to export data", userId);

            try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){

                System.out.println("First you should to choose the account id:");
                Long accountId = chooseAccount(bf);

                System.out.println("Second should to enter diapason of dates between you want to export data");
                System.out.println("Enter first date from which you want to get operations");
                Timestamp dateFrom = readDateTime(bf);

                System.out.println("Enter second date to which you want to get operations");
                Timestamp dateTo = readDateTime(bf);

                List<String> data = dataExportService.getOperationsDataBetweenDates(accountId, dateFrom, dateTo);

                System.out.println("Enter the path where you want to save the data");
                String pathToFile = bf.readLine();

                System.out.println("Enter name of the file where you want to save the data");
                String fileName = bf.readLine();

                String path = pathToFile + fileName + ".csv";
                dataExportService.exportDataToCsvFile(data, path);

                System.out.println("Data has been successfully exported");
                info.info("User id:{} exported data successfully", userId);

            } catch (IOException exception){
                error.error("IOException in exporting data by user id:{}; Reason:{}", userId, exception.getMessage());
                System.out.println("Incorrect data entered. Please try again");
            }

        } catch (SQLException exception) {
            error.error("SQLException in running application method. Reason:{}", exception.getMessage());
            throw new RuntimeException(exception);
        }
    }


    private Timestamp readDateTime(BufferedReader reader) throws IOException{
        String strDate, strTime;

        System.out.println("Enter date in format yyyy-mm-dd: ");
        strDate = reader.readLine();
        System.out.println("Do you want to enter time? 1-yes, else-no");
        if(reader.readLine().equals("1")){
            System.out.println("Enter time in format hh:mm:ss");
            strTime = reader.readLine();
        } else {
            strTime = "0:0:0";
        }

        return Timestamp.valueOf(strDate + " " + strTime);
    }


    public Long chooseAccount(BufferedReader reader){
        info.info("User id:{} choosing the account", userId);

        List<String> accountsData = dataExportService.getUserAccountsInfo(userId);
        try {
            for(String info : accountsData){
                System.out.println(info);
            }

            System.out.println("Choose account by id");
            Long accountId = Long.parseLong(reader.readLine());
            info.info("Account id:{} was selected successfully by user id:{}", accountId, userId);
            return accountId;

        } catch (IOException exception){
            error.error("IOException in choosing account by user id:{}; Reason:{}", userId, exception.getMessage());
            throw new RuntimeException("Incorrect value entered");
        }
    }
}