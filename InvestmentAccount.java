public class InvestmentAccount extends Account {
    private double monthlyRate = 0.05;

    public InvestmentAccount(double initialDeposit, String branch) {
        super(initialDeposit >= 500 ? initialDeposit : 0, branch);
        if (initialDeposit < 500) {
            System.out.println("Investment Account requires minimum BWP500.00 to open.");
        }
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
        balance += balance * monthlyRate;
    }

    @Override
    public String toString() {
        return "InvestmentAccount";
    }
}

