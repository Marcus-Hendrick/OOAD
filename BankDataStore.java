import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BankDataStore {

    private static final String DATA_FILE = "account.txt";

    public static Customer loadCustomer(String pin) {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return createDefaultCustomer(pin);
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            Customer currentCustomer = null;
            List<Account> accounts = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 0) continue;

                if ("C".equals(parts[0])) {
                    if (currentCustomer != null) {
                        currentCustomer.getAccounts().addAll(accounts);
                        if (currentCustomer.authenticate(pin)) {
                            return currentCustomer;
                        }
                    }

                    if (parts.length >= 4) {
                        String fullName = parts[1];
                        String address = parts[2];
                        String customerPin = parts[3];
                        currentCustomer = new Customer(fullName, address, customerPin);
                        accounts = new ArrayList<>();
                    }
                } else if ("A".equals(parts[0]) && currentCustomer != null) {
                    if (parts.length >= 4) {
                        String type = parts[1];
                        double balance = Double.parseDouble(parts[2]);
                        String branch = parts[3];
                        Account account = null;

                        switch (type) {
                            case "Cheque" -> {
                                String employerName = parts.length > 4 ? parts[4] : "";
                                String employerAddress = parts.length > 5 ? parts[5] : "";
                                account = new ChequeAccount(balance, branch, employerName, employerAddress);
                            }
                            case "Savings" -> account = new SavingsAccount(balance, branch);
                            case "Investment" -> account = new InvestmentAccount(balance, branch);
                        }

                        if (account != null) {
                            accounts.add(account);
                        }
                    }
                }
            }

            if (currentCustomer != null) {
                currentCustomer.getAccounts().addAll(accounts);
                if (currentCustomer.authenticate(pin)) {
                    return currentCustomer;
                }
            }
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }

        return createDefaultCustomer(pin);
    }

    public static void saveCustomer(Customer customer) {
        File file = new File(DATA_FILE);
        List<Customer> allCustomers = new ArrayList<>();

        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                Customer currentCustomer = null;
                List<Account> accounts = new ArrayList<>();

                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(";");
                    if (parts.length == 0) continue;

                    if ("C".equals(parts[0])) {
                        if (currentCustomer != null) {
                            currentCustomer.getAccounts().addAll(accounts);
                            allCustomers.add(currentCustomer);
                        }

                        if (parts.length >= 4) {
                            String fullName = parts[1];
                            String address = parts[2];
                            String customerPin = parts[3];
                            currentCustomer = new Customer(fullName, address, customerPin);
                            accounts = new ArrayList<>();
                        }
                    } else if ("A".equals(parts[0]) && currentCustomer != null) {
                        if (parts.length >= 4) {
                            String type = parts[1];
                            double balance = Double.parseDouble(parts[2]);
                            String branch = parts[3];
                            Account account = null;

                            switch (type) {
                                case "Cheque" -> {
                                    String employerName = parts.length > 4 ? parts[4] : "";
                                    String employerAddress = parts.length > 5 ? parts[5] : "";
                                    account = new ChequeAccount(balance, branch, employerName, employerAddress);
                                }
                                case "Savings" -> account = new SavingsAccount(balance, branch);
                                case "Investment" -> account = new InvestmentAccount(balance, branch);
                            }

                            if (account != null) {
                                accounts.add(account);
                            }
                        }
                    }
                }

                if (currentCustomer != null) {
                    currentCustomer.getAccounts().addAll(accounts);
                    allCustomers.add(currentCustomer);
                }
            } catch (IOException | NumberFormatException e) {
                e.printStackTrace();
            }
        }

        boolean updated = false;
        for (int i = 0; i < allCustomers.size(); i++) {
            Customer c = allCustomers.get(i);
            if (c.getPin().equals(customer.getPin())) {
                allCustomers.set(i, customer);
                updated = true;
                break;
            }
        }

        if (!updated) {
            allCustomers.add(customer);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Customer c : allCustomers) {
                writer.write("C;" + c.getFullName() + ";" + c.getAddress() + ";" + c.getPin());
                writer.newLine();
                for (Account a : c.getAccounts()) {
                    if (a instanceof ChequeAccount cheque) {
                        writer.write("A;Cheque;" + cheque.getBalance() + ";" + "Main Branch" + ";" + "" + ";" + "");
                    } else if (a instanceof SavingsAccount savings) {
                        writer.write("A;Savings;" + savings.getBalance() + ";" + "Main Branch");
                    } else if (a instanceof InvestmentAccount investment) {
                        writer.write("A;Investment;" + investment.getBalance() + ";" + "Main Branch");
                    }
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Customer createDefaultCustomer(String pin) {
        if (!"1234".equals(pin)) {
            return null;
        }

        Customer marcus = new Customer("Marcus Molefe", "Gaborone", "1234",
                                       "ID123456", "0000000000",
                                       "marcus", "1234");
        marcus.addAccount(new ChequeAccount(1500, "Main Branch", "TechCorp", "Plot 123, CBD"));
        marcus.addAccount(new InvestmentAccount(600, "Main Branch"));
        marcus.addAccount(new SavingsAccount(1000, "Main Branch"));
        saveCustomer(marcus);
        return marcus;
    }
}
