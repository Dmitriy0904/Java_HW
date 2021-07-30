package db;
import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import java.sql.*;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.ArrayList;
import java.util.List;


public class DbWorker {

    //Логи
    private Connection connection;


    public DbWorker(Connection connection) {
        this.connection = connection;
    }


    public User findUserById(Long userId){
        String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ? LIMIT 1";
        try (PreparedStatement findUser = connection.prepareStatement(FIND_USER_BY_ID)){
            findUser.setLong(1, userId);

            ResultSet resultSet = findUser.executeQuery();

            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setFirstname(resultSet.getString(2));
                user.setLastname(resultSet.getString(3));
                return user;
            }

            throw new RuntimeException("User with id " + userId + " does not exist");

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }



    public List<Account> getUserAccounts(Long userId){
        String FIND_USER_ACCOUNTS = "SELECT * FROM accounts WHERE user_id = ?";

        try (PreparedStatement findAccounts = connection.prepareStatement(FIND_USER_ACCOUNTS)){
            findAccounts.setLong(1, userId);

            ResultSet resultSet = findAccounts.executeQuery();

            List<Account> accounts = new ArrayList<>();
            while (resultSet.next()) {
                Account account = new Account();
                account.setId(resultSet.getLong(1));
                account.setUserId(resultSet.getLong(2));
                account.setTotalAmount(resultSet.getDouble(3));
                accounts.add(account);
            }

            return accounts;

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }


    public List<Category> getAccountCurrentCategories(Long accountId){
        String SELECT_CATEGORIES = "SELECT DISTINCT categories.id, categories.name, categories.description, categories.type " +
                                    "FROM categories " +
                                    "LEFT JOIN operations " +
                                    "ON operations.category_id = categories.id " +
                                    "WHERE operations.account_id = ?";

        try (PreparedStatement selectCategories = connection.prepareStatement(SELECT_CATEGORIES)){
            selectCategories.setLong(1, accountId);

            ResultSet resultSet = selectCategories.executeQuery();

            List<Category> categories = new ArrayList<>();
            while (resultSet.next()){
                Category category = new Category();

                category.setId(resultSet.getLong(1));
                category.setName(resultSet.getString(2));
                category.setDescription(resultSet.getString(3));
                category.setType(resultSet.getBoolean(4));

                categories.add(category);
            }

            return categories;

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }


    public void insertOperation(Operation operationToInsert){
        String INSERT_OPERATION = "INSERT INTO operations(category_id, account_id, amount, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertOperation = connection.prepareStatement(INSERT_OPERATION)){

            insertOperation.setLong(1, operationToInsert.getCategoryId());
            insertOperation.setLong(2, operationToInsert.getAccountId());
            insertOperation.setDouble(3, operationToInsert.getAmount());
            insertOperation.setTimestamp(4, Timestamp.from(operationToInsert.getDate()));
            insertOperation.execute();

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }


    public void insertCategory(Category categoryToInsert){
        String INSERT_CATEGORY = "INSERT INTO categories(name, description, type) VALUES (?, ?, ?)";

        try (PreparedStatement insertCategory = connection.prepareStatement(INSERT_CATEGORY)){
            insertCategory.setString(1, categoryToInsert.getName());
            insertCategory.setString(2, categoryToInsert.getDescription());
            insertCategory.setBoolean(3, categoryToInsert.getType());

            insertCategory.execute();

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }

    }


    public void updateAccount(Account accountToUpd){
        String UPDATE_ACCOUNT = "UPDATE accounts SET user_id = ?, money = ? WHERE id = ?";
        try (PreparedStatement updateAccount = connection.prepareStatement(UPDATE_ACCOUNT)){
            updateAccount.setLong(1, accountToUpd.getUserId());
            updateAccount.setDouble(2, accountToUpd.getTotalAmount());
            updateAccount.setLong(3, accountToUpd.getId());

            updateAccount.execute();

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }



    public List<Operation> exportData(Long accountId, Timestamp dateFrom, Timestamp dateTo){

        //Order by
        String SELECT_OPERATIONS = "SELECT * FROM operations WHERE account_id = ? AND date BETWEEN ? AND ?";
        try (PreparedStatement selectOperations = connection.prepareStatement(SELECT_OPERATIONS)){
            selectOperations.setLong(1, accountId);
            selectOperations.setTimestamp(2, dateFrom);
            selectOperations.setTimestamp(3, dateTo);

            selectOperations.execute();
            ResultSet resultSet = selectOperations.getResultSet();

            List<Operation> operations = new ArrayList<>();

            while (resultSet.next()){
                Operation operation = new Operation();

                operation.setId(resultSet.getLong(1));
                operation.setAccountId(resultSet.getLong(3));
                operation.setCategoryId(resultSet.getLong(2));
                operation.setAmount(resultSet.getDouble(4));
                Instant dateTime = resultSet.getTimestamp(5).toInstant();
                operation.setDate(dateTime);

                operations.add(operation);
            }
            return operations;

        } catch (SQLException exception){
            throw new RuntimeException(exception);
        }
    }

}