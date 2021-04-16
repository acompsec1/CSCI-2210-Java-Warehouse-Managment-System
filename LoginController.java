import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.net.URL;
//import java.sql.Connection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Window;
import javafx.scene.layout.BorderPane;



public class LoginController implements Initializable{

    @FXML
    private BorderPane rootPane;

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

    }

    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }

    @FXML
    void onSignInClick(ActionEvent event) throws Exception {

        Window window = signin_btn.getScene().getWindow();

        if (input_uname.getText().isEmpty()) {
            showAlert(AlertType.ERROR, window, "Form Error!",
                    "Please enter your username");
            return;
        }
        if (input_psword.getText().isEmpty()) {
            showAlert(AlertType.ERROR, window , "Form Error!",
                    "Please enter a password");
            return;
        }
        String username = input_uname.getText();
        String password = getMD5(input_psword.getText());

        DatabaseConnector database = new DatabaseConnector();
        boolean flag = database.validate(username, password);


        if (!flag) {
            infoBox("Please enter correct Email and Password", null, "Failed");
        } else {
            Boolean role = database.getRole(username, password);
            if (role){
                BorderPane adminPane = FXMLLoader.load(getClass().getResource("JavaFXML_Files/Admin_screen.fxml"));
                rootPane.getChildren().setAll(adminPane);
            }
            else{
                BorderPane userPane= FXMLLoader.load(getClass().getResource("JavaFXML_Files/User_screen.fxml"));
                rootPane.getChildren().setAll(userPane);
            }
//            else{
//                System.out.print("THIS FAILED");
//            }
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

    @FXML
    void onSignUpClick(ActionEvent event) throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("JavaFXML_Files/Registration_screen.fxml"));
        rootPane.getChildren().setAll(pane);
    }
    @Override
    public void initialize(URL url, ResourceBundle rb){

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
}