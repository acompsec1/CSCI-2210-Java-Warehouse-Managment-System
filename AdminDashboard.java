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

}
