public class ChequeAccount extends Account {
    private String employerName;
    private String employerAddress;

    public ChequeAccount(double balance, String branch, String employerName, String employerAddress) {
        super(balance, branch);
        this.employerName = employerName;
        this.employerAddress = employerAddress;
    }

    @Override
    public void withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
        } else {
            System.out.println("Insufficient funds.");
        }
    }

    @Override
    public void applyInterest() {
        // No interest for Cheque Account
    }

    @Override
    public String toString() {
        return "ChequeAccount";
    }
}
