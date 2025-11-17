import java.util.ArrayList;
import java.util.List;

public class Customer {
    private String fullName;
    private String address;
    private String pin;
    private String idNumber;
    private String phone;
    private String username;
    private String password;
    private List<Account> accounts;
    private List<String> transactionHistory;

    // Existing constructor kept for backward compatibility
    public Customer(String fullName, String address, String pin) {
        this(fullName, address, pin, "", "", fullName, pin);
    }

    // New constructor with full details
    public Customer(String fullName, String address, String pin,
                    String idNumber, String phone,
                    String username, String password) {
        this.fullName = fullName;
        this.address = address;
        this.pin = pin;
        this.idNumber = idNumber;
        this.phone = phone;
        this.username = username;
        this.password = password;
        this.accounts = new ArrayList<>();
        this.transactionHistory = new ArrayList<>();
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

    public String getAddress() {
        return address;
    }

    public String getPin() {
        return pin;
    }

    public String getIdNumber() {
        return idNumber;
    }

    public String getPhone() {
        return phone;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void addHistoryEntry(String entry) {
        transactionHistory.add(entry);
    }

    public List<String> getTransactionHistory() {
        return transactionHistory;
    }
}
