package db;
import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


public class DbWorker {
    private Connection connection;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");


    public DbWorker(Connection connection) {
        this.connection = connection;
    }


    public User findUserById(Long userId){
        info.info("Start finding user by id:{}", userId);

        String FIND_USER_BY_ID = "SELECT * FROM users WHERE id = ? LIMIT 1";
        try (PreparedStatement findUser = connection.prepareStatement(FIND_USER_BY_ID)){
            findUser.setLong(1, userId);

            ResultSet resultSet = findUser.executeQuery();

            while (resultSet.next()){
                User user = new User();
                user.setId(resultSet.getLong(1));
                user.setFirstname(resultSet.getString(2));
                user.setLastname(resultSet.getString(3));

                info.info("User with id:{} was found successfully", userId);
                return user;
            }

            error.error("User with id:{} does not exist", userId);
            throw new RuntimeException("User with id " + userId + " does not exist");

        } catch (SQLException exception){
            error.error("SQLException in finding user. Id:{}; Reason:{}", userId, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }



    public List<Account> getUserAccounts(Long userId){
        info.info("Start getting user accounts by user id:{}", userId);

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

            info.info("User id:{} accounts were found successfully", userId);
            return accounts;

        } catch (SQLException exception){
            error.error("SQLException in finding user accounts. User id:{}; Reason:{}", userId, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }


    public List<Category> getAccountCurrentCategories(Long accountId){
        info.info("Start getting account current categories by account id:{}", accountId);

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

            info.info("Account id:{} current categories were found successfully", accountId);
            return categories;

        } catch (SQLException exception){
            error.error("SQLException in finding account current categories. Account id:{}; Reason:{}", accountId, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }


    public void insertOperation(Operation operationToInsert){
        info.info("Starting insert new operation id:{}", operationToInsert.getId());

        String INSERT_OPERATION = "INSERT INTO operations(category_id, account_id, amount, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement insertOperation = connection.prepareStatement(INSERT_OPERATION)){

            insertOperation.setLong(1, operationToInsert.getCategoryId());
            insertOperation.setLong(2, operationToInsert.getAccountId());
            insertOperation.setDouble(3, operationToInsert.getAmount());
            insertOperation.setTimestamp(4, Timestamp.from(operationToInsert.getDate()));
            insertOperation.execute();

            info.info("New operation id:{} insertion completed successfully", operationToInsert.getId());

        } catch (SQLException exception){
            error.error("SQLException in insertion new operation. Operation id:{}; Reason:{}", operationToInsert.getId(), exception.getMessage());
            throw new RuntimeException(exception);
        }
    }


    public void updateAccount(Account accountToUpd){
        info.info("Start updating account id:{}", accountToUpd.getId());

        String UPDATE_ACCOUNT = "UPDATE accounts SET user_id = ?, money = ? WHERE id = ?";
        try (PreparedStatement updateAccount = connection.prepareStatement(UPDATE_ACCOUNT)){
            updateAccount.setLong(1, accountToUpd.getUserId());
            updateAccount.setDouble(2, accountToUpd.getTotalAmount());
            updateAccount.setLong(3, accountToUpd.getId());

            updateAccount.execute();
            info.info("Account id:{} updating completed successfully", accountToUpd.getId());

        } catch (SQLException exception){
            error.error("SQLException in account updating. Account id:{}; Reason:{}", accountToUpd.getId(), exception.getMessage());
            throw new RuntimeException(exception);
        }
    }



    public List<Operation> exportData(Long accountId, Timestamp dateFrom, Timestamp dateTo){
        info.info("Start exporting operations data in account id:{}", accountId);

        String SELECT_OPERATIONS = "SELECT * FROM operations WHERE account_id = ? AND date BETWEEN ? AND ? ORDER BY date";
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

            info.info("Operation export in account id:{} completed successfully", accountId);
            return operations;

        } catch (SQLException exception){
            error.error("SQLException in exporting operations data. Account id:{}; Reason:{}", accountId, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

}