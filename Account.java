public abstract class Account {
    protected double balance;
    protected String branch;

    public Account(double balance, String branch) {
        this.balance = balance;
        this.branch = branch;
    }

    public void deposit(double amount) {
        balance += amount;
    }

    public double getBalance() {
        return balance;
    }

    public abstract void withdraw(double amount);
    public abstract void applyInterest();
}
