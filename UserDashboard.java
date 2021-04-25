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
    public Button add_fav_button;
    public TextField favorite_id_field;
    public Button deleteFavorite;
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

    void showFavorites() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "favorites");
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

    public void onBorrowedHistoryClick(ActionEvent event) throws Exception{
        showRequests();
        // Search for your history based on Username entry - can get the UserID by searching for name - getUsername in databaseconnector (i believe)
    }

    public void showFavoritesClick(ActionEvent event) throws Exception {
        // search for the favorites by entering username - get userID by using the getUsername function (i believe) - see admin.java for examples
        showFavorites();
    }

    public void onRequestClick(ActionEvent event) throws Exception {
        showItems();
        DatabaseConnector database = new DatabaseConnector();
        Window window = request_button.getScene().getWindow();

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


        Integer id_item = Integer.parseInt(item_id_field.getText());
        boolean item_exists = database.getItem(id_item);
        Integer user_id = database.session;
        boolean number_of_previous_requests = database.getBorrowRequestCount(user_id);

        if (item_exists){
            if (number_of_previous_requests){
                database.infoBox("You have too many borrow requests that are pending, please wait for previous requests to be approved or rejected.", null, "Failed");
                showRequests();
            }
            else {
                String time_in = time_in_field.getText();
                String time_out = time_out_field.getText();
                database.createRequest(user_id, id_item, time_in, time_out);
                showRequests();
            }
        }
        else{
            database.infoBox("The item ID you entered does not exist. Please check your entry.", null, "Failed");
        }
    }

    public void addFavoritesClick(ActionEvent event) throws Exception {
        // take item id and name - then add to favorites table - cross verify that the item id and name match - model after username function
        DatabaseConnector database = new DatabaseConnector();
        //Statement statement = con.createStatement();
        Window window = add_fav_button.getScene().getWindow();
        //statement.executeUpdate("INSERT INTO favorites ");

        if (item_id_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ID of the ITEM you wish to add to your favorites list.");
            return;
        }
        if (item_name_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the NAME of the ITEM you wish to add to the favorites list.");
            return;
    }
        Integer id_item = Integer.parseInt(item_id_field.getText());
        String item_name = item_name_field.getText();
        Integer user_id = database.session;

        boolean item_id_exists = database.getItem(id_item);
        boolean item_name_exists = database.itemExists(item_name);
        boolean item_info_matches = database.verifyItem(id_item, item_name);

        if (item_info_matches) {
            database.addFavorite(id_item, item_name, user_id);
            showFavorites();
            item_name_field.clear();
            item_id_field.clear();
        }
        else if (!item_id_exists) {
            database.infoBox("The ITEM ID you entered does not exist. Please check your entry.", null, "Failed");
        }
        else if (!item_name_exists) {
            database.infoBox("The ITEM NAME you entered does not exist. Please check your entry.", null, "Failed");
        }
        else {
            database.infoBox("The ITEM NAME entered is not associated with the ITEM ID entered. Please ensure item data matches correctly.", null, "Failed");
        }
    }

    public void deleteFavoritesClick(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = deleteFavorite.getScene().getWindow();
        showFavorites();
        Integer user_id = database.session;
        if (favorite_id_field.getText().isEmpty()){
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the FAVORITE_ID of the Favorite you wish to delete.");
            return;
        }

        Integer id_favorite = Integer.parseInt(favorite_id_field.getText());
        boolean flag = database.getFavorite(id_favorite);

        if (flag) {
            database.deleteFavorite(id_favorite, user_id);
            favorite_id_field.clear();
        }
        else {
            database.infoBox("This FAVORITE ID does not exist, please check your data entries.", null, "Failed");
        }
        showFavorites();

    }
}
