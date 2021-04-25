import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.application.Application;
//import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;



public class DynamicTableView {

    //TABLE VIEW AND DATA
    private ObservableList<ObservableList> data;

    //CONNECTION DATABASE
    public void buildData(BorderPane border, TableView tableView, String option) throws Exception {

        Connection c;
        data = FXCollections.observableArrayList();
        tableView.getItems().clear();
        tableView.getColumns().clear();

        try {
            c = DatabaseConnector.getConnection();
            ResultSet rs;
            String sqlQuery  = "";
            DatabaseConnector dbc = new DatabaseConnector();


            if (option.equals("users")) {
                sqlQuery = "SELECT * from users";
            }
            else if (option.equals("borrowed")) {
                sqlQuery = "SELECT * from borrowed_items WHERE user_id = " + DatabaseConnector.session;
//                ResultSet rs = c.createStatement().executeQuery(sqlQuery);
            }
            else if (option.equals("items")) {
                sqlQuery = "SELECT * from items";
//                ResultSet rs = c.createStatement().executeQuery(sqlQuery);
            }
            else if (option.equals("requested")){
                sqlQuery = "Select * from borrowed_items WHERE borrow_status = 0";
            }
            else if (option.equals("favorites")){
                sqlQuery = "Select * from favorites WHERE user_id = " + DatabaseConnector.session;
            }
            else{
                System.out.println("There was an error setting the query statement");
            }

            rs = c.createStatement().executeQuery(sqlQuery);




            //ResultSet
            /**
             * ********************************
             * TABLE COLUMN ADDED DYNAMICALLY *
             *********************************
             */
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1));
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tableView.getColumns().addAll(col);
//                System.out.println("Column [" + i + "] ");
            }

            /**
             * ******************************
             * Data added to ObservableList *
             *******************************
             */
            while (rs.next()) {
                //Iterate Row
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
//                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            //FINALLY ADDED TO TableView
            tableView.setItems(data);
            tableView.setEditable(true);
            border.setCenter(tableView);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error on Building Data");
        }
    }
}