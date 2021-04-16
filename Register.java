import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
// import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

public class Register implements Initializable{

//    @FXML
//    private AnchorPane rootPane;
    @FXML
    private Button register_btn;

    @FXML
//    private AnchorPane root;
    private BorderPane rootPane;

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
        BorderPane pane2 = FXMLLoader.load(getClass().getResource("JavaFXML_Files/Login_screen.fxml"));
        rootPane.getChildren().setAll(pane2);
    }

    @FXML
    void onSignInClick(ActionEvent event) {

    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    public static String getMD5(String password)
    {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");

            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while(hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        // Source: https://www.geeksforgeeks.org/md5-hash-in-java/
    }

    public void onSignUpClick(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = register_btn.getScene().getWindow();

        if (input_uname.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter your username");
            return;
        }
        if (input_psword1.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "Please enter a password");
            return;
        }
        if (input_psword2.getText().isEmpty()){
            showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "Please confirm password");
            return;
        }

        String username = input_uname.getText();
        String password1 = input_psword1.getText();
        String password2 = input_psword2.getText();

        if (!(password1.equals(password1))){
            showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "Passwords do not match!");
            return;
        }
        else {
            boolean flag = database.userExists(username);

            if (flag) {
                infoBox("Username already exists", null, "Failed");
            }
            else {
                System.out.println("WILL CREATE USER HERE");
            }

        }

    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();

    }
}