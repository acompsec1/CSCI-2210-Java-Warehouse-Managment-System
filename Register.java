import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.math.BigInteger;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

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