import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String fullName;
    private String address;
    private String pin;
    private List<Account> accounts;

    public Customer(String fullName, String address, String pin) {
        this.fullName = fullName;
        this.address = address;
        this.pin = pin;
        this.accounts = new ArrayList<>();
    }

    public boolean authenticate(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void addAccount(Account account) {
        accounts.add(account);
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public String getFullName() {
        return fullName;
    }
}
