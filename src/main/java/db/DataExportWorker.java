package db;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DataExportWorker {
    private final Connection connection;
    private static final Logger info = LoggerFactory.getLogger("info");
    private static final Logger error = LoggerFactory.getLogger("error");


    public DataExportWorker(Connection connection){
        this.connection = connection;
    }


    public List<String> exportData(Long accountId, Timestamp dateFrom, Timestamp dateTo){
        info.info("Start exporting operations data in account id:{}", accountId);

        String SELECT_OPERATIONS = "SELECT * FROM operations WHERE account_id = ? AND date BETWEEN ? AND ? ORDER BY date";
        try (PreparedStatement selectOperations = connection.prepareStatement(SELECT_OPERATIONS)){
            selectOperations.setLong(1, accountId);
            selectOperations.setTimestamp(2, dateFrom);
            selectOperations.setTimestamp(3, dateTo);

            selectOperations.execute();
            ResultSet resultSet = selectOperations.getResultSet();

            List<String> data = new ArrayList<>();

            while (resultSet.next()) {
                StringBuilder operationData = new StringBuilder("");
                operationData.append(resultSet.getLong(1)).append(",");
                operationData.append(resultSet.getLong(2)).append(",");
                operationData.append(resultSet.getLong(3)).append(",");
                operationData.append(resultSet.getDouble(4)).append(",");
                operationData.append(resultSet.getTimestamp(5).toString()).append("\n");

                data.add(operationData.toString());
            }

            info.info("Operation export in account id:{} completed successfully", accountId);
            return data;

        } catch (SQLException exception){
            error.error("SQLException in exporting operations data. Account id:{}; Reason:{}", accountId, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }



    public List<String> getUserAccountsInfo(Long userId){
        info.info("Start getting user accounts by user id:{}", userId);

        String FIND_USER_ACCOUNTS = "SELECT * FROM accounts WHERE user_id = ?";
        try (PreparedStatement findAccounts = connection.prepareStatement(FIND_USER_ACCOUNTS)){
            findAccounts.setLong(1, userId);

            ResultSet resultSet = findAccounts.executeQuery();

            List<String> accountsInfo = new ArrayList<>();
            while (resultSet.next()) {
                StringBuilder data = new StringBuilder("");
                data.append("Account id:").append(resultSet.getLong(1)).append(" ");
                data.append("User id:").append(resultSet.getLong(2)).append("");
                data.append("Total amount:").append(resultSet.getDouble(3));
                accountsInfo.add(data.toString());
            }

            info.info("User id:{} accounts were found successfully", userId);
            return accountsInfo;

        } catch (SQLException exception){
            error.error("SQLException in finding user accounts. User id:{}; Reason:{}", userId, exception.getMessage());
            throw new RuntimeException(exception);
        }
    }

}