package entity;

import java.util.List;

public class Account {
    private Long id;
    private User user;
    private List<Operation> operations;
    private Double totalAmount;     //Все деньги на счёту у пользователя

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public List<Operation> getOperations() {
        return operations;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setOperations(List<Operation> operations) {
        this.operations = operations;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void addOperation(){

    }

    public void deleteOperation(){

    }
}