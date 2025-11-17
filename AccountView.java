import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AccountView {
    public void display() throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("CreateAccount.fxml"));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Create Account");
        stage.setScene(new Scene(root));
        stage.show();
        stage.centerOnScreen();
    }
}