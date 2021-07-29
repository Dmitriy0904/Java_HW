package controller;

import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import service.DbService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.util.List;


public class Controller {
    private DbService dbService;
    private Long userId;

    public Controller(Long userId, Connection connection) {
        this.dbService = new DbService(connection);
        this.userId = userId;
    }

    //Логи

    public void userInterface(){
        //ПРоверка на то, что такой пользователь не найден
        User user = dbService.findUserById(userId);

        //Бесконечный цикл
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){

            System.out.println("Choose what do you want to do: 1-add operation, 2-export data, 0-exit");
            switch (bf.readLine()){
                case "1" -> addOperation(user);
                case "2" -> exportData(user);
                case "0" -> { return; }
                default -> System.out.println("Incorrect data entered. Please try again");
            }
        } catch (IOException exception){
            System.out.println("Incorrect data entered. Please try again");
        }

    }


    private void addOperation(User user){
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

            //Нужно ли проверять есть ли у пользователя воможность совершить операцию?
            //Например у него на счету 100 грн, а он хочет совершить операцию на -200 грн
            account.changeTotalAmount(amount, category.getType());
            dbService.updateAccount(account);

            dbService.insertOperation(operationToAdd);
            System.out.println("Operation was added successfully");
            checkAccountBalance(account, bf);

        } catch (IOException exception){
            System.out.println("Incorrect data entered. Please try again");
        }
    }


    private Account chooseAccount(User user, BufferedReader reader){
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
                throw new RuntimeException("Incorrect index entered");
            }

            return accounts.get(index - 1);

        } catch (IOException exception){
            throw new RuntimeException("Incorrect value entered");
        }
    }


    private Category chooseCategory(Long accountId, BufferedReader reader){
        List<Category> categories = dbService.getAccountCurrentCategories(accountId);
        try{
            for(int i = 0; i < categories.size(); i++){
                System.out.println("Category #" + (i + 1) +
                        " Name: " + categories.get(i).getName());
            }
            System.out.println("Choose the index of category, or press 0 to create new category");

            Integer index = Integer.parseInt(reader.readLine());
            if(index == 0){
                Category category = createCategory(reader);
                return category;
            }

            if(index -1 < 0 || index - 1  > categories.size()){
                throw new RuntimeException("Incorrect index entered");
            }

            return categories.get(index - 1);

        } catch (IOException exception){
            throw new RuntimeException("Incorrect value entered");
        }
    }


    private Category createCategory(BufferedReader reader) throws IOException{
        Category newCategory = new Category();
        String name = readNameNewCategory(reader);
        newCategory.setName(name);

        System.out.println("Do you want to enter description? 1-yes, else-no");
        if(reader.readLine().equals("1")){
            String description = readDescriptionNewCategory(reader);
            newCategory.setDescription(description);
        }

        Boolean type = readTypeNewCategory(reader);
        newCategory.setType(type);

        dbService.insertCategory(newCategory);
        System.out.println("Your category was created successfully");
        //Не получается получить айди, тк оно генерируется бд. Тем самым не получатся добавить
        //Эту категорию пользователю
        return newCategory;
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
        try(BufferedReader bf = new BufferedReader(new InputStreamReader(System.in))){

            System.out.println("Choose the account the data from which you want to export:");

            System.out.println("You should to enter diapason of dates between you want to export data");

            //Дать пользователю возможность ввести путь где сохранить файл
            //Или дефолтный путь
            System.out.println("Enter the path where you want to save the file");


        } catch (IOException exception){
            System.out.println("Incorrect data entered. Please try again");
        }
    }

}