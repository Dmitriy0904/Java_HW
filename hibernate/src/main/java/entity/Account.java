package entity;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "account")
    private List<Operation> operations;

    @Column(name = "money", nullable = false)
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