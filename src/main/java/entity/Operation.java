package entity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "operations")
public class Operation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @Column(name = "amount", nullable = false)
    private Double amount;

    @Column(name = "date", nullable = false)
    private Timestamp date = Timestamp.from(Instant.now());


    public Long getId() {
        return id;
    }

    public Category getCategory() {
        return category;
    }

    public Account getAccount() {
        return account;
    }

    public Double getAmount() {
        return amount;
    }

    public Timestamp getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
