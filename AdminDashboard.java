import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Window;
import javafx.util.Callback;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AdminDashboard implements Initializable {
    
    public TextField username_field;
    public TextField password_field;
    public TextField role_field;
    public TextField user_id_field;
    public TextField category_field;
    public TextField item_name_field;
    public TextField number_items;
    public TextField item_field;
    public TextField price_field;
    public TextField time_in_field;
    public TextField time_out_field;
    public TextField request_id_field;
    public TextField accept_reject_field;
    public Button list_favorites;
    public Button list_items;
    public Button list_users;
    public Button itemAdd;
    public Button favoriteDelete;
    public Button accept_reject_button;
    public Button userEdit;
    public Button favoriteAdd;
    public Button userDelete;
    public Button borrow_rent_button;
    public Button editItem;
    public Button userAdd;
    public Button itemDelete;
    public Button show_borrow_rent;
    public TextField provider;
    public TextField location;
    public TextField favorite_id_field;

    @FXML
    private BorderPane rootPane;
    @FXML
    private TableView tableView;

//    @FXML
//    private Button login_button;
//    @FXML
//    private TableView tableView;
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
        BorderPane pane = FXMLLoader.load(getClass().getResource("JavaFXML_Files/Login_screen.fxml"));
        rootPane.getChildren().clear();
        rootPane.getChildren().setAll(pane);
    }
    @FXML
    void showItemsClick(ActionEvent event) throws Exception {
        showItems();
    }

    @FXML
    void onAddItemClick(ActionEvent event) throws IOException{
        System.out.print("Add Item Button Pressed");
    }

    @FXML
    void onDeleteItemClick(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = userAdd.getScene().getWindow();

        if (item_field.getText().isEmpty()){
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ITEM ID");
            return;
        }
        if (item_name_field.getText().isEmpty()){
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ITEM NAME");
            return;
        }

        Integer itemID = Integer.parseInt(item_field.getText());
        String item_name = item_name_field.getText();

        boolean flag = database.getItem(itemID);

        boolean item_info_matches = database.getItemName(itemID, item_name);

        if (flag) {
            if (item_info_matches) {
                database.deleteItem(itemID, item_name);
                item_field.clear();
                item_name_field.clear();
            }
            else {
                database.infoBox("The ITEM NAME entered is not associated with the ITEMID entered. Please ensure item data matches correctly.", null, "Failed");
            }
        }
        else {
            database.infoBox("This ITEMID or Item NAME does not exist, please check your data entries.", null, "Failed");
        }
        showItems();
    }

    @FXML
    void showUsersClick(ActionEvent event) throws Exception{
        showUsers();
    }

    // Functions to more easily updates our tableview
    void showUsers() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "users");
    }

    void showItems() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "items");
    }

    @FXML
    void showFavorites() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "favorites");
    }

    void showRequests() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "borrowed");
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        tableView.setEditable(true);
    }

    public void onAddUser(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = userAdd.getScene().getWindow();
        if (username_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the username");
            return;
        }
        if (password_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "Please enter a password for the user");
            return;
        }
        if (role_field.getText().isEmpty()){
            database.showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "Please enter a role");
            return;
        }

        String username = username_field.getText();
        String password = password_field.getText();
        Integer role = Integer.parseInt(role_field.getText());

        boolean flag = database.userExists(username);

        if (flag) {
            database.infoBox("Username already exists", null, "Failed");
        }
        else {
            String hashed_password = database.getMD5(password);
            database.create(username, hashed_password, role);
            tableView.refresh();
            username_field.clear();
            password_field.clear();
            role_field.clear();
        }
        showUsers();
    }

    public void onAcceptReject(ActionEvent event) throws Exception{
        DatabaseConnector database = new DatabaseConnector();
        Window window = userAdd.getScene().getWindow();
        showBorrowed();
        if (request_id_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the request ID");
            return;
        }
        if (accept_reject_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "Please enter accept or reject option");
            return;
        }

        int request_id = Integer.parseInt(request_id_field.getText());
        String option = accept_reject_field.getText();

        boolean flag = database.requestExists(request_id);

        if(flag)
        {
            if(option.equalsIgnoreCase("accept"))
            {
                database.acceptRequest(request_id);
            }else if(option.equalsIgnoreCase("reject"))
            {
                database.rejectRequest(request_id);
            }
        }else
        {
            database.showAlert(Alert.AlertType.ERROR, window , "Form Error!",
                    "That item request does not exist, please try again.");
            return;
        }
        showBorrowed();
    }

    public void onDeleteFavorite(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = favoriteDelete.getScene().getWindow();
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

    public void onEditUser(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = userDelete.getScene().getWindow();

        if (user_id_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ID of the user you wish to delete");
            return;
        }
        if (username_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the username of the user you wish to delete");
            return;
        }

        if (password_field.getText().isEmpty() && role_field.getText().isEmpty()){
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter a new password to update the password or a role id to change the role");
            return;
        }

        String options;
        if (!password_field.getText().isEmpty() && role_field.getText().isEmpty()){
            options = "password";
        }
        else if (password_field.getText().isEmpty() && !role_field.getText().isEmpty()){
            options = "role";
        }
        else {
            options = "both";
        }

        Integer userID = Integer.parseInt(user_id_field.getText());
        String username = username_field.getText();

        boolean flag = database.userExists(username);
        boolean user_info_matches = database.getUsername(userID, username);
        Integer role;
        String hashed_password;
        if (flag) {
            if (user_info_matches) {
                String password = password_field.getText();
                try {
                    role = Integer.parseInt(role_field.getText());
                }catch(NumberFormatException e){
                    role = 0;
                }
                hashed_password = database.getMD5(password);
                database.updateUser(userID, username, hashed_password, role, options);
                password_field.clear();
                role_field.clear();
                username_field.clear();
                user_id_field.clear();
            }
            else {
                database.infoBox("The username entered is not associated with the ID entered. Please ensure user data matches correctly.", null, "Failed");
            }
        }
        else {
            database.infoBox("This username or ID does not exist, please check your data entries.", null, "Failed");
        }
        showUsers();
    }

    public void onAddFavorite(ActionEvent event) throws Exception {
        // take item id and name - then add to favorites table - cross verify that the item id and name match - model after username function
        DatabaseConnector database = new DatabaseConnector();
        //Statement statement = con.createStatement();
        Window window = favoriteAdd.getScene().getWindow();
        //statement.executeUpdate("INSERT INTO favorites ");

        showItems();

        if (item_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ID of the ITEM you wish to add to your favorites list.");
            return;
        }
        if (item_name_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the NAME of the ITEM you wish to add to the favorites list.");
            return;
        }
        Integer id_item = Integer.parseInt(item_field.getText());
        String item_name = item_name_field.getText();
        Integer user_id = database.session;

        boolean item_id_exists = database.getItem(id_item);
        boolean item_name_exists = database.itemExists(item_name);
        boolean item_info_matches = database.verifyItem(id_item, item_name);

        if (item_info_matches) {
            database.addFavorite(id_item, item_name, user_id);
            showFavorites();
            item_name_field.clear();
            item_field.clear();
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

    public void onDeleteUser(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = userDelete.getScene().getWindow();

        if (user_id_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ID of the user you wish to delete");
            return;
        }
        if (username_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the username of the user you wish to delete");
            return;
        }
        Integer user_id_value = Integer.parseInt(user_id_field.getText());
        String username = username_field.getText();

        boolean flag = database.userExists(username);
        boolean user_info_matches = database.getUsername(user_id_value, username);

        if (flag) {
            if (user_info_matches) {
                database.deleteUser(user_id_value, username);
                username_field.clear();
                user_id_field.clear();
            }
            else {
                database.infoBox("The username entered is not associated with the ID entered. Please ensure user data matches correctly.", null, "Failed");
            }
        }
        else {
            database.infoBox("This username or ID does not exist, please check your data entries.", null, "Failed");
        }
        showUsers();
    }

    public void onEditItemClick(ActionEvent event) {
    }

    void showBorrowed() throws Exception
    {
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "requested");
    }

    public void onShowBorrow(ActionEvent event) throws Exception {
        showBorrowed();
    }

    public void onBorrowRequest(ActionEvent event) throws Exception {
        DatabaseConnector database = new DatabaseConnector();
        Window window = borrow_rent_button.getScene().getWindow();
        showItems();

        if (item_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the ID of the ITEM you wish to borrow");
            return;
        }

        if (time_in_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the time in information for your request");
            return;
        }

        if (time_out_field.getText().isEmpty()) {
            database.showAlert(Alert.AlertType.ERROR, window, "Form Error!",
                    "Please enter the time out information for your request");
            return;
        }

        Integer id_item = Integer.parseInt(item_field.getText());
        boolean item_exists = database.getItem(id_item);

        Integer user_id = database.session;
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
}
