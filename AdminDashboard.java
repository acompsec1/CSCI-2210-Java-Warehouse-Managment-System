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
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "items");
    }

    @FXML
    void onAddItemClick(ActionEvent event) throws IOException{
        System.out.print("Add Item Button Pressed");
    }

    @FXML
    void onDeleteItemClick(ActionEvent event) throws IOException{
        System.out.print("Delete Button Pressed");

    }

    @FXML
    void showUsersClick(ActionEvent event) throws Exception{
        showUsers();
    }

    // Updates our table
    void showUsers() throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "users");
    }

    @FXML
    void showFavorites(ActionEvent event) throws Exception{
        DynamicTableView table = new DynamicTableView();
        table.buildData(rootPane, tableView, "favorites");
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

    public void onAcceptReject(ActionEvent event) {
    }

    public void onDeleteFavorite(ActionEvent event) {
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

    public void onAddFavorite(ActionEvent event) {
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

    public void onBorrow(ActionEvent event) {
    }

    public void onEditItemClick(ActionEvent event) {
    }

}
