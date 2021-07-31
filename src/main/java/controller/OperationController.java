package controller;

import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.OperationService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;


public class OperationController {
    private OperationService operationService;
    private User user;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger warn = LoggerFactory.getLogger("warn");
    private static final Logger error = LoggerFactory.getLogger("error");


    public void insertOperation(Long userId, String dbUsername, String dbPassword){
        Configuration configuration = new Configuration().configure()
                .setProperty("hibernate.connection.username", dbUsername)
                .setProperty("hibernate.connection.password", dbPassword);

        try(SessionFactory sessionFactory = configuration.buildSessionFactory();
            Session session = sessionFactory.openSession()){
            operationService = new OperationService(session);

            user = operationService.findUserById(userId);
            info.info("User id:{} adding new operation", userId);

            try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){
                Operation newOperation = new Operation();

                System.out.println("Choose account");
                Account account = chooseAccount(bf);
                newOperation.setAccount(account);

                System.out.println("Choose category");
                Category category = chooseCategory(account.getId(), bf);
                newOperation.setCategory(category);

                System.out.println("Enter the amount");
                Double amount = Double.parseDouble(bf.readLine());
                if(amount < 0){
                    throw new IOException("Negative amount value was entered");
                }
                newOperation.setAmount(amount);
                account.changeTotalAmount(amount, category.getType());

                operationService.insertOperation(newOperation);
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
    }


    public Account chooseAccount(BufferedReader reader){
        info.info("User id:{} choosing the account", user.getId());

        List<Account> accounts = user.getAccounts();
        try {
            for(int i = 0; i < accounts.size(); i ++){
                System.out.println("Account #" + (i + 1) +
                        " id:" + accounts.get(i).getId() +
                        " amount of money:" + accounts.get(i).getFormattedTotalAmount());
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

        List<Category> categories = operationService.getAccountCurrentCategories(accountId);
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

            operationService.insertCategory(category);
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
            System.out.println("Current amount of money: " + account.getFormattedTotalAmount());
        }
    }
}
