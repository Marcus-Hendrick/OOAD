import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CreateAccountController {

    @FXML
    private ComboBox<String> accountTypeComboBox;

    @FXML
    private VBox chequeAccountPane;

    @FXML
    private TextField initialBalanceField;

    @FXML
    private TextField branchField;

    @FXML
    private TextField employerNameField;

    @FXML
    private TextField employerAddressField;

    @FXML
    private VBox savingsAccountPane;

    @FXML
    private TextField savingsBalanceField;

    @FXML
    private TextField savingsBranchField;

    @FXML
    private VBox investmentAccountPane;

    @FXML
    private TextField investmentBalanceField;

    @FXML
    private TextField investmentBranchField;

    @FXML
    private Label statusLabel;

    @FXML
    private void initialize() {
        accountTypeComboBox.setOnAction(e -> updateAccountTypePane());
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        System.out.println("CreateAccountController.handleCreateAccount invoked");
        String type = accountTypeComboBox.getValue();
        if (type == null) {
            showStatus("Please select an account type.", true);
            return;
        }

        Customer current = BankSystem.getCurrentCustomer();
        if (current == null) {
            showStatus("No current customer loaded.", true);
            return;
        }

        try {
            Account newAccount = null;

            switch (type) {
                case "Cheque Account" -> {
                    double initialBalance = Double.parseDouble(initialBalanceField.getText());
                    String branch = branchField.getText();
                    String employerName = employerNameField.getText();
                    String employerAddress = employerAddressField.getText();
                    newAccount = new ChequeAccount(initialBalance, branch, employerName, employerAddress);
                }
                case "Savings Account" -> {
                    double balance = Double.parseDouble(savingsBalanceField.getText());
                    String branch = savingsBranchField.getText();
                    newAccount = new SavingsAccount(balance, branch);
                }
                case "Investment Account" -> {
                    double deposit = Double.parseDouble(investmentBalanceField.getText());
                    String branch = investmentBranchField.getText();
                    newAccount = new InvestmentAccount(deposit, branch);
                    if (newAccount.getBalance() == 0) {
                        showStatus("Investment Account requires at least BWP500.00.", true);
                        return;
                    }
                }
                default -> {
                    showStatus("Unknown account type.", true);
                    return;
                }
            }

            if (newAccount != null) {
                current.addAccount(newAccount);
                BankDataStore.saveCustomer(current);
                BankAccountStore.saveAccount(current, newAccount);
                showStatus("Account created successfully.", false);
            }
        } catch (NumberFormatException e) {
            System.out.println("ERROR creating account (NumberFormatException):");
            e.printStackTrace(System.out);
            showStatus("Please enter valid numeric values.", true);
        } catch (Exception e) {
            System.out.println("ERROR creating account:");
            e.printStackTrace(System.out);
            showStatus("Unable to create account.", true);
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        Stage stage = (Stage) statusLabel.getScene().getWindow();
        stage.close();
    }

    private void updateAccountTypePane() {
        String type = accountTypeComboBox.getValue();
        chequeAccountPane.setVisible(false);
        savingsAccountPane.setVisible(false);
        investmentAccountPane.setVisible(false);

        if ("Cheque Account".equals(type)) {
            chequeAccountPane.setVisible(true);
        } else if ("Savings Account".equals(type)) {
            savingsAccountPane.setVisible(true);
        } else if ("Investment Account".equals(type)) {
            investmentAccountPane.setVisible(true);
        }
    }

    private void showStatus(String message, boolean error) {
        statusLabel.setText(message);
        statusLabel.setTextFill(error ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
        statusLabel.setVisible(true);
    }
}
