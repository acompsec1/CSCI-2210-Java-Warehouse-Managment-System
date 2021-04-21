import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class UserDashboard implements Initializable {
    public TextField username_field;
    public TextField item_id_field;
    public TextField item_name_field;
    public TextField time_in_field;
    public TextField time_out_field;
    public TextField item_producer_field;
    public TextField number_of_items;
    public Button request_button;
    public Button list_items;
//    public Button list_items;

    @FXML
    private BorderPane rootPane;
    @FXML
    private TableView tableView;

    //    @FXML
//    private Button login_button;
//    @FXML
//    private TableView tableView;
    void showItems() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "items");
    }

    void showRequests() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "borrowed");
    }

    @FXML
    void onSignOutClick(ActionEvent event) throws IOException {
        BorderPane pane = FXMLLoader.load(getClass().getResource("JavaFXML_Files/Login_screen.fxml"));
        rootPane.getChildren().clear();
        rootPane.getChildren().setAll(pane);

    }
    @FXML
    void showItemsClick(ActionEvent event) throws Exception {
        showItems();
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

    public void onRequestClick(ActionEvent event) throws Exception {
//        showItems();
        DatabaseConnector database = new DatabaseConnector();
        Window window = request_button.getScene().getWindow();

        if (username_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter your username");
            return;
        }

        if (item_id_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ID of the ITEM you wish to borrow");
            return;
        }

        if (time_in_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the TIME IN information for your request");
            return;
        }

        if (time_out_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the TIME OUT information for your request");
            return;
        }

        String username = username_field.getText();
        boolean flag = database.userExists(username);
        Integer id_item = Integer.parseInt(item_id_field.getText());
        boolean item_exists = database.getItem(id_item);

        if (flag){
            Integer user_id = database.getUserID(username);
            if (item_exists){
                String time_in = time_in_field.getText();
                String time_out = time_out_field.getText();
                database.createRequest(user_id, id_item, time_in, time_out);
                showRequests();
            }
            else{
                database.infoBox("The item ID you entered does not exist. Please check your entry.", null, "Failed");
            }

        }
        else {
            database.infoBox("This username does not exist, please check your username entry field.", null, "Failed");
        }

    }

    public void addFavoritesClick(ActionEvent event) {
        // take username, item id and name - then add to favorites table - cross verify that the item id and name match - model after username function
    }
}
