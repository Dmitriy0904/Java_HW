package controller;

import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.DataExportService;
import service.DbService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.Timestamp;
import java.util.List;

public class Controller {
    private Long userId;
    private DbService dbService;
    private DataExportService dataExportService;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger warn = LoggerFactory.getLogger("warn");
    private static final Logger error = LoggerFactory.getLogger("error");


    public Controller(Long userId, Connection connection) {
        this.dbService = new DbService(connection);
        this.userId = userId;
        dataExportService = new DataExportService();
    }


    public void userInterface(){
        info.info("Finding user by id: {}", userId);
        User user = dbService.findUserById(userId);
        info.info("User with id:{} was found successfully", userId);

        String choose;
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
//            do{
                System.out.println("Choose what do you want to do: 1-add operation, 2-export data, 0-exit");
                choose = bf.readLine();

                switch (choose){
                    case "1" -> addOperation(user);
                    case "2" -> exportData(user);
                    case "0" -> { return; }
                    default -> System.out.println("Incorrect data entered. Please try again");
                }

//            } while (bf.readLine() != null);

        } catch (IOException | RuntimeException exception){
            System.out.println("Incorrect data entered. Please try again");
        }
    }


    private void addOperation(User user){
        info.info("User id:{} adding new operation", userId);

        try (BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
            Operation operationToAdd = new Operation();

            System.out.println("Choose the account");
            Account account = chooseAccount(user, bf);
            operationToAdd.setAccountId(account.getId());

            System.out.println("Choose category:");
            Category category = chooseCategory(account.getUserId(), bf);
            operationToAdd.setCategoryId(category.getId());

            System.out.println("Enter amount of money:");
            Double amount = Double.parseDouble(bf.readLine());
            operationToAdd.setAmount(amount);

            account.changeTotalAmount(amount, category.getType());
            dbService.updateAccount(account);

            dbService.insertOperation(operationToAdd);
            System.out.println("Operation was added successfully");
            info.info("New operation was added successfully by user:{}", userId);

            info.info("User id:{} checking account id:{} balance", userId, account.getId());
            checkAccountBalance(account, bf);
            info.info("Balance in account id:{} was checked successfully bu user id:{}", account.getId(), userId);


        } catch (IOException exception){
            error.error("IOException in adding new operation by user:{}; Reason{}", userId, exception.getMessage());
            System.out.println("Incorrect data entered. Please try again");
        }
    }


    private Account chooseAccount(User user, BufferedReader reader){
        info.info("User id:{} choosing the account", userId);

        List<Account> accounts = dbService.getUserAccounts(user.getId());
        try {
            for(int i = 0; i < accounts.size(); i ++){
                System.out.println("Account #" + (i + 1) +
                        " id:" + accounts.get(i).getId() +
                        " amount of money:" + accounts.get(i).getTotalAmount());
            }

            //Мб првоерять на стороне сервиса
            Integer index = Integer.parseInt(reader.readLine());
            if(index - 1 < 0 || index - 1 > accounts.size()){
                error.error("Incorrect index entered in choosing account by user id:{}", userId);
                throw new RuntimeException("Incorrect index entered");
            }

            Account chosenAccount = accounts.get(index - 1);
            info.info("User id:{} successfully chose an account. Chosen account id:{}", userId, chosenAccount.getId());
            return chosenAccount;

        } catch (IOException exception){
            error.error("IOException in choosing account by user id:{}; Reason:{}", userId, exception.getMessage());
            throw new RuntimeException("Incorrect value entered. Please, try again");
        }
    }


    private Category chooseCategory(Long accountId, BufferedReader reader){
        info.info("User id:{} choosing category", userId);

        List<Category> categories = dbService.getAccountCurrentCategories(accountId);
        try{
            for(int i = 0; i < categories.size(); i++){
                System.out.println("Category #" + (i + 1) +
                        " Name: " + categories.get(i).getName());
            }

            System.out.println("Choose the index of category:");
            Integer index = Integer.parseInt(reader.readLine());

            if(index -1 < 0 || index - 1  > categories.size()){
                error.error("Incorrect index entered in choosing category by user id:{}", userId);
                throw new RuntimeException("Incorrect index entered");
            }

            Category chosenCategory = categories.get(index - 1);
            info.info("User id:{} successfully chose category. Chosen category id:{}", userId, chosenCategory.getId());
            return chosenCategory;

        } catch (IOException exception){
            error.error("IOException in choosing category by user id:{}; Reason:{}", userId, exception.getMessage());
            throw new RuntimeException("Incorrect value entered. Please try again");
        }
    }


    private String readNameNewCategory(BufferedReader reader) throws IOException{
        System.out.println("Enter name of the new category:");
        String name = reader.readLine();
        return name;
    }


    private String readDescriptionNewCategory(BufferedReader reader) throws IOException{
        System.out.println("Enter description");
        String description = reader.readLine();
        return description;
    }


    private Boolean readTypeNewCategory(BufferedReader reader) throws IOException {
        System.out.println("If your category makes a profit, press 1, otherwise, if it makes a loss, press 0");
        return switch (reader.readLine()){
            case "1" ->  true;
            case "0" -> false;
            default -> throw new IOException("Incorrect value entered");
        };
    }


    private void checkAccountBalance(Account account, BufferedReader reader) throws IOException{
        System.out.println("Do you want to check your current balance? 1-yes, else-no");
        if(reader.readLine().equals("1")){
            System.out.println("Current amount of money: " + account.getTotalAmount());
        }
    }


    private void exportData(User user){
        info.info("User id:{} trying to export data", userId);

        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){

            System.out.println("Choose the account the data from which you want to export:");
            Account account = chooseAccount(user, bf);

            System.out.println("You should to enter diapason of dates between you want to export data");
            System.out.println("Enter first date from which you want to get operations");
            Timestamp dateFrom = readDateTime(bf);

            System.out.println("Enter second date to which you want to get operations");
            Timestamp dateTo = readDateTime(bf);

            List<Operation> operations = dbService.getOperationsBetweenDates(account.getId(), dateFrom, dateTo);

            System.out.println("Enter the path where you want to save the data");
            String pathToFile = bf.readLine();

            System.out.println("Enter name of the file where you want to save the data");
            //Мб предложить назвать текщей датой
            String fileName = bf.readLine();

            String path = pathToFile + fileName + ".csv";
            dataExportService.exportDataToCsvFile(operations, path);

            System.out.println("Data has been successfully exported");
            info.info("User id:{} exported data successfully", userId);

        } catch (IOException exception){
            error.error("IOException in exporting data by user id:{}; Reason:{}", userId, exception.getMessage());
            System.out.println("Incorrect data entered. Please try again");
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

}