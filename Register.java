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
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

public class Register implements Initializable{

//    @FXML
//    private AnchorPane rootPane;
    @FXML
    private Button register_btn;

    @FXML
    private AnchorPane root;

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
        AnchorPane pane2 = FXMLLoader.load(getClass().getResource("Login_screen.fxml"));
        root.getChildren().setAll(pane2);
    }

    @FXML
    void onSignInClick(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

}