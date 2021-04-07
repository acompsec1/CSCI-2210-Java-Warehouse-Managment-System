import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Register {

    @FXML
    private Button register_btn;

    @FXML
    private TextField input_uname;

    @FXML
    private PasswordField input_psword1;

    @FXML
    private PasswordField input_psword2;

    @FXML
    private Button signin_btn_register;

    @FXML
    void onSignInRegisterClick(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Login_screen.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Registration");
        stage.setScene(new Scene(root1));  
        stage.show();
    }

    @FXML
    void onSignInClick(ActionEvent event) {

    }

}