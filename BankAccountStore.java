import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class BankAccountStore {

    private static final String ACCOUNT_FILE = "bankacc.txt";

    public static void saveAccount(Customer customer, Account account) {
        File file = new File(ACCOUNT_FILE);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            StringBuilder line = new StringBuilder();
            line.append(customer.getFullName()).append(";");
            line.append(account.getClass().getSimpleName()).append(";");
            line.append(account.getBalance()).append(";");

            if (account instanceof ChequeAccount cheque) {
                line.append("Cheque;");
                line.append(cheque.toString());
            } else if (account instanceof SavingsAccount) {
                line.append("Savings;");
            } else if (account instanceof InvestmentAccount) {
                line.append("Investment;");
            } else {
                line.append("Unknown;");
            }

            writer.write(line.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
