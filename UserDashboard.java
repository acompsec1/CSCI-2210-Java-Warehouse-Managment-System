import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboard implements Initializable {
    public TextField username_field;
    public TextField id_number_field;
    public TextField item_name_field;
    public TextField time_in_field;
    public TextField time_out_field;
    public TextField item_producer_field;
//    public Button list_items;

    @FXML
    private BorderPane rootPane;
    @FXML
    private TableView tableView;

    //    @FXML
//    private Button login_button;
//    @FXML
//    private TableView tableView;
    @FXML
    void onSignOutClick(ActionEvent event) throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("JavaFXML_Files/Login_screen.fxml"));
        rootPane.getChildren().clear();
        rootPane.getChildren().setAll(pane);

    }
    @FXML
    void showItemsClick(ActionEvent event) throws Exception {
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "items");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    public void onSearchClick(ActionEvent event) {
        // Take input from fields - Item ID, item name, producer - Don't believe you need to cross-check entry relationships
    }

    public void onBorrowedHistoryClick(ActionEvent event) {
        // Search for your history based on Username entry - can get the UserID by searching for name - getUsername in databaseconnector (i believe)
    }

    public void showFavoritesClick(ActionEvent event) {
        // search for the favorites by entering username - get userID by using the getUsername function (i believe) - see admin.java for examples
    }

    public void onRequestClick(ActionEvent event) {
        // take entry for all the necessary fields - username, item name and id, time in and time out - could request to borrow based on id or item name
    }

    public void addFavoritesClick(ActionEvent event) {
        // take username, item id and name - then add to favorites table - cross verify that the item id and name match - model after username function
    }
}
