public class Transaction {
    private Account account;
    private String type;
    private double amount;

    public Transaction(Account account, String type, double amount) {
        this.account = account;
        this.type = type.toLowerCase();
        this.amount = amount;
    }

    public void process() {
        switch (type) {
            case "deposit" -> account.deposit(amount);
            case "withdraw" -> account.withdraw(amount);
            default -> System.out.println("Invalid transaction type.");
        }
    }
}
