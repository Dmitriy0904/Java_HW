package db;

import entity.Category;
import entity.Operation;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Timestamp;
import java.util.List;


public class DbWorker {
    private Session session;
    private static final Logger info = LoggerFactory.getLogger(DbWorker.class);
    private static final Logger error = LoggerFactory.getLogger(DbWorker.class);

    public DbWorker(Session session) {
        this.session = session;
    }

    public void insertOperation(Operation operationToInsert){
        try{
            info.info("Starting transaction inserting operation");
            session.beginTransaction();
            session.persist(operationToInsert);
            session.getTransaction().commit();
            info.info("Inserting operation transaction was completed successfully");

        } catch (Exception exception){
            error.error("Exception in inserting operation transaction. Reason:{}", exception.getMessage());
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    public void insertCategory(Category categoryToInsert){
        try{
            info.info("Starting transaction inserting category");
            session.beginTransaction();
            session.persist(categoryToInsert);
            session.getTransaction().commit();
            info.info("Inserting category transaction was completed successfully");

        } catch (Exception exception){
            error.error("Exception in inserting category transaction. Reason:{}", exception.getMessage());
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    public List<Category> getAccountCurrentCategories(Long accountId){
        try {
            info.info("Starting transaction getting account current categories. Account id:{}", accountId);
            session.beginTransaction();

            String sqlRequest = "SELECT DISTINCT c FROM Category c LEFT JOIN Operation o ON o.category = c.id WHERE o.account = " + accountId;
            Query<Category> query = session.createQuery(sqlRequest, Category.class);
            List<Category> categories = query.getResultList();

            session.getTransaction().commit();
            info.info("Getting account current categories transaction was completed successfully. Account id:{}", accountId);
            return categories;

        } catch (Exception exception){
            error.error("Exception in getting account current categories transaction. Account id:{} Reason:{}", accountId, exception.getMessage());
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    public List<Operation> getOperationsBetweenDates(Long accountId, Timestamp strDateFrom, Timestamp strDateTo){
        try {
            info.info("Starting transaction getting operations between dates. Account id:{}", accountId);
            session.beginTransaction();

            String sqlRequest ="SELECT o FROM Operation o WHERE o.account.id = :id AND o.date BETWEEN :from AND :to ORDER BY o.date";
            Query<Operation> query = session.createQuery(sqlRequest, Operation.class);
            query.setParameter("id", accountId);
            query.setParameter("from", strDateFrom);
            query.setParameter("to", strDateTo);

            List<Operation> operations = query.getResultList();
            session.getTransaction().commit();
            info.info("Getting operations between dates transaction was completed successfully. Account id:{}", accountId);

            return operations;

        } catch (Exception exception){
            error.error("Exception in getting operations between dates transaction. Account id:{} Reason:{}", accountId, exception.getMessage());
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }

}