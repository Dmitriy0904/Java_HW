package entity;

public class Account {
    private Long id;
    private Long userId;
    private Double totalAmount;     //Все деньги на счёту у пользователя

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void changeTotalAmount(Double value, Boolean type){
        if(type){
            totalAmount += value;
            return;
        }
        totalAmount -= value;
    }
}