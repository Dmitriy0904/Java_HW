package service;

import db.DbWorker;
import entity.Account;
import entity.Category;
import entity.Operation;
import entity.User;
import java.sql.Connection;
import java.time.Instant;
import java.util.List;

public class DbService {
    private DbWorker dbWorker;

    public DbService(Connection connection) {
        this.dbWorker = new DbWorker(connection);
    }


    public User findUserById(Long userId){
        isEmpty(userId);
        return dbWorker.findUserById(userId);
    }


    public List<Account> getUserAccounts(Long userId){
        isEmpty(userId);
        return dbWorker.getUserAccounts(userId);
    }


    public List<Category> getAccountCurrentCategories(Long accountId){
        isEmpty(accountId);
        return dbWorker.getAccountCurrentCategories(accountId);
    }


    public void insertOperation(Operation operationToInsert){
        isEmpty(operationToInsert);
        dbWorker.insertOperation(operationToInsert);
    }


    public void insertCategory(Category categoryToInsert){
        isEmpty(categoryToInsert);
        dbWorker.insertCategory(categoryToInsert);
    }


    public void updateAccount(Account accountToUpd){
        isEmpty(accountToUpd);
        dbWorker.updateAccount(accountToUpd);
    }


    public List<Operation> exportData(Account account, Instant dateFrom, Instant dateTo){
        isEmpty(account);
        isEmpty(dateFrom);
        isEmpty(dateTo);
        return dbWorker.exportData(account, dateFrom, dateTo);
    }


    private void isEmpty(Object objectToCheck){
        if(objectToCheck == null){
            throw new RuntimeException("Object is empty");
        }
    }
}