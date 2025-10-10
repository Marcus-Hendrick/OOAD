public class SavingsAccount extends Account {
    private double monthlyRate = 0.0005;

    public SavingsAccount(double balance, String branch) {
        super(balance, branch);
    }

    @Override
    public void withdraw(double amount) {
        System.out.println("Withdrawals not allowed from Savings Account.");
    }

    @Override
    public void applyInterest() {
        balance += balance * monthlyRate;
    }

    @Override
    public String toString() {
        return "SavingsAccount";
    }
}
