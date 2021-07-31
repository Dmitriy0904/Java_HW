package controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.image.ImagingOpException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class Controller {
    private Long userId;
    private String dbUsername;
    private String dbPassword;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger warn = LoggerFactory.getLogger("warn");
    private static final Logger error = LoggerFactory.getLogger("error");


    public Controller() {
//        this.userId = userId;
//        this.dbUsername = dbUsername;
//        this.dbPassword = dbPassword;
    }


    public void userInterface(){
        OperationController operationController = new OperationController();
        DataExportController dataExportController = new DataExportController();

        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
            userInitialization(bf);

            while (true){
                System.out.println("Choose what do you want to do: 1-add operation, 2-export data, 0-exit");
                String choose = bf.readLine();
                switch (choose) {
                    case "1" -> operationController.insertOperation(userId, dbUsername, dbPassword);
                    case "2" -> dataExportController.exportDataToCSv(userId, dbUsername, dbPassword);
                    case "0" -> { return; }
                    default -> System.out.println("Incorrect data entered. Please try again");
                }
            }

        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }


    public void userInitialization(BufferedReader reader){
        try{
            System.out.println("Enter user id:)");
            this.userId = Long.parseLong(reader.readLine());

            System.out.println("Enter database username");
            this.dbUsername = reader.readLine();

            System.out.println("Enter database password");
            this.dbPassword = reader.readLine();

        } catch (IOException exception){
            throw new RuntimeException(exception);
        }

    }

}