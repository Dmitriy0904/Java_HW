package controller;

import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DataExportService;
import service.DbService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.util.List;


public class Controller {
    private User user;
    private DbService dbService;
    private DataExportService dataExportService;
    private static final Logger info = LoggerFactory.getLogger(Controller.class);
    private static final Logger warn = LoggerFactory.getLogger(Controller.class);
    private static final Logger error = LoggerFactory.getLogger(Controller.class);


    public Controller(User user, Session session) {
        this.user = user;
        this.dbService = new DbService(session);
        dataExportService = new DataExportService();
    }


    public void userInterface(){
        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("What do you want to do? 1-add new operation, 2-export data, 0-exit");
            switch (bf.readLine()){
                case "1" -> createOperation(user);
                case "2" -> exportData();
                case "0" -> { return; }
                default -> System.out.println("Incorrect value entered. Try again");
            }
        } catch (IOException exception){
            System.out.println("Incorrect value entered. Try again");
        }

    }


    public void createOperation(User user){
        info.info("User id:{} adding new operation", user.getId());

        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
            Operation newOperation = new Operation();

            System.out.println("First you should to choose account:");
            Account account = chooseAccount(bf);
            newOperation.setAccount(account);

            System.out.println("Choose the category:");
            Category category = chooseCategory(account.getId(), bf);
            newOperation.setCategory(category);

            System.out.println("Enter the amount of money:");
            Double amount = Double.parseDouble(bf.readLine());
            newOperation.setAmount(amount);

            account.changeTotalAmount(amount, category.getType());

            dbService.insertOperation(newOperation);
            System.out.println("Operation was added successfully");
            info.info("New operation was added successfully by user:{}", user.getId());

            info.info("User id:{} checking account id:{} balance", user.getId(), account.getId());
            checkAccountBalance(account, bf);
            info.info("Balance in account id:{} was checked successfully bu user id:{}", account.getId(), user.getId());

        } catch (IOException exception){
            error.error("IOException in adding new operation by user:{}; Reason{}", user.getId(), exception.getMessage());
            System.out.println("Incorrect data entered. Please try again");
        }
    }


    public Account chooseAccount(BufferedReader reader){
        info.info("User id:{} choosing the account", user.getId());

        List<Account> accounts = user.getAccounts();
        try {
            for(int i = 0; i < accounts.size(); i ++){
                System.out.println("Account #" + (i + 1) +
                                    " id:" + accounts.get(i).getId() +
                                    " amount of money:" + accounts.get(i).getTotalAmount());
            }

            Integer index = Integer.parseInt(reader.readLine());
            if(index - 1 < 0 || index - 1 > accounts.size()){
                error.error("Incorrect index entered in choosing account by user id:{}", user.getId());
                throw new RuntimeException("Incorrect index entered");
            }

            Account chosenAccount = accounts.get(index - 1);  //Т.к. для пользователя индексация начинается с 1
            info.info("User id:{} successfully chose an account. Chosen account id:{}", user.getId(), chosenAccount.getId());
            return chosenAccount;

        } catch (IOException exception){
            error.error("IOException in choosing account by user id:{}; Reason:{}", user.getId(), exception.getMessage());
            throw new RuntimeException("Incorrect value entered");
        }
    }


    private Category chooseCategory(Long accountId, BufferedReader reader){
        info.info("User id:{} choosing category", user.getId());

        List<Category> categories = dbService.getAccountCurrentCategories(accountId);
        try{
            for(int i = 0; i < categories.size(); i++){
                System.out.println("Category #" + (i+1) +
                                    " Name: " + categories.get(i).getName());
            }

            System.out.println("Choose the index of category, or press 0 to create new category");
            Integer index = Integer.parseInt(reader.readLine());

            if(index == 0){
                Category category = createCategory(reader);
                return category;
            }

            if(index - 1 < 0 || index - 1 > categories.size()){
                error.error("Incorrect index entered in choosing category by user id:{}", user.getId());
                throw new RuntimeException("Incorrect index entered");
            }

            Category chosenCategory = categories.get(index - 1);
            info.info("User id:{} successfully chose category. Chosen category id:{}", user.getId(), chosenCategory.getId());
            return chosenCategory;

        } catch (IOException exception){
            error.error("IOException in choosing category by user id:{}; Reason:{}", user.getId(), exception.getMessage());
            throw new RuntimeException("Incorrect value entered. Please try again");
        }
    }


    private Category createCategory(BufferedReader reader){
        info.info("User id:{} creating new category", user.getId());
        try {
            Category category = new Category();

            System.out.println("Enter name of the category:");
            String name = reader.readLine();
            category.setName(name);

            System.out.println("Do you want to add description? 1-yes, else - no");
            if(reader.readLine().equals("1")){
                System.out.println("Enter description of the category");
                String description = reader.readLine();
                category.setDescription(description);
            }

            Boolean type;
            System.out.println("If your category makes a profit, press 1, otherwise, if it makes a loss, press 0");
            switch (reader.readLine()){
                case "1" ->  type = true;
                case "0" -> type = false;
                default -> throw new IOException("Incorrect value entered");
            }
            category.setType(type);

            dbService.insertCategory(category);
            System.out.println("New category was added successfully");
            info.info("New category was successfully created by user id:{}", user.getId());

            return category;

        } catch (IOException exception){
            error.error("IOException in creating new category by user id:{}; Reason:{}", user.getId(), exception.getMessage());
            throw new RuntimeException(exception);
        }
    }


    private void checkAccountBalance(Account account, BufferedReader reader) throws IOException{
        System.out.println("Do you want to check your current balance? 1-yes, else-no");
        if(reader.readLine().equals("1")){
            System.out.println("Current amount of money: " + account.getTotalAmount());
        }
    }


    public void exportData(){
        info.info("User id:{} trying to export data", user.getId());

        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
            System.out.println("Choose the account the data from which you want to export:");
            Account account = chooseAccount(bf);

            System.out.println("You should to enter diapason of dates between you want to export data");
            System.out.println("Enter first date from which you want to get operations. In format yyyy-mm-dd");
            Timestamp dateFrom = readDateTime(bf);

            System.out.println("Enter second date to which you want to get operations. In format yyyy-mm-dd");
            Timestamp dateTo = readDateTime(bf);

            List<Operation> operations = dbService.getOperationsBetweenDates(account.getId(), dateFrom, dateTo);

            System.out.println("Enter the path where you want to save the data");
            String pathToFile = bf.readLine();

            System.out.println("Enter name of the file where you want to save the data");
            String fileName = bf.readLine();

            String path = pathToFile + fileName + ".csv";
            dataExportService.exportDataToCsvFile(operations, path);

            System.out.println("Data has been successfully exported");
            info.info("User id:{} exported data successfully", user.getId());

        } catch (IOException exception){
            error.error("IOException in exporting data by user id:{}; Reason:{}", user.getId(), exception.getMessage());
            System.out.println("Incorrect data entered. Please try again");
        }
    }


    private Timestamp readDateTime(BufferedReader reader){
        try {
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

        }  catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }
}