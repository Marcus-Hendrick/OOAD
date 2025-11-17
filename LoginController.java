import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.application.Platform;

public class LoginController {

    @FXML
    private TextField loginUsernameField;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private Label errorLabel;

    @FXML
    private TextField registerFullNameField;

    @FXML
    private TextField registerAddressField;

    @FXML
    private TextField registerIdNumberField;

    @FXML
    private TextField registerPhoneField;

    @FXML
    private TextField registerUsernameField;

    @FXML
    private PasswordField registerPasswordField;

    // Dummy field kept for backward compatibility with older compiled versions
    // that may still reference registerPinField. It is not used by the new code.
    @FXML
    private PasswordField registerPinField;

    @FXML
    private Label registerStatusLabel;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = loginUsernameField.getText();
        String password = loginPasswordField.getText();

        if (username == null || username.isBlank() || password == null || password.isBlank()) {
            showError("Please enter username and password.");
            return;
        }

        // For now, we use the password as the PIN stored in the file
        Customer customer = BankDataStore.loadCustomer(password);
        if (customer == null) {
            showError("Invalid username or password.");
            return;
        }

        BankSystem.setCurrentCustomer(customer);

        try {
            MainMenuView mainMenuView = new MainMenuView();
            mainMenuView.display();
        } catch (Exception e) {
            System.out.println("ERROR opening main menu:");
            e.printStackTrace(System.out);
            showError("Unable to open main menu.");
            return;
        }

        Stage stage = (Stage) loginUsernameField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    @FXML
    private void handleRegister(ActionEvent event) {
        String fullName = registerFullNameField.getText();
        String address = registerAddressField.getText();
        String idNumber = registerIdNumberField.getText();
        String phone = registerPhoneField.getText();
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();

        if (fullName == null || fullName.isBlank() ||
            address == null || address.isBlank() ||
            idNumber == null || idNumber.isBlank() ||
            phone == null || phone.isBlank() ||
            username == null || username.isBlank() ||
            password == null || password.isBlank()) {
            showRegisterStatus("Please fill in all registration fields.", true);
            return;
        }

        // Check whether a customer with the same password/PIN already exists
        Customer existing = BankDataStore.loadCustomer(password);
        if (existing != null) {
            showRegisterStatus("A customer with these credentials already exists.", true);
            return;
        }

        // For now, store password as the PIN field so it can be verified from account.txt
        Customer newCustomer = new Customer(fullName, address, password);
        BankDataStore.saveCustomer(newCustomer);
        showRegisterStatus("Registration successful. You can now log in.", false);

        registerFullNameField.clear();
        registerAddressField.clear();
        registerIdNumberField.clear();
        registerPhoneField.clear();
        registerUsernameField.clear();
        registerPasswordField.clear();
    }

    private void showRegisterStatus(String message, boolean error) {
        registerStatusLabel.setText(message);
        registerStatusLabel.setTextFill(error ? javafx.scene.paint.Color.RED : javafx.scene.paint.Color.GREEN);
        registerStatusLabel.setVisible(true);
    }
}
