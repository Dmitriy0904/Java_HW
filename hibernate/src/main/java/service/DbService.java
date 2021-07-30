package service;

import db.DbWorker;
import entity.Category;
import entity.Operation;
import org.hibernate.Session;

import java.time.Instant;
import java.util.List;

public class DbService {
    private DbWorker dbWorker;

    public DbService(Session session) {
        this.dbWorker = new DbWorker(session);
    }


    public void insertOperation(Operation operation){
        isNotNull(operation);
        dbWorker.insertOperation(operation);
    }

    public void insertCategory(Category categoryToInsert){
        isNotNull(categoryToInsert);
        dbWorker.insertCategory(categoryToInsert);
    }

    public List<Category> getAccountCurrentCategories(Long accountId){
        isNotNull(accountId);
        return dbWorker.getAccountCurrentCategories(accountId);
    }

    public List<Operation> getOperationsBetweenDates(Long accountId, String strDateFrom, String strDateTo){
        isNotNull(accountId);
        isNotNull(strDateFrom);
        isNotNull(strDateTo);
        return dbWorker.getOperationsBetweenDates(accountId, strDateFrom, strDateTo);
    }

    private void isNotNull(Object objectToCheck){
        if(objectToCheck == null){
            throw new RuntimeException("Object is null");
        }
    }
}