import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenuController {

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button depositButton;

    @FXML
    private Button withdrawButton;

    @FXML
    private Button applyInterestButton;

    @FXML
    private Button createAccountButton;

    @FXML
    private Button exitButton;

    @FXML
    private VBox accountInfoPane;

    @FXML
    private ListView<Account> accountListView;

    @FXML
    private Button selectAccountButton;

    @FXML
    private Button cancelButton;

    @FXML
    private VBox transactionPane;

    @FXML
    private Label transactionLabel;

    @FXML
    private TextField amountField;

    @FXML
    private Button confirmButton;

    @FXML
    private Button backButton;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox historyPane;

    @FXML
    private ListView<String> historyListView;

    @FXML
    private Button closeHistoryButton;

    private enum ActionType { NONE, DEPOSIT, WITHDRAW, APPLY_INTEREST }

    private ActionType currentAction = ActionType.NONE;
    private Account selectedAccount;

    @FXML
    private void initialize() {
        Customer current = BankSystem.getCurrentCustomer();
        if (current != null) {
            welcomeLabel.setText("Welcome, " + current.getFullName());
            accountListView.setItems(FXCollections.observableArrayList(current.getAccounts()));
        }
    }

    @FXML
    private void handleDeposit(ActionEvent event) {
        currentAction = ActionType.DEPOSIT;
        showAccountSelection("Select an account to deposit into:");
    }

    @FXML
    private void handleWithdraw(ActionEvent event) {
        currentAction = ActionType.WITHDRAW;
        showAccountSelection("Select an account to withdraw from:");
    }

    @FXML
    private void handleApplyInterest(ActionEvent event) {
        currentAction = ActionType.APPLY_INTEREST;
        showAccountSelection("Select an account to apply interest to:");
    }

    @FXML
    private void handleCreateAccount(ActionEvent event) {
        System.out.println("Create Account button clicked");
        try {
            AccountView accountView = new AccountView();
            accountView.display();
        } catch (Exception e) {
            System.out.println("ERROR opening Create Account screen:");
            e.printStackTrace(System.out);
            showStatus("Unable to open Create Account screen.", true);
        }
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSelectAccount(ActionEvent event) {
        selectedAccount = accountListView.getSelectionModel().getSelectedItem();
        if (selectedAccount == null) {
            showStatus("Please select an account.", true);
            return;
        }

        accountInfoPane.setVisible(false);

        if (currentAction == ActionType.DEPOSIT || currentAction == ActionType.WITHDRAW) {
            transactionPane.setVisible(true);
            transactionLabel.setText("Enter amount:");
        } else if (currentAction == ActionType.APPLY_INTEREST) {
            selectedAccount.applyInterest();
            Customer customer = BankSystem.getCurrentCustomer();
            if (customer != null) {
                String entry = "Applied interest on account " + selectedAccount.toString() + ", new balance: " + selectedAccount.getBalance();
                customer.addHistoryEntry(entry);
                BankDataStore.saveCustomer(customer);
            }
            refreshAccountList();
            showStatus("Interest applied. New balance: " + selectedAccount.getBalance(), false);
            currentAction = ActionType.NONE;
        }
    }

    @FXML
    private void handleCancel(ActionEvent event) {
        accountInfoPane.setVisible(false);
        currentAction = ActionType.NONE;
    }

    @FXML
    private void handleConfirmTransaction(ActionEvent event) {
        if (selectedAccount == null) {
            showStatus("No account selected.", true);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountField.getText());
        } catch (NumberFormatException e) {
            showStatus("Please enter a valid amount.", true);
            return;
        }

        if (amount <= 0) {
            showStatus("Amount must be positive.", true);
            return;
        }

        String actionText;
        if (currentAction == ActionType.DEPOSIT) {
            Transaction transaction = new Transaction(selectedAccount, "deposit", amount);
            transaction.process();
            actionText = "Deposit";
        } else if (currentAction == ActionType.WITHDRAW) {
            Transaction transaction = new Transaction(selectedAccount, "withdraw", amount);
            transaction.process();
            actionText = "Withdraw";
        } else {
            showStatus("No action selected.", true);
            return;
        }

        Customer customer = BankSystem.getCurrentCustomer();
        if (customer != null) {
            String entry = actionText + " of " + amount + " on account " + selectedAccount.toString() + ", new balance: " + selectedAccount.getBalance();
            customer.addHistoryEntry(entry);
            BankDataStore.saveCustomer(customer);
        }

        refreshAccountList();

        showStatus("Transaction completed. New balance: " + selectedAccount.getBalance(), false);
        amountField.clear();
        transactionPane.setVisible(false);
        currentAction = ActionType.NONE;
    }

    @FXML
    private void handleBack(ActionEvent event) {
        transactionPane.setVisible(false);
        currentAction = ActionType.NONE;
    }

    @FXML
    private void handleViewHistory(ActionEvent event) {
        Customer customer = BankSystem.getCurrentCustomer();
        if (customer == null) {
            showStatus("No current customer loaded.", true);
            return;
        }

        historyListView.setItems(FXCollections.observableArrayList(customer.getTransactionHistory()));
        historyPane.setVisible(true);
        accountInfoPane.setVisible(false);
        transactionPane.setVisible(false);
        statusLabel.setVisible(false);
    }

    @FXML
    private void handleCloseHistory(ActionEvent event) {
        historyPane.setVisible(false);
    }

    private void showAccountSelection(String message) {
        statusLabel.setVisible(false);
        accountInfoPane.setVisible(true);
    }

    private void showStatus(String message, boolean error) {
        statusLabel.setText(message);
        statusLabel.setTextFill(error ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
        statusLabel.setVisible(true);
    }

    private void refreshAccountList() {
        Customer current = BankSystem.getCurrentCustomer();
        if (current != null) {
            accountListView.setItems(FXCollections.observableArrayList(current.getAccounts()));
        }
    }
}
