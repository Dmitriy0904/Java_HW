package controller;

import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import org.hibernate.Session;
import service.DataExportService;
import service.DbService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Controller {
    private DataExportService dataExportService = new DataExportService();
    private DbService dbService;
    private User user;
    private final BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    //Логи

    public Controller(User user, Session session) {
        this.user = user;
        this.dbService = new DbService(session);
    }

    public void userInterface(){
        //try with resources и передавать в методы ридер
        try {
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
        try {
            Operation newOperation = new Operation();

            System.out.println("First you should to choose account:");
            Account account = chooseAccount();
            newOperation.setAccount(account);

            System.out.println("Choose the category:");
            Category category = chooseCategory(account.getId());
            newOperation.setCategory(category);

            System.out.println("Enter the amount of money:");
            Double amount = Double.parseDouble(bf.readLine());
            newOperation.setAmount(amount);


            //Нужно ли проверять есть ли у пользователя воможность совершить операцию?
            //Например у него на счету 100 грн, а он хочет совершить операцию на -200 грн
            account.changeTotalAmount(amount, category.getType());

            dbService.insertOperation(newOperation);
            System.out.println("Operation was added successfully");
            checkAccountBalance(account);

        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }


    public Account chooseAccount(){
        List<Account> accounts = user.getAccounts();

        try {
            for(int i = 0; i < accounts.size(); i ++){
                System.out.println("Account #" + (i + 1) +
                                    " id:" + accounts.get(i).getId() +
                                    " amount of money:" + accounts.get(i).getTotalAmount());
            }

            //Мб првоерять на стороне сервиса
            Integer index = Integer.parseInt(bf.readLine());
            if(index - 1 < 0 || index - 1 > accounts.size()){
                throw new RuntimeException("Incorrect index entered");
            }

            return accounts.get(index - 1);     //Т.к. для пользователя индексация начинается с 1

        } catch (IOException exception){
            throw new RuntimeException("Incorrect value entered");
        }
    }


    private Category chooseCategory(Long accountId){
        List<Category> categories = dbService.getAccountCurrentCategories(accountId);
        try{
            for(int i = 0; i < categories.size(); i++){
                System.out.println("Category #" + (i+1) +
                                    " Name: " + categories.get(i).getName());
            }
            System.out.println("Choose the index of category, or press 0 to create new category");

            Integer index = Integer.parseInt(bf.readLine());
            if(index == 0){
                Category category = createCategory();
                return category;
            }

            if(index - 1 < 0 || index - 1 > categories.size()){
                throw new RuntimeException("Incorrect index entered");
            }

            return categories.get(index - 1);

        } catch (IOException exception){
            throw new RuntimeException("Incorrect value entered");
        }
    }


    //Разбить на подметоды
    private Category createCategory(){
        try {
            Category category = new Category();

            System.out.println("Enter name of the category:");
            String name = bf.readLine();
            category.setName(name);

            System.out.println("Do you want to add description? 1-yes, else - no");
            if(bf.readLine().equals("1")){
                System.out.println("Enter description of the category");
                String description = bf.readLine();
                category.setDescription(description);
            }

            Boolean type;
            System.out.println("If your category makes a profit, press 1, otherwise, if it makes a loss, press 0");
            switch (bf.readLine()){
                case "1" ->  type = true;
                case "0" -> type = false;
                default -> throw new IOException("Incorrect value entered");
            }
            category.setType(type);

            dbService.insertCategory(category);
            System.out.println("New category was added successfully");

            return category;

        } catch (IOException exception){
            throw new RuntimeException(exception);
        }
    }


    private void checkAccountBalance(Account account) throws IOException{
        System.out.println("Do you want to check your current balance? 1-yes, else-no");
        if(bf.readLine().equals("1")){
            System.out.println("Current amount of money: " + account.getTotalAmount());
        }
    }


    public void exportData(){
        try {
            DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

            System.out.println("Choose the account the data from which you want to export:");
            Account account = chooseAccount();

            System.out.println("You should to enter diapason of dates between you want to export data");
            System.out.println("Enter first date from which you want to get operations. In format yyyy-mm-dd");
            String strDateFrom = bf.readLine();
            Date dateFrom = formatter.parse(strDateFrom);
            strDateFrom = formatter.format(dateFrom);

            System.out.println("Enter second date to which you want to get operations. In format yyyy-mm-dd");
            String strDateTo = bf.readLine();
            Date dateTo = formatter.parse(strDateTo);
            strDateTo = formatter.format(dateTo);

            List<Operation> operations = dbService.getOperationsBetweenDates(account.getId(), strDateFrom, strDateTo);

            for(Operation operation : operations){
                System.out.println(operation.getId() + " " + operation.getCategory().getName() + " " + operation.getAmount());
            }

            System.out.println("Enter the path where you want to save the data");
            String path = bf.readLine();

            dataExportService.exportDataToCsvFile(operations, path);

        } catch (IOException exception){
            throw new RuntimeException(exception);
        } catch (ParseException exception) {
            throw new RuntimeException(exception);
        }
    }
}