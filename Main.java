import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Customer marcus = new Customer("Marcus Molefe", "Gaborone", "1234");

        marcus.addAccount(new ChequeAccount(1500, "Main Branch", "TechCorp", "Plot 123, CBD"));
        marcus.addAccount(new InvestmentAccount(600, "Main Branch"));
        marcus.addAccount(new SavingsAccount(1000, "Main Branch"));

        int attempts = 0;
        boolean authenticated = false;

        while (attempts < 3 && !authenticated) {
            System.out.print("Enter your PIN: ");
            String inputPin = scanner.nextLine();

            if (marcus.authenticate(inputPin)) {
                authenticated = true;
            } else {
                attempts++;
                System.out.println("Incorrect PIN. Attempts left: " + (3 - attempts));
            }
        }

        if (!authenticated) {
            System.out.println("Too many failed attempts. Access denied.");
            return;
        }

        System.out.println("Welcome, " + marcus.getFullName());

        boolean running = true;
        while (running) {
            System.out.println("\nChoose an option:");
            System.out.println("1. Deposit");
            System.out.println("2. Withdraw");
            System.out.println("3. Apply Interest");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1 -> {
                    Account acc = selectAccount(scanner, marcus.getAccounts());
                    if (acc != null) {
                        System.out.print("Enter amount to deposit: ");
                        double amount = scanner.nextDouble();
                        new Transaction(acc, "deposit", amount).process();
                    }
                }
                case 2 -> {
                    Account acc = selectAccount(scanner, marcus.getAccounts());
                    if (acc != null) {
                        if (acc instanceof SavingsAccount) {
                            System.out.println("Withdrawals are not allowed from Savings Account.");
                        } else {
                            System.out.print("Enter amount to withdraw: ");
                            double amount = scanner.nextDouble();
                            new Transaction(acc, "withdraw", amount).process();
                        }
                    }
                }
                case 3 -> {
                    for (Account acc : marcus.getAccounts()) {
                        acc.applyInterest();
                        System.out.println("Interest applied to " + acc);
                    }
                }
                case 4 -> {
                    running = false;
                    System.out.println("Goodbye!");
                }
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static Account selectAccount(Scanner scanner, List<Account> accounts) {
        System.out.println("\nSelect an account:");
        for (int i = 0; i < accounts.size(); i++) {
            System.out.println((i + 1) + ". " + accounts.get(i));
        }
        System.out.print("Enter choice: ");
        int selection = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (selection >= 1 && selection <= accounts.size()) {
            return accounts.get(selection - 1);
        } else {
            System.out.println("Invalid selection.");
            return null;
        }
    }
}
