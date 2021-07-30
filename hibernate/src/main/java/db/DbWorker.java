package db;

import entity.Category;
import entity.Operation;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;


public class DbWorker {
    //Логи
    private Session session;

    public DbWorker(Session session) {
        this.session = session;
    }


    public void insertOperation(Operation operationToInsert){
        try{
            session.beginTransaction();
            session.persist(operationToInsert);
            session.getTransaction().commit();

        } catch (Exception exception){
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    public void insertCategory(Category categoryToInsert){
        try{
            session.beginTransaction();
            session.persist(categoryToInsert);
            session.getTransaction().commit();

        } catch (Exception exception){
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    public List<Category> getAccountCurrentCategories(Long accountId){
        try {
            session.beginTransaction();

            String sqlRequest = "SELECT DISTINCT c FROM Category c LEFT JOIN Operation o ON o.category = c.id WHERE o.account = " + accountId;
            Query<Category> query = session.createQuery(sqlRequest, Category.class);
            List<Category> categories = query.getResultList();

            session.getTransaction().commit();

            return categories;

        } catch (Exception exception){
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    public List<Operation> getOperationsBetweenDates(Long accountId, Instant dateFrom, Instant dateTo){
        try {
            session.beginTransaction();

        //УСТАНОВИТЬ ЕЩЁ ОРДЕР БАЙ
            String sqlRequest ="SELECT o FROM Operation o WHERE o.account = ? AND date BETWEEN 'from' AND 'to'";
            Query<Operation> query = session.createQuery(sqlRequest, Operation.class);
            query.setParameter("from", Timestamp.from(dateFrom));
            query.setParameter("to", Timestamp.from(dateTo));
            List<Operation> operations = query.getResultList();

            session.getTransaction().commit();

            return operations;

        } catch (Exception exception){
            session.getTransaction().rollback();
            throw new RuntimeException(exception);
        }
    }


    //Метод updateAccount


    public void setSession(Session session) {
        this.session = session;
    }
}
