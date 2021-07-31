package service;

import db.OperationWorker;
import entity.Category;
import entity.Operation;
import entity.User;
import org.hibernate.Session;
import java.util.List;

public class OperationService {
    private OperationWorker operationWorker;

    public OperationService(Session session) {
        operationWorker = new OperationWorker(session);
    }

    public User findUserById(Long userId){
        return operationWorker.findUserById(userId);
    }

    public void insertOperation(Operation operationToInsert){
        isNotNull(operationToInsert);
        operationWorker.insertOperation(operationToInsert);
    }

    public void insertCategory(Category categoryToInsert){
        isNotNull(categoryToInsert);
        operationWorker.insertCategory(categoryToInsert);
    }

    public List<Category> getAccountCurrentCategories(Long accountId){
        isNotNull(accountId);
        return operationWorker.getAccountCurrentCategories(accountId);
    }


    private void isNotNull(Object objectToCheck){
        if(objectToCheck == null){
            throw new RuntimeException("Object is empty");
        }
    }
}