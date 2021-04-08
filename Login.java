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


public class Login implements Initializable{

    @FXML
    private AnchorPane root;

    @FXML
    private Button signin_btn;

    @FXML
    private PasswordField input_psword;

    @FXML
    private TextField input_uname;

    @FXML
    private Button psword_reset;

    @FXML
    private Button signup_btn;



    @FXML
    void onPasswordResetClick(ActionEvent event) {
        //forgot password button
    }

    @FXML
    void onSignInClick(ActionEvent event) throws IOException{
    }

    @FXML
    void onSignUpClick(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Registration_screen.fxml"));
        root.getChildren().setAll(pane);
//        Parent root1 = (Parent) fxmlLoader.load();
//        Stage stage = new Stage();
//        stage.setTitle("Registration");
//        stage.setScene(new Scene(root1));
//        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){

    }
}