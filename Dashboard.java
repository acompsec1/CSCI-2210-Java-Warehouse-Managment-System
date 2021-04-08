import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Dashboard implements Initializable {
    @FXML
    private AnchorPane root;
    private BorderPane border;

    @FXML
    private Button login_button;

    @FXML
    private Button add_item;

    @FXML
    private Button delete_item;

    @FXML
    private Button edit_item;

    @FXML
    private Button people_btn;

    @FXML
    private Button sharing_btn;

    @FXML
    private Button settings_btn;

    @FXML
    void onSignOutClick(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("Login_screen.fxml"));
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
