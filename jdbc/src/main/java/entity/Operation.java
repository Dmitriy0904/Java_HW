package entity;

import java.time.Instant;

public class Operation {
    private Long id;
    private Long categoryId;
    private Long accountId;
    private Double amount;
    private Instant date = Instant.now();

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public Double getAmount() {
        return amount;
    }

    public Instant getDate() {
        return date;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
