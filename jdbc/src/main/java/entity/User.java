package entity;
import java.util.List;
import java.util.Set;

public class User {
    private Long id;
    private String firstname;
    private String lastname;
    private List<Account> accounts;      //Лист или сет?

    public Long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getFullName(){
        return firstname + " " + lastname;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }
}
